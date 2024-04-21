package com.axonactive.dojo.relative.message;

import com.axonactive.dojo.base.message.BaseMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RelativeMessage extends BaseMessage {
    public static final String RELATIVE_NOT_FOUND = "Relative not found";
}
