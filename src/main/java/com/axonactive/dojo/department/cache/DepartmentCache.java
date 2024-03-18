package com.axonactive.dojo.department.cache;

import com.axonactive.dojo.base.cache.BaseCache;
import com.axonactive.dojo.department.entity.Department;

import javax.ejb.Singleton;

@Singleton
public class DepartmentCache extends BaseCache<Department> {
    public DepartmentCache() {
        super();
    }
}
