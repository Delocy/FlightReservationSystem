/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import util.enumeration.ScheduleTypeEnum;

/**
 *
 * @author zares
 */
@Entity
public class FlightSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightScheduleId;
    @Enumerated(EnumType.STRING)
    private ScheduleTypeEnum scheduleType;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date departureDateTime;
    
    @Column(nullable = false)
    private Double flightDuration;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private FlightSchedulePlan flightSchedulePlan;
    
    @OneToMany(mappedBy = "flightSchedule", fetch = FetchType.EAGER)
    private List<SeatInventory> seatInventory;
    
//    @OneToMany(mappedBy = "flightSchedule", fetch = FetchType.EAGER)
//    private List<FlightReservation> reservations;

    public FlightSchedule() {
        this.seatInventory = new ArrayList<>();
        //this.reservations = new ArrayList<>();
    }

    public FlightSchedule(Date departureDateTime, Double flightDuration) {
        this.departureDateTime = departureDateTime;
        this.flightDuration = flightDuration;
    }
    
    
    public Long getFlightScheduleId() {
        return flightScheduleId;
    }

    public void setFlightScheduleId(Long flightScheduleId) {
        this.flightScheduleId = flightScheduleId;
    }

    public ScheduleTypeEnum getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ScheduleTypeEnum scheduleType) {
        this.scheduleType = scheduleType;
    }

    public Date getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(Date departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public Double getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(Double flightDuration) {
        this.flightDuration = flightDuration;
    }

    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }

    public List<SeatInventory> getSeatInventory() {
        return seatInventory;
    }

    public void setSeatInventory(List<SeatInventory> seatInventory) {
        this.seatInventory = seatInventory;
    }
    
    

//    public List<FlightReservation> getReservations() {
//        return reservations;
//    }
//
//    public void setReservations(List<FlightReservation> reservations) {
//        this.reservations = reservations;
//    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightScheduleId != null ? flightScheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightScheduleId fields are not set
        if (!(object instanceof FlightSchedule)) {
            return false;
        }
        FlightSchedule other = (FlightSchedule) object;
        if ((this.flightScheduleId == null && other.flightScheduleId != null) || (this.flightScheduleId != null && !this.flightScheduleId.equals(other.flightScheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedule[ id=" + flightScheduleId + " ]";
    }
    
}
