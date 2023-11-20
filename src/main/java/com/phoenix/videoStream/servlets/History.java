package com.phoenix.videoStream.servlets;

import com.google.gson.Gson;
import com.phoenix.videoStream.annotation.Auth;
import com.phoenix.videoStream.entities.User;
import com.phoenix.videoStream.entities.Video;
import com.phoenix.videoStream.util.AddHistory;
import com.phoenix.videoStream.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;


@WebServlet(name = "AddHistory", urlPatterns = "/history")
@Auth
public class History extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        int vid = Integer.valueOf(req.getParameter("video"));

        Query<Video> query = session.createQuery("SELECT v FROM Video v WHERE v.id=:id ", Video.class);
        query.setParameter("id", vid);

        User u = (User) req.getSession().getAttribute("user");

        if (u != null) {
            Video video = query.getSingleResult();
            com.phoenix.videoStream.entities.History history = new com.phoenix.videoStream.entities.History();
            history.setVideo(video);
            history.setUser(u);
            System.out.println(history.getUser().getEmail());
            session.close();


            AddHistory.setHistory(history);
        }

    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(getServletContext().getRealPath(""));
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Query<com.phoenix.videoStream.entities.History>  q = s.createQuery("SELECT h FROM History h WHERE (h.user.id=:kw) ORDER BY h.updatedAt DESC");
        q.setParameter("kw", ((User)req.getSession().getAttribute("user")).getId());


        List<com.phoenix.videoStream.entities.History> histories = q.getResultList();


        List<Map> data = new LinkedList();

        for (com.phoenix.videoStream.entities.History history : histories) {
            String thumbnail = history.getVideo().getThumbnail();
            int id = history.getVideo().getId();
            String vid = history.getVideo().getVideo();
            String description = history.getVideo().getDescription();
            String title = history.getVideo().getTitle();
            User user = history.getVideo().getUser();
            String path = getServletContext().getRealPath("") + "/" + thumbnail;

            File f = new File(path);
            BufferedImage bi = ImageIO.read(f);
            Map colors = new HashMap();
            for (int y = 0; y < bi.getHeight(); y = y + 10) {
                for (int x = 0; x < bi.getWidth(); x = x + 10) {
                    int p = bi.getRGB(x, y);

                    int red = (p >> 16) & 0xff;
                    int green = (p >> 8) & 0xff;
                    int blue = (p) & 0xff;

                    int avg = (red + green + blue) / 3;
                    if (avg >= 20 || avg < 10) {
                        String colo = String.format("rgb(%s,%s,%s)", red, green, blue);
                        if (colors.get(colo) == null) {
                            colors.put(colo, 1);
                        } else {
                            int count = (Integer) colors.get(colo) + 1;
                            colors.replace(colo, count);
                        }

                    }


                }
            }

//                colors.remove(getMax(colors));
            String color = (getMax(colors));
            String minCol = (getMin(colors));
            Map m = new HashMap();
            m.put("thumbnail", thumbnail);
            m.put("video", vid);
            m.put("title", title);
            m.put("description", description);
            m.put("width", bi.getWidth());
            m.put("height", bi.getHeight());
            m.put("color", color);
            m.put("id", id);
            m.put("minColor", minCol);
            m.put("userEmail", user.getEmail());
            m.put("userName", user.getFirstName()+" "+user.getLastName());
            m.put("userType", user.getUserType().getUserType());

            data.add(m);

        }

        s.close();


        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(new Gson().toJson(data));

    }
    private String getMax(Map colors) {
        int maxVal = 0;
        String maxColor = "";
        Set keys = colors.keySet();

        for (Object c : keys) {
            if ((Integer) colors.get(c) > maxVal) {
                maxVal = (Integer) colors.get(c);
                maxColor = (String) c;
            }
        }

        return maxColor;

    }

    private String getMin(Map colors) {

        Set keys = colors.keySet();
        int minVal = 0;
        String minColor = "";
        for (Object c : keys) {
            if (minVal == 0) {
                minVal = (Integer) colors.get(c);
            }
            if ((Integer) colors.get(c) < minVal) {
                minVal = (Integer) colors.get(c);
                minColor = (String) c;
            }
        }

        return minColor;

    }

}
