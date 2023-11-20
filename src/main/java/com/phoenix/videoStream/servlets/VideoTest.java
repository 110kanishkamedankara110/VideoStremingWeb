//package com.phoenix.videoStream.servlets;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.MultipartConfig;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//
//
//@WebServlet(name = "test", urlPatterns = "/test")
//@MultipartConfig(
//        fileSizeThreshold = 1024 * 1024 * 1024 * 1024,
//        maxFileSize = 1024 * 1024 * 1024 * 1024L,
//        maxRequestSize = 1024 * 1024 * 1024 * 1024L
//)
//public class VideoTest extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//
//        File f = new File(getServletContext().getRealPath("") + "/Resources/Videos/1690373873729.mp4");
//        long start = 0;
//        long end = f.length() - 1;
//        long contentLen;
//        String head = req.getHeader("Range");
//        resp.setContentType("video/mp4");
//        if (head != null) {
//            head = head.substring("bytes=".length());
//            String[] val = head.split("-");
//            if (val.length > 0) {
//                start = new Long(val[0]);
//                if (val.length > 1) {
//                    end = new Long(val[1]);
//                }
//            }
//            resp.setHeader("Keep-Alive", "true");
//            resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
//        }
//
//        String newRange = String.format("bytes %s-%s/%s", start, end, f.length());
//
//        resp.setContentLengthLong(end - start + 1);
//        resp.setHeader("Content-Range", newRange);
////        InputStream is = new FileInputStream(f);
////        OutputStream ous = resp.getOutputStream();
//
//
//    }
//
//
//
//}
