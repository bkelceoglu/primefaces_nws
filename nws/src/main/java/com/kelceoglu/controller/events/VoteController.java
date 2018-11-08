/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.kelceoglu.controller.events;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;

import com.kelceoglu.hibernate.DAO;
import com.kelceoglu.model.UserModel;
import com.kelceoglu.model.VoteModel;

/**
 * @author caveman
 */
@ManagedBean
@SessionScoped
public class VoteController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Map<String, String> artistListUserName;
    private String artistNameSurname;
    private String localusername;
    private static final Logger LOG = Logger.getLogger(VoteController.class.getName());

    @PostConstruct
    public void init() {
        artistListUserName = new HashMap<>();
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

                artistListUserName.put(um.getUsername(), um.getUsername());
            }
        }
    }

    public void saveVote() {
        DetachedCriteria getIPList = DetachedCriteria.forClass(VoteModel.class);
        List<VoteModel> iplist = null;
        String whomvotes = null;
        VoteModel vm = new VoteModel();
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
        UserModel attribute = (UserModel) session.getAttribute("userObject");
        if (attribute == null) {
            whomvotes = "Anonymous";
        } else {
            whomvotes = attribute.getUsername();
        }
        HttpServletRequest req = (HttpServletRequest) fc.getExternalContext().getRequest();

        try {
            iplist = (List<VoteModel>) DAO.fetchDetachedCriteria(getIPList);
            if (!iplist.isEmpty()) {
                for (VoteModel v : iplist) {
                    if (req.getRemoteAddr().equals(v.getVotefromip()) && whomvotes.equals("Anonymous")
                            && artistNameSurname.equals(v.getForwhomvoted())) {

                        fc.addMessage("voteartist", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                                "You have already voted for " + artistNameSurname));
                        LOG.log(Level.INFO, "if error", "");
                        return;
                    } else if (whomvotes.equals(v.getWhomvoted()) && artistNameSurname.equals(v.getForwhomvoted())) {
                        fc.addMessage("voteartist", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                                whomvotes + ", you have already voted for " + artistNameSurname));
                        LOG.log(Level.INFO, "else if error", "");
                        return;
                    }
                }
            }
            vm.setWhomvoted(whomvotes);
            vm.setForwhomvoted(this.getArtistNameSurname());
            vm.setVotefromip(req.getRemoteAddr());
            DAO.saveObject(vm);
            fc.addMessage("voteartist", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
                    "Your Vote for " + localusername + " is recorded..."));
            LOG.log(Level.INFO, "else ", "Your Vote for " + localusername + " is recorded...");
            LOG.log(Level.INFO, "lan noldu bea ", "");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }

    }

    @SuppressWarnings("unchecked")
    public List<VoteModel> hallOfFame() {
        List<VoteModel> voteModel = new ArrayList<>();
        DetachedCriteria dc = DetachedCriteria.forClass(VoteModel.class);
        dc.setProjection(Projections.projectionList().add(Projections.count("forwhomvoted").as("counted"))
                .add(Projections.groupProperty("forwhomvoted")));
        try {
            voteModel = (List<VoteModel>) DAO.fetchDetachedCriteria(dc);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }

        return voteModel;
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

    public Map<String, String> getArtistListUserName() {
        return artistListUserName;
    }

    public void setArtistListUserName(Map<String, String> artistListUserName) {
        this.artistListUserName = artistListUserName;
    }

}
