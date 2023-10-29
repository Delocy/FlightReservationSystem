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
public class SeatInventory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatInventoryId;
    @Column(nullable = false, length = 5)
    private Long totalAvailableSeats;
    @Column(nullable = false, length = 5)
    private Long totalReserveSeats;
    @Column(nullable = false, length = 5)
    private Long totalBalanceSeats;
    @Column(nullable = false, length = 5)
    private Long availableSeats;
    @Column(nullable = false, length = 5)
    private Long reserveSeats;
    @Column(nullable = false, length = 5)
    private Long balanceSeats;
    

    public Long getSeatInventoryId() {
        return seatInventoryId;
    }

    public void setSeatInventoryId(Long seatInventoryId) {
        this.seatInventoryId = seatInventoryId;
    }

    public Long getTotalAvailableSeats() {
        return totalAvailableSeats;
    }

    public void setTotalAvailableSeats(Long totalAvailableSeats) {
        this.totalAvailableSeats = totalAvailableSeats;
    }

    public Long getTotalReserveSeats() {
        return totalReserveSeats;
    }

    public void setTotalReserveSeats(Long totalReserveSeats) {
        this.totalReserveSeats = totalReserveSeats;
    }

    public Long getTotalBalanceSeats() {
        return totalBalanceSeats;
    }

    public void setTotalBalanceSeats(Long totalBalanceSeats) {
        this.totalBalanceSeats = totalBalanceSeats;
    }

    public Long getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Long availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Long getReserveSeats() {
        return reserveSeats;
    }

    public void setReserveSeats(Long reserveSeats) {
        this.reserveSeats = reserveSeats;
    }

    public Long getBalanceSeats() {
        return balanceSeats;
    }

    public void setBalanceSeats(Long balanceSeats) {
        this.balanceSeats = balanceSeats;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (seatInventoryId != null ? seatInventoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the seatInventoryId fields are not set
        if (!(object instanceof SeatInventory)) {
            return false;
        }
        SeatInventory other = (SeatInventory) object;
        if ((this.seatInventoryId == null && other.seatInventoryId != null) || (this.seatInventoryId != null && !this.seatInventoryId.equals(other.seatInventoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SeatInventory[ id=" + seatInventoryId + " ]";
    }
    
}
