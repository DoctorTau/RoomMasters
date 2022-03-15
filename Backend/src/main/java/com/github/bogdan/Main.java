package com.github.bogdan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.controller.MainController;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.*;
import com.github.bogdan.serializer.WebExceptionSerializer;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.Javalin;

import java.sql.SQLException;

import static com.github.bogdan.databaseConfiguration.DatabasePath.getBagaPath;

public class Main {

    public static void main(String[] args) throws SQLException {


        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.enableDevLogging();
            javalinConfig.enableCorsForAllOrigins();
            javalinConfig.defaultContentType = "application/json";
        }).start(22865);


//        if (args.length == 0) {
//            throw new RuntimeException("Args can't be null");
//        }

        String jdbcConnectionString = "jdbc:sqlite:" + getBagaPath();
        DatabasePath.setPath(jdbcConnectionString);
        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(jdbcConnectionString);

        Dao<User, Integer> userDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(), User.class);

        app.post("/users", ctx -> MainController.add(ctx,userDao,User.class));
        app.get("/users", ctx -> MainController.get(ctx,userDao, User.class));
        app.get("/users/:id", ctx -> MainController.getById(ctx,userDao, User.class));
        app.patch("/users/:id", ctx -> MainController.change(ctx,userDao,User.class));
        app.delete("/users/:id",ctx -> MainController.delete(ctx,userDao,User.class));


        app.get("/authorized",ctx -> MainController.getAuthorized(ctx));

        app.get("/search",ctx -> MainController.search(ctx,userDao));

        app.exception(IllegalArgumentException.class,(e, ctx) ->{
            WebException w = new WebException("Such enum constant doesn't exist: "+e.getMessage(),400);
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(WebException.class,new WebExceptionSerializer());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(simpleModule);
            try {
                ctx.result(objectMapper.writeValueAsString(w));
                ctx.status(w.getStatus());
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }
        });
        app.exception(WebException.class, (e, ctx) -> {
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(WebException.class,new WebExceptionSerializer());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(simpleModule);
            try {
                ctx.result(objectMapper.writeValueAsString(e));
                ctx.status(e.getStatus());
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }
        });
    }
}
