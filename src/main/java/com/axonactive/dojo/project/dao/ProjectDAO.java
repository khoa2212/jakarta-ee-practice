package com.axonactive.dojo.project.dao;

import com.axonactive.dojo.assignment.entity.Assignment;
import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.project.entity.Project;

public class ProjectDAO extends BaseDAO<Project> {
    public ProjectDAO() {
        super(Project.class);
    }
}
