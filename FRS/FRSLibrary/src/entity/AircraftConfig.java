/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Size(min = 1, max = 64)
    @NotNull
    private String aircraftConfigName;
    @Column(nullable = false)
    @Min(1)
    @Max(4)
    @NotNull
    private Integer numCabinClass;
    
    @OneToMany(mappedBy="aircraftConfig", fetch = FetchType.EAGER)
    private List<Flight> flights;
    
    @ManyToOne(optional = false)
    private AircraftType aircraftType;
    
    @OneToMany(mappedBy = "aircraftConfig", fetch = FetchType.EAGER)
    private List<CabinClassConfig> cabinClassConfig;

    public AircraftConfig() {
        this.cabinClassConfig = new ArrayList<>();
        this.flights = new ArrayList<>();
    }

    public AircraftConfig(String aircraftConfigName, Integer numCabinClass) {
        this();
        this.aircraftConfigName = aircraftConfigName;
        this.numCabinClass = numCabinClass;
    }

    public AircraftConfig(String aircraftConfigName, Integer numCabinClass, AircraftType aircraftType) {
        this();
        this.aircraftConfigName = aircraftConfigName;
        this.numCabinClass = numCabinClass;
        this.aircraftType = aircraftType;
    }

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

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
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
