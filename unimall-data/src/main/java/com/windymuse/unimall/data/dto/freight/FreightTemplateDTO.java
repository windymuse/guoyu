package com.windymuse.unimall.data.dto.freight;

import com.windymuse.unimall.data.domain.FreightTemplateCarriageDO;
import com.windymuse.unimall.data.domain.FreightTemplateDO;
import com.windymuse.unimall.data.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: kbq
 * Date: 2019-07-07
 * Time: 下午3:09
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FreightTemplateDTO extends SuperDTO {

    private FreightTemplateDO freightTemplateDO;

    private List<FreightTemplateCarriageDO> freightTemplateCarriageDOList;

}
