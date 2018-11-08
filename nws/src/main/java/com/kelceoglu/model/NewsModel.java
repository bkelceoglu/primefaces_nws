/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.kelceoglu.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * @author caveman
 */
@Entity
@Table(name = "tbl_news")
public class NewsModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "newsid", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long newsid;

    @Column(name = "header")
    private String header;
    @Column(name = "newsbody")
    private String newsbody;
    @Column(name = "newsattributes")
    private String newsattributes;
    @Column(name = "newskeywords")
    private String newskeywords;
    @Column(name = "newsdate")
    @Type(type = "date")
    private Date newsdate;

    public Long getNewsid() {
        return newsid;
    }

    public void setNewsid(Long newsid) {
        this.newsid = newsid;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getNewsbody() {
        return newsbody;
    }

    public void setNewsbody(String newsbody) {
        this.newsbody = newsbody;
    }

    public String getNewsattributes() {
        return newsattributes;
    }

    public void setNewsattributes(String newsattributes) {
        this.newsattributes = newsattributes;
    }

    public String getNewskeywords() {
        return newskeywords;
    }

    public void setNewskeywords(String newskeywords) {
        this.newskeywords = newskeywords;
    }

    public Date getNewsdate() {
        return newsdate;
    }

    public void setNewsdate(Date newsdate) {
        this.newsdate = newsdate;
    }

}
