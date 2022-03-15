package com.github.bogdan.service;

import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import static com.github.bogdan.service.UserService.checkBooleanIsUserAdmin;

public class SortingService {
    static Logger LOGGER = LoggerFactory.getLogger(SortingService.class);

    public static <T> ArrayList<T> getByQueryParams(int userId, Dao<User, Integer> userDao, Dao<T, Integer> dao, Class<T> tClass, ArrayList<String> queryParams, Context ctx) throws NoSuchFieldException, SQLException, IllegalAccessException, UnsupportedEncodingException {
        ArrayList<T> objects = new ArrayList<>();
        for (String s : queryParams) {
            Field field = tClass.getDeclaredField(s);
            LOGGER.info("Field " + field.getName());
            String currentParam = ctx.queryParam(s);
            LOGGER.info("CurrentParam " + currentParam);
            if (currentParam != null) {
                field.setAccessible(true);
                for (T obj : dao.queryForAll()) {
                    Object value = field.get(obj);
                    String valueString = null;
                    if (value != null) {
                        valueString = value.toString();
                    }
                    if (URLDecoder.decode(currentParam, StandardCharsets.UTF_8.toString()).equals(valueString)) {
//                        if (tClass == PostApplication.class) {
//                            LOGGER.info("PostApplication obj:");
//                            if (field.getName().equals("post")) {
//                                if (postDao.queryForId(Integer.valueOf(currentParam)) != null) {
//                                    if (postDao.queryForId(Integer.valueOf(currentParam)).getUser().getId() != userId && !checkBooleanIsUserAdmin(userId, userDao)) {
//                                        throw new WebException("Это не ваш пост", 400);
//                                    }
//                                } else {
//                                    throw new WebException("Такого поста не существует", 400);
//                                }
//                            }
//                        } else {
                            LOGGER.info("Obj:" + obj);
                        //}
                        objects.add(obj);
                    }
                }
                field.setAccessible(false);
            }
        }
//        if (tClass == Post.class) {
//            boolean ignoreBoolean = true;
//            if (ctx.queryParam("sort") != null) {
//                if (ctx.queryParam("sort").equalsIgnoreCase("false")) {
//                    ignoreBoolean = false;
//                }
//            }
//            boolean sort = Boolean.parseBoolean(ctx.queryParam("sort"));
//
//
////            if (!ignoreBoolean) {
////                PostCategoryComparator comparator = new PostCategoryComparator();
////                ((List<Post>) objects).sort(comparator);
////            } else return objects;
//            return objects;
//        }
//        else if (tClass == PostApplication.class) {
//            objects.clear();
//            objects.addAll((Collection<? extends T>) getPostApplications2(getUsersPosts(userId, postDao), postApplicationDao));
//            return objects;
//        }else
        return objects;

    }

}