package com.axonactive.dojo.base.kafka_serializer.relative;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class RelativeDeserializer implements Deserializer {
    @Override
    public void configure(Map configs, boolean isKey) {}

    @Override
    public RelativeMessageDTO deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        new RelativeMessageDTO();
        RelativeMessageDTO relativeMessageDTO;
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
