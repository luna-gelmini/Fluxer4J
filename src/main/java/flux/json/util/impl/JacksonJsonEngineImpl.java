package flux.json.util.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flux.api.entities.Message;
import flux.api.entities.channel.Channel;
import flux.json.util.JsonEngine;
import flux.json.util.mixin.ChannelMixIn;
import flux.json.util.mixin.MessageMixIn;

public class JacksonJsonEngineImpl implements JsonEngine {

    private final ObjectMapper objectMapper;

    public JacksonJsonEngineImpl() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.addMixIn(Channel.class, ChannelMixIn.class);
        this.objectMapper.addMixIn(Message.class, MessageMixIn.class);

        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar objeto para JSON", e);
        }
    }

    @Override
    public <T> T fromJsonString(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            System.err.println("Jackson Deserialization Exception for class " + clazz.getName() + ": " + e.getMessage());
            throw new RuntimeException("Erro ao desserializar JSON para objeto: " + clazz.getSimpleName(), e);
        }
    }

    @Override
    public <T> T fromJsonString(String jsonString, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(jsonString, typeReference);
        } catch (JsonProcessingException e) {
            System.err.println("Jackson Deserialization Exception for TypeReference " + typeReference.getType() + ": " + e.getMessage());
            throw new RuntimeException("Erro ao desserializar JSON para tipo genérico: " + typeReference.getType(), e);
        }
    }
}
