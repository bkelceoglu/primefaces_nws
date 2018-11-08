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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;

/**
 * @author caveman
 */
@Entity
@Table(name = "tbl_locations")
public class LocationsModel implements Serializable {

    @Id
    @Column(name = "locationid", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long locationid;

    @Column(name = "priceforentrance")
    private Float priceforentrance;

    @Column(name = "datefor")
    @Type(type = "date")
    private Date datefor;

    @Column(name = "summary")
    private String summary;

    @OneToOne
    @Cascade(CascadeType.ALL)
    private UserModel userobject;

    private static final Logger LOG = Logger.getLogger(LocationsModel.class.getName());

    public Long getLocationid() {
        return locationid;
    }

    public void setLocationid(Long locationid) {
        this.locationid = locationid;
    }

    public Float getPriceforentrance() {
        return priceforentrance;
    }

    public void setPriceforentrance(Float priceforentrance) {
        this.priceforentrance = priceforentrance;
    }

    public Date getDatefor() {
        return datefor;
    }

    public void setDatefor(Date datefor) {
        this.datefor = datefor;
    }

    public String getSummary() {
        return summary;
    }

    public UserModel getUserobject() {
        return userobject;
    }

    public void setUserobject(UserModel userobject) {
        this.userobject = userobject;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
