package com.phoenix.videoStream.middleware;

import com.phoenix.videoStream.annotation.Auth;
import com.phoenix.videoStream.entities.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Auth
@Provider
public class IsAuthenticated implements ContainerRequestFilter {
    @Context
    HttpServletRequest req;
    @Context
    HttpServletResponse resp;

    @Override
    public void filter(ContainerRequestContext  containerRequestFilter) throws IOException {
        User u = (User) req.getSession().getAttribute("user");
        if(u==null){
            resp.sendRedirect("Login.jsp");
        }
    }
}
