package com.axonactive.dojo.relative.mapper;

import com.axonactive.dojo.base.kafka_serializer.relative.RelativeMessageDTO;
import com.axonactive.dojo.base.mapper.BaseMapper;
import com.axonactive.dojo.relative.dto.RelativeDTO;
import com.axonactive.dojo.relative.entity.Relative;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface RelativeMapper extends BaseMapper<RelativeDTO, Relative> {
    @Mapping(target = "employeeId", source = "relative.employee.id")
    RelativeMessageDTO toRelativeMessageDTO(Relative relative);
}
