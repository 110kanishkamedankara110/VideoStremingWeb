package com.phoenix.videoStream.listener;

import com.phoenix.videoStream.entities.User;
import com.phoenix.videoStream.entities.UserType;
import com.phoenix.videoStream.provider.MailServiceProvider;
import com.phoenix.videoStream.util.Env;
import com.phoenix.videoStream.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        MailServiceProvider.getInstance().shutdown();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("started...........");

        UserType userType1 = new UserType();
        UserType userType2 = new UserType();

        userType1.setUserType("Admin");
        userType2.setUserType("User");

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        Query<UserType> query = s.createQuery("SELECT u FROM UserType u", UserType.class);
        List<UserType> userTypes = query.getResultList();
        if (userTypes.size() != 2) {
            Transaction ta = s.getTransaction();
            try {
                ta.begin();
                s.save(userType1);
                s.save(userType2);
                ta.commit();
            } catch (Exception e) {
                ta.rollback();
            }
        }
        s.close();
        Env.getProperties().setProperty("app.url","http://localhost:8080"+sce.getServletContext().getContextPath()+"/");
        MailServiceProvider.getInstance().start();

    }
}
