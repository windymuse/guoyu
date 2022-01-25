package com.windymuse.unimall.data.domain;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * Created by zengwei on 2021/11/26.
 */
@TableName("unimall_storage")
@Data
public class StorageDO extends SuperDO {

    private String key;

    private String name;

    private String type;

    private Integer size;

    private String url;

    private Boolean deleted;

}
