/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

/**
 * @author caveman
 */
@Entity
@Table(name = "tbl_videos")
public class VideosModel implements Serializable {

    @Id
    @Column(name = "videosid", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long videosid;

    private static final long serialVersionUID = 1L;
    @Column(name = "videoname", nullable = true)
    private String videoname;

    @Column(name = "videolink", nullable = false)
    private String videolink;

    @Column(name = "explations", nullable = true)
    private String explanations;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserModel.class)
    @JoinColumn(name = "user_id")
    private UserModel usermodel;

    public Long getVideosid() {
        return videosid;
    }

    public void setVideosid(Long videosid) {
        this.videosid = videosid;
    }

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }

    public String getVideolink() {
        return videolink;
    }

    public void setVideolink(String videolink) {
        this.videolink = videolink;
    }

    public String getExplanations() {
        return explanations;
    }

    public void setExplanations(String explanations) {
        this.explanations = explanations;
    }

    public UserModel getUsermodel() {
        return usermodel;
    }

    public void setUsermodel(UserModel usermodel) {
        this.usermodel = usermodel;
    }
}
