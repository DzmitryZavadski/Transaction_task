package org.example.config.jsonparsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.example.config.jsonparsing.pojo.SimpleTestJsonPOJO;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonTest {

    private String jsonSourceUser1 = "{\n" +
            "  \"id\": 1,\n" +
            "  \"name\": \"Dima\",\n" +
            "  \"age\": 25,\n" +
            "  \"email\": \"dima@example.com\"\n" +
            "}";

    @Test
    void parseJson() throws IOException {
        JsonNode node = Json.parseJson(jsonSourceUser1);
        assertEquals(node.get("id").asText(), "1");
        assertEquals(node.get("name").asText(), "Dima");
        assertEquals(node.get("age").asText(), "25");
        assertEquals(node.get("email").asText(), "dima@example.com");
    }

    @Test
    void fromJson() throws IOException {
        JsonNode node = Json.parseJson(jsonSourceUser1);
        SimpleTestJsonPOJO pojo = Json.fromJson(node, SimpleTestJsonPOJO.class);
        assertEquals(pojo.getId(), 1);
        assertEquals(pojo.getName(), "Dima");
        assertEquals(pojo.getAge(), "25");
        assertEquals(pojo.getEmail(), "dima@example.com");
    }

    @Test
    void toJson() {
        SimpleTestJsonPOJO pojo = new SimpleTestJsonPOJO();
        pojo.setId(1);
        pojo.setName("Dima");

        JsonNode jsonNode = Json.toJson(pojo);
        assertEquals(jsonNode.get("id").asText(), "1");
        assertEquals(jsonNode.get("name").asText(), "Dima");
    }


    @Test
    void stringify() throws JsonProcessingException {
        SimpleTestJsonPOJO pojo = new SimpleTestJsonPOJO();
        pojo.setId(1);
        pojo.setName("Dima");
        pojo.setAge("65");
        pojo.setEmail("email@email.com");

        JsonNode jsonNode = Json.toJson(pojo);

        System.out.println(Json.stringify(jsonNode));
        System.out.println(Json.prettyPrint(jsonNode));
    }
}
