package com.windymuse.unimall.data.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by rize on 2019/7/1.
 */
@Data
public class ShunFenDTO {
    String orderId;
    String language;
    List<Cargo> cargoDetails;
    List<ContactInfo> contactInfoList;


    /**
     * 货物
     */
    @Data
    public static class Cargo {
        Double count;
        String unit;
        Double weight;
        Double amount;
        String currency;
        String name;
        String sourceArea;
    }


    /**
     * 联系人
     */
    @Data
    public static class ContactInfo {
        String address;
        String contact;
        /**
         * 地址类型：
         * 1，寄件方信息 2，到件方信息
         */
        Number contactType;
        String country;
        String postCode;
        String tel;
    }


}
