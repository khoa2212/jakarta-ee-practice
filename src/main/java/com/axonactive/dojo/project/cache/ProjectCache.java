package com.axonactive.dojo.project.cache;

import com.axonactive.dojo.base.cache.BaseCache;
import com.axonactive.dojo.project.entity.Project;

import javax.ejb.Singleton;

@Singleton
public class ProjectCache extends BaseCache<Project> {
    public ProjectCache() {
        super();
    }
}
