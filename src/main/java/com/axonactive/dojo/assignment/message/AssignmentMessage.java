package com.axonactive.dojo.assignment.message;

import com.axonactive.dojo.base.message.BaseMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssignmentMessage extends BaseMessage {
    public static final String NOT_FOUND_ASSIGNMENT = "This assignment is not existed";
    public static final String EXISTED_ASSIGNMENT = "This assignment is already existed";
}
