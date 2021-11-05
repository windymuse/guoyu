package com.windymuse.unimall.data.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.windymuse.unimall.data.domain.MemberLevelDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zengwei on 2021/10/6.
 */
public interface MemberLevelMapper extends BaseMapper<MemberLevelDO> {

    public List<MemberLevelDO> getMemberLevelList(
            @Param("id") Long id, @Param("name") String name,
            @Param("degree") Integer degree, @Param("percent") Integer percent,
            @Param("description") String description, @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    public Integer countMemberLevel(
            @Param("id") Long id, @Param("name") String name,
            @Param("degree") Integer degree, @Param("percent") Integer percent,
            @Param("description") String description);

}
