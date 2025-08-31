package com.phoenix.videoStream.servlets;

import com.google.gson.Gson;
import com.phoenix.videoStream.dto.UpdateDto;
import com.phoenix.videoStream.entities.Video;
import com.phoenix.videoStream.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

@WebServlet(name ="update", urlPatterns = {"/Update"})
@MultipartConfig
public class Update extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HashMap<String, String> responseMap = new HashMap<>();

        try {
            String dataJson = req.getParameter("data");
            if (dataJson == null || dataJson.isEmpty()) {
                responseMap.put("status", "error");
                responseMap.put("message", "No data provided");
                resp.getWriter().write(new Gson().toJson(responseMap));
                return;
            }

            UpdateDto updateDto = new Gson().fromJson(dataJson, UpdateDto.class);

            if (updateDto.getTitle() == null || updateDto.getTitle().trim().isEmpty()) {
                responseMap.put("status", "error");
                responseMap.put("message", "Title is required");
                resp.getWriter().write(new Gson().toJson(responseMap));
                return;
            }

            if (updateDto.getDescription() == null || updateDto.getDescription().trim().isEmpty()) {
                responseMap.put("status", "error");
                responseMap.put("message", "Description is required");
                resp.getWriter().write(new Gson().toJson(responseMap));
                return;
            }

            Part filePart = req.getPart("image");
            String uploadedFileName = null;

            if (filePart != null && filePart.getSize() > 0) {
                uploadedFileName = uploadThumbnailFile(filePart); // save the file and get filename
            }

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            Transaction tx = session.getTransaction();

            try {
                tx.begin();

                Video video = session.get(Video.class, updateDto.getId());
                if (video == null) {
                    responseMap.put("status", "error");
                    responseMap.put("message", "Video not found");
                    resp.getWriter().write(new Gson().toJson(responseMap));
                    return;
                }

                video.setTitle(updateDto.getTitle());
                video.setDescription(updateDto.getDescription());
                if (uploadedFileName != null) {
                    video.setThumbnail(uploadedFileName);
                }

                session.update(video);
                tx.commit();

                responseMap.put("status", "success");
                responseMap.put("message", "Video updated successfully");
                resp.getWriter().write(new Gson().toJson(responseMap));

            } catch (Exception e) {
                tx.rollback();
                e.printStackTrace();
                responseMap.put("status", "error");
                responseMap.put("message", "Update failed");
                resp.getWriter().write(new Gson().toJson(responseMap));
            } finally {
                session.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("status", "error");
            responseMap.put("message", "Server error");
            resp.getWriter().write(new Gson().toJson(responseMap));
        }
    }

    private String uploadThumbnailFile(Part filePart) throws IOException {
        String uploadDir = "C:/videoStream/Resources/Thumbnail/";

        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) uploadFolder.mkdirs();

        String fileName = System.currentTimeMillis() + ".png";
        File savedFile = new File(uploadFolder, fileName);

        // Use NIO to copy the file stream
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, savedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        return fileName;
    }


}
