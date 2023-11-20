package com.phoenix.videoStream.servlets;

import com.google.gson.Gson;
import com.phoenix.videoStream.entities.User;
import com.phoenix.videoStream.entities.Video;
import com.phoenix.videoStream.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Query;

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

@WebServlet(name = "getVideos", urlPatterns = {"/getVideos", "/videos", "/playlist"})
public class Videos extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(getServletContext().getRealPath(""));
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        Query q = null;
        if (req.getParameter("s") == null&&req.getParameter("u") == null&&req.getParameter("v") == null) {
            q = s.createQuery("SELECT v FROM Video v ORDER BY v.updatedAt DESC");

        } else if(req.getParameter("s") != null){
            String kw = req.getParameter("s");
            kw = kw + "%";
            q = s.createQuery("SELECT v FROM Video v WHERE (v.description LIKE :kw OR v.title LIKE :kw ) ORDER BY v.updatedAt DESC");
            q.setParameter("kw", kw);
        }else if(req.getParameter("u") != null){
            String kw = req.getParameter("u");
            q = s.createQuery("SELECT v FROM Video v WHERE (v.user.id=:kw) ORDER BY v.updatedAt DESC");
            q.setParameter("kw", Integer.valueOf(kw));
        }else if(req.getParameter("v") != null){
            String kw = req.getParameter("v");
            q = s.createQuery("SELECT v FROM Video v WHERE (v.id=:kw) ORDER BY v.updatedAt DESC");
            q.setParameter("kw", Integer.valueOf(kw));
        }


        List<Video> videos = q.getResultList();


        List<Map> data = new LinkedList();

        for (Video video : videos) {
            String thumbnail = video.getThumbnail();
            int id = video.getId();
            String vid = video.getVideo();
            String description = video.getDescription();
            String title = video.getTitle();
            User user = video.getUser();
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
            String minCol = getMin(colors);
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
