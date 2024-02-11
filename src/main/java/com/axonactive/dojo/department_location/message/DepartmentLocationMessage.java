package com.axonactive.dojo.department_location.message;

import com.axonactive.dojo.base.message.BaseMessage;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class DepartmentLocationMessage extends BaseMessage {
    public static final String NOT_FOUND_LOCATION = "This location is not existed";

    public static final String EXISTED_LOCATION = "This location is existed in the department";
}
