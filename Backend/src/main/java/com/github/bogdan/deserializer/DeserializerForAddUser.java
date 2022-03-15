package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.model.Role;
import com.github.bogdan.model.User;
import com.google.i18n.phonenumbers.NumberParseException;
import com.j256.ormlite.dao.Dao;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.github.bogdan.service.ContactService.*;
import static com.github.bogdan.service.DeserializerService.*;
import static com.github.bogdan.service.LocalDateService.checkAge;
import static com.github.bogdan.service.LocalDateService.checkLocalDateFormat;
import static com.github.bogdan.service.UserService.*;

public class DeserializerForAddUser extends StdDeserializer<User> {

    public DeserializerForAddUser(Dao<User,Integer> userDao) {
        super(User.class);
        this.userDao = userDao;

    }
    private Dao<User,Integer> userDao;


    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            User u = new User();

            String fname = getStringFieldValue(node,"fname");
            u.setFname(fname);

            String lname = getStringFieldValue(node,"lname");
            u.setLname(lname);

            String email = getOldStringFieldValue(node,"email",null);
            if(email!=null) {
                u.setEmail(email);
                checkValidateEmail(email);
                checkIsEmailAlreadyInUse(email,userDao);
            }

            String phone = getOldStringFieldValue(node,"phone",null);
            if(phone!=null){
                u.setPhone(phone);
                checkValidatePhone(phone);
                checkIsPhoneAlreadyInUse(phone,userDao);
            }

            checkIsEmailPhoneNull(phone,email);

            boolean phoneIsShown = getOldBooleanFieldValue(node,"phoneIsShown",false);
            u.setPhoneIsShown(phoneIsShown);

            boolean emailIsShown =  getOldBooleanFieldValue(node,"emailIsShown",false);
            u.setEmailIsShown(emailIsShown);

            LocalDate localDate = LocalDate.now();
            u.setDateOfRegister(localDate.toString());

            String dateOfBirthday = getOldStringFieldValue(node,"dateOfBirthday",null);
            checkLocalDateFormat(dateOfBirthday);
            checkAge(dateOfBirthday);
            u.setDateOfBirthday(dateOfBirthday);

            u.setRole(Role.USER);

            String password = getStringFieldValue(node,"password");
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
            u.setPassword(hashedPassword);

            String address = getStringFieldValue(node,"address");
            u.setAddress(address);

            return u;

        } catch (SQLException | NumberParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
