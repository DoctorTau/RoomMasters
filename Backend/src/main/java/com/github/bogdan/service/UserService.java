package com.github.bogdan.service;

import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Role;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserService {
    public static void checkIsUserAdmin(User user){
        if(user.getRole() != Role.ADMIN){
            throw new WebException("You aren't admin",400);
        }
    }
    public static boolean checkBooleanIsUserAdmin(int userId,Dao<User, Integer> userDao) throws SQLException {
        if(userDao.queryForId(userId).getRole() == Role.ADMIN){
            return true;
        }
        return false;
    }
    public static void checkIsUserAdmin(int userId,Dao<User, Integer> userDao) throws SQLException {
        if(userDao.queryForId(userId).getRole() != Role.ADMIN){
            throw new WebException("You aren't admin",400);
        }
    }
    public static void checkIsEmailPhoneNull(String phone,String email){
        if(phone == null && email == null){
            throw new WebException("Одно из полей \"phone\" или \"email\" должно быть заполнено",400);
        }
    }


    public static void checkIsUserAdmin(Context ctx,Dao<User, Integer> userDao) throws SQLException {
        checkIsUserAdmin(getUserByLogin(ctx.basicAuthCredentials().getUsername(),userDao));
    }


    public static void checkValidLogin(String login) {
        for(int i = 0; i < login.length();i++){
            if(login.charAt(i) >= 'A' && login.charAt(i) <= 'Z' || login.charAt(i) >= 'a' && login.charAt(i) <= 'z'){}
        }
    }

    public static User getUserByLogin(String login,Dao<User, Integer> userDao) throws SQLException {
        for(User user: userDao.queryForAll()){
            if(user.getPhone()!=null){
                if(user.getPhone().equals(login)){
                    return user;
                }
            }
            if(user.getEmail()!=null){
                if(user.getEmail().equals(login)){
                    return user;
                }
            }
        }
        return null;
    }

    public static User getUserById(int id,Dao<User, Integer> userDao) throws SQLException {
        return userDao.queryForId(id);
    }

    public static void checkDoesSuchUserExist(int id,Dao<User, Integer> userDao) throws SQLException {
        if(userDao.queryForId(id)==null){
            throw new WebException("User with such id isn't exist",400);
        }
    }
    public static ArrayList<User> getUsers(Dao<User,Integer> userDao) throws SQLException {
        return new ArrayList<>(userDao.queryForAll());

    }


}