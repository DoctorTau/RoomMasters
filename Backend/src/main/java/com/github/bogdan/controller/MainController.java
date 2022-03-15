package com.github.bogdan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.deserializer.*;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.*;
import com.github.bogdan.serializer.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.github.bogdan.service.AuthService.checkAuthorization;
import static com.github.bogdan.service.CtxService.*;

import static com.github.bogdan.service.SortingService.getByQueryParams;
import static com.github.bogdan.service.UserService.*;

public class MainController {
    static Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    private static DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(DatabasePath.getPath());

    private static Dao<User, Integer> userDao;

    static {
        try {
            userDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(), User.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static <T> void add(Context ctx, Dao<T, Integer> dao, Class<T> clazz) throws JsonProcessingException, SQLException {
        ctx.header("content-type:app/json");
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();

        if (clazz == User.class) {
            simpleModule.addDeserializer(User.class, new DeserializerForAddUser(userDao));
        } else {
            checkDoesBasicAuthEmpty(ctx);
            checkAuthorization(ctx, userDao);
        }

        checkBodyRequestIsEmpty(ctx);
        String body = ctx.body();
        objectMapper.registerModule(simpleModule);
        Object obj = objectMapper.readValue(body, clazz);
        dao.create((T) obj);
        if (clazz != User.class) {
            created(ctx);
        } else {
            simpleModule.addSerializer(User.class, new UserGetSerializer(((User) obj).getId()));
            ctx.result(objectMapper.writeValueAsString(obj));
        }
    }

    public static <T> void get(Context ctx, Dao<T, Integer> dao, Class<T> clazz) throws JsonProcessingException, SQLException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException {
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        int userId = 0;
        if (ctx.basicAuthCredentialsExist()) {
            if (getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao) != null) {
                userId = getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getId();
            }
        }
        simpleModule.addSerializer(User.class, new UserGetSerializer(userId));

        objectMapper.registerModule(simpleModule);


        ArrayList<String> params = new ArrayList<>();
        if (clazz == User.class) {
            User u = new User();
            params.addAll(u.getQueryParams());
        }

        String serialized;


        if(doesQueryParamsEmpty(ctx,params)){
            serialized = objectMapper.writeValueAsString(getUsers(userDao));
        }else {
            serialized = objectMapper.writeValueAsString(getByQueryParams(userId, userDao,dao, clazz, params, ctx));
        }
        ctx.result(serialized);
    }

    public static <T> void getById(Context ctx, Dao<T, Integer> dao, Class<T> clazz) throws SQLException, JsonProcessingException {
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        int userId = 0;
        if (ctx.basicAuthCredentialsExist()) {
            if (getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao) != null) {
                userId = getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getId();
            }
        }
        simpleModule.addSerializer(User.class, new UserGetSerializer(userId));
//        simpleModule.addSerializer(Post.class, new PostGetSerializer(postApplicationDao));
//        simpleModule.addSerializer(Category.class, new CategoryGetSerializer());

        objectMapper.registerModule(simpleModule);
        int id = Integer.parseInt(ctx.pathParam("id"));
        if (dao.queryForId(id) == null) {
            throw new WebException("Такого не существует", 404);
        }
        String serialized = objectMapper.writeValueAsString(dao.queryForId(id));

        ctx.result(serialized);

    }

    public static <T> void change(Context ctx, Dao<T, Integer> dao, Class<T> clazz) throws JsonProcessingException, SQLException {
        checkDoesBasicAuthEmpty(ctx);

        ctx.header("content-type", "app/json");
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("id"));
        checkBodyRequestIsEmpty(ctx);
        String body = ctx.body();

        checkAuthorization(ctx, userDao);
        if (clazz == User.class) {
            if (getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getRole() != Role.ADMIN) {
                if (id != getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getId()) {
                    youAreNotAdmin(ctx);
                }
            }
            checkDoesSuchUserExist(id, userDao);
            simpleModule.addDeserializer(User.class, new DeserializerForChangeUser(id, userDao));
        }

        objectMapper.registerModule(simpleModule);
        Object obj = objectMapper.readValue(body, clazz);

        dao.update((T) obj);
        updated(ctx);
    }

    public static <T> void delete(Context ctx, Dao<T, Integer> dao, Class<T> clazz) throws JsonProcessingException, SQLException {
        checkDoesBasicAuthEmpty(ctx);
        ctx.header("content-type:app/json");
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("id"));
        checkAuthorization(ctx, userDao);
        if (clazz == User.class) {
            if (getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getRole() != Role.ADMIN) {
                if (id != getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getId()) {
                    youAreNotAdmin(ctx);
                }
            }
            checkDoesSuchUserExist(id, userDao);
        }

        objectMapper.registerModule(simpleModule);
        Object obj = dao.queryForId(id);
        dao.delete((T) obj);
        deleted(ctx);
    }

    public static void search(Context ctx, Dao<User, Integer> userDao) throws SQLException, JsonProcessingException {
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        String searchString = ctx.queryParam("searchString");
        Set<User> users = new HashSet<>();

        String[] search = searchString.toLowerCase().split(" ");
        if (search.length != 0) {
            for (User u : userDao.queryForAll()) {
                if (search.length == 1) {
                    if (u.getLname().toLowerCase().contains(search[0])
                            || u.getFname().toLowerCase().contains(search[0])
                    ) {
                        users.add(u);
                    }
                } else {
                    if (u.getFname().toLowerCase().contains(search[0])
                            && u.getLname().toLowerCase().contains(search[1])
                            || u.getFname().toLowerCase().contains(search[1])
                            && u.getLname().toLowerCase().contains(search[0])
                    ) {
                        users.add(u);
                    }
                }
            }
        }


        simpleModule.addSerializer(User.class, new UserGetSerializer(0));
        objectMapper.registerModule(simpleModule);
        ctx.result(objectMapper.writeValueAsString(users));
    }

    public static void getAuthorized(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper objectMapper = new ObjectMapper();

        checkDoesBasicAuthEmpty(ctx);
        checkAuthorization(ctx, userDao);
        SimpleModule simpleModule = new SimpleModule();

        simpleModule.addSerializer(User.class, new UserGetSerializer(getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getId()));
        objectMapper.registerModule(simpleModule);
        checked(ctx);
    }


}
