package com.kelceoglu.controller.news;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

import org.apache.log4j.Logger;

import com.kelceoglu.hibernate.DAO;
import com.kelceoglu.model.NewsModel;

@ManagedBean
@SessionScoped
public class NewsController implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<NewsModel> newsList = new ArrayList<NewsModel>();
    private NewsModel singlenews;

    @SuppressWarnings("unchecked")
    public List<NewsModel> fetchNews() {
        try {
            this.setNewsList((List<NewsModel>) DAO.fetchDetachedCriteria(
                    DetachedCriteria.forClass(NewsModel.class)));
        } catch (Exception e) {
            Logger.getLogger(NewsController.class.getName()).error(e);
        }

        return newsList;
    }

    public void readnewsid(Long id) {
        DetachedCriteria dc = DetachedCriteria.forClass(NewsModel.class).add(Property.forName("newsid").eq(id));
        try {
            singlenews = (NewsModel) DAO.fetchDetachedCriteriaUniqueResult(dc);
            if (singlenews != null) {
                FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                        .handleNavigation(FacesContext.getCurrentInstance(), null, "newsdetail.xhtml");
            }
        } catch (Exception e) {
            Logger.getLogger(NewsController.class.getName()).error(e);
        }
    }

    public List<NewsModel> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsModel> newsList) {
        this.newsList = newsList;
    }

    public NewsModel getSinglenews() {
        return singlenews;
    }

    public void setSinglenews(NewsModel singlenews) {
        this.singlenews = singlenews;
    }
}
