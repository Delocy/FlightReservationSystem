/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import util.enumeration.CabinClassNameEnum;

/**
 *
 * @author zares
 */
@Entity
public class Fare implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fareId;
    @Column(nullable = false, length = 7)
    private String fareBasisCode;
    @Column(nullable = false)
    private BigDecimal fare;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CabinClassNameEnum cabinClassName;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private FlightSchedulePlan flightSchedulePlan;

    public Fare() {
    }

    public Fare(String fareBasisCode, BigDecimal fare, CabinClassNameEnum cabinClassName) {
        this.fareBasisCode = fareBasisCode;
        this.fare = fare;
        this.cabinClassName = cabinClassName;
    }


    public Long getFareId() {
        return fareId;
    }

    public void setFareId(Long fareId) {
        this.fareId = fareId;
    }

    public String getFareBasisCode() {
        return fareBasisCode;
    }

    public void setFareBasisCode(String fareBasisCode) {
        this.fareBasisCode = fareBasisCode;
    }

    public BigDecimal getFare() {
        return fare;
    }

    public void setFare(BigDecimal fare) {
        this.fare = fare;
    }

    public CabinClassNameEnum getCabinClassName() {
        return cabinClassName;
    }

    public void setCabinClassName(CabinClassNameEnum cabinClassName) {
        this.cabinClassName = cabinClassName;
    }

    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fareId != null ? fareId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the fareId fields are not set
        if (!(object instanceof Fare)) {
            return false;
        }
        Fare other = (Fare) object;
        if ((this.fareId == null && other.fareId != null) || (this.fareId != null && !this.fareId.equals(other.fareId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Fare[ id=" + fareId + " ]";
    }
    
}
