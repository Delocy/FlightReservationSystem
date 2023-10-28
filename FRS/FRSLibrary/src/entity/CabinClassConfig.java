/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    @Enumerated(EnumType.STRING)
    private CabinClassNameEnum cabinClassName;
    @Column(nullable = false)
    private String seatConfiguration; //idk what type

    public Long getCabinClassConfigId() {
        return cabinClassConfigId;
    }

    public void setCabinClassConfigId(Long cabinClassConfigId) {
        this.cabinClassConfigId = cabinClassConfigId;
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
