package com.axonactive.dojo.assignment.message;

import com.axonactive.dojo.base.message.BaseMessage;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class AssignmentMessage extends BaseMessage {
    public static final String NOT_FOUND_ASSIGNMENT = "This assignment is not existed";
}
