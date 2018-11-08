package com.kelceoglu.filter;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedProperty;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.kelceoglu.controller.user.UserController;

public class UserFilter implements Filter, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{userController}")
    private UserController userController;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        Boolean isUserLoggedIn = userController.getIsUserLoggedIn();
        if (isUserLoggedIn) {
            chain.doFilter(request, response);
        } else {
            // redirect to login
        }

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    public UserController getUserController() {
        return userController;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

}
