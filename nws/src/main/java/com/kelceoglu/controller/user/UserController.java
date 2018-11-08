/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.kelceoglu.controller.user;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.primefaces.context.RequestContext;

import com.kelceoglu.hibernate.DAO;
import com.kelceoglu.model.IPModel;
import com.kelceoglu.model.UserModel;
import com.kelceoglu.model.restricted.RestrictedWord;

/**
 * @author caveman com.kelceoglu.controller.userUserController.java / caveman /
 * Nov 16, 2016
 */
@ManagedBean
@SessionScoped
@SuppressWarnings("unchecked")
public class UserController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String realName, realSurname, address, role, telephone, email;
    private Map<String, String> roles;
    private Boolean isUserLoggedIn;
    private UserModel um = new UserModel();
    private Boolean isUserAdmin;

    @PostConstruct
    public void init() {
        roles = new HashMap<>();
        roles.put("Artist", "Artist");
        roles.put("Visitor", "Visitor");
        roles.put("Venue", "Venue");
        roles.put("Investor", "Investor");
        roles.put("Nomad", "Nomad");
    }

    public void doSomething(ActionEvent actionEvent) {
        // <p:commandButton value="Save"
        // actionListener="#{myBean.doSomething}"/>
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("jQuery('#IdOfThePanel').hide()");
    }

    /**
     * checks if user successfully logged in and sets http session parameters.
     *
     * @param event
     * @return TODO change setAttribute("userobject")
     */
    public void checkUserLogin(ActionEvent event) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = fc.getExternalContext().getSessionMap();
        RequestContext context = RequestContext.getCurrentInstance();

        if (username == null || password == null) {
            fc.addMessage(null, new FacesMessage("empty username or password is not allowed"));
            try {
                fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath());
                return;
            } catch (IOException ex) {
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        DetachedCriteria dc = DetachedCriteria.forClass(UserModel.class);
        dc.add(Property.forName("username").eq(username));
        dc.add(Property.forName("password").eq(password));

        List<UserModel> fetchDetachedCriteria = null;
        try {
            fetchDetachedCriteria = (List<UserModel>) DAO.fetchDetachedCriteria(dc);
        } catch (Exception e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, e.getMessage());
        }
        if (fetchDetachedCriteria.isEmpty()) {
            fc.addMessage("loginmessage", new FacesMessage("we could not find you sorry"));
            this.setIsUserLoggedIn(false);
//            try {
//                fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath());
//            } catch (IOException ex) {
//                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
//            }
            return;
        }
        UserModel userObject = fetchDetachedCriteria.get(0);
        if (userObject != null) {
            this.setUm(userObject);
            context.addCallbackParam("loggedinuser", true);

            sessionMap.put("loggedinuser", username);
            sessionMap.put("userrole", userObject.getPersonrole());
            sessionMap.put("userObject", userObject);
            IPModel userIPAddress = new IPModel();

            HttpServletRequest req = (HttpServletRequest) fc.getExternalContext().getRequest();
            userIPAddress.setIpaddress(req.getRemoteAddr());
            userIPAddress.setLogindate(new java.sql.Date(new java.util.Date().getTime()));
            userIPAddress.setUsermodel(userObject);
            userIPAddress.setLogaction("LOGIN");
            try {

                DAO.saveObject(userIPAddress);

                fc.addMessage(null, new FacesMessage("welcome " + username));
                ExternalContext externalContext = fc.getExternalContext();
                externalContext.redirect(externalContext.getRequestContextPath());
            } catch (Exception ex) {
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.setIsUserLoggedIn(true);
            if (userObject.getPersonrole() == "ADMIN") {
                this.setIsUserAdmin(true);
            } else {
                this.setIsUserAdmin(false);
            }
        }

    }

    /**
     * @param event TODO change getAttribute("userobject")
     */
    public void logoutUser(ActionEvent event) {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
        UserModel attribute = (UserModel) session.getAttribute("userObject");
        IPModel im = new IPModel();
        im.setUsermodel(attribute);
        im.setLogindate(new java.sql.Date(new java.util.Date().getTime()));
        im.setLogaction("LOGOUT");
        this.setIsUserLoggedIn(false);
        this.setIsUserAdmin(false);
        try {
            fc.getExternalContext().invalidateSession();
            // DAO.saveObject(im);
            fc.getExternalContext().redirect("/nws/");
        } catch (Exception ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @return user information...
     */
    public void registerUser() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Boolean allfields = this.getRealName() != null && this.getRealSurname() != null && this.getAddress() != null
                && this.getEmail() != null && this.getUsername() != null && this.getPassword() != null
                && this.getRole() != null;
        if (!allfields) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "please fill all fields...", ""));
            return;
        }
        String confirmIfThereisUser = this.confirmIfThereisUser(username, realName, realSurname, email);
        if (!"".equals(confirmIfThereisUser)) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "the choosen " + confirmIfThereisUser + " is already taken... ", ""));
            return;
        }

        Boolean confirmRestrictedWordsNotUsed = this.confirmRestrictedWordsNotUsed();
        if (confirmRestrictedWordsNotUsed) {
            // possibly to record user as attacker...
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "YOU CAN NOT SELECT ONE OF THE RESTRICTED WORD", ""));
            return;
        }
        UserModel localum = new UserModel();
        localum.setUsername(username);
        localum.setAddress(address);
        localum.setEmail(email);
        localum.setPassword(password);
        localum.setPersonrole(role);
        localum.setRealsurname(realSurname);
        localum.setRealname(realName);
        if (this.getTelephone() != null) {
            localum.setTelepone(telephone);
        }

        // DAO dao = new DAO();
        try {
            DAO.saveObject(localum);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Registration Completed. Please check your email in couple hours.",
                    ""));
            return;
        } catch (Exception ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occured....", "Contack to SystemAdmin"));
            return;
        }
    }

    public Map<String, String> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, String> roles) {
        this.roles = roles;
    }

    public UserModel getUm() {
        return um;
    }

    public void setUm(UserModel um) {
        this.um = um;
    }

    private Boolean confirmRestrictedWordsNotUsed() {
        Boolean rv = false;
        for (String r : RestrictedWord.restrictedWords) {
            if (r.equalsIgnoreCase(this.getUsername()) || r.equalsIgnoreCase(this.getRealName())
                    || r.equalsIgnoreCase(this.getRealName()) || r.equalsIgnoreCase(this.getRealSurname())
                    || r.equalsIgnoreCase(this.getEmail()) || r.equalsIgnoreCase(this.getAddress())) {
                rv = true;
            }
        }
        return rv;
    }

    private String confirmIfThereisUser(String username, String realName, String realSurname, String email) {
        String rv = "";

        List<UserModel> us = null;
        try {
            us = (List<UserModel>) DAO.fetchDetachedCriteria(DetachedCriteria.forClass(UserModel.class));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, e.getMessage());
        }

        for (UserModel u : us) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                rv = " EMAIL ";
            } else if (u.getRealname().equalsIgnoreCase(realName)) {
                rv = " REAL NAME ";
            } else if (u.getRealsurname().equalsIgnoreCase(realSurname)) {
                rv = " REAL SURNAME ";
            } else if (u.getUsername().equalsIgnoreCase(username)) {
                rv = " USERNAME ";
            }
        }
        return rv;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealSurname() {
        return realSurname;
    }

    public void setRealSurname(String realSurname) {
        this.realSurname = realSurname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsUserLoggedIn() {
        return isUserLoggedIn;
    }

    public void setIsUserLoggedIn(Boolean isUserLoggedIn) {
        this.isUserLoggedIn = isUserLoggedIn;
    }

    public Boolean getIsUserAdmin() {
        return isUserAdmin;
    }

    public void setIsUserAdmin(Boolean isUserAdmin) {
        this.isUserAdmin = isUserAdmin;
    }

}
