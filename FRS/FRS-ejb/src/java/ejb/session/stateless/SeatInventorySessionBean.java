/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClassConfig;
import entity.FlightSchedule;
import entity.SeatInventory;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.SeatInventoryNotFoundException;

/**
 *
 * @author zares
 */
@Stateless
public class SeatInventorySessionBean implements SeatInventorySessionBeanRemote, SeatInventorySessionBeanLocal {

    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public SeatInventory createSeatInventory(SeatInventory seatInventory, FlightSchedule flightSchedule, CabinClassConfig cabinClass) {
        int noOfRows = cabinClass.getNumRows();
        int noOfSeatsAbreast = cabinClass.getNumSeatsAbreast();
        char[][] seats = new char[noOfRows][noOfSeatsAbreast];

        for (int i = 0; i < noOfRows; i++) {
            for (int j = 0; j < noOfSeatsAbreast; j++) {
                seats[i][j] = '-';
            }
        }
        seatInventory.setSeats(seats);
        
        em.persist(seatInventory);

        seatInventory.setCabinClass(cabinClass);
        seatInventory.setFlightSchedule(flightSchedule);
        flightSchedule.getSeatInventory().add(seatInventory);

        return seatInventory;
    }
    
    public SeatInventory retrieveSeatsBySeatId(Long seatInventoryID) throws SeatInventoryNotFoundException{
        SeatInventory seat = em.find(SeatInventory.class, seatInventoryID);
        
        if(seat != null) {
            return seat;
        } else {
            throw new SeatInventoryNotFoundException("Seat Inventory does not exist!");
        }
    }
    
    @Override
    public void deleteSeatInventory(List<SeatInventory> seatInventories) {
        for (SeatInventory seat: seatInventories) {
            em.remove(seat);
        }
    }
}
