package com.phoenix.videoStream.servlets;

import com.google.gson.Gson;
import com.phoenix.videoStream.annotation.Auth;
import com.phoenix.videoStream.entities.User;
import com.phoenix.videoStream.entities.Video;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;


@WebServlet(name = "videoPlayer", urlPatterns = "/videoPlay")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1024 * 1024,
        maxFileSize = 1024 * 1024 * 1024 * 1024L,
        maxRequestSize = 1024 * 1024 * 1024 * 1024L
)

public class VideoPlay extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String video = "/videoStream/Resources/Videos/" + req.getParameter("video");

        File res = new File(video);


        long start = 0;
        long end = res.length() - 1;
        String range = req.getHeader("Range");

        if (range != null) {
            String rangeStartEnd = range.substring("bytes=".length());
            String[] rangeAr = rangeStartEnd.split("-");
            String startSt = rangeAr[0];
            String endSt;
            if (rangeAr.length == 1) {
                endSt = " ";
            } else {
                endSt = rangeAr[1];
            }
            if (startSt.length() > 0 && !startSt.isEmpty()) {
                start = Long.parseLong(startSt);
            }
            if (endSt.length() > 1 && !endSt.isEmpty()) {
                end = Long.parseLong(endSt);
            }


            resp.setHeader("Connection", "Keep-Alive");
            resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        } else {
//            resp.setStatus(HttpServletResponse.SC_OK);
        }

        long contentLength = end - start + 1;

        String customRange = "bytes " + start + "-" + end + "/" + res.length();
        resp.setContentLength((int) contentLength);
        resp.setContentType("video/mp4");
        resp.setHeader("Content-Range", customRange);

        InputStream ist = new FileInputStream(res);
        OutputStream ost = resp.getOutputStream();
        ist.skip(start);


        byte[] b = new byte[4098];
        int read = 0;
        while ((read = ist.read(b)) != -1) {
            ost.write(b, 0, read);
        }
        ist.close();
        ost.flush();
        ost.close();
        System.gc();
    }


    @Override

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        @Auth
        Part video = req.getPart("video");
        Part thumbnail = req.getPart("image");
        Part details = req.getPart("details");
        User u = (User) req.getSession().getAttribute("user");
        if (video.getSubmittedFileName() == null) {
            resp.getWriter().write("Select Video");
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(details.getInputStream()));
            StringBuilder det = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                det.append(line);
            }

            String jsonDetails = det.toString();

            Gson gs = new Gson();

            Video videodet = gs.fromJson(jsonDetails, Video.class);
            if (videodet.getDescription().equals("")) {
                resp.getWriter().write("Enter Details");
            } else {
                if (videodet.getTitle().equals("")) {
                    videodet.setTitle(video.getSubmittedFileName().substring(0, video.getSubmittedFileName().length() - 4));
                }
                try {
                    videodet.setVideo(uploadVideoFile(video));

                    if (thumbnail.getSubmittedFileName() == null) {
                        videodet.setThumbnail(uploadThumbnailFile((videodet.getVideo())));
                    } else {
                        videodet.setThumbnail(uploadThumbnailFile(thumbnail));
                    }
                    SessionFactory sf = com.phoenix.videoStream.util.HibernateUtil.getSessionFactory();
                    Session ses = sf.openSession();
                    Transaction ta = ses.beginTransaction();
                    try {
                        videodet.setUser(u);
                        ses.save(videodet);
                        ta.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                        ta.rollback();
                    } finally {
                        ses.close();
                    }
                    resp.getWriter().write("Sucess");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private String uploadThumbnailFile(Part p) throws Exception {

        File main=new File("/videoStream");
        File res = new File("/videoStream/Resources");
        File thumb = new File("/videoStream/Resources/Thumbnail");

        String name = "" + System.currentTimeMillis() + ".png";
        File file = new File("/videoStream/Resources/Thumbnail/" + name);

        if(!main.exists()){
            main.mkdirs();
        }

        if (!res.exists()) {
            res.mkdir();
        }
        if (!thumb.exists()) {
            thumb.mkdir();
        }

        file.createNewFile();

        return uploadImg(p, file, name);
    }

    private String uploadVideoFile(Part p) throws Exception {
        File main=new File("/videoStream");
        File res = new File("/videoStream/Resources");
        File thumb = new File("/videoStream/Resources/Videos");
        String ext = p.getSubmittedFileName().substring(p.getSubmittedFileName().length() - 3, p.getSubmittedFileName().length());

        String name = System.currentTimeMillis() + "." + (ext);
        File file = new File("/videoStream/Resources/Videos/" + name);

        if(!main.exists()){
            main.mkdirs();
        }
        if (!res.exists()) {
            res.mkdir();
        }
        if (!thumb.exists()) {
            thumb.mkdir();
        }

        file.createNewFile();

        return upload(p, file, name);
    }

    private String uploadThumbnailFile(String link) throws Exception {
        File main=new File("/videoStream");

        File res = new File( "/videoStream/Resources");
        File thumb = new File("/videoStream/Resources/Thumbnail");
        String name = "" + System.currentTimeMillis() + ".png";

        if(!main.exists()){
            main.mkdirs();
        }
        if (!res.exists()) {
            res.mkdir();
        }
        if (!thumb.exists()) {
            thumb.mkdir();
        }

        File f=new File("/videoStream/Resources/Videos/"+link);

        FFmpegFrameGrabber g = new FFmpegFrameGrabber(f.getAbsolutePath());
        g.start();

        Java2DFrameConverter converter = new Java2DFrameConverter();
        int j = 50;
        int black = 0;
        int pix = 0;
        for (int i = 0; i < j; i++) {
            Frame frame = g.grabImage();
            BufferedImage bi = converter.convert(frame);
            for (int y = 0; y < bi.getHeight(); y = y + 5) {
                for (int x = 0; x < bi.getWidth(); x = x + 5) {
                    int p = bi.getRGB(x, y);

                    int red = (p >> 16) & 0xff;
                    int green = (p >> 8) & 0xff;
                    int blue = (p) & 0xff;

                    int avg = (red + green + blue) / 3;

                    pix++;
                    if (avg <= 20) {
                        black++;
                    }


                }
            }
            if (black >= ((pix / 3) * 2)) {

            } else {
                ImageIO.write(bi, "png", new File( "/videoStream/Resources/Thumbnail/" + name));
                break;
            }
            if (i == j - 1) {
                j *= 2;
            }

        }

        g.stop();
        return name;
    }


    private String upload(Part p, File file, String name) throws Exception {
        InputStream is = p.getInputStream();

        FileOutputStream fos = new FileOutputStream(file);

        ReadableByteChannel rbc = Channels.newChannel(is);
        FileChannel fc = fos.getChannel();
        fc.transferFrom(rbc, 0, Long.MAX_VALUE);


        is.close();
        fos.close();
        fc.close();
        rbc.close();

        return name;
    }

    private String uploadImg(Part p, File file, String name) throws Exception {
        InputStream is = p.getInputStream();

        FileOutputStream fos = new FileOutputStream(file);

        ReadableByteChannel rbc = Channels.newChannel(is);
        FileChannel fc = fos.getChannel();
        fc.transferFrom(rbc, 0, Long.MAX_VALUE);


        is.close();
        fos.close();
        fc.close();
        rbc.close();

        return name;
    }


}
