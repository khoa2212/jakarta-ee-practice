package com.axonactive.dojo.department.message;

import com.axonactive.dojo.base.message.BaseMessage;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class DepartmentMessage extends BaseMessage {

    public static final String EXISTED_NAME = "This department's name is already existed";

    public static final String NOT_FOUND_DEPARTMENT = "This department is not existed";

}
