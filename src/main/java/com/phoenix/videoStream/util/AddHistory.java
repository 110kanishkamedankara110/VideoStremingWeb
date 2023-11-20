package com.phoenix.videoStream.util;

import com.phoenix.videoStream.entities.History;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class AddHistory {
    private static final SessionFactory sessionFactory;
    private static Session session;

    static {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    private static List<History> get(Query<History> query) {
        List<History> histories = query.getResultList();
        return histories;
    }

    private static List<History> getHistory(History history) {
        session = sessionFactory.openSession();
        Query<History> query = session.createQuery("SELECT h FROM History h WHERE h.user.id=:uid AND h.video.id=:vid", History.class);
        query.setParameter("uid", history.getUser().getId());
        query.setParameter("vid", history.getVideo().getId());
        return get(query);
    }

    private static boolean isThere(History history) {
        return (getHistory(history).size() >= 1);
    }

    private static boolean update(History history) {
        session = sessionFactory.openSession();
        History historyObj = getHistory(history).get(0);
        Transaction transaction = session.beginTransaction();
        boolean result = true;
        try {
            historyObj.setUpdatedAt(LocalDateTime.now());
            session.update(historyObj);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            result = false;
        } finally {
            session.close();
        }
        return result;

    }

    private static boolean set(History history) {
        session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        boolean result = true;
        try {
            session.save(history);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            result = false;
        } finally {
            session.close();
        }
        return result;
    }

    public static boolean setHistory(History history) {
        return isThere(history) ? update(history) : set(history);
    }

}
