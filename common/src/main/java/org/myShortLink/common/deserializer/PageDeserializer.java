package org.myShortLink.common.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.data.domain.Page;

import java.io.IOException;

public class PageDeserializer extends JsonDeserializer<Page<?>> {

    @Override
    public Page<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        return null;
    }
}
