package com.axonactive.dojo.project.mapper;

import com.axonactive.dojo.base.mapper.BaseMapper;
import com.axonactive.dojo.project.dto.ProjectDTO;
import com.axonactive.dojo.project.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi")
public interface ProjectMapper extends BaseMapper<ProjectDTO, Project> {

}
