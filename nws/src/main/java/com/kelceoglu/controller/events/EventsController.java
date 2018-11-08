/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.kelceoglu.controller.events;

import com.kelceoglu.controller.user.UserController;
import com.kelceoglu.hibernate.DAO;
import com.kelceoglu.hibernate.HibernateUtil;
import com.kelceoglu.model.EventsModel;
import com.kelceoglu.model.UserModel;
import com.kelceoglu.model.VideosModel;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author caveman com.kelceoglu.controller.eventsEventsController.java /
 *         caveman / Nov 16, 2016
 */
@ManagedBean
@SessionScoped
public class EventsController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private List<EventsModel> eventlistforweek;
    private List<EventsModel> eventsOfDay;
    private List<EventsModel> eventListAll;
    private List<EventsModel> eventsForSelectedArtist;
    private Map<String, String> eventoftheday = new HashMap<>();
    private String selectedEvent;
    private static final Logger LOG = Logger.getLogger(EventsController.class.getName());

    private String username;
    private String artistUserName;

    // form vars..
    private java.util.Date startDate;
    private java.util.Date endDate;
    private java.util.Date startTime;
    private java.util.Date endTime;
    private String eventName;
    private String eventType;
    private String musiceventGenre;
    private String location;
    private String information;
    private String videoname;
    private String videolink;
    private String explanation;

    private ScheduleModel scheduleModel;

    private ScheduleEvent event = new DefaultScheduleEvent();

    // inject usercontroller for the usermodel
    @ManagedProperty(value = "#{userController}")
    private UserController userController;

    @PostConstruct
    public void init() {
        this.fetchAllEvents();
    }

    public ScheduleModel allEventsModel() {
        scheduleModel = new DefaultScheduleModel();
        this.fetchAllEvents();
        // DefaultScheduleEvent("event title", "start time", "end time")
        if (!this.eventListAll.isEmpty()) {
            for (EventsModel em : eventListAll) {
                scheduleModel.addEvent(new DefaultScheduleEvent(em.getEventname(),
                        calculateEventDate(em.getEventdate(),
                                em.getStarttime()),
                        calculateEventDate(em.getEventdate(), em.getEndtime())));
                LOG.info("event added...");
            }
        }

        return scheduleModel;
    }

    private java.util.Date calculateEventDate(java.sql.Date d, String time) {
        TimeZone timeZone;
        timeZone = TimeZone.getTimeZone("GMT+0:00");
        TimeZone.setDefault(timeZone);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeZone(timeZone);
        calendar.set(1970, 0, 1, 0, 0, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MILLISECOND);
        calendar.setTime(d);
        calendar.add(Calendar.DATE, 1);
        String[] times = time.split(":");
        if (Integer.parseInt(times[0]) >= 12) {
            calendar.set(Calendar.AM_PM, Calendar.PM);
            calendar.set(Calendar.HOUR, (Integer.parseInt(times[0]) - 12));
        } else {
            calendar.set(Calendar.AM_PM, Calendar.AM);
            calendar.set(Calendar.HOUR, Integer.parseInt(times[0]));
        }
        calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
        // LOG.log(Level.INFO, "calendar value: {0}", calendar.toString());
        return calendar.getTime();

    }

    public void addEventToDb() {

        if (this.getStartDate() == null || this.getStartTime() == null || this.getEndTime() == null
                || this.getEventName() == null || this.getEventType() == null || this.getMusiceventGenre() == null
                || this.getInformation() == null || this.getLocation() == null) {
            return;
        }

        LocalDate ld = this.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Calendar st = Calendar.getInstance();

        LocalDate todaysDate = Calendar.getInstance().getTime().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();

        if (java.sql.Date.valueOf(ld).before(java.sql.Date.valueOf(todaysDate))) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "You can not set time before than today"));
            return;
        }
        st.setTime(this.getStartTime());
        String timeMinute = "00";
        if (st.get(Calendar.MINUTE) != 0) {
            timeMinute = String.valueOf(st.get(Calendar.MINUTE));
        }
        String stime = String.valueOf(st.get(Calendar.HOUR_OF_DAY)) + ":" + timeMinute;
        Calendar et = Calendar.getInstance();
        et.setTime(this.getEndTime());
        if (et.getTime().before(st.getTime())) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "You can not set time before than start time"));
            return;
        }

        if (et.get(Calendar.MINUTE) != 0) {
            timeMinute = String.valueOf(et.get(Calendar.MINUTE));
        }
        String etime = String.valueOf(et.get(Calendar.HOUR_OF_DAY)) + ":" + timeMinute;

        EventsModel em = new EventsModel();
        em.setEventdate(java.sql.Date.valueOf(ld));
        em.setStarttime(stime);
        em.setEndtime(etime);
        em.setEventname(this.getEventName());
        em.setEventtype(this.getEventType());
        em.setGenre(this.getMusiceventGenre());
        em.setInformation(this.getInformation());
        em.setLocationofevent(this.getLocation());
        em.setUsermodel(this.getUserController().getUm());
        // em.setUsermodel(attribute);
        try {
            DAO.saveObject(em);
            LOG.info("object saved...");

            this.setStartDate(null);
            this.setStartTime(null);
            this.setEndTime(null);
            this.setEventName(null);
            this.setEventType(null);
            this.setMusiceventGenre(null);
            this.setInformation(null);
            this.setLocation(null);
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
        }
    }

    public void deleteEvent(Long eventid) {
        if (eventid.equals("")) {
            return;
        }
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(EventsModel.class);
            dc.add(Restrictions.eq("eventid", eventid));
            EventsModel deleteEvent = (EventsModel) DAO.fetchDetachedCriteriaUniqueResult(dc);
            DAO.deleteObject(deleteEvent);
        } catch (Exception e) {
            LOG.severe(e.getMessage());
        }

    }

    // TODO: date control to be added.
    public void performThisEvent(Long eventid) {
        if (eventid.equals("")) {
            return;
        }
        LOG.info("eventid : " + eventid + " is starting...");
        DetachedCriteria dc = DetachedCriteria.forClass(EventsModel.class);
        dc.add(Restrictions.eq("eventid", eventid));
        EventsModel performEvent = null;
        try {
            performEvent = (EventsModel) DAO.fetchDetachedCriteriaUniqueResult(dc);
        } catch (Exception e) {
            LOG.severe(e.getMessage());
        }
        performEvent.setPerformed(true);
        try {
            DAO.saveObject(performEvent);
        } catch (Exception e) {
            LOG.severe(e.getMessage());
        }

    }

    public void fetchEventForSearch() {
        eventsForSelectedArtist = new ArrayList<>();
        if ("".equals(artistUserName)) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage("searchpanelmessage",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "Please select one of the artist..."));
            return;
        }
        // get UserModel for the searched artist username
        DetachedCriteria dc = DetachedCriteria.forClass(UserModel.class);
        dc.add(Property.forName("username").eq(artistUserName));
        UserModel um = new UserModel();

        try {
            um = (UserModel) DAO.fetchDetachedCriteriaUniqueResult(dc);
        } catch (Exception ex) {
            Logger.getLogger(EventsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<EventsModel> eventsForSA = new ArrayList<>();
        if (um != null) {
            LOG.info(um.getUsername());
            DetachedCriteria dcevents = DetachedCriteria.forClass(EventsModel.class);
            dcevents.add(Property.forName("usermodel").eq(um));
            // dcevents.add(Restrictions.ge("eventdate", java.sql.Date.valueOf(LocalDate.now())));
            dcevents.add(Property.forName("eventdate").ge(java.sql.Date.valueOf(LocalDate.now())));

            try {
                eventsForSA = (List<EventsModel>) DAO.fetchDetachedCriteria(dcevents);
                if (!eventsForSA.isEmpty()) {
                    LOG.info(eventsForSA.toString());
                    this.setEventsForSelectedArtist(eventsForSA);
                } else {
                    LOG.severe("there is not record found");
                }
            } catch (Exception ex) {
                LOG.severe("DAO exception:" + ex.getMessage());
            }
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('dlgforart').show();");
    }

    @SuppressWarnings("unchecked")
    public List<EventsModel> fetchEventForUser(String username, String email) {

        List<EventsModel> eventmodel = new ArrayList<>();
        DetachedCriteria dc = DetachedCriteria.forClass(EventsModel.class);
        dc.add(Property.forName("usermodel").eq(this.getUserController().getUm()));
        dc.add(Restrictions.ge("eventdate", java.sql.Date.valueOf(LocalDate.now())));
        try {
            eventmodel = (List<EventsModel>) DAO.fetchDetachedCriteria(dc);
        } catch (Exception e) {
            LOG.severe(e.getMessage());
        }
        return eventmodel;
    }

    public List<VideosModel> fetchVideos(String artistname) {
        List<VideosModel> videosModels = new ArrayList<>();
        DetachedCriteria dc = DetachedCriteria.forClass(VideosModel.class);
        if ("".equals(artistname)) {
            dc.add(Restrictions.eq("usermodel", this.getUserController().getUm()));
        } else {
            DetachedCriteria getum = DetachedCriteria.forClass(UserModel.class);
            getum.add(Restrictions.eq("username", artistname));
            try {
                UserModel um = (UserModel) DAO.fetchDetachedCriteriaUniqueResult(getum);
                dc.add(Restrictions.eq("usermodel", um));
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Usermodel: {0}", ex.getMessage());
            }
        }
        try {
            videosModels = (List<VideosModel>) DAO.fetchDetachedCriteria(dc);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error fetching videos: {0}", e.getMessage());
        }
        return videosModels;
    }

    public void deleteVideo(Long id) {
        if (id.equals("")) {
            return;
        } else {
            try {
                DetachedCriteria deleteVideo = DetachedCriteria.forClass(VideosModel.class);
                deleteVideo.add(Restrictions.eq("videosid", id));
                VideosModel tobedelete = (VideosModel) DAO.fetchDetachedCriteriaUniqueResult(deleteVideo);
                DAO.deleteObject(tobedelete);
            } catch (Exception e) {
                LOG.severe("video delete error: " + e.getMessage());
            }
        }
    }

    public void addVideo() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if ("".equals(this.getVideoname()) || "".equals(this.getVideolink())) {
            fc.addMessage("addvideomsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "did you forget to fill videoname or videolink"));
            return;
        }
        VideosModel vm = new VideosModel();
        vm.setVideoname(this.getVideoname());
        vm.setVideolink(this.getVideolink());
        vm.setExplanations(this.getExplanation());
        vm.setUsermodel(this.getUserController().getUm());
        try {
            DAO.saveObject(vm);
            fc.addMessage("addvideomsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
                    "video added successfully"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "video adding error{0}", ex.getMessage());
            fc.addMessage("addvideomsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "video could not be added..."));
        }

    }

    private List<EventsModel> returnEventsBetweenDates(Date startD, Date endD) {
        Session hibSession = HibernateUtil.getSession();
        @SuppressWarnings("unchecked")
        List<EventsModel> eventModelList = hibSession.createCriteria(EventsModel.class)
                .add(Restrictions.between("eventdate", startD, endD)).list();
        return eventModelList;
    }

    @SuppressWarnings("unchecked")
    public void fetchAllEvents() {
        try {
            eventListAll = (List<EventsModel>) DAO.fetchDetachedCriteria(
                    DetachedCriteria.forClass(EventsModel.class));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOG.severe(e.getMessage());
        }
    }

    public String timeLineHeader(int i) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E MM dd");
        LocalDate today = LocalDate.now();
        String format = (today.plusDays(i)).format(dtf);
        LOG.log(Level.INFO, " returning date {0}", format);
        return format;
    }

    @SuppressWarnings("unchecked")
    public List<EventsModel> fetchEventofDay(String day) {

        DetachedCriteria d = DetachedCriteria.forClass(EventsModel.class);

        LocalDate today = LocalDate.now();
        switch (day) {
            case "firstDay":
                d.add(Restrictions.eq("eventdate", Date.valueOf(today)));
                break;
            case "secondDay":
                d.add(Restrictions.eq("eventdate", Date.valueOf(today.plusDays(1))));
                break;
            case "thirdDay":
                d.add(Restrictions.eq("eventdate", Date.valueOf(today.plusDays(2))));
                break;
            case "fourthDay":
                d.add(Restrictions.eq("eventdate", Date.valueOf(today.plusDays(3))));
                break;
            case "fifthDay":
                d.add(Restrictions.eq("eventdate", Date.valueOf(today.plusDays(4))));
                break;
            case "sixthDay":
                d.add(Restrictions.eq("eventdate", Date.valueOf(today.plusDays(5))));
                break;
            case "seventhDay":
                d.add(Restrictions.eq("eventdate", Date.valueOf(today.plusDays(6))));
                break;
        }
        List<EventsModel> eventsod = null;
        try {
            eventsod = (List<EventsModel>) DAO.fetchDetachedCriteria(d);
        } catch (Exception e) {
            Logger.getLogger(EventsController.class.getName()).log(Level.SEVERE, null, e.getMessage());
        }
        if (eventsod.isEmpty()) {
            return null;
        } else {
            for (EventsModel evm : eventsod) {
                UserModel um = evm.getUsermodel();
                eventoftheday.put(um.getUsername() + " : " + evm.getEventname() + " / " + evm.getEventdate(),
                        um.getUsername() + " : " + evm.getEventname() + " / " + evm.getEventdate());
            }
        }

        return eventsod;

    }

    public void fetchEventsOfTheWeek() {
        LocalDate today = LocalDate.now();
        LocalDate beginningOfWeek;
        LocalDate endOfWeek;
        if (!today.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            beginningOfWeek = today.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        } else {
            beginningOfWeek = today;
        }

        if (!today.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
            endOfWeek = today.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
        } else {
            endOfWeek = today;
        }
        java.sql.Date sqlbeginning = Date.valueOf(beginningOfWeek);
        java.sql.Date sqlend = Date.valueOf(endOfWeek);
        eventlistforweek = this.returnEventsBetweenDates(sqlbeginning, sqlend);

    }

    // getter setter...
    public List<EventsModel> getEventlistforweek() {
        return eventlistforweek;
    }

    public void setEventlistforweek(List<EventsModel> eventlistforweek) {
        this.eventlistforweek = eventlistforweek;
    }

    public List<EventsModel> getEventsOfDay() {
        return eventsOfDay;
    }

    public void setEventsOfDay(List<EventsModel> eventsOfDay) {
        this.eventsOfDay = eventsOfDay;
    }

    public List<EventsModel> getEventListAll() {
        return eventListAll;
    }

    public void setEventListAll(List<EventsModel> eventListAll) {
        this.eventListAll = eventListAll;
    }

    public String getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(String selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public java.util.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }

    public java.util.Date getEndDate() {
        return endDate;
    }

    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }

    public java.util.Date getStartTime() {
        return startTime;
    }

    public void setStartTime(java.util.Date startTime) {
        this.startTime = startTime;
    }

    public java.util.Date getEndTime() {
        return endTime;
    }

    public void setEndTime(java.util.Date endTime) {
        this.endTime = endTime;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getMusiceventGenre() {
        return musiceventGenre;
    }

    public void setMusiceventGenre(String musiceventGenre) {
        this.musiceventGenre = musiceventGenre;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public UserController getUserController() {
        return userController;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public Map<String, String> getEventoftheday() {
        return eventoftheday;
    }

    public void setEventoftheday(Map<String, String> eventoftheday) {
        this.eventoftheday = eventoftheday;
    }

    public ScheduleModel getScheduleModel() {
        return scheduleModel;
    }

    public void setScheduleModel(ScheduleModel scheduleModel) {
        this.scheduleModel = scheduleModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public String getArtistUserName() {
        return artistUserName;
    }

    public void setArtistUserName(String artistUserName) {
        this.artistUserName = artistUserName;
    }

    public List<EventsModel> getEventsForSelectedArtist() {
        return eventsForSelectedArtist;
    }

    public void setEventsForSelectedArtist(List<EventsModel> eventsForSelectedArtist) {
        this.eventsForSelectedArtist = eventsForSelectedArtist;
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

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

}
