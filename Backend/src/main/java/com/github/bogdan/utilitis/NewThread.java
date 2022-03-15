//package com.github.bogdan.utilitis;
//
//import com.github.bogdan.model.Status;
//import com.github.bogdan.model.User;
//import com.j256.ormlite.dao.Dao;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.temporal.ChronoUnit;
//import static com.github.bogdan.service.PostApplicationService.getPostApplications;
//import static com.github.bogdan.service.PostService.addPostQt;
//
//public class NewThread extends Thread{
//
//    public NewThread( Dao<User,Integer> userDao,Dao<Post,Integer> postDao,Dao<PostApplication,Integer> postApplicationDao) throws SQLException {
//        this.userDao=userDao;
//        this.postDao=postDao;
//        this.postApplicationDao = postApplicationDao;
//    }
//    private Dao<User,Integer> userDao;
//    private Dao<Post,Integer> postDao;
//    private Dao<PostApplication,Integer> postApplicationDao;
//    Logger LOGGER = LoggerFactory.getLogger(NewThread.class);
//    public void run(){
//        try {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String currentTime = LocalDate.now().format(formatter);
//            while (true) {
//                for (Post p : postDao.queryForAll()) {
//                    LocalDate l = LocalDate.parse(p.getDateOfCreate(), formatter);
//                    if (p.getStatus() == Status.PROCESSING) {
//                        if (ChronoUnit.DAYS.between(l, LocalDate.now()) >= 15 && !getPostApplications(p.getId(),postApplicationDao).isEmpty()) {
//                            p.setStatus(Status.COMPLETED);
//                            addPostQt(p,userDao,postApplicationDao);
//                        }
//                    }
//                    postDao.update(p);
//                }
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//    }
//
//}
