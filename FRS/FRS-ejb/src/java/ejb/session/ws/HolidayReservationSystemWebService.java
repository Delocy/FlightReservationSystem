/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.FlightReservationSessionBeanLocal;
import ejb.session.stateless.FlightScheduleSessionBeanLocal;
import ejb.session.stateless.ItinerarySessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.SeatInventorySessionBeanLocal;
import entity.Fare;
import entity.FlightReservation;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Itinerary;
import entity.Partner;
import entity.Passenger;
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
import util.exception.CustomerNotFoundException;
import util.exception.FlightNotFoundException;
import util.exception.FlightReservationExistException;
import util.exception.FlightReservationNotFoundException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ItineraryExistException;
import util.exception.ItineraryNotFoundException;
import util.exception.PersonNotFoundException;
import util.exception.SeatInventoryNotFoundException;
import util.exception.SeatsBookedException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@WebService(serviceName = "HolidayReservationSystemWebService")
@Stateless()
public class HolidayReservationSystemWebService {

    @EJB
    private ItinerarySessionBeanLocal itinerarySessionBeanLocal;

    @EJB
    private FlightReservationSessionBeanLocal flightReservationSessionBeanLocal;

    @EJB
    private SeatInventorySessionBeanLocal seatInventorySessionBeanLocal;

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
            
            // TRIALLLLL
//            for (FlightSchedulePlan fsp : fs.getFlightSchedulePlan().getFlight().getFlightSchedulePlan()) {
//                fsp.setFlight(null);
//            }
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
                                                        @WebParam(name = "cabinclassname")CabinClassNameEnum cabinclassname) throws ParseException, FlightNotFoundException {        
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
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
    
    @WebMethod(operationName = "retrieveFlightScheduleById")
    public FlightSchedule retrieveFlightScheduleById(@WebParam(name = "flightscheduleid") long flightscheduleid) throws FlightScheduleNotFoundException {
        FlightSchedule fs = flightScheduleSessionBeanLocal.retrieveFlightScheduleByIdDetached(flightscheduleid);
        fs.getFlightSchedulePlan().getFlight().getFlightRoute().getFlights().clear();
        fs.getFlightSchedulePlan().getFlight().getFlightRoute().setOriginAirport(null);
        fs.getFlightSchedulePlan().getFlight().getFlightRoute().setDestinationAirport(null);
        fs.getFlightSchedulePlan().getFlight().setFlightSchedulePlan(null);
        fs.getFlightSchedulePlan().getFlight().setOriginalFlight(null);
        fs.getFlightSchedulePlan().getFlight().setReturningFlight(null);
        fs.getFlightSchedulePlan().getFlight().setAircraftConfig(null);
        for (Fare fare: fs.getFlightSchedulePlan().getFares()) {
            fare.setFlightSchedulePlan(null);
        }
        fs.getFlightSchedulePlan().setReturnSchedulePlan(null);
        fs.getFlightSchedulePlan().setOriginalSchedulePlan(null);
        fs.getFlightSchedulePlan().getFlightSchedule().clear();
        fs.getReservations().clear();
        
//        for (FlightReservation fr : fs.getReservations()) {
//            fr.setFlightSchedule(null);
//        }
        for (SeatInventory seats: fs.getSeatInventory()) {
            if (seats.getCabinClass()!= null) {seats.getCabinClass().setAircraftConfig(null);}
            seats.setFlightSchedule(null);
        }
        
        //fs.setFlightSchedulePlan(null);
        return fs;   
    }
    
    @WebMethod(operationName = "getValidSeatInventory")
    public SeatInventory getValidSeatInventory(@WebParam(name = "flightscheduleentity") FlightSchedule flightschedule,
            @WebParam(name = "cabinclasstype") CabinClassNameEnum cabinclasstype) throws 
            FlightScheduleNotFoundException, 
            SeatInventoryNotFoundException {
        SeatInventory seats = flightScheduleSessionBeanLocal.getValidSeatInventoryDetached(flightschedule, cabinclasstype);
        if (seats.getCabinClass()!= null) {seats.getCabinClass().setAircraftConfig(null);}
        //seats.setCabinClass(null);
        seats.setFlightSchedule(null);
        return seats;
    }
    
    @WebMethod(operationName = "checkAvailability")
    public boolean checkAvailability(@WebParam(name = "seatinventory") SeatInventory seatinventory,
            @WebParam(name = "seatnumber") String seatnumber) {
        return seatInventorySessionBeanLocal.checkAvailability(seatinventory, seatnumber);
    }
    
    @WebMethod(operationName = "createNewReservation")
    public long createNewReservation(@WebParam(name = "reservationentity") FlightReservation flightreservation,
            @WebParam(name = "passengers") List<Passenger> passengers,
            @WebParam(name = "flightscheduleid") long flightscheduleid,
            @WebParam(name = "itineraryid") long itineraryid) throws 
            FlightReservationExistException,
            UnknownPersistenceException,
            FlightScheduleNotFoundException,
            SeatInventoryNotFoundException,
            SeatsBookedException,
            ItineraryNotFoundException,
            FlightReservationNotFoundException {
        
        Long id = flightReservationSessionBeanLocal.createNewReservationDetached(flightreservation, passengers, flightscheduleid, itineraryid);
        //FlightReservation fr = flightReservationSessionBeanLocal.retrieveFlightReservationByReserverationId(id);
//        fr.setFlightSchedule(null);
//        fr.setItinerary(null);

        return id;
    }
    
    @WebMethod(operationName = "createNewItinerary")
    public Itinerary createNewItinerary(
            @WebParam(name = "itinerary") Itinerary itinerary,
//            @WebParam(name = "date") String date,
//            @WebParam(name = "cvv") String cvv,
            @WebParam(name = "userid") long userid) throws 
            UnknownPersistenceException,
            PersonNotFoundException,
            ItineraryExistException
            {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy");
//        Date expiryDate = formatter.parse(date);
        
        Itinerary i = itinerarySessionBeanLocal.createNewItinerary(itinerary, userid);
        i.setPerson(null);
        for (FlightReservation fr : i.getReservations()) {
            fr.setItinerary(null);
        }
        return i;
    }
    
    @WebMethod(operationName = "retrieveItinerariesByPartnerId")
    public List<Itinerary> retrieveItinerariesByPartnerId(@WebParam(name = "partnerid") long partnerid) {
        List<Itinerary> list = itinerarySessionBeanLocal.retrieveItinerariesByPersonIdDetached(partnerid);
        for (Itinerary itinerary: list) {
            itinerary.getPerson().setItineraries(null);
            //itinerary.setPerson(null);
            for (FlightReservation res: itinerary.getReservations()) {
                res.setItinerary(null);
                res.getFlightSchedule().setReservations(null);
                res.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);
                res.getFlightSchedule().getFlightSchedulePlan().getFlight().setFlightSchedulePlan(null);
                res.getFlightSchedule().getFlightSchedulePlan().getFlight().setOriginalFlight(null);
                res.getFlightSchedule().getFlightSchedulePlan().getFlight().setReturningFlight(null);
                res.getFlightSchedule().getFlightSchedulePlan().getFlight().setAircraftConfig(null);
                for (Fare fare: res.getFlightSchedule().getFlightSchedulePlan().getFares()) {
                    fare.setFlightSchedulePlan(null);
                }
                res.getFlightSchedule().getFlightSchedulePlan().setReturnSchedulePlan(null);
                res.getFlightSchedule().getFlightSchedulePlan().setOriginalSchedulePlan(null);
                res.getFlightSchedule().getFlightSchedulePlan().setFlightSchedule(null);
                res.getFlightSchedule().setReservations(null);
                for (SeatInventory seatInventory: res.getFlightSchedule().getSeatInventory()) {
                    if (seatInventory.getCabinClass()!= null) {seatInventory.getCabinClass().setAircraftConfig(null);}
                    seatInventory.setFlightSchedule(null);
                }
            }
        }
        return list;
    }
    
    @WebMethod(operationName = "retreiveItineraryById")
    public Itinerary retrieveItineraryById(@WebParam(name = "itineraryid") long itineraryid) throws ItineraryNotFoundException {
       Itinerary itinerary = itinerarySessionBeanLocal.retrieveItineraryByIDDetached(itineraryid);
       itinerary.getPerson().setItineraries(null);
       //itinerary.setPerson(null);
        for (FlightReservation res: itinerary.getReservations()) {
            res.setItinerary(null);
            res.getFlightSchedule().setReservations(null);
            res.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);
            res.getFlightSchedule().getFlightSchedulePlan().getFlight().setFlightSchedulePlan(null);
            res.getFlightSchedule().getFlightSchedulePlan().getFlight().setOriginalFlight(null);
            res.getFlightSchedule().getFlightSchedulePlan().getFlight().setReturningFlight(null);
            res.getFlightSchedule().getFlightSchedulePlan().getFlight().setAircraftConfig(null);
            for (Fare fare: res.getFlightSchedule().getFlightSchedulePlan().getFares()) {
                fare.setFlightSchedulePlan(null);
            }
            res.getFlightSchedule().getFlightSchedulePlan().setReturnSchedulePlan(null);
            res.getFlightSchedule().getFlightSchedulePlan().setOriginalSchedulePlan(null);
            res.getFlightSchedule().getFlightSchedulePlan().setFlightSchedule(null);
            res.getFlightSchedule().setReservations(null);
            for (SeatInventory seats: res.getFlightSchedule().getSeatInventory()) {
                if (seats.getCabinClass()!= null) {seats.getCabinClass().setAircraftConfig(null);}
                seats.setFlightSchedule(null);
            }
        }
        return itinerary;
    }
}
