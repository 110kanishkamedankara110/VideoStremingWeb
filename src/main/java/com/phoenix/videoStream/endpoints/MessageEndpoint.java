package com.phoenix.videoStream.endpoints;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/mes")
public class MessageEndpoint {
    private static List<Session> sessionList=new LinkedList();
    @OnOpen
    public void init(Session session){
        this.sessionList.add(session);
    }

    @OnMessage
    public void onMessage(String message){
        System.out.println("typing................");
        System.out.println(sessionList.size());
        for(Session ses:sessionList){
            try {
                ses.getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @OnClose
    public void onCLise(Session session){
        this.sessionList.remove(session);
    }


}


