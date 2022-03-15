package com.github.bogdan.service;


import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class LocalDateService {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate getLocalDateByString(String text){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(text, formatter);
        return parsedDate;
    }

    public static void checkLocalDateFormat(String text){
        try {
            LocalDate parsedDate = LocalDate.parse(text, formatter);
        }catch (DateTimeParseException e){
            throw new WebException("Неправильный формат времени, дата должна быть записана как: ГГГГ-ММ-ДД",400);
        }
    }

    public static void checkAge(String dateOfBirthday){
        LocalDate date = LocalDate.parse(dateOfBirthday,formatter);
        Period period = Period.between(date,LocalDate.now());
        if(period.getYears() < 14){
            throw new WebException("Этот сервис могут использовать пользователи, достигшие 14 лет",403);
        }
    }

    public static void checkLocalDateTimeFormat(String text){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalTime parsedDate = LocalTime.parse(text, formatter);
        }catch (DateTimeParseException e){
            throw new WebException("Wrong time format, correct date format should be HH:mm"+'\n'+e.getMessage(),400);
        }
    }

    public static void checkValidDate(String dateOfEnrollment,String dateOfDrop){
        LocalDate first = LocalDate.parse(dateOfEnrollment);
        LocalDate second = LocalDate.parse(dateOfDrop);
        if(!first.isBefore(second) ||first.equals(second)){
            throw new WebException("Date of enrollment cannot be earlier the date of drop",400);
        }
    }


//    public static void checkDoesSuchDeadlineExist(int id){
//
//    }
}
