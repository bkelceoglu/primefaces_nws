/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.kelceoglu.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;

/**
 * @author caveman
 */
@Entity
@Table(name = "tbl_ipmodel")
public class IPModel implements Serializable {

    @Id
    @Column(name = "ipid", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ipid;

    @Column(name = "ipaddress")
    private String ipaddress;
    @Column(name = "logindate")
    @Type(type = "date")
    private Date logindate;
    @Column(name = "logaction")
    private String logaction;

    @ManyToOne
    @Cascade(CascadeType.ALL)
    private UserModel usermodel;

    private static final Logger LOG = Logger.getLogger(IPModel.class.getName());

    public Long getIpid() {
        return ipid;
    }

    public void setIpid(Long ipid) {
        this.ipid = ipid;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public Date getLogindate() {
        return logindate;
    }

    public void setLogindate(Date logindate) {
        this.logindate = logindate;
    }

    public String getLogaction() {
        return logaction;
    }

    public void setLogaction(String logaction) {
        this.logaction = logaction;
    }

    public UserModel getUsermodel() {
        return usermodel;
    }

    public void setUsermodel(UserModel usermodel) {
        this.usermodel = usermodel;
    }

}
