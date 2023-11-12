/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.ScheduleTypeEnum;

/**
 *
 * @author zares
 */
@Entity
public class FlightSchedulePlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightSchedulePlanId;
    
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min=3, max=32)
    private String flightNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private ScheduleTypeEnum scheduleType;
    
    @Column(nullable = false)
    @NotNull
    private boolean disabled;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date recurrentEndDate;
    
    @OneToMany(mappedBy = "flightSchedulePlan",  fetch = FetchType.EAGER)
    private List<FlightSchedule> flightSchedule;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Flight flight;
    
    @OneToMany(mappedBy = "flightSchedulePlan",  fetch = FetchType.EAGER)
    private List<Fare> fares;
    
    @OneToOne(mappedBy = "returnSchedulePlan")
    private FlightSchedulePlan originalSchedulePlan;
    
    @OneToOne
    private FlightSchedulePlan returnSchedulePlan;
    
    public FlightSchedulePlan() {
        flightSchedule = new ArrayList<>();
        fares = new ArrayList<>();
        disabled = false;
    }
    //Single schedules
    public FlightSchedulePlan(ScheduleTypeEnum scheduleType, Flight flight) {
        this();
        this.scheduleType = scheduleType;
        this.flight = flight;
        this.flightNumber = flight.getFlightNumber();
        this.recurrentEndDate = null;
    }

    //Recurrent schedules
    public FlightSchedulePlan(ScheduleTypeEnum scheduleType, Date recurrentEndDate, Flight flight) {
        this();
        this.scheduleType = scheduleType;
        this.recurrentEndDate = recurrentEndDate;
        this.flight = flight;
        this.flightNumber = flight.getFlightNumber();
    }
    
    
    public Long getFlightSchedulePlanId() {
        return flightSchedulePlanId;
    }

    public void setFlightSchedulePlanId(Long flightSchedulePlanId) {
        this.flightSchedulePlanId = flightSchedulePlanId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public ScheduleTypeEnum getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ScheduleTypeEnum scheduleType) {
        this.scheduleType = scheduleType;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Date getRecurrentEndDate() {
        return recurrentEndDate;
    }

    public void setRecurrentEndDate(Date recurrentEndDate) {
        this.recurrentEndDate = recurrentEndDate;
    }

    public List<FlightSchedule> getFlightSchedule() {
        return flightSchedule;
    }

    public void setFlightSchedule(List<FlightSchedule> flightSchedule) {
        this.flightSchedule = flightSchedule;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }

    public FlightSchedulePlan getOriginalSchedulePlan() {
        return originalSchedulePlan;
    }

    public void setOriginalSchedulePlan(FlightSchedulePlan originalSchedulePlan) {
        this.originalSchedulePlan = originalSchedulePlan;
    }

    public FlightSchedulePlan getReturnSchedulePlan() {
        return returnSchedulePlan;
    }

    public void setReturnSchedulePlan(FlightSchedulePlan returnSchedulePlan) {
        this.returnSchedulePlan = returnSchedulePlan;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightSchedulePlanId != null ? flightSchedulePlanId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightSchedulePlanId fields are not set
        if (!(object instanceof FlightSchedulePlan)) {
            return false;
        }
        FlightSchedulePlan other = (FlightSchedulePlan) object;
        if ((this.flightSchedulePlanId == null && other.flightSchedulePlanId != null) || (this.flightSchedulePlanId != null && !this.flightSchedulePlanId.equals(other.flightSchedulePlanId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedulePlan[ id=" + flightSchedulePlanId + " ]";
    }
    
}
