package com.axonactive.dojo.base.kafka_serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class BaseSerializer implements Serializer {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, Object o) {
        mapper.findAndRegisterModules();
        byte[] bytes = null;

        try {
            bytes = mapper.writeValueAsString(o).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bytes;
    }

    @Override
    public void close() {}
}
