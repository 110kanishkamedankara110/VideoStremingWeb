package com.phoenix.videoStream.util;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory SESSION_FACTORY;
    private static final Configuration CONFIGURATION=new Configuration();
    static{
        CONFIGURATION.configure();
        SESSION_FACTORY=CONFIGURATION.buildSessionFactory();
    }
    public static SessionFactory getSessionFactory(){
        return SESSION_FACTORY;
    }
}
