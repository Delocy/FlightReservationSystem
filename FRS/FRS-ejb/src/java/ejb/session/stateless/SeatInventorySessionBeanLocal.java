/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClassConfig;
import entity.FlightSchedule;
import entity.SeatInventory;
import java.util.List;
import javax.ejb.Local;
import util.exception.SeatInventoryNotFoundException;

/**
 *
 * @author zares
 */
@Local
public interface SeatInventorySessionBeanLocal {

    public SeatInventory createSeatInventory(SeatInventory seatInventory, FlightSchedule flightSchedule, CabinClassConfig cabinClass);

    public SeatInventory retrieveSeatsBySeatId(Long seatInventoryID) throws SeatInventoryNotFoundException;

    public void deleteSeatInventory(List<SeatInventory> seats);
    
}
