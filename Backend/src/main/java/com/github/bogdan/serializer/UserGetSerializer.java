package com.github.bogdan.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.bogdan.model.User;

import java.io.IOException;

public class UserGetSerializer extends StdSerializer<User> {
    private int id;
    public UserGetSerializer(int id) {
        super(User.class);
        this.id = id;
    }

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id",user.getId());
        jsonGenerator.writeStringField("fname",user.getFname());
        jsonGenerator.writeStringField("lname",user.getLname());
        jsonGenerator.writeStringField("dateOfBirthday",user.getDateOfBirthday());
        jsonGenerator.writeStringField("dateOfRegister",user.getDateOfRegister());
        if(user.isPhoneIsShown() || id==user.getId()){
            jsonGenerator.writeStringField("phone",user.getPhone());
        }
        if(user.isEmailIsShown() || id==user.getId()) {
            jsonGenerator.writeStringField("email", user.getEmail());
        }

        jsonGenerator.writeObjectField("address",user.getAddress());
        jsonGenerator.writeEndObject();
    }
}
