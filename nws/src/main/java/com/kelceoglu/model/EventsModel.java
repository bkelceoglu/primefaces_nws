/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.kelceoglu.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * @author caveman com.kelceoglu.modelEventsModel.java / caveman / Nov 16, 2016
 */
@Entity
@Table(name = "tbl_events")
public class EventsModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "eventid", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventid;

    @Column(name = "eventdate", nullable = false)
    @Type(type = "date") // hibernate type
    private java.sql.Date eventdate;

    @Column(name = "starttime", nullable = false)
    private String starttime;

    @Column(name = "endtime", nullable = false)
    private String endtime;

    @Column(name = "musicgenre", nullable = false)
    private String musicgenre;

    @Column(name = "eventname", nullable = false)
    private String eventname;

    @Column(name = "eventtype", nullable = false)
    private String eventtype;

    @Column(name = "information", nullable = true)
    private String information;

    @Column(name = "yourranking", nullable = true)
    private String yourranking;

    @Column(name = "locationofevent", nullable = true)
    private String locationofevent;

    @Column(name = "useridforevent", nullable = true)
    private String useridforevent;

    @Column(name = "performed", nullable = true)
    private Boolean performed;

    @Column(name = "rateget", nullable = true, columnDefinition = "int default 0")
    private Integer rateget;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserModel.class)
    @JoinColumn(name = "user_id")
    private UserModel usermodel;

    public Long getEventid() {
        return eventid;
    }

    public void setEventid(Long eventid) {
        this.eventid = eventid;
    }

    public String getGenre() {
        return musicgenre;
    }

    public void setGenre(String musicgenre) {
        this.musicgenre = musicgenre;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getYourranking() {
        return yourranking;
    }

    public void setYourranking(String yourranking) {
        this.yourranking = yourranking;
    }

    public String getMusicgenre() {
        return musicgenre;
    }

    public void setMusicgenre(String musicgenre) {
        this.musicgenre = musicgenre;
    }

    public String getLocationofevent() {
        return locationofevent;
    }

    public void setLocationofevent(String locationofevent) {
        this.locationofevent = locationofevent;
    }

    public String getUseridforevent() {
        return useridforevent;
    }

    public void setUseridforevent(String useridforevent) {
        this.useridforevent = useridforevent;
    }

    public UserModel getUsermodel() {
        return usermodel;
    }

    public void setUsermodel(UserModel usermodel) {
        this.usermodel = usermodel;
    }

    public String getEventtype() {
        return eventtype;
    }

    public void setEventtype(String eventtype) {
        this.eventtype = eventtype;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public java.sql.Date getEventdate() {
        return eventdate;
    }

    public void setEventdate(java.sql.Date eventdate) {
        this.eventdate = eventdate;
    }

    public Boolean getPerformed() {
        return performed;
    }

    public void setPerformed(Boolean performed) {
        this.performed = performed;
    }

    public Integer getRateget() {
        return rateget;
    }

    public void setRateget(Integer rateget) {
        this.rateget = rateget;
    }

}
