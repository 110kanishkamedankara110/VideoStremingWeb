package com.phoenix.videoStream.servlets;

import com.google.gson.Gson;
import com.phoenix.videoStream.dto.UpdateDto;
import com.phoenix.videoStream.dto.UserLoginDto;
import com.phoenix.videoStream.entities.User;
import com.phoenix.videoStream.entities.Video;
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
import javax.servlet.http.Part;
import java.util.HashMap;
import java.util.List;

@WebServlet(name ="update",urlPatterns = {"/Update"})
public class Update extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        UpdateDto updateDto = new Gson().fromJson((String) req.getParameter("data"), UpdateDto.class);
        String image=req.getParameter("img");
//        System.out.println(image);

        Query<Video> query = session.createQuery("SELECT v FROM Video v WHERE v.id=:id ", Video.class);
        query.setParameter("id", updateDto.getId());


        Video video = query.getSingleResult();
        HashMap<String, String> massage = new HashMap();


        System.out.println(updateDto.getTitle());
        System.out.println(updateDto.getDescription());
        Transaction ta = session.getTransaction();
        try {
            ta.begin();
            video.setTitle(updateDto.getTitle());
            video.setDescription(updateDto.getDescription());
            session.update(video);
            ta.commit();
        } catch (Exception e) {
            ta.rollback();
            e.printStackTrace();
        }
        session.close();
        massage.put("title", "Sucess");

        try {
            resp.setContentType("application/json");
            resp.getWriter().write(new Gson().toJson(massage));
        } catch (Exception e) {

        }
    }
}
