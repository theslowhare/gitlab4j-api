package org.gitlab4j.api;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.gitlab4j.api.utils.JacksonJson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {
    
    private static JacksonJson jacksonJson;
    static {
        jacksonJson = new JacksonJson();
        jacksonJson.getObjectMapper().configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        jacksonJson.getObjectMapper().configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
    }
    

    static <T> T unmarshal(Class<T> returnType, String filename) throws JsonParseException, JsonMappingException, IOException {
        InputStreamReader reader = new InputStreamReader(GitLabApi.class.getResourceAsStream(filename));
        return (jacksonJson.unmarshal(returnType, reader));
    }

    static <T> List<T> unmarshalList(Class<T> returnType,  String filename) throws JsonParseException, JsonMappingException, IOException {
        InputStreamReader reader = new InputStreamReader(GitLabApi.class.getResourceAsStream(filename));
        return (jacksonJson.unmarshalList(returnType, reader));
    }

    static <T> Map<String, T> unmarshalMap(Class<T> returnType,  String filename) throws JsonParseException, JsonMappingException, IOException {
        InputStreamReader reader = new InputStreamReader(GitLabApi.class.getResourceAsStream(filename));
        return (jacksonJson.unmarshalMap(returnType, reader));
    }

    static <T> boolean compareJson(T apiObject, String filename) throws IOException {

        InputStreamReader reader = new InputStreamReader(GitLabApi.class.getResourceAsStream(filename));
        String objectJson = jacksonJson.marshal(apiObject);
        JsonNode tree1 = jacksonJson.getObjectMapper().readTree(objectJson.getBytes());
        JsonNode tree2 = jacksonJson.getObjectMapper().readTree(reader);

        boolean sameJson = tree1.equals(tree2);
        if (!sameJson) {
            System.err.println("JSON did not match:");
            sortedDump(tree1);
            sortedDump(tree2);
        }

        return (sameJson);
    }

    static void sortedDump(final JsonNode node) throws JsonProcessingException {
        final Object obj = jacksonJson.getObjectMapper().treeToValue(node, Object.class);
        System.out.println(jacksonJson.getObjectMapper().writeValueAsString(obj));
        System.out.flush();
    }
}
