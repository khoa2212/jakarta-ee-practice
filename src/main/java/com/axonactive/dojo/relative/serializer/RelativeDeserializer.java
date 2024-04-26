package com.axonactive.dojo.relative.serializer;

import com.axonactive.dojo.base.serializer.BaseDeserializer;
import com.axonactive.dojo.relative.dto.RelativeMessageDTO;

public class RelativeDeserializer extends BaseDeserializer<RelativeMessageDTO> {
    public RelativeDeserializer() {
        super(RelativeMessageDTO.class);
    }
}
