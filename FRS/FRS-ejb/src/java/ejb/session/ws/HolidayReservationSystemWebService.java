/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.FlightScheduleSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.Fare;
import entity.FlightSchedule;
import entity.Partner;
import entity.SeatInventory;
import extra.FsPair;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.enumeration.CabinClassNameEnum;
import util.exception.FlightNotFoundException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author xinni
 */
@WebService(serviceName = "HolidayReservationSystemWebService")
@Stateless()
public class HolidayReservationSystemWebService {

    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBeanLocal;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    @WebMethod(operationName = "doLogin")
    public Long doLogin(@WebParam(name = "username") String username,
                        @WebParam(name = "password") String password) throws InvalidLoginCredentialException {
        return partnerSessionBeanLocal.partnerLogin(username, password);
    }
    
    @WebMethod(operationName = "getListOfFlightSchedules")
    public List<FlightSchedule> getListOfFlightSchedules(@WebParam(name = "origin")String origin, 
                                                        @WebParam(name = "destination")String destination, 
                                                        @WebParam(name = "date")String date, 
                                                        @WebParam(name = "cabinclassname")CabinClassNameEnum cabinclassname) throws ParseException, FlightNotFoundException {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date departureDate = dateTimeFormat.parse(date);
        List<FlightSchedule> schedules = flightScheduleSessionBeanLocal.retrieveListOfFlightScheduleDetach(origin, destination, departureDate, cabinclassname);
        for (FlightSchedule fs : schedules) {
            fs.getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);
            fs.getFlightSchedulePlan().getFlight().setFlightSchedulePlan(null);
            fs.getFlightSchedulePlan().getFlight().setReturningFlight(null);
            fs.getFlightSchedulePlan().getFlight().setOriginalFlight(null);
            fs.getFlightSchedulePlan().getFlight().setAircraftConfig(null);
            for (Fare f: fs.getFlightSchedulePlan().getFares()) {
                f.setFlightSchedulePlan(null);
            }
            fs.getFlightSchedulePlan().setReturnSchedulePlan(null);
            fs.getFlightSchedulePlan().setOriginalSchedulePlan(null);
            fs.getFlightSchedulePlan().setFlightSchedule(null);
            fs.setReservations(null);
            for (SeatInventory seats: fs.getSeatInventory()) {
                if (seats.getCabinClass()!= null) {seats.getCabinClass().setAircraftConfig(null);}
                seats.setFlightSchedule(null);
            }
        }
        return schedules;
    }
    
    @WebMethod(operationName = "getListOfConnectingFlightSchedules")
    public List<FsPair> getListOfConnectingFlightSchedules(@WebParam(name = "origin")String origin, 
                                                        @WebParam(name = "destination")String destination, 
                                                        @WebParam(name = "date")String date, 
                                                        @WebParam(name = "cabinclassname")CabinClassNameEnum cabinclassname) throws ParseException, FlightNotFoundException {        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date departureDate = inputFormat.parse(date);
        List<Pair<FlightSchedule, FlightSchedule>> list = flightScheduleSessionBeanLocal.retrieveConnectingFlightSchedulesDetach(origin, destination, departureDate, cabinclassname);
        List<FsPair> schedules = new ArrayList<>();
        
        List<Pair<FlightSchedule, FlightSchedule>> newList = new ArrayList<>();
        for (Pair<FlightSchedule, FlightSchedule> pairs : list) {
            FsPair pair = new FsPair(pairs.getKey(), pairs.getValue());
            schedules.add(pair);
        } 
        for (FsPair pair : schedules) {
            FlightSchedule fs = pair.getFs1();
  
            fs.getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);
            fs.getFlightSchedulePlan().getFlight().setFlightSchedulePlan(null);
            fs.getFlightSchedulePlan().getFlight().setOriginalFlight(null);
            fs.getFlightSchedulePlan().getFlight().setReturningFlight(null);
            fs.getFlightSchedulePlan().getFlight().setAircraftConfig(null);
            for (Fare fare: fs.getFlightSchedulePlan().getFares()) {
                fare.setFlightSchedulePlan(null);
            }
            fs.getFlightSchedulePlan().setReturnSchedulePlan(null);
            fs.getFlightSchedulePlan().setOriginalSchedulePlan(null);
            fs.getFlightSchedulePlan().setFlightSchedule(null);
            fs.setReservations(null);
            for (SeatInventory seats: fs.getSeatInventory()) {
                if (seats.getCabinClass()!= null) {seats.getCabinClass().setAircraftConfig(null);}
                seats.setFlightSchedule(null);
            }
            
            fs = pair.getFs2();
            fs.getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);
            fs.getFlightSchedulePlan().getFlight().setFlightSchedulePlan(null);
            fs.getFlightSchedulePlan().getFlight().setOriginalFlight(null);
            fs.getFlightSchedulePlan().getFlight().setReturningFlight(null);
            fs.getFlightSchedulePlan().getFlight().setAircraftConfig(null);
            for (Fare fare: fs.getFlightSchedulePlan().getFares()) {
                fare.setFlightSchedulePlan(null);
            }
            fs.getFlightSchedulePlan().setReturnSchedulePlan(null);
            fs.getFlightSchedulePlan().setOriginalSchedulePlan(null);
            fs.getFlightSchedulePlan().setFlightSchedule(null);
            fs.setReservations(null);
            for (SeatInventory seats: fs.getSeatInventory()) {
                if (seats.getCabinClass()!= null) {seats.getCabinClass().setAircraftConfig(null);}
                seats.setFlightSchedule(null);
            }

        }
        
        return schedules;
    }
    
    @WebMethod(operationName = "highestFare")
    public Fare highestFare(@WebParam(name = "flightschedule")FlightSchedule flightschedule, 
                            @WebParam(name = "cabinclassname")CabinClassNameEnum cabinClassName) throws FlightScheduleNotFoundException {
        Fare fare = flightScheduleSessionBeanLocal.highestFare(flightschedule, cabinClassName);
        fare.setFlightSchedulePlan(null);
        
        return fare;
    }
}
