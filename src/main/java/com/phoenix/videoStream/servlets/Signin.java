package com.phoenix.videoStream.servlets;

import com.google.gson.Gson;
import com.phoenix.videoStream.dto.UserLoginDto;
import com.phoenix.videoStream.entities.User;
import com.phoenix.videoStream.entities.UserType;
import com.phoenix.videoStream.middleware.Encrypt;
import com.phoenix.videoStream.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;


@WebServlet(name = "signin", urlPatterns = {"/Signin"})
public class Signin extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        String data = req.getParameter("data");

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        User user = new Gson().fromJson(data, User.class);

       List<User> u = session.createQuery("SELECT u FROM User u WHERE u.email=:email", User.class).setParameter("email",user.getEmail()).getResultList();


        Query<UserType> userTypeQuery = session.createQuery("SELECT uty FROM UserType uty WHERE uty.userType=:type");
        userTypeQuery.setParameter("type", "User");
        UserType userType = userTypeQuery.getSingleResult();

        HashMap<String, String> massage = new HashMap();
         if (user.getFirstName().isEmpty()) {
            massage.put("title", "Enter First Name");
        } else if (user.getLastName().isEmpty()) {
            massage.put("title", "Enter Last Name");

        } else if (user.getEmail().isEmpty()) {
            massage.put("title", "Enter Email");

        }else if (u.size()>=1) {
            massage.put("title", "User Already Registered");
        }  else {
            String password = Encrypt.encrypt(user.getPassword());
            user.setPassword(password);
            user.setUserType(userType);
            userType.setUsers(user);

            Transaction transaction = session.beginTransaction();
            try {
                session.save(user);
                transaction.commit();
                massage.put("title", "Sucess");

            } catch (Exception e) {
                e.printStackTrace();
                transaction.rollback();
                massage.put("title", "Error");

            }


        }
        resp.setContentType("application/json");
        try {
            resp.getWriter().write(new Gson().toJson(massage));
        } catch (Exception e) {

        }

    }
}
