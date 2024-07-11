package com.phoenix.videoStream.endpoints;

import javax.imageio.ImageIO;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

@ServerEndpoint("/vidStr")
public class MessageEndPoint2 {
    private static List<Session> sessionList = new LinkedList();

    @OnOpen
    public void init(Session session) {
        this.sessionList.add(session);
    }

    @OnMessage
    public void onMessage(byte[] ima, Session ses) {
        try {
            for (Session s : sessionList) {
                s.getAsyncRemote().sendBinary(ByteBuffer.wrap(ima));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnClose
    public void onCLise(Session session) {
        this.sessionList.remove(session);
    }


}