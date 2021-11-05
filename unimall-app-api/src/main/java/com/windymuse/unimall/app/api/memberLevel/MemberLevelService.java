package com.windymuse.unimall.app.api.memberLevel;

import com.windymuse.unimall.core.annotation.HttpMethod;
import com.windymuse.unimall.core.annotation.HttpOpenApi;
import com.windymuse.unimall.core.annotation.HttpParam;
import com.windymuse.unimall.core.annotation.HttpParamType;
import com.windymuse.unimall.core.annotation.param.NotNull;
import com.windymuse.unimall.core.annotation.param.Range;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.domain.MemberLevelDO;
import com.windymuse.unimall.data.model.Page;

import java.util.List;

/**
 * Description:
 * User: Zengwei
 * Date: 2021-10-07
 * Time: 下午7:30
 */

@HttpOpenApi(group = "member_level", description = "会员等级")
public interface MemberLevelService {

    @HttpMethod(description = "查询")
    public List<MemberLevelDO> getMemberLevel(
            @HttpParam(name = "id", type = HttpParamType.COMMON, description = "用户ID") Long id,
            @HttpParam(name = "name", type = HttpParamType.COMMON, description = "会员等级称号") String name,
            @HttpParam(name = "degree", type = HttpParamType.COMMON, description = "会员等级") Integer degree,
            @HttpParam(name = "percent", type = HttpParamType.COMMON, description = "会员等级通用折扣") Integer percent,
            @HttpParam(name = "description", type = HttpParamType.COMMON, description = "会员等级描述") String description,
            @Range(min = 1) @HttpParam(name = "pageNo", type = HttpParamType.COMMON, description = "当前页码") Integer pageNo,
            @Range(min = 1) @HttpParam(name = "limit", type = HttpParamType.COMMON, description = "页码长度") Integer limit) throws ServiceException;
}
