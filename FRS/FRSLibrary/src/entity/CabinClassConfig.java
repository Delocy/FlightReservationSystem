/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
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
public class CabinClassConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cabinClassConfigId;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CabinClassNameEnum cabinClassName;
    
    @Column(nullable = false)
    private String seatConfiguration;
    
    @Column(nullable = false)
    private int numRows;
    
    @Column(nullable = false)
    private int numAisles;
    
    @Column(nullable = false)
    private int numSeatsAbreast;
    
    @Column(nullable = false)
    private int maxSeatCapacity;
    
    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(nullable = false)
    private AircraftConfig aircraftConfig;

    public CabinClassConfig() {
    }

    public CabinClassConfig(CabinClassNameEnum cabinClassName, String seatConfiguration, int numRows, int numAisles, int numSeatsAbreast) {
        this.cabinClassName = cabinClassName;
        this.seatConfiguration = seatConfiguration;
        this.numRows = numRows;
        this.numAisles = numAisles;
        this.numSeatsAbreast = numSeatsAbreast;
        this.maxSeatCapacity = numRows * numSeatsAbreast;
        //this.aircraftConfig = aircraftConfig;
    }

    public Long getCabinClassConfigId() {
        return cabinClassConfigId;
    }

    public void setCabinClassConfigId(Long cabinClassConfigId) {
        this.cabinClassConfigId = cabinClassConfigId;
    }

    public CabinClassNameEnum getCabinClassName() {
        return cabinClassName;
    }

    public void setCabinClassName(CabinClassNameEnum cabinClassName) {
        this.cabinClassName = cabinClassName;
    }

    public String getSeatConfiguration() {
        return seatConfiguration;
    }

    public void setSeatConfiguration(String seatConfiguration) {
        this.seatConfiguration = seatConfiguration;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public int getNumAisles() {
        return numAisles;
    }

    public void setNumAisles(int numAisles) {
        this.numAisles = numAisles;
    }

    public int getNumSeatsAbreast() {
        return numSeatsAbreast;
    }

    public void setNumSeatsAbreast(int numSeatsAbreast) {
        this.numSeatsAbreast = numSeatsAbreast;
    }

    public int getMaxSeatCapacity() {
        return maxSeatCapacity;
    }

    public void setMaxSeatCapacity(int maxSeatCapacity) {
        this.maxSeatCapacity = maxSeatCapacity;
    }

    public AircraftConfig getAircraftConfig() {
        return aircraftConfig;
    }

    public void setAircraftConfig(AircraftConfig aircraftConfig) {
        this.aircraftConfig = aircraftConfig;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cabinClassConfigId != null ? cabinClassConfigId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the cabinClassConfigId fields are not set
        if (!(object instanceof CabinClassConfig)) {
            return false;
        }
        CabinClassConfig other = (CabinClassConfig) object;
        if ((this.cabinClassConfigId == null && other.cabinClassConfigId != null) || (this.cabinClassConfigId != null && !this.cabinClassConfigId.equals(other.cabinClassConfigId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CabinClassConfig[ id=" + cabinClassConfigId + " ]";
    }
    
}
