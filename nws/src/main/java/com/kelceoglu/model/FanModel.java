package com.kelceoglu.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "tbl_fanmodel")
public class FanModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "fanid", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long fanid;

    @Column(name = "whois", nullable = false)
    private String whois;
    @Column(name = "fanfor", nullable = false)
    private String fanfor;
    @Column(name = "ipaddress", nullable = false)
    private String ipaddress;
    @Column(name = "fandate", nullable = false)
    @Type(type = "date") // hibernate type
    private java.sql.Date fandate;

    public Long getFanid() {
        return fanid;
    }

    public void setFanid(Long fanid) {
        this.fanid = fanid;
    }

    public String getWhois() {
        return whois;
    }

    public void setWhois(String whois) {
        this.whois = whois;
    }

    public String getFanfor() {
        return fanfor;
    }

    public void setFanfor(String fanfor) {
        this.fanfor = fanfor;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public java.sql.Date getFandate() {
        return fandate;
    }

    public void setFandate(java.sql.Date fandate) {
        this.fandate = fandate;
    }

}
