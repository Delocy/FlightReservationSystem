/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package frsreservationclient;

import ejb.session.stateless.CabinClassConfigSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.ItinerarySessionBeanRemote;
import ejb.session.stateless.SeatInventorySessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author zares
 */
public class Main {

    @EJB
    private static FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;

    @EJB
    private static ItinerarySessionBeanRemote itinerarySessionBeanRemote;

    @EJB
    private static FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;

    @EJB
    private static SeatInventorySessionBeanRemote seatInventorySessionBeanRemote;

    @EJB
    private static FlightSessionBeanRemote flightSessionBeanRemote;

    @EJB
    private static CabinClassConfigSessionBeanRemote cabinClassConfigSessionBeanRemote;

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(customerSessionBeanRemote, cabinClassConfigSessionBeanRemote, flightSessionBeanRemote, flightScheduleSessionBeanRemote, seatInventorySessionBeanRemote, flightReservationSessionBeanRemote, itinerarySessionBeanRemote);
        mainApp.runApp();
    }
    
}
