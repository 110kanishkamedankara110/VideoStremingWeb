package com.phoenix.videoStream.servlets;

import com.google.gson.Gson;
import com.phoenix.videoStream.dto.UserLoginDto;
import com.phoenix.videoStream.entities.User;
import com.phoenix.videoStream.middleware.Encrypt;
import com.phoenix.videoStream.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;



@WebServlet(name = "login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        UserLoginDto userLoginDto=new Gson().fromJson((String)req.getParameter("data"),UserLoginDto.class);



        Query<User> query = session.createQuery("SELECT u FROM User u WHERE u.email=:em AND u.password=:pw", User.class);
        query.setParameter("em", userLoginDto.getEmail());
        query.setParameter("pw", Encrypt.encrypt(userLoginDto.getPassword()));

        List<User> userList = query.getResultList();
        HashMap<String, String> massage = new HashMap();
        session.close();
        if (userList.size()!=1) {
            massage.put("title", "User Not Found");
        } else {
            req.getSession().setAttribute("user", userList.get(0));
            massage.put("title","Sucess");
        }
        try{
            resp.setContentType("application/json");
            resp.getWriter().write(new Gson().toJson(massage));
        }catch(Exception e){

        }
    }
}
