package com.axonactive.dojo.base.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@RequiredArgsConstructor
public class BaseDeserializer<T> implements Deserializer<T>{
    private final Class<T> entityDTOClass;

    @Override
    public void configure(Map configs, boolean isKey) {}

    @Override
    public T deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        T entityDTO = null;
        try {
            entityDTO = mapper.readValue(bytes, entityDTOClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return entityDTO;
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
