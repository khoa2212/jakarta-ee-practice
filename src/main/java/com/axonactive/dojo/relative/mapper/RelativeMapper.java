package com.axonactive.dojo.relative.mapper;

import com.axonactive.dojo.base.mapper.BaseMapper;
import com.axonactive.dojo.relative.dto.RelativeDTO;
import com.axonactive.dojo.relative.entity.Relative;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface RelativeMapper extends BaseMapper<RelativeDTO, Relative> {

}
