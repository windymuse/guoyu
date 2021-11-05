package com.windymuse.unimall.app.api.memberLevel;

import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.domain.MemberLevelDO;
import com.windymuse.unimall.data.mapper.MemberLevelMapper;
import com.windymuse.unimall.data.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * MemberLevel: kbq
 * Date: 2019-07-11
 * Time: 下午7:57
 */
@Service
public class MemberLevelServiceImpl implements MemberLevelService {

    @Autowired
    private MemberLevelMapper memberLevelMapper;

    @Override
    public List<MemberLevelDO> getMemberLevel(Long id, String name, Integer degree, Integer percent, String description,Integer pageNo,Integer limit) throws ServiceException {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(degree);
        System.out.println();
        System.out.println();
        System.out.println();
        Integer count = memberLevelMapper.countMemberLevel(id, name, degree, percent, description);

        Map<String, Object> aMap = new HashMap<String, Object>();
        aMap.put("degree", degree);
        List<MemberLevelDO> memberLevelDOList = memberLevelMapper.selectByMap(aMap);
//        List<MemberLevelDO> memberLevelDOList = memberLevelMapper.getMemberLevelList(id,name,degree,percent,description,limit*(pageNo-1),limit);
//        Page<MemberLevelDO> page = new Page<MemberLevelDO>(memberLevelDOList,pageNo,limit,count);
        return memberLevelDOList;
    }
}
