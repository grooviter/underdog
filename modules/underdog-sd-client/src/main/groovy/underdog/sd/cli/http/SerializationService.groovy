package underdog.sd.cli.http

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.TupleConstructor

@TupleConstructor
class SerializationService {
  ObjectMapper objectMapper

  String toJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object)
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e)
    }
  }

    <T> T fromJson(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz)
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e)
    }
  }

  <T> T fromJson(InputStream inputStream, Class<T> clazz) {
    try {
      return objectMapper.readValue(inputStream, clazz)
    } catch (IOException e) {
      throw new RuntimeException(e)
    }
  }

}
