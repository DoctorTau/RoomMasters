package com.github.bogdan.service;

import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.User;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.j256.ormlite.dao.Dao;
import org.apache.commons.validator.routines.EmailValidator;

import java.sql.SQLException;

public class ContactService {



    public static void checkValidatePhone(String phone) throws NumberParseException {
        if (!doesPhoneAvailable(phone)){
            throw new WebException("Такого номера телефона не существует",400);
        }
    }

    public static void checkValidateEmail(String email){
        if(!doesEmailAvailable(email)){
            throw new WebException("Такой почты не существет",400);
        }
    }

    public static boolean doesEmailAvailable(String email){
        email = email.trim();
        EmailValidator eValidator = EmailValidator.getInstance();
        if(eValidator.isValid(email)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean doesPhoneAvailable(String swissNumberStr) throws NumberParseException {
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(swissNumberStr, null);
            return (phoneUtil.isValidNumber(swissNumberProto));
        }catch (NumberParseException e){
            throw new WebException("Такого номера телефона не существует",400);
        }

    }

    public static void checkIsPhoneAlreadyInUse(String phone,Dao<User, Integer> userDao) throws SQLException {
        for(User user: userDao.queryForAll()){
            if(user.getPhone()!= null) {
                if (user.getPhone().equals(phone)) {
                    throw new WebException("Данный номер уже используется", 400);
                }
            }
        }
    }

    public static void checkIsPhoneAlreadyInUse(String phone,int userId,Dao<User, Integer> userDao) throws SQLException {
        for(User user: userDao.queryForAll()){
            if(user.getPhone()!= null){
                if(user.getPhone().equals(phone) && user.getId() != userId){
                    throw new WebException("Данный номер уже используется",400);
                }
            }
        }
    }

    public static void checkIsEmailAlreadyInUse(String email,Dao<User, Integer> userDao) throws SQLException {
        for(User user: userDao.queryForAll()){
            if(user.getEmail()!= null){
                if(user.getEmail().equals(email)){
                    throw new WebException("Данный email уже используется",400);
                }
            }

        }
    }

    public static void checkIsEmailAlreadyInUse(String email,int userId,Dao<User, Integer> userDao) throws SQLException {
        for(User user: userDao.queryForAll()){
            if(user.getEmail()!= null) {
                if (user.getEmail().equals(email) && user.getId() != userId) {
                    throw new WebException("Данный почтовый адрес уже используется", 400);
                }
            }
        }
    }


}
