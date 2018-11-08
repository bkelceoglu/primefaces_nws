package com.kelceoglu.controller.events;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

import com.kelceoglu.hibernate.DAO;
import com.kelceoglu.model.FanModel;
import com.kelceoglu.model.UserModel;

@ManagedBean(name = "fanController")
@SessionScoped
public class FanController implements Serializable {

    private static final long serialVersionUID = 1L;
    private Map<String, String> artistList;
    private String artistNameSurname;
    private String localusername;
    private List<FanModel> fanList;
    private static final Logger LOG = Logger.getLogger(FanController.class.getName());

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {
        artistList = new HashMap<>();
        DetachedCriteria dc = DetachedCriteria.forClass(UserModel.class);
        dc.add(Property.forName("personrole").eq("Artist"));

        List<UserModel> userModelList = null;
        try {
            userModelList = (List<UserModel>) DAO.fetchDetachedCriteria(dc);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (userModelList != null) {
            for (UserModel um : userModelList) {
                artistList.put(um.getUsername(), um.getUsername());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void saveFan() {
        LOG.log(Level.INFO, "saveFan Started...");
        DetachedCriteria getIPList = DetachedCriteria.forClass(FanModel.class);
        List<FanModel> iplist = null;
        String whomfan = null;
        FanModel fm = new FanModel();
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
        UserModel attribute = (UserModel) session.getAttribute("userObject");
        if (attribute == null) {
            whomfan = "Anonymous";
        } else {
            whomfan = attribute.getUsername();
        }
        HttpServletRequest req = (HttpServletRequest) fc.getExternalContext().getRequest();

        try {
            iplist = (List<FanModel>) DAO.fetchDetachedCriteria(getIPList);
            if (!iplist.isEmpty()) {
                for (FanModel v : iplist) {
                    if (req.getRemoteAddr().equals(v.getIpaddress()) && whomfan.equals("Anonymous")
                            && artistNameSurname.equals(v.getFanfor())) {

                        fc.addMessage("fanartist", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                                "You are already fan for " + artistNameSurname));
                        LOG.log(Level.INFO, "if error", "");
                        return;
                    } else if (whomfan.equals(v.getWhois()) && artistNameSurname.equals(v.getFanfor())) {
                        fc.addMessage("fanartist", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                                whomfan + ", you are already fan for " + artistNameSurname));
                        LOG.log(Level.INFO, "else if error", "");
                        return;
                    }
                }
            }
            java.sql.Date today = java.sql.Date
                    .valueOf(
                            Calendar.getInstance().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            fm.setWhois(whomfan);
            fm.setFanfor(this.getArtistNameSurname());
            fm.setIpaddress(req.getRemoteAddr());
            fm.setFandate(today);
            DAO.saveObject(fm);
            fc.addMessage("fanartist", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
                    "You have become fan for " + this.getArtistNameSurname() + ". Congrads..."));
            LOG.log(Level.INFO, "You have become fan for {0}. Congrads...", this.getArtistNameSurname());

        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }

    }

    @SuppressWarnings("unchecked")
    public List<FanModel> fanListMethod(String username) {
        fanList = new ArrayList<FanModel>();
        DetachedCriteria dc = DetachedCriteria.forClass(FanModel.class);
        dc.add(Property.forName("fanfor").eq(username));
        try {
            fanList = (List<FanModel>) DAO.fetchDetachedCriteria(dc);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
        }
        return fanList;
    }

    public void sendThanks(String fanName) {

    }

    public Map<String, String> getArtistList() {
        return artistList;
    }

    public void setArtistList(Map<String, String> artistList) {
        this.artistList = artistList;
    }

    public String getArtistNameSurname() {
        return artistNameSurname;
    }

    public void setArtistNameSurname(String artistNameSurname) {
        this.artistNameSurname = artistNameSurname;
    }

    public String getLocalusername() {
        return localusername;
    }

    public void setLocalusername(String localusername) {
        this.localusername = localusername;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<FanModel> getFanList() {
        return fanList;
    }

    public void setFanList(List<FanModel> fanList) {
        this.fanList = fanList;
    }

}
