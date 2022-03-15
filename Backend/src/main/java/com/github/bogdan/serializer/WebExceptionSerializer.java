package com.github.bogdan.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.bogdan.exception.WebException;

import java.io.IOException;

public class WebExceptionSerializer extends StdSerializer<WebException> {

    public WebExceptionSerializer() {
        super(WebException.class);
    }

    @Override
    public void serialize(WebException e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("message",e.getMessage());
        jsonGenerator.writeNumberField("status",e.getStatus());
        jsonGenerator.writeEndObject();
    }
}
