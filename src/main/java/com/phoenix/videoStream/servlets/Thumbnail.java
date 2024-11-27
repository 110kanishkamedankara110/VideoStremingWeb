package com.phoenix.videoStream.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(name = "thumbnail",urlPatterns = "/thumbnail")
public class Thumbnail extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String thumbnail = "videoStream/Resources/Thumbnail/" + req.getParameter("thumbnail");

        resp.setContentType("image/jpeg");

        File thumbnailFile = new File(thumbnail);

        if (!thumbnailFile.exists() || thumbnailFile.isDirectory()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Thumbnail not found");
            return;
        }

        try (FileInputStream fis = new FileInputStream(thumbnailFile);
             OutputStream os = resp.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error reading thumbnail");
        }

    }
}
