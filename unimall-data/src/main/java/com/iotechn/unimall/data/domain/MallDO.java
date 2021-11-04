package com.iotechn.unimall.data.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: kbq
 * Date: 2019-07-08
 * Time: 上午8:30
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("unimall_mall")
public class MallDO extends SuperDO {

    private String title;

    @TableField("logo_url")
    private String logoUrl;

    private String description;

    private String address;

    @TableField("show_type")
    private Integer showType;

    private Double longitude;

    private Double latitude;
}
