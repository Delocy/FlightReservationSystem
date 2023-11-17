/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author xinni
 */
@Entity
public class Itinerary implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itineraryID;
    
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32)
    private String creditCardNumber;
    
    @Column(nullable = false)
    @NotNull
    @Size(min = 1, max = 32)
    private String expiryDate;
    
    @Column(nullable = false, length = 3)
    @NotNull
    @Size(min = 3, max = 3)
    private String cvv;
    
    @ManyToOne(optional = false)
    @JoinColumn
    private Person person;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "itinerary")
    private List<FlightReservation> reservations;

    public Itinerary() {
        this.reservations = new ArrayList<>();
    }
    
    public Itinerary(String creditCardNumber, String expiryDate, String cvv) {
        this();
        this.creditCardNumber = creditCardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public Long getItineraryID() {
        return itineraryID;
    }

    public void setItineraryID(Long itineraryID) {
        this.itineraryID = itineraryID;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<FlightReservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<FlightReservation> reservations) {
        this.reservations = reservations;
    }
    
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itineraryID != null ? itineraryID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the itineraryID fields are not set
        if (!(object instanceof Itinerary)) {
            return false;
        }
        Itinerary other = (Itinerary) object;
        if ((this.itineraryID == null && other.itineraryID != null) || (this.itineraryID != null && !this.itineraryID.equals(other.itineraryID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Itinerary[ id=" + itineraryID + " ]";
    }
    
}
