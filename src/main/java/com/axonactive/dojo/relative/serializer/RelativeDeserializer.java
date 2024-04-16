package com.axonactive.dojo.relative.serializer;

import com.axonactive.dojo.relative.entity.Relative;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class RelativeDeserializer implements Deserializer {
    @Override
    public void configure(Map configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public Relative deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        Relative relative = null;
        try {
            relative = mapper.readValue(bytes, Relative.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return relative;
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
