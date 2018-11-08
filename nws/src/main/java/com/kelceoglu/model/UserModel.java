/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.kelceoglu.model;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author caveman
 */
@Entity
@Table(name = "tbl_user")
public class UserModel implements Serializable {

    public UserModel(String username, String email) {
        this.setUsername(username);
        this.setEmail(email);
    }

    public UserModel() {

    }

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;

    private static final Logger LOG = Logger.getLogger(UserModel.class.getName());
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "realname", nullable = false)
    private String realname;
    @Column(name = "realsurname", nullable = false)
    private String realsurname;
    @Column(name = "address", nullable = true)
    private String address;
    @Column(name = "telephone", nullable = true)
    private String telepone;
    @Column(name = "personorcompany", nullable = true)
    private String personorcompany;
    @Column(name = "personrole", nullable = true) // admin, controller, etc..
    private String personrole;
    @Column(name = "personstate", nullable = true) // artist, visitor, voter etc.
    private String personstate;
    @Column(name = "rank", nullable = true) // important about knowing if person is performer etc..
    private String rank;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usermodel", targetEntity = EventsModel.class)
    private List<EventsModel> eventlist;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usermodel", targetEntity = VideosModel.class)
    private List<VideosModel> videosList;
    //
    // @OneToOne( targetEntity = IPModel.class )
    // @JoinColumn( name = "useriplist" )
    // @Cascade( value = CascadeType.ALL )
    // private IPModel ipmodel;
    //
    // @OneToMany( targetEntity = LocationsModel.class )
    // @JoinColumn( name = "userlocationslist" )
    // @Cascade( value = CascadeType.ALL )
    // private List<LocationsModel> locationlist;

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

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealsurname() {
        return realsurname;
    }

    public void setRealsurname(String realsurname) {
        this.realsurname = realsurname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelepone() {
        return telepone;
    }

    public void setTelepone(String telepone) {
        this.telepone = telepone;
    }

    public String getPersonorcompany() {
        return personorcompany;
    }

    public void setPersonorcompany(String personorcompany) {
        this.personorcompany = personorcompany;
    }

    public String getPersonrole() {
        return personrole;
    }

    public void setPersonrole(String personrole) {
        this.personrole = personrole;
    }

    public String getPersonstate() {
        return personstate;
    }

    public void setPersonstate(String personstate) {
        this.personstate = personstate;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public List<EventsModel> getEventlist() {
        return eventlist;
    }

    public void setEventlist(List<EventsModel> eventlist) {
        this.eventlist = eventlist;
    }

    public List<VideosModel> getVideosList() {
        return videosList;
    }

    public void setVideosList(List<VideosModel> videosList) {
        this.videosList = videosList;
    }

}
