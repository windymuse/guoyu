package com.windymuse.unimall.admin.api.excel;

import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.core.annotation.ExcelModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@ExcelModel(sheetName = "商品日汇总表")
public class NewGoodsSale {
    @ExcelColumn(order = 0, title = "日期", format = "yyyy-MM-dd")
    private Date time;
    @ExcelColumn(order = 1, title = "品名")
    private String spuName;
    @ExcelColumn(order = 2, title = "类别")
    private String skuName;
    @ExcelColumn(order = 3, title = "品牌")
    private String brandName;
    @ExcelColumn(order = 4, title = "原价")
    private String originPrice;
    @ExcelColumn(order = 5, title = "均价")
    private String avePrice;
    @ExcelColumn(order = 6, title = "销售数量")
    private String sellNum;
    @ExcelColumn(order = 7, title = "销售金额")
    private String sellTotalPrice;
    @ExcelColumn(order = 8, title = "退货数量")
    private String returnNum;
    @ExcelColumn(order = 9, title = "退货金额")
    private String returnPrice;
    @ExcelColumn(order = 10, title = "赠送数量")
    private String giveNum;
    @ExcelColumn(order = 11, title = "赠送金额")
    private String givePrice;
    @ExcelColumn(order = 12, title = "合计数量")
    private String totalNum;
    @ExcelColumn(order = 13, title = "合计金额")
    private String totalPrice;
    @ExcelColumn(order = 14, title = "差额")
    private String differ;
    @ExcelColumn(order = 15, title = "实收金额")
    private String actualPrice;

}