package com.windymuse.unimall.admin.api.excel;

import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.core.annotation.ExcelModel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@ExcelModel(sheetName = "日销售汇总表")
public class DaySale {

    @ExcelColumn(order = 0, title = "日期", format = "yyyy-MM-dd")
    private Date time;

    @ExcelColumn(order = 1, title = "销售单号")
    private String orderNo;

    @ExcelColumn(order = 2, title = "客户")
    private String customerName;

    @ExcelColumn(order = 3,title = "品名")
    private String spuName;

    @ExcelColumn(order = 4, title = "规格")
    private String skuName;

    @ExcelColumn(order = 5, title = "单价（元/g）")
    private String price;

    @ExcelColumn(order = 6, title = "重量（g）")
    private String weight;

    @ExcelColumn(order = 7, title = "金额（元）")
    private String totalPrice;

    @ExcelColumn(order = 8, title = "付款金额（元）")
    private String realPrice;

    @ExcelColumn(order = 9, title = "备注")
    private String other;


//    @IgnoreColumn
//    private String hobby;
}