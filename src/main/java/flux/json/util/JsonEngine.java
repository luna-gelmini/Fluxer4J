package flux.json.util;

import com.fasterxml.jackson.core.type.TypeReference;

public interface JsonEngine {
    String toJsonString(Object object);

    <T> T fromJsonString(String jsonString, TypeReference<T>  typeReference);

    <T> T fromJsonString(String jsonString, Class<T> clazz);

}
