package com.axonactive.dojo.relative.serializer;

import com.axonactive.dojo.relative.dto.RelativeMessageDTO;
import com.axonactive.dojo.relative.entity.Relative;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class RelativeDeserializer implements Deserializer {
    @Override
    public void configure(Map configs, boolean isKey) {}

    @Override
    public RelativeMessageDTO deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        RelativeMessageDTO relativeMessageDTO = null;
        try {
            relativeMessageDTO = mapper.readValue(bytes, RelativeMessageDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return relativeMessageDTO;
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
