package com.windymuse.unimall.app.api.goods;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.windymuse.unimall.biz.service.goods.GoodsBizService;
import com.windymuse.unimall.biz.service.groupshop.GroupShopBizService;
import com.windymuse.unimall.core.exception.ExceptionDefinition;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.core.exception.ThirdPartServiceException;
import com.windymuse.unimall.core.util.GeneratorUtil;
import com.windymuse.unimall.data.dto.goods.SpuDTO;
import com.windymuse.unimall.data.enums.UserLoginType;
import com.windymuse.unimall.data.mapper.GroupShopMapper;
import com.windymuse.unimall.data.model.Page;
import com.windymuse.unimall.data.storage.StorageService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Created by rize on 2019/7/2.
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsBizService goodsBizService;

    @Autowired
    private GroupShopBizService groupShopBizService;

    private OkHttpClient okHttpClient = new OkHttpClient();

    @Value("${com.windymuse.unimall.wx.mini.app-id}")
    private String wxMiNiAppid;

    @Value("${com.windymuse.unimall.wx.mini.app-secret}")
    private String wxMiNiSecret;

    @Value("${oss.aliyun.oss.basekUrl}")
    private String baseUrl;
//    @Autowired
//    private OSSClient ossClient;
    @Autowired
    private StorageService storageService;
    @Override
    public Page<SpuDTO> getGoodsPage(Integer pageNo, Integer pageSize, Long categoryId, String orderBy,Boolean isAsc, String title) throws ServiceException {
        return goodsBizService.getGoodsPage(pageNo, pageSize, categoryId, orderBy, isAsc, title);
    }

    @Override
    public SpuDTO getGoods(Long spuId, Long groupShopId, Long userId) throws ServiceException {
        //若团购Id不为空，则额外添加团购信息
        SpuDTO goods = goodsBizService.getGoods(spuId, userId);
        if (groupShopId != null) {
            goods.setGroupShop(groupShopBizService.getGroupShopById(groupShopId));
        }
        return goods;
    }

    @Override
    public Object getQRCode(Long spuId, Long groupShopId, Long userId) throws ServiceException {
        try {
            String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxMiNiAppid+
                    "&secret=" + wxMiNiSecret;
            String body = okHttpClient.newCall(new Request.Builder()
                    .url(tokenUrl).get().build()).execute().body().string();
            JSONObject jsonObject = JSONObject.parseObject(body);
            Integer errcode = jsonObject.getInteger("errcode");
            if (errcode == null || errcode == 0) {
                String accessToken = jsonObject.getString("access_token");

                //然后调用微信官方api生成二维码
                String createQrCodeUrl = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;

                String scene = spuId.toString();
                if (null != groupShopId) {
                    scene += "&" + groupShopId;
                }
                String page = "pages/product/detail";
                //此处我是使用的阿里巴巴的fastJson
                JSONObject createQrParam = new JSONObject();
                System.out.println("scene: " + scene);
                createQrParam.put("scene", scene);
                createQrParam.put("page", page);
//                createQrParam.put("env_version", "trial");


                PrintWriter out = null;
                InputStream in = null;
                URL realUrl = new URL(createQrCodeUrl);
                // 打开和URL之间的连接
                URLConnection conn = realUrl.openConnection();
                // 设置通用的请求属性
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                // 发送POST请求必须设置如下两行
                conn.setDoOutput(true);
                conn.setDoInput(true);
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数,利用connection的输出流，去写数据到connection中，我的参数数据流出我的电脑内存到connection中，让connection把参数帮我传到URL中去请求。
                out.print(createQrParam);
                // flush输出流的缓冲
                out.flush();
                //获取URL的connection中的输入流，这个输入流就是我请求的url返回的数据,返回的数据在这个输入流中，流入我内存，我将从此流中读取数据。
                in = conn.getInputStream();
                //定义个空字节数组
                byte[] data = null;
                // 读取图片字节数组
                try {
                    //创建字节数组输出流作为中转仓库，等待被写入数据
                    ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                    byte[] buff = new byte[100];
                    int rc = 0;
                    while ((rc = in.read(buff, 0, 100)) > 0) {
                        //向中转的输出流循环写出输入流中的数据
                        swapStream.write(buff, 0, rc);
                    }
                    //此时connection的输入流返回给我们的请求结果数据已经被完全地写入了我们定义的中转输出流swapStream中
                    data = swapStream.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                String base64Code = new String(Objects.requireNonNull(Base64.encodeBase64(data)));
                //Base64转byte[]数组
                String tempFilePath = "temp_qrcode.jpg";
                GenerateImage(base64Code, tempFilePath);

                File aFile = new File(tempFilePath);
                FileInputStream fileInputStream = new FileInputStream(aFile);

                MultipartFile file = new MockMultipartFile(aFile.getName(), aFile.getName(),
                        ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(file.getSize());
                objectMetadata.setContentType(file.getContentType());
                String ext = FilenameUtils.getExtension(file.getOriginalFilename());
                String uuid = GeneratorUtil.genFileName();
//                PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, "qrcode/" + uuid + "."+ext, file.getInputStream(), objectMetadata);
//                ossClient.putObject(putObjectRequest);
                String url = storageService.store(file.getInputStream(), objectMetadata.getContentLength(), objectMetadata.getContentType(), "qrcode/" + uuid+"."+ext);
                Map<String, Object> res_data = new HashMap<>();
                res_data.put("url", url);
                return res_data;

            }
        } catch (Exception e) {
            System.out.println("请求出现异常！" + e);
            e.printStackTrace();
            throw new ThirdPartServiceException(ExceptionDefinition.THIRD_PART_SERVICE_EXCEPTION.getMsg(), ExceptionDefinition.THIRD_PART_SERVICE_EXCEPTION.getCode());
        }
        return null;
    }

    public static String GetImageStr(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;

        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgFilePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    public static boolean GenerateImage(String imgStr, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
//
//    @Override
//    public Object getQRCode(Long spuId, Long groupShopId, Long userId) throws ServiceException {
//
//        try {
//            String body = okHttpClient.newCall(new Request.Builder()
//                    .url("https://api.weixin.qq.com/sns/jscode2session?grant_type=client_credential&appid=" + wxMiNiAppid+
//                            "&secret=" + wxMiNiSecret).get().build()).execute().body().string();
//            JSONObject jsonObject = JSONObject.parseObject(body);
//            Integer errcode = jsonObject.getInteger("errcode");
//            if (errcode == null || errcode == 0) {
//                String accessToken = jsonObject.getString("access_token");
//                String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=ACCESS_TOKEN" + accessToken;
//                String scene = "id=" + spuId.toString();
//                if (null != groupShopId) {
//                    scene += "&gid=" + groupShopId.toString();
//                }
//                String json = "{\"page\":\"pages/product/detail\",\"scene\":\"" + scene + "\"}";
//                RequestBody json_body = RequestBody.create(JSON, json);
//                Request request = new Request.Builder()
//                        .url(url)
//                        .post(json_body)
//                        .build();
//                String res = okHttpClient.newCall(request).execute().body().string();
//                JSONObject resJsonObject = JSONObject.parseObject(res);
//                byte[] buffers = resJsonObject.getBytes("buffer");
//
//                String base64Code = new String(Objects.requireNonNull(Base64.encodeBase64(buffers)));
//                //Base64转byte[]数组
//                System.out.println(base64Code);
//                Map<String, Object> data = new HashMap<>();
//                data.put("base64", base64Code);
//                data.put("errno", 200);
//                data.put("errmsg", "成功");
//                return data;
//
//            }
//        } catch (Exception e) {
//            throw new ThirdPartServiceException(ExceptionDefinition.THIRD_PART_SERVICE_EXCEPTION.getMsg(), ExceptionDefinition.THIRD_PART_SERVICE_EXCEPTION.getCode());
//        }
//        return null;
//    }
}
