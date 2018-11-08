/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.kelceoglu.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;

/**
 * @author caveman
 */
public class DAO implements Serializable {

    private static final long serialVersionUID = 2736170763472882633L;
    private static org.hibernate.Session session = null;
    private static final Logger LOGGER = Logger.getLogger(DAO.class.getName());

    protected static org.hibernate.Session getSession() {
        session = HibernateUtil.getSession();
        return session;
    }

    private static void commit() throws Exception {
        try {
            Transaction trn = getSession().getTransaction();
            trn.begin();
            trn.commit();
        } catch (Exception e) {
            throw e;
            // HbnSessionUtil.rollbackOnly();
        }
    }

    protected static void closeSession() {
        session.close();
    }

    public static void deleteObject(Object o) throws Exception {
        try {
            getSession().delete(o);
            commit();
        } catch (Exception e) {
            LOGGER.warning("Delete Operation: " + e.getMessage());
            throw e;
        }
    }

    public static void saveObject(Object o) throws Exception {
        try {
            getSession().save(o);
            commit();
            // closeSession();
        } catch (Exception e) {
            Throwable ex = e;
            while (ex.getCause() != null) {
                ex = ex.getCause();
            }
            LOGGER.warning(ex.getMessage());
            throw e;
        }
    }

    public static List<?> fetchDetachedCriteria(DetachedCriteria dc) throws Exception {
        List<?> list = dc.getExecutableCriteria(getSession()).list();
        commit();
        return list;
    }

    public static Object fetchDetachedCriteriaUniqueResult(DetachedCriteria dc) throws Exception {
        try {
            return dc.getExecutableCriteria(getSession()).uniqueResult();
        } catch (Exception e) {
            LOGGER.warning("Unique Result Operation: " + e.getMessage());
            throw e;
        }
    }

}
