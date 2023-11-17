/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author zares
 */
@Entity
public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;
    @Column(nullable = false, unique = true, length = 7)
    @NotNull
    @Size(min = 3, max = 7)
    private String flightNumber;
    @Column(nullable = false)
    @NotNull
    private boolean isDisabled;
    
    @ManyToOne
    @JoinColumn(name = "flightRouteId")
    private FlightRoute flightRoute;
    
    @OneToOne
//    @JoinColumn(name = "returnFlightId") // Owning side
    private Flight returningFlight;

    @OneToOne(mappedBy = "returningFlight") // Inverse side
    private Flight originalFlight;

    @ManyToOne
    @JoinColumn(name = "aircraftConfigId")
    private AircraftConfig aircraftConfig;
    
    @OneToMany(mappedBy = "flight", fetch = FetchType.EAGER)
    private List<FlightSchedulePlan> flightSchedulePlan;

    public Flight() {
        this.isDisabled = false;
        this.flightSchedulePlan = new ArrayList<>();
    }

    public Flight(String flightNumber) {
        this();
        this.flightNumber = flightNumber;
    }
    
    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public List<FlightSchedulePlan> getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(List<FlightSchedulePlan> flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }
    
    

    public FlightRoute getFlightRoute() {
        return flightRoute;
    }

    public void setFlightRoute(FlightRoute flightRoute) {
        this.flightRoute = flightRoute;
    }

    public Flight getOriginalFlight() {
        return originalFlight;
    }

    public void setOriginalFlight(Flight originalFlight) {
        this.originalFlight = originalFlight;
    }

    
    public Flight getReturningFlight() {
        return returningFlight;
    }

    public void setReturningFlight(Flight returningFlight) {
        this.returningFlight = returningFlight;
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
        hash += (flightId != null ? flightId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightId fields are not set
        if (!(object instanceof Flight)) {
            return false;
        }
        Flight other = (Flight) object;
        if ((this.flightId == null && other.flightId != null) || (this.flightId != null && !this.flightId.equals(other.flightId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Flight[ id=" + flightId + " ]";
    }
    
}
