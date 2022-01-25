//package com.windymuse.unimall.admin.api.delivery;
//
//import com.alibaba.fastjson.JSON;
//import com.aliyun.oss.model.ObjectMetadata;
//import com.github.liaochong.myexcel.core.ExcelBuilder;
//import com.github.liaochong.myexcel.core.FreemarkerExcelBuilder;
//import com.github.liaochong.myexcel.utils.FileExportUtil;
//import com.sf.csim.express.service.CallExpressServiceTools;
//import com.sf.csim.express.service.HttpClientUtil;
//import com.windymuse.unimall.biz.service.order.OrderBizService;
//import com.windymuse.unimall.core.exception.ServiceException;
//import com.windymuse.unimall.core.util.GeneratorUtil;
//import com.windymuse.unimall.data.config.ShunFenConfig;
//import com.windymuse.unimall.data.domain.OrderDO;
//import com.windymuse.unimall.data.dto.ShunFenDTO;
//import com.windymuse.unimall.data.dto.order.OrderDTO;
//import com.windymuse.unimall.data.mapper.OrderMapper;
//import com.windymuse.unimall.data.storage.StorageService;
//import org.apache.commons.io.FilenameUtils;
//import org.apache.http.entity.ContentType;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * Created with IntelliJ IDEA.
// * Description:
// * User: kbq
// * Date: 2019-07-12
// * Time: 下午11:26
// */
//@Service
//public class AdminDeliveryServiceImpl implements AdminDeliveryService {
//
//    @Value("${oss.aliyun.oss.accessId}")
//    private String accessId;
//    @Value("${oss.aliyun.oss.accessKey}")
//    private String accessKey;
//    @Value("${oss.aliyun.oss.endpoint}")
//    private String endpoint;
//    @Value("${oss.aliyun.oss.bucket}")
//    private String bucket;
//    @Value("${oss.aliyun.oss.dir}")
//    private String dir;
//    @Value("${oss.aliyun.oss.callbackUrl}")
//    private String callbackUrl;
//    @Value("${oss.aliyun.oss.basekUrl}")
//    private String baseUrl;
//
//    @Autowired
//    private StorageService storageService;
//
//    @Autowired
//    private OrderBizService orderBizService;
//    @Autowired
//    private OrderMapper orderMapper;
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Map<String, Object> createOrder(Long adminId, Long orderId) throws ServiceException, IOException {
//
//        CallExpressServiceTools client=CallExpressServiceTools.getInstance();
//        Map<String, String> params = new HashMap<String, String>();
//
//        String timeStamp = ((Long)System.currentTimeMillis()).toString();
//        ShunFenDTO shunFenDTO = new ShunFenDTO();
//        shunFenDTO.setOrderId("testorder001");
//        shunFenDTO.setLanguage("zh-CN");
//
//        ShunFenDTO.Cargo cargo = new ShunFenDTO.Cargo();
//        cargo.setCount(2.333);
//        cargo.setUnit("个");
//        cargo.setWeight(6.2);
//        cargo.setAmount(100.3);
//        cargo.setName("护肤品111");
//        cargo.setSourceArea("CHN");
//        List<ShunFenDTO.Cargo> cargos = new ArrayList<>();
//        cargos.add(cargo);
//        shunFenDTO.setCargoDetails(cargos);
//
//        ShunFenDTO.ContactInfo contactInfo1 = new ShunFenDTO.ContactInfo();
//        contactInfo1.setAddress("广东省深圳市南山区软件产业基地11栋");
//        contactInfo1.setContact("小曾");
//        contactInfo1.setContactType(1);
//        contactInfo1.setCountry("CN");
//        contactInfo1.setPostCode("580058");
//        contactInfo1.setTel("18688806057");
//
//
//        ShunFenDTO.ContactInfo contactInfo2 = new ShunFenDTO.ContactInfo();
//        contactInfo2.setAddress("广东省广州市白云区湖北大厦");
//        contactInfo2.setContact("顺丰速运");
//        contactInfo2.setContactType(2);
//        contactInfo2.setCountry("CN");
//        contactInfo2.setPostCode("580058");
//        contactInfo2.setTel("4006789888");
//
//        List<ShunFenDTO.ContactInfo> contactInfoList = new ArrayList<>();
//        contactInfoList.add(contactInfo1);
//        contactInfoList.add(contactInfo2);
//        shunFenDTO.setContactInfoList(contactInfoList);
//
//        String msgData = JSON.toJSONString(shunFenDTO);
//
//        params.put("partnerID", ShunFenConfig.clientCode);  // 顾客编码 ，对应丰桥上获取的clientCode
//
//        params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
//
//        params.put("serviceCode","EXP_RECE_CREATE_ORDER");// 接口服务码
//
//        params.put("timestamp", timeStamp);
//
//        params.put("msgData", msgData);
//
//        params.put("msgDigest", client.getMsgDigest(msgData,timeStamp,ShunFenConfig.checkword));//数据签名
//
//        System.out.println("----------------------");
//        System.out.println(params);
//        System.out.println("----------------------");
//
//        String result = HttpClientUtil.post(ShunFenConfig.accessUrl, params);
//
//        System.out.println("----------------------");
//        System.out.println(result);
//        System.out.println("----------------------");
//
//        Map<String, Object> resMap = new HashMap<>();
//        resMap.put("data", JSON.parseObject(result));
//        resMap.put("state", "ok");
//
//        return resMap;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Map<String, Object> getRoute(Long adminId, Long orderId) throws ServiceException, IOException {
//
//        String timeStamp = ((Long)System.currentTimeMillis()).toString();
//
//        String msgData = "{\"language\": \"0\",\"trackingType\": \"1\",\"trackingNumber\": [\"SF1342618169716\"],\"methodType\": \"1\",\"checkPhoneNo\": \"3695\"}";
//
////        {
////            "language": "0",
////                "trackingType": "1",
////                "trackingNumber": ["SF1342618169716"],
////            "methodType": "1",
////                "checkPhoneNo": "3695"
////        }
//
//
//        CallExpressServiceTools client = CallExpressServiceTools.getInstance();
//        Map<String, String> params = new HashMap<String, String>();
//
//        params.put("partnerID", ShunFenConfig.clientCode);  // 顾客编码 ，对应丰桥上获取的clientCode
//
//        params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
//
//        params.put("serviceCode","EXP_RECE_SEARCH_ROUTES");// 接口服务码
//
//        params.put("timestamp", timeStamp);
//
//        params.put("msgData", msgData);
//
//        params.put("msgDigest", client.getMsgDigest(msgData,timeStamp, ShunFenConfig.checkword));//数据签名
//
//        System.out.println("----------------------");
//        System.out.println(params);
//        System.out.println("----------------------");
//
//        String result = HttpClientUtil.post(ShunFenConfig.accessUrl, params);
//
//        System.out.println("----------------------");
//        System.out.println(result);
//        System.out.println("----------------------");
//
//        Map<String, Object> resMap = new HashMap<>();
//        resMap.put("data", JSON.parseObject(result));
//        resMap.put("state", "ok");
//
//        return resMap;
//    }
//
//
//}
