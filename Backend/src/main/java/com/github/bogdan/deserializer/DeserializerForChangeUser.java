package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.model.User;
import com.google.i18n.phonenumbers.NumberParseException;
import com.j256.ormlite.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.service.ContactService.*;
import static com.github.bogdan.service.ContactService.checkIsPhoneAlreadyInUse;
import static com.github.bogdan.service.DeserializerService.*;
import static com.github.bogdan.service.LocalDateService.checkAge;
import static com.github.bogdan.service.LocalDateService.checkLocalDateFormat;

public class DeserializerForChangeUser extends StdDeserializer<User> {
    static Logger LOGGER = LoggerFactory.getLogger(DeserializerForChangeUser.class);

    public DeserializerForChangeUser(int id,Dao<User, Integer> userDao) {
        super (User.class);
        this.id = id;
        this.userDao = userDao;
    }
    private Dao<User, Integer> userDao;
    private int id;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    @Override

    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            User userBase = userDao.queryForId(id);
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            User u = new User();


            String fname = getOldStringFieldValue(node,"fname",userBase.getFname());
            u.setFname(fname);

            String lname = getOldStringFieldValue(node,"lname",userBase.getLname());
            u.setLname(lname);


            String email = getOldStringFieldValue(node,"email",userBase.getEmail());
            u.setEmail(email);

            String address = getOldStringFieldValue(node,"address",userBase.getAddress());
            u.setAddress(address);

            checkValidateEmail(email);
            checkIsEmailAlreadyInUse(email,id,userDao);

            String phone = getOldStringFieldValue(node,"phone",userBase.getPhone());
            u.setPhone(phone);
            checkValidatePhone(phone);
            checkIsPhoneAlreadyInUse(phone,id,userDao);

            u.setDateOfRegister(userBase.getDateOfRegister());

            String dateOfBirthday = getOldStringFieldValue(node,"dateOfBirthday",userBase.getDateOfBirthday());
            checkLocalDateFormat(dateOfBirthday);
            checkAge(dateOfBirthday);
            u.setDateOfBirthday(dateOfBirthday);

            u.setRole(userBase.getRole());

            String password = getOldPasswordFieldValue(node,"password",userBase.getPassword());

            u.setPassword(password);
            u.setId(id);
            return u;

        } catch (SQLException | NumberParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
