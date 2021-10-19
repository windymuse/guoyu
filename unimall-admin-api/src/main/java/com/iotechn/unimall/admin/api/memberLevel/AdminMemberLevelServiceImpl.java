package com.iotechn.unimall.admin.api.memberLevel;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.iotechn.unimall.admin.api.memberLevel.AdminMemberLevelService;
import com.iotechn.unimall.core.exception.AdminServiceException;
import com.iotechn.unimall.core.exception.ExceptionDefinition;
import com.iotechn.unimall.core.exception.ServiceException;
import com.iotechn.unimall.data.domain.MemberLevelDO;
import com.iotechn.unimall.data.mapper.MemberLevelMapper;
import com.iotechn.unimall.data.model.Page;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * MemberLevel: kbq
 * Date: 2019-07-11
 * Time: 下午7:57
 */
@Service
public class AdminMemberLevelServiceImpl implements AdminMemberLevelService {

    @Autowired
    private MemberLevelMapper memberLevelMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addMemberLevel(Long adminId, MemberLevelDO memberLevel) throws ServiceException {
        if (memberLevel == null){
            throw new AdminServiceException(ExceptionDefinition.MEMBER_LEVEL_INFORMATION_MISSING);
        }
        if(memberLevel.getName() == null){
            throw new AdminServiceException(ExceptionDefinition.MEMBER_LEVEL_INFORMATION_MISSING);
        }
        if(memberLevel.getDegree() == null){
            throw new AdminServiceException(ExceptionDefinition.MEMBER_LEVEL_INFORMATION_MISSING);
        }
        if(memberLevel.getPercent() == null){
            throw new AdminServiceException(ExceptionDefinition.MEMBER_LEVEL_INFORMATION_MISSING);
        }
        if(memberLevel.getImg() == null){
            throw new AdminServiceException(ExceptionDefinition.MEMBER_LEVEL_INFORMATION_MISSING);
        }
        if(memberLevel.getDescription() == null){
            throw new AdminServiceException(ExceptionDefinition.MEMBER_LEVEL_INFORMATION_MISSING);
        }
        if(memberLevelMapper.selectCount(new EntityWrapper<MemberLevelDO>().eq("name",memberLevel.getName())) > 0){
            throw new AdminServiceException(ExceptionDefinition.MEMBER_LEVEL_NAME_EXIST);
        }
        Date now = new Date();
        memberLevel.setId(null);
        memberLevel.setGmtCreate(now);
        memberLevel.setGmtUpdate(now);
        return memberLevelMapper.insert(memberLevel) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteMemberLevel(Long adminId, Long id, String name) throws ServiceException {
        return memberLevelMapper.delete(new EntityWrapper<MemberLevelDO>()
                .eq("id", id)
                .eq("name",name)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateMemberLevel(Long adminId, MemberLevelDO memberLevel) throws ServiceException {
        if (memberLevel == null || memberLevel.getId() == null){
            throw new AdminServiceException(ExceptionDefinition.MEMBER_LEVEL_INFORMATION_MISSING);
        }
        if(memberLevel.getName() == null){
            throw new AdminServiceException(ExceptionDefinition.MEMBER_LEVEL_INFORMATION_MISSING);
        }
        Date now = new Date();
        memberLevel.setGmtUpdate(now);
        MemberLevelDO memberLevelDO  = memberLevelMapper.selectById(memberLevel.getId());
        if(memberLevelDO.getName() != null){
            return memberLevelMapper.updateById(memberLevel) > 0;
        }
        return memberLevelMapper.updateById(memberLevel) > 0;
    }

    @Override
    public Page<MemberLevelDO> getMemberLevel(Long adminId, Long id, String name, Integer degree, Integer percent, String description,Integer pageNo,Integer limit) throws ServiceException {
        Integer count = memberLevelMapper.countMemberLevel(id,name,degree,percent,description);
        List<MemberLevelDO> memberLevelDOList = memberLevelMapper.getMemberLevelList(id,name,degree,percent,description,limit*(pageNo-1),limit);
        Page<MemberLevelDO> page = new Page<MemberLevelDO>(memberLevelDOList,pageNo,limit,count);
        return page;
    }
}
