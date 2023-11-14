/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClassConfig;
import entity.FlightSchedule;
import entity.SeatInventory;
import java.util.List;
import javax.ejb.Remote;
import util.exception.SeatInventoryNotFoundException;
import util.exception.SeatsBookedException;

/**
 *
 * @author zares
 */
@Remote
public interface SeatInventorySessionBeanRemote {
    public SeatInventory createSeatInventory(SeatInventory seatInventory, FlightSchedule flightSchedule, CabinClassConfig cabinClass);
    
    public SeatInventory retrieveSeatsBySeatId(Long seatInventoryID) throws SeatInventoryNotFoundException;

    public void deleteSeatInventory(List<SeatInventory> seats);

    public boolean checkAvailability(SeatInventory seatInventory, String seatNumber);

    public void bookSeat(long seatInventoryId, String seatNumber) throws SeatInventoryNotFoundException, SeatsBookedException;
}
