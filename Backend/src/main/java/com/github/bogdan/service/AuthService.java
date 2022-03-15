package com.github.bogdan.service;

import com.github.bogdan.controller.MainController;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class AuthService {
    static Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    public static boolean authorization(Context ctx,Dao<User, Integer> userDao) throws SQLException {
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        for(User u:userDao.queryForAll()){
            if(u.getPhone() != null){
                if(u.getPhone().equals(login)){
                    if(!BCrypt.checkpw(password,u.getPassword())){
                        return false;
                    }
                    return true;
                }
            }
            if(u.getEmail() != null){
                if(u.getEmail().equals(login)){
                    if(!BCrypt.checkpw(password,u.getPassword())){
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }
    public static void checkAuthorization(Context ctx,Dao<User, Integer> userDao) throws SQLException {
        if(!authorization(ctx,userDao))
            throw new WebException("Ошибка авторизации",400);
    }

}
