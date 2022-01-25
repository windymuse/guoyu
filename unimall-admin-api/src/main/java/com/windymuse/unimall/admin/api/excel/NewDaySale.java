package com.windymuse.unimall.admin.api.excel;

import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.core.annotation.ExcelModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@ExcelModel(sheetName = "零售销售明细报表")
public class NewDaySale {

    @ExcelColumn(order = 0, title = "分店编码")
    private String mallNo;

    @ExcelColumn(order = 1, title = "分店名称")
    private String mallName;

    @ExcelColumn(order = 2, title = "营业员编码")
    private String sellerNo;

    @ExcelColumn(order = 3, title = "营业员名称")
    private String sellerName;

    @ExcelColumn(order = 4, title = "机号")
    private String machineNo;

    @ExcelColumn(order = 5, title = "收银员编号")
    private String cashierNo;

    @ExcelColumn(order = 6, title = "收银员名称")
    private String cashierName;

    @ExcelColumn(order = 7, title = "单号")
    private String orderNo;

    @ExcelColumn(order = 8, title = "销售时间", format = "yyyy-MM-dd hh:mm:ss")
    private Date time;

    @ExcelColumn(order = 9, title = "货号")
    private String goodsNo;

    @ExcelColumn(order = 10,title = "品名")
    private String spuName;

    @ExcelColumn(order = 11, title = "单位")
    private String unit;

    @ExcelColumn(order = 12, title = "规格")
    private String skuName;

    @ExcelColumn(order = 13, title = "条码")
    private String barCode;


    @ExcelColumn(order = 14, title = "类别编号")
    private String categoryNo;

    @ExcelColumn(order = 15, title = "类别名称")
    private String categoryName;

    @ExcelColumn(order = 16, title = "品牌编号")
    private String brandNo;

    @ExcelColumn(order = 17, title = "品牌名称")
    private String brandName;

    @ExcelColumn(order = 18, title = "销售方式")
    private String sellType;

    @ExcelColumn(order = 19, title = "计价方式")
    private String cashierType;

    @ExcelColumn(order = 20, title = "销售数量")
    private String sellNum;

    @ExcelColumn(order = 21, title = "合计数量")
    private String totalNum;

    @ExcelColumn(order = 22, title = "售价")
    private String sellPrice;

    @ExcelColumn(order = 23, title = "原价")
    private String originPrice;

    @ExcelColumn(order = 24, title = "金额")
    private String totalPrice;

    @ExcelColumn(order = 25, title = "优惠金额")
    private String discountPrice;

    @ExcelColumn(order = 26, title = "成本价")
    private String costPrice;

    @ExcelColumn(order = 27, title = "毛利")
    private String grossProfit;

    @ExcelColumn(order = 28, title = "备注")
    private String other;

}