/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author zares
 */
@Entity
public class AircraftConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftConfigId;
    @Column(nullable = false, unique = true, length = 64)
    private String aircraftConfigName;
    @Column(nullable = false)
    private Integer numCabinClass;
    
    @ManyToOne(optional = false)
    private AircraftType aircraftType;
    
    @OneToMany(mappedBy = "aircraftConfig", fetch = FetchType.EAGER)
    private List<CabinClassConfig> cabinClassConfig;

    public Long getAircraftConfigId() {
        return aircraftConfigId;
    }

    public void setAircraftConfigId(Long aircraftConfigId) {
        this.aircraftConfigId = aircraftConfigId;
    }

    public String getAircraftConfigName() {
        return aircraftConfigName;
    }

    public void setAircraftConfigName(String aircraftConfigName) {
        this.aircraftConfigName = aircraftConfigName;
    }

    public Integer getNumCabinClass() {
        return numCabinClass;
    }

    public void setNumCabinClass(Integer numCabinClass) {
        this.numCabinClass = numCabinClass;
    }

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }

    public List<CabinClassConfig> getCabinClassConfig() {
        return cabinClassConfig;
    }

    public void setCabinClassConfig(List<CabinClassConfig> cabinClassConfig) {
        this.cabinClassConfig = cabinClassConfig;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aircraftConfigId != null ? aircraftConfigId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the aircraftConfigId fields are not set
        if (!(object instanceof AircraftConfig)) {
            return false;
        }
        AircraftConfig other = (AircraftConfig) object;
        if ((this.aircraftConfigId == null && other.aircraftConfigId != null) || (this.aircraftConfigId != null && !this.aircraftConfigId.equals(other.aircraftConfigId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftConfig[ id=" + aircraftConfigId + " ]";
    }
    
}
