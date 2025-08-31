package com.phoenix.videoStream.middleware;

import com.phoenix.videoStream.annotation.Auth;
import com.phoenix.videoStream.entities.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URI;
import java.util.Map;


@WebFilter("/*")
public class IsAuthenticated implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI();



        boolean requiresAuth = false;

        Map<String, ? extends ServletRegistration> servletMap = req.getServletContext().getServletRegistrations();
        for(ServletRegistration reg : servletMap.values()){
            for(String mapping : reg.getMappings()){
                if(path.endsWith(mapping.replace("/*",""))){
                    try {
                        Class<?> servletClass = Class.forName(reg.getClassName());
                        if(servletClass.isAnnotationPresent(Auth.class)){
                            requiresAuth = true;
                        }
                    } catch (ClassNotFoundException e){
                        e.printStackTrace();
                    }
                }
            }
        }

        if(requiresAuth){
            User user = (User) req.getSession().getAttribute("user");
            if(user == null){
                resp.sendRedirect("Login.jsp");
                return;
            }
        }

        // Continue request
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}

}
