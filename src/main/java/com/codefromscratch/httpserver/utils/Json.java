package com.codefromscratch.httpserver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Json {
   private static ObjectMapper objMapper = defaultObjMapper(); 

   private static ObjectMapper defaultObjMapper()
   {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
   }

   public static JsonNode parse(String JsonPath) throws JsonMappingException, JsonProcessingException{
        return objMapper.readTree(JsonPath);
   }

   public static <T> T fromJson(JsonNode node, Class<T> toClass) throws JsonProcessingException, IllegalArgumentException{
    return objMapper.treeToValue(node, toClass);
   }

   public static JsonNode toJson(Object obj){
    return objMapper.valueToTree(obj);
   }

   public static String stringify(JsonNode node) throws JsonProcessingException
   {
    return generateJson(node, false);
   }
   public static String stringifyPretty(JsonNode node) throws JsonProcessingException
   {
    return generateJson(node, true);
   }

   private static String generateJson(Object o, boolean pretty) throws JsonProcessingException{
    ObjectWriter writer = objMapper.writer();
    if(pretty){
        writer = writer.with(SerializationFeature.INDENT_OUTPUT);
    }
    return writer.writeValueAsString(o);
   }


}
