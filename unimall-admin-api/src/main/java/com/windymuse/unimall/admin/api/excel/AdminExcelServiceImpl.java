package com.windymuse.unimall.admin.api.excel;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.core.ExcelBuilder;
import com.github.liaochong.myexcel.core.FreemarkerExcelBuilder;
import com.github.liaochong.myexcel.core.parser.Tr;
import com.github.liaochong.myexcel.utils.FileExportUtil;
import com.sun.javafx.binding.StringFormatter;
import com.windymuse.unimall.biz.service.order.OrderBizService;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.core.util.GeneratorUtil;
import com.windymuse.unimall.data.domain.OrderDO;
import com.windymuse.unimall.data.domain.OrderSkuDO;
import com.windymuse.unimall.data.domain.UserDO;
import com.windymuse.unimall.data.dto.order.OrderDTO;
import com.windymuse.unimall.data.dto.order.OrderGoodsSumDTO;
import com.windymuse.unimall.data.mapper.OrderMapper;
import com.windymuse.unimall.data.mapper.OrderSkuMapper;
import com.windymuse.unimall.data.mapper.UserMapper;
import com.windymuse.unimall.data.storage.StorageService;
import com.windymuse.unimall.data.util.ConverToChinesePart;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.entity.ContentType;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: kbq
 * Date: 2019-07-12
 * Time: 下午11:26
 */
@Service
public class AdminExcelServiceImpl implements AdminExcelService {

    @Value("${oss.aliyun.oss.accessId}")
    private String accessId;
    @Value("${oss.aliyun.oss.accessKey}")
    private String accessKey;
    @Value("${oss.aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${oss.aliyun.oss.bucket}")
    private String bucket;
    @Value("${oss.aliyun.oss.dir}")
    private String dir;
    @Value("${oss.aliyun.oss.callbackUrl}")
    private String callbackUrl;
    @Value("${oss.aliyun.oss.basekUrl}")
    private String baseUrl;

    @Autowired
    private StorageService storageService;

    @Autowired
    private OrderBizService orderBizService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderSkuMapper orderSkuMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createSaleConfirmation(Long adminId, Long orderId) throws ServiceException, IOException {

        try (ExcelBuilder excelBuilder = new FreemarkerExcelBuilder()) {
            String path = String.format("/gy%06d.xlsx", orderId);
            Map<String, Object> dataMap = this.getDataMap(orderId);
            Workbook workbook = excelBuilder
                    .classpathTemplate("/templates/salesConfirmation.ftl")
                    .build(dataMap);
            File aFile = new File(path);
            FileExportUtil.export(workbook, aFile);
            FileInputStream fileInputStream = new FileInputStream(aFile);

            MultipartFile file = new MockMultipartFile(aFile.getName(), aFile.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            String uuid = GeneratorUtil.genFileName();
            String filename = String.format("gy%06d", orderId);
            String url = storageService.store(file.getInputStream(), objectMetadata.getContentLength(), objectMetadata.getContentType(), "saleConfirmation/" + filename + "." + ext);
            Map<String, Object> data = new HashMap<>();
            data.put("url", url);
            return data;
        }
    }

    @Override
    public Map<String, Object> createDaySales(Long adminId) throws ServiceException, IOException {

        try (DefaultExcelBuilder excelBuilder = DefaultExcelBuilder.of(DaySale.class)) {
            String path = String.format("/day.xlsx");
            List<DaySale> daySaleList = this.getDaySaleList();
            Workbook workbook = excelBuilder.build(daySaleList);
            File aFile = new File(path);
            FileExportUtil.export(workbook, aFile);
            FileInputStream fileInputStream = new FileInputStream(aFile);

            MultipartFile file = new MockMultipartFile(aFile.getName(), aFile.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            String uuid = GeneratorUtil.genFileName();
            String filename = String.format("day");
            String url = storageService.store(file.getInputStream(), objectMetadata.getContentLength(), objectMetadata.getContentType(), "daySale/" + filename + "." + ext);
            Map<String, Object> data = new HashMap<>();
            data.put("url", url);
            return data;
        }
    }

    @Override
    public Map<String, Object> createSomeDaySales(Long adminId, String startDay, String endDay) throws ServiceException, IOException {

        try (DefaultExcelBuilder excelBuilder = DefaultExcelBuilder.of(DaySale.class)) {
            String path = String.format("/day.xlsx");
            List<DaySale> daySaleList = this.getDaySaleList(startDay, endDay);
            Workbook workbook = excelBuilder.build(daySaleList);
            File aFile = new File(path);
            FileExportUtil.export(workbook, aFile);
            FileInputStream fileInputStream = new FileInputStream(aFile);

            MultipartFile file = new MockMultipartFile(aFile.getName(), aFile.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            String uuid = GeneratorUtil.genFileName();
            String filename = String.format("day");
            String url = storageService.store(file.getInputStream(), objectMetadata.getContentLength(), objectMetadata.getContentType(), "daySale/" + filename + "." + ext);
            Map<String, Object> data = new HashMap<>();
            data.put("url", url);
            return data;
        }
    }

    @Override
    public Map<String, Object> createNewSomeDaySales(Long adminId, String startDay, String endDay) throws ServiceException, IOException {

        try (DefaultExcelBuilder excelBuilder = DefaultExcelBuilder.of(NewDaySale.class)) {
            String path = String.format("/newDay.xlsx");
            List<NewDaySale> daySaleList = this.getNewDaySaleList(startDay, endDay);
            Workbook workbook = excelBuilder.build(daySaleList);
            File aFile = new File(path);
            FileExportUtil.export(workbook, aFile);
            FileInputStream fileInputStream = new FileInputStream(aFile);

            MultipartFile file = new MockMultipartFile(aFile.getName(), aFile.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            String uuid = GeneratorUtil.genFileName();
            String filename = String.format("day");
            String url = storageService.store(file.getInputStream(), objectMetadata.getContentLength(), objectMetadata.getContentType(), "daySale/" + filename + "." + ext);
            Map<String, Object> data = new HashMap<>();
            data.put("url", url);
            return data;
        }
    }

    @Override
    public Map<String, Object> createNewGoodsSales(Long adminId, String startDay, String endDay) throws ServiceException, IOException {

        try (DefaultExcelBuilder excelBuilder = DefaultExcelBuilder.of(NewGoodsSale.class)) {
            String path = String.format("/newGoods.xlsx");
            List<NewGoodsSale> goodsSaleList = this.getNewGoodsSaleList(startDay, endDay);
            Workbook workbook = excelBuilder.build(goodsSaleList);
            File aFile = new File(path);
            FileExportUtil.export(workbook, aFile);
            FileInputStream fileInputStream = new FileInputStream(aFile);

            MultipartFile file = new MockMultipartFile(aFile.getName(), aFile.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            String uuid = GeneratorUtil.genFileName();
            String filename = String.format("day");
            String url = storageService.store(file.getInputStream(), objectMetadata.getContentLength(), objectMetadata.getContentType(), "daySale/" + filename + "." + ext);
            Map<String, Object> data = new HashMap<>();
            data.put("url", url);
            return data;
        }
    }

    private Map<String, Object> getDataMap(Long orderId) throws ServiceException {
        OrderDTO orderDetail = orderBizService.getOrderDetail(orderId, null);
        OrderDO orderDO = orderMapper.selectById(orderId);

        orderDetail.setSkuList(orderSkuMapper.selectList(new EntityWrapper<OrderSkuDO>().eq("order_id", orderId)));

        List<OrderSkuDO> skuList = orderDetail.getSkuList();
        Map<String, Object> dataMap = new HashMap<>();
        // 不足6行，填充空行
        Integer blankLines = 6 - skuList.size();
        if (blankLines < 0) {
            blankLines = 0;
        }
        List<Integer> indexes = new ArrayList<>();
        for (int i = blankLines; i > 0; i--) {
            indexes.add(7 - i);
        }
        dataMap.put("blankLines", indexes);

        dataMap.put("orderNo", String.format("gy%06d", orderDetail.getId()));
        if (null != orderDO.getShipCode()) {
            dataMap.put("shipCode", String.format("%s", orderDO.getShipCode().toString()));
        } else {
            dataMap.put("shipCode", "无");
        }
        Date gmtCreate = orderDO.getGmtCreate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String orderTime = simpleDateFormat.format(gmtCreate);
        dataMap.put("orderTime", orderTime);

        dataMap.put("goods", orderDetail.getSkuList());

        dataMap.put("detail", orderDetail);
        String chinesePrice = new ConverToChinesePart((double) orderDetail.getActualPrice() / 100).convertToChinese();
        dataMap.put("chinesePrice", chinesePrice);
        dataMap.put("actualPrice", String.format("%.2f", (double) orderDetail.getActualPrice() / 100));

        // 收货地址
        String address = "";
        if (true == orderDetail.getSelfTake()) {
            address = "自取";
        } else {
            address = orderDetail.getAddress();
        }
        dataMap.put("address", address);

        // 联系电话
        dataMap.put("phone", orderDetail.getPhone());

        System.out.println(dataMap);

        return dataMap;
    }

    private List<DaySale> getDaySaleList() throws ServiceException {
        List<DaySale> daySaleList = new ArrayList<>();


        Wrapper<OrderDO> wrapper = new EntityWrapper<OrderDO>();
        wrapper.orderBy("gmt_pay", false);
        wrapper.isNotNull("gmt_pay");
        List<OrderDO> orderDOS = orderMapper.selectList(wrapper);

        for(int i = 0; i < orderDOS.size(); i++) {
            OrderDO orderDO = orderDOS.get(i);
            DaySale daySale = new DaySale();
            daySale.setTime(orderDO.getGmtPay());
            daySale.setOrderNo(String.format("gy%06d",orderDO.getId()));

            UserDO userById = userMapper.getUserById(orderDO.getUserId());
            daySale.setCustomerName(userById.getNickname());

            OrderDTO orderDetail = orderBizService.getOrderDetail(orderDO.getId(), null);
            List<OrderSkuDO> orderSkuDOList = orderSkuMapper.selectList(new EntityWrapper<OrderSkuDO>().eq("order_id", orderDO.getId()));
            daySale.setSpuName(orderSkuDOList.get(0).getSpuTitle());
            daySale.setSkuName(orderSkuDOList.get(0).getTitle());

            daySale.setPrice(String.format("%.2f", ((double)orderSkuDOList.get(0).getPrice() / 100)));
            daySale.setWeight(String.format("%d * %s", orderSkuDOList.get(0).getNum(), orderSkuDOList.get(0).getUnit()));
            daySale.setTotalPrice(String.format("%.2f", ((double)orderDetail.getActualPrice() / 100)));
            daySale.setRealPrice(String.format("%.2f", ((double)orderDetail.getPayPrice() / 100)));

            daySaleList.add(daySale);
        }



        return daySaleList;
    }

    private List<DaySale> getDaySaleList(String startDay, String endDay) throws ServiceException {
        List<DaySale> daySaleList = new ArrayList<>();


        Wrapper<OrderDO> wrapper = new EntityWrapper<OrderDO>();
        wrapper.orderBy("gmt_pay", false);
        wrapper.ge("gmt_pay", startDay);
        wrapper.le("gmt_pay", endDay);
        List<OrderDO> orderDOS = orderMapper.selectList(wrapper);

        for(int i = 0; i < orderDOS.size(); i++) {
            OrderDO orderDO = orderDOS.get(i);
            DaySale daySale = new DaySale();
            daySale.setTime(orderDO.getGmtPay());
            daySale.setOrderNo(String.format("gy%06d",orderDO.getId()));

            UserDO userById = userMapper.getUserById(orderDO.getUserId());
            daySale.setCustomerName(userById.getNickname());

            OrderDTO orderDetail = orderBizService.getOrderDetail(orderDO.getId(), null);
            List<OrderSkuDO> orderSkuDOList = orderSkuMapper.selectList(new EntityWrapper<OrderSkuDO>().eq("order_id", orderDO.getId()));
            daySale.setSpuName(orderSkuDOList.get(0).getSpuTitle());
            daySale.setSkuName(orderSkuDOList.get(0).getTitle());

            daySale.setPrice(String.format("%.2f", ((double)orderSkuDOList.get(0).getPrice() / 100)));
            daySale.setWeight(String.format("%d * %s", orderSkuDOList.get(0).getNum(), orderSkuDOList.get(0).getUnit()));
            daySale.setTotalPrice(String.format("%.2f", ((double)orderDetail.getActualPrice() / 100)));
            daySale.setRealPrice(String.format("%.2f", ((double)orderDetail.getPayPrice() / 100)));

            daySaleList.add(daySale);
        }



        return daySaleList;
    }

    private List<NewDaySale> getNewDaySaleList(String startDay, String endDay) throws ServiceException {
        List<NewDaySale> newDaySaleList = new ArrayList<>();


        Wrapper<OrderDO> wrapper = new EntityWrapper<OrderDO>();
        wrapper.orderBy("gmt_pay", false);
        wrapper.ge("gmt_pay", startDay);
        wrapper.le("gmt_pay", endDay);
        List<OrderDO> orderDOS = orderMapper.selectList(wrapper);

        for(int i = 0; i < orderDOS.size(); i++) {
            OrderDO orderDO = orderDOS.get(i);

            UserDO userById = userMapper.getUserById(orderDO.getUserId());
            OrderDTO orderDetail = orderBizService.getOrderDetail(orderDO.getId(), null);
            List<OrderSkuDO> orderSkuDOList = orderSkuMapper.selectList(new EntityWrapper<OrderSkuDO>().eq("order_id", orderDO.getId()));

            for (int j = 0; j < orderSkuDOList.size(); j ++) {
                OrderSkuDO orderSkuDO = orderSkuDOList.get(j);


                NewDaySale newDaySale = new NewDaySale(
                        "0001",
                        "国渔鲜生",
                        "",
                        "",
                        "",
                        "",
                        "",
                        orderSkuDO.getOrderNo(),
                        orderDO.getGmtPay(), // time
                        orderSkuDO.getId().toString(),
                        orderSkuDO.getSpuTitle(),
                        orderSkuDO.getUnit(),
                        orderSkuDO.getTitle(),
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        orderSkuDO.getNum().toString(),
                        orderSkuDO.getNum().toString(),
                        String.format("%.2f", ((double)orderSkuDO.getPrice() / 100)),
                        String.format("%.2f", ((double)orderSkuDO.getOriginalPrice() / 100)),
                        String.format("%.2f", ((double)orderSkuDO.getPrice() / 100) * orderSkuDO.getNum()),
                        String.format("%.2f", ((double)(orderSkuDO.getOriginalPrice() - orderSkuDO.getPrice()) / 100)),
                        "0.00",
                        String.format("%.2f", ((double)orderSkuDO.getPrice() / 100) * orderSkuDO.getNum()),
                        ""
                );


                newDaySaleList.add(newDaySale);
            }
        }

        return newDaySaleList;
    }

    private List<NewGoodsSale> getNewGoodsSaleList(String startDay, String endDay) throws ServiceException {
        List<NewGoodsSale> newGoodsSaleList = new ArrayList<>();

        Wrapper<OrderDO> wrapper = new EntityWrapper<OrderDO>();
        wrapper.orderBy("gmt_pay", false);
        wrapper.ge("gmt_pay", startDay);
        wrapper.le("gmt_pay", endDay);

        List<OrderGoodsSumDTO> orderGoodsSumDTOS = orderMapper.selectOrderGoodsSum(startDay, endDay);
        for(int i = 0; i < orderGoodsSumDTOS.size(); i++) {
            OrderGoodsSumDTO orderGoodsSumDTO = orderGoodsSumDTOS.get(i);
            System.out.println("--------------------------------------------");
            System.out.println(orderGoodsSumDTO);
            System.out.println("--------------------------------------------");
            if (null == orderGoodsSumDTO.getOriginalPrice()) {
                orderGoodsSumDTO.setOriginalPrice(0);
            }
            if (null == orderGoodsSumDTO.getAvgPrice()) {
                orderGoodsSumDTO.setAvgPrice(0);
            }
            NewGoodsSale newGoodsSale = new NewGoodsSale(
                    orderGoodsSumDTO.getGmtCreate(),
                    orderGoodsSumDTO.getSpuTitle(),
                    orderGoodsSumDTO.getTitle(),
                    "",
                    String.format("%.2f", ((double)orderGoodsSumDTO.getOriginalPrice() / 100)),
                    String.format("%.2f", ((double)orderGoodsSumDTO.getAvgPrice() / 100)),
                    String.format("%.3f", orderGoodsSumDTO.getTotalNum()),
                    String.format("%.2f", ((double)orderGoodsSumDTO.getTotalPrice() / 100)),
                    "0.00",
                    "0.00",
                    "0.00",
                    "0.00",
                    String.format("%.3f", orderGoodsSumDTO.getTotalNum()),
                    String.format("%.2f", ((double)orderGoodsSumDTO.getTotalPrice() / 100)),
                    "0.00",
                    String.format("%.2f", ((double)orderGoodsSumDTO.getTotalPrice() / 100))
            );

            newGoodsSaleList.add(newGoodsSale);

        }

        return newGoodsSaleList;
    }

}
