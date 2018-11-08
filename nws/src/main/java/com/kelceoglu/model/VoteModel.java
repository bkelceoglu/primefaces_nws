/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.kelceoglu.model;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author caveman
 */
@Entity
@Table(name = "tbl_votemodel")
public class VoteModel implements Serializable {

    @Id
    @Column(name = "voteid", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long voteid;
    private static final Logger LOG = Logger.getLogger(VoteModel.class.getName());

    @Column(name = "whomvoted", nullable = true)
    private String whomvoted;

    @Column(name = "forwhomvoted", nullable = true)
    private String forwhomvoted;

    @Column(name = "votefromip", nullable = false)
    private String votefromip;

    @Column(name = "votetotal")
    private Integer votetotal;

    public Long getVoteid() {
        return voteid;
    }

    public void setVoteid(Long voteid) {
        this.voteid = voteid;
    }

    public String getWhomvoted() {
        return whomvoted;
    }

    public void setWhomvoted(String whomvoted) {
        this.whomvoted = whomvoted;
    }

    public String getForwhomvoted() {
        return forwhomvoted;
    }

    public void setForwhomvoted(String forwhomvoted) {
        this.forwhomvoted = forwhomvoted;
    }

    public String getVotefromip() {
        return votefromip;
    }

    public void setVotefromip(String votefromip) {
        this.votefromip = votefromip;
    }

    public Integer getVotetotal() {
        return votetotal;
    }

    public void setVotetotal(Integer votetotal) {
        this.votetotal = votetotal;
    }

}
