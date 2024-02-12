package com.axonactive.dojo.employee.message;

import com.axonactive.dojo.base.message.BaseMessage;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class EmployeeMessage extends BaseMessage {
    public static final String NOT_FOUND_EMPLOYEE = "This employee is not existed";
}
