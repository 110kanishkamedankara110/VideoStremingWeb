package com.phoenix.videoStream.servlets;

import com.phoenix.videoStream.annotation.Auth;
import com.phoenix.videoStream.entities.Video;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@WebServlet(name = "delete",urlPatterns = {"/Delete"})
@Auth
public class Delete extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionFactory sf = com.phoenix.videoStream.util.HibernateUtil.getSessionFactory();
        Session ses = sf.openSession();

        Query<Video> query = ses.createQuery("SELECT v FROM Video v WHERE v.id=:id", Video.class);
        query.setParameter("id",Integer.valueOf(req.getParameter("v")));
        Video v = query.getSingleResult();
        Transaction ta = ses.beginTransaction();
        ses.delete(v);
        ta.commit();
        resp.sendRedirect("MyVideos.jsp");
        File file=new File(getServletContext().getRealPath("") + "/Resources/Videos/"+v.getVideo());
        file.delete();

        File file2=new File(getServletContext().getRealPath("") +"/"+v.getThumbnail());
        file2.delete();
    }


}
