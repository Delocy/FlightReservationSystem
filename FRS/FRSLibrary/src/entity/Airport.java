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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author zares
 */
@Entity
public class Airport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long airportId;
    @Column(nullable = false, length = 64)
    @Size(min = 1, max = 64)
    @NotNull
    private String airportName;
    @Column(nullable = false, unique = true, length = 3)
    @Size(min = 3, max = 3)
    @NotNull
    private String airportCode;
    @Column(nullable = false, length = 64)
    @Size(min = 1, max = 64)
    @NotNull
    private String city;
    @Column(nullable = false, length = 64)
    @Size(min = 1, max = 64)
    @NotNull
    private String state;
    @Column(nullable = false, length = 64)
    @Size(min = 1, max = 64)
    @NotNull
    private String country;
    @Column(nullable = false)
    @Min(0)
    @Max(24)
    @NotNull
    private int gmt;

    public Airport() {
    }
    
    public Airport(String airportCode) {
        this.airportCode = airportCode;
    }

    public Airport(String airportName, String airportCode, String city, String state, String country) {
        this.airportName = airportName;
        this.airportCode = airportCode;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Airport(String airportName, String airportCode, String city, String state, String country, int gmt) {
        this.airportName = airportName;
        this.airportCode = airportCode;
        this.city = city;
        this.state = state;
        this.country = country;
        this.gmt = gmt;
    }
    

    public Long getAirportId() {
        return airportId;
    }

    public void setAirportId(Long airportId) {
        this.airportId = airportId;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getGmt() {
        return gmt;
    }

    public void setGmt(int gmt) {
        this.gmt = gmt;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (airportId != null ? airportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the airportId fields are not set
        if (!(object instanceof Airport)) {
            return false;
        }
        Airport other = (Airport) object;
        if ((this.airportId == null && other.airportId != null) || (this.airportId != null && !this.airportId.equals(other.airportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Airport[ id=" + airportId + " ]";
    }
    
}
