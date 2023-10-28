/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author zares
 */
@Entity
public class AircraftType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftTypeId;
    @Column(nullable = false)
    private String aircraftTypeName;
    @Column(nullable = false)
    private Long maxCapacity;
    
    //need mapping - need clarify also im mega confused about this!
    private AircraftConfig aircraftConfig;

    public Long getAircraftTypeId() {
        return aircraftTypeId;
    }

    public void setAircraftTypeId(Long aircraftTypeId) {
        this.aircraftTypeId = aircraftTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aircraftTypeId != null ? aircraftTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the aircraftTypeId fields are not set
        if (!(object instanceof AircraftType)) {
            return false;
        }
        AircraftType other = (AircraftType) object;
        if ((this.aircraftTypeId == null && other.aircraftTypeId != null) || (this.aircraftTypeId != null && !this.aircraftTypeId.equals(other.aircraftTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftType[ id=" + aircraftTypeId + " ]";
    }
    
}
