/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package holidayreservationsystemclient;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import ws.holiday.CabinClassNameEnum;
import ws.holiday.Fare;
import ws.holiday.Flight;
import ws.holiday.FlightNotFoundException;
import ws.holiday.FlightNotFoundException_Exception;
import ws.holiday.FlightReservation;
import ws.holiday.FlightReservationExistException;
import ws.holiday.FlightReservationExistException_Exception;
import ws.holiday.FlightReservationNotFoundException_Exception;
import ws.holiday.FlightSchedule;
import ws.holiday.FlightScheduleNotFoundException;
import ws.holiday.FlightScheduleNotFoundException_Exception;
import ws.holiday.FsPair;
import ws.holiday.HolidayReservationSystemWebService;
import ws.holiday.HolidayReservationSystemWebService_Service;
import ws.holiday.InvalidLoginCredentialException_Exception;
import ws.holiday.Itinerary;
import ws.holiday.ItineraryExistException_Exception;
import ws.holiday.ItineraryNotFoundException;
import ws.holiday.ItineraryNotFoundException_Exception;
import ws.holiday.ParseException_Exception;
import ws.holiday.Passenger;
import ws.holiday.PersonNotFoundException_Exception;
import ws.holiday.SeatInventory;
import ws.holiday.SeatInventoryNotFoundException;
import ws.holiday.SeatInventoryNotFoundException_Exception;
import ws.holiday.SeatsBookedException;
import ws.holiday.SeatsBookedException_Exception;
import ws.holiday.UnknownPersistenceException;
import ws.holiday.UnknownPersistenceException_Exception;
import ws.holiday.UnsignedShortArray;

/**
 *
 * @author xinni
 */
public class HolidayReservationSystemClient {
    private Long currentPartner;
    
    /**
     * @param args the command line arguments
     */
    
    public HolidayReservationSystemClient() {
        
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** Welcome to Holiday Reservation System ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    try {
                        loginPage();
                    } catch (InvalidLoginCredentialException_Exception ex) {
                        Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Login successful!\n");
                    menuMain();
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 2) {
                break;
            }
        }
    }
    
    public void loginPage() throws InvalidLoginCredentialException_Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Holiday Reservation System :: Login ***\n");
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        
        currentPartner = doLogin(username, password);
    }
    
    public void menuMain() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        
        while (true) {
            System.out.println("*** Welcome to Holiday Reservation System ***\n");
            System.out.println();
            System.out.println("1: Search for Flights");
            System.out.println("2: View My Flight Reservations");
            System.out.println("3: View My Flight Reservation Details");
            System.out.println("4: Logout");

            response = 0;
            while(response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                if(response == 1) {
                    doSearchFlight();
                } else if(response == 2) {
                    //doViewFlightReservation();
                } else if (response == 3) {
                    //doViewFlightReservationDetails();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 4) {
                        break;
                    }
                }
        
    }
    
    public static Long doLogin(String username, String password) throws InvalidLoginCredentialException_Exception {
        HolidayReservationSystemWebService_Service service = new HolidayReservationSystemWebService_Service();
        HolidayReservationSystemWebService port = service.getHolidayReservationSystemWebServicePort();
        return port.doLogin(username, password);
    }
    
    public void doSearchFlight() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("*** Flight Reservation Portal :: Search Flight ***\n");
            System.out.print("Enter trip type (0 for round-trip, 1 for one-way trip)> ");
            int tripType = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Enter IATA code of departure airport> ");
            String depAirport = scanner.nextLine().trim();
            
            System.out.print("Enter IATA code of destination airport> ");
            String desAirport = scanner.nextLine().trim();
            
            Date departureDate;
            SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy");
            SimpleDateFormat outputFormatter = new SimpleDateFormat("dd/M/yyyy hh:mm:ss a");
            
            System.out.print("Enter Departure Date (dd/mm/yyyy)> ");
            String input = scanner.nextLine().trim();
            departureDate = formatter.parse(input);
            
            Date returnDate = null;
            if (tripType == 0) {
                System.out.print("Enter Return Date (dd/mm/yyyy)> ");
                input = scanner.nextLine().trim();
                returnDate = formatter.parse(input);
            }
            
            System.out.print("Enter number of passengers> ");
            int numPasengers = scanner.nextInt();
            
            System.out.print("Enter flight preference (0 for direct flight, 1 for connecting flight, 2 for any)> ");
            int flightPreference = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Select cabin class (ANY (A), First (F), Business (J), Premium Economy (W), Economy (Y)> ");
            String response = scanner.nextLine().trim();
            
            CabinClassNameEnum cabinClassName = CabinClassNameEnum.FIRST;
            if (response.equalsIgnoreCase("F")) {
                cabinClassName = CabinClassNameEnum.FIRST;
            } else if (response.equalsIgnoreCase("J")) {
                cabinClassName = CabinClassNameEnum.BUSINESS;
            } else if (response.equalsIgnoreCase("W")) {
                cabinClassName = CabinClassNameEnum.PREMIUM_ECONOMY;
            } else if (response.equalsIgnoreCase("Y")) {
                cabinClassName = CabinClassNameEnum.ECONOMY;
            } else if (response.equalsIgnoreCase("A")) { 
                cabinClassName = null;
            }
            
            
            if (flightPreference == 0) { // OUTBOUND direct flight 
                searchDirectFlights(depAirport, desAirport, departureDate, cabinClassName, numPasengers);
            } else if (flightPreference == 1) { // OUTBOUND connecting flights
                searchConnectingFlights(depAirport, desAirport, departureDate, cabinClassName, numPasengers);
            } else if (flightPreference == 2) { // OUTBOUND any flights
                searchDirectFlights(depAirport, desAirport, departureDate, cabinClassName, numPasengers);
                searchConnectingFlights(depAirport, desAirport, departureDate, cabinClassName, numPasengers);
            }
            
            if (tripType == 0) {
                if (flightPreference == 0) { // RETURN direct flight 
                    searchDirectFlights(desAirport, depAirport, returnDate, cabinClassName, numPasengers);
                } else if (flightPreference == 1) { // RETURN connecting flights
                    searchConnectingFlights(desAirport, depAirport, returnDate, cabinClassName, numPasengers);
                } else if (flightPreference == 2) { // RETURN any flights
                    searchDirectFlights(desAirport, depAirport, returnDate, cabinClassName, numPasengers);
                    searchConnectingFlights(desAirport, depAirport, returnDate, cabinClassName, numPasengers);
                }
            }
            
            
                System.out.println("\nDo you want to reserve a flight? (Y/N)> ");
                response = scanner.nextLine().trim();
                if (response.equalsIgnoreCase("N")) {
                    return;
                }

                if (flightPreference == 2) { // no preference
                    while (flightPreference != 0 && flightPreference != 1) {
                        System.out.print("Select type of flight (0. Direct, 1. Connecting)> ");
                        flightPreference = scanner.nextInt();
                    }
                }
                Long o1 = null, o2 = null, i1 = null, i2 = null;
                if (tripType == 0) { // round-trip
                    if (flightPreference == 0) { // direct
                        System.out.print("Enter outbound flight ID to reserve> ");
                        o1 = scanner.nextLong();

                        System.out.print("Enter return flight ID to reserve> ");
                        i1 = scanner.nextLong();
                    } else if (flightPreference == 1) { // connecting
                        System.out.print("Enter 1st connecting outbound flight ID to reserve> ");
                        o1 = scanner.nextLong();
                        System.out.print("Enter 2nd connecting outbound flight ID to reserve> ");
                        o2 = scanner.nextLong();

                        System.out.print("Enter 1st connecting return flight ID to reserve> ");
                        i1 = scanner.nextLong();
                        System.out.print("Enter 2nd connecting return flight ID to reserve> ");
                        i2 = scanner.nextLong();

                    }
                } else if (tripType == 1) { // one-way trip
                    if (flightPreference == 0) { // direct
                        System.out.print("Enter outbound flight ID to reserve> ");
                        o1 = scanner.nextLong();

                    } else if (flightPreference == 1) { // connecting
                        System.out.print("Enter 1st connecting outbound flight ID to reserve> ");
                        o1 = scanner.nextLong();
                        System.out.print("Enter 2nd connecting outbound flight ID to reserve> ");
                        o2 = scanner.nextLong();
                    }
                }

               doReserveFlight(o1, o2, i1, i2, cabinClassName, numPasengers);
            
            
            
        } catch (ParseException ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return;
    }
    
    public void searchDirectFlights(String originAirport, String destinationAirport, Date departureDate, CabinClassNameEnum cabinClassName, int numPassenger) {
        
        try {
            List<FlightSchedule> outboundActual = getListOfFlightSchedules(originAirport, destinationAirport, departureDate.toString(), cabinClassName);
            //System.out.println("SIZE: " + outboundActual.size());
            Calendar c = Calendar.getInstance();
            c.setTime(departureDate);
            c.add(Calendar.DATE, -1);
            //System.out.println("DATE: " + c.toString());
            List<FlightSchedule> outbound1Before = getListOfFlightSchedules(originAirport, destinationAirport, c.getTime().toString(), cabinClassName);
            c.add(Calendar.DATE, -1);
            List<FlightSchedule> outbound2Before = getListOfFlightSchedules(originAirport, destinationAirport, c.getTime().toString(), cabinClassName);
            c.add(Calendar.DATE, -1);
            List<FlightSchedule> outbound3Before = getListOfFlightSchedules(originAirport, destinationAirport, c.getTime().toString(), cabinClassName);
            c.setTime(departureDate);
            c.add(Calendar.DATE, 1);
            List<FlightSchedule> outbound1After = getListOfFlightSchedules(originAirport, destinationAirport, c.getTime().toString(), cabinClassName);
            c.add(Calendar.DATE, 1);
            List<FlightSchedule> outbound2After = getListOfFlightSchedules(originAirport, destinationAirport, c.getTime().toString(), cabinClassName);
            c.add(Calendar.DATE, 1);
            List<FlightSchedule> outbound3After = getListOfFlightSchedules(originAirport, destinationAirport, c.getTime().toString(), cabinClassName);
            
            System.out.println("=======================================================================================================");
            System.out.println("************** DIRECT FLIGHTS FROM " + originAirport + " TO " + destinationAirport + " **************");
            System.out.println("=======================================================================================================");
            System.out.println("************** Flights On Selected Date **************");
            printFlightSchedule(outboundActual, numPassenger, cabinClassName);
            System.out.println("\n************** Flights 1 Day Before Selected Date **************");
            printFlightSchedule(outbound1Before, numPassenger, cabinClassName);
            System.out.println("\n************** Flights 2 Days Before Selected Date **************");
            printFlightSchedule(outbound2Before, numPassenger, cabinClassName);
            System.out.println("\n************** Flights 3 Days Before Selected Date **************");
            printFlightSchedule(outbound3Before, numPassenger, cabinClassName);
            System.out.println("\n************** Flights 1 Day After Selected Date **************");
            printFlightSchedule(outbound1After, numPassenger, cabinClassName);
            System.out.println("\n************** Flights 2 Days After Selected Date **************");
            printFlightSchedule(outbound2After, numPassenger, cabinClassName);
            System.out.println("\n************** Flights 3 Days After Selected Date **************");
            printFlightSchedule(outbound3After, numPassenger, cabinClassName);
            
            System.out.println("");
            
        } catch (FlightNotFoundException_Exception ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException_Exception ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void searchConnectingFlights(String originAirport, String destinationAirport, Date departureDate, CabinClassNameEnum cabinClassName, int numPassenger) {
        try {
            List<FsPair> outboundActual = getListOfConnectingFlightSchedules(originAirport, destinationAirport, departureDate.toString(), cabinClassName);
            
            Calendar c = Calendar.getInstance();
            c.setTime(departureDate);
            c.add(Calendar.DATE, -1);
            //System.out.println("Date: " + c.toString());
            List<FsPair> outbound1Before = getListOfConnectingFlightSchedules(originAirport, destinationAirport, c.getTime().toString(), cabinClassName);
            c.add(Calendar.DATE, -1);
            List<FsPair> outbound2Before = getListOfConnectingFlightSchedules(originAirport, destinationAirport, c.getTime().toString(), cabinClassName);
            c.add(Calendar.DATE, -1);
            List<FsPair> outbound3Before = getListOfConnectingFlightSchedules(originAirport, destinationAirport, c.getTime().toString(), cabinClassName);
            c.setTime(departureDate);
            c.add(Calendar.DATE, 1);
            List<FsPair> outbound1After = getListOfConnectingFlightSchedules(originAirport, destinationAirport, c.getTime().toString(), cabinClassName);
            c.add(Calendar.DATE, 1);
            List<FsPair> outbound2After = getListOfConnectingFlightSchedules(originAirport, destinationAirport, c.getTime().toString(), cabinClassName);
            c.add(Calendar.DATE, 1);
            List<FsPair> outbound3After = getListOfConnectingFlightSchedules(originAirport, destinationAirport, c.getTime().toString(), cabinClassName);
            
            System.out.println("=======================================================================================================");
            System.out.println("************** CONNECTING FLIGHTS FROM " + originAirport + " TO " + destinationAirport + " **************");
            System.out.println("=======================================================================================================");
            System.out.println("************** Flights On Selected Date **************");
            printFlightScheduleWithConnecting(outboundActual, numPassenger, cabinClassName);
            System.out.println("\n************** Flights 1 Day Before Selected Date **************");
            printFlightScheduleWithConnecting(outbound1Before, numPassenger, cabinClassName);
            System.out.println("\n************** Flights 2 Days Before Selected Date **************");
            printFlightScheduleWithConnecting(outbound2Before, numPassenger, cabinClassName);
            System.out.println("\n************** Flights 3 Days Before Selected Date **************");
            printFlightScheduleWithConnecting(outbound3Before, numPassenger, cabinClassName);
            System.out.println("\n************** Flights 1 Day After Selected Date **************");
            printFlightScheduleWithConnecting(outbound1After, numPassenger, cabinClassName);
            System.out.println("\n************** Flights 2 Days After Selected Date **************");
            printFlightScheduleWithConnecting(outbound2After, numPassenger, cabinClassName);
            System.out.println("\n************** Flights 3 Days After Selected Date **************");
            printFlightScheduleWithConnecting(outbound3After, numPassenger, cabinClassName);
            
            System.out.println("");
        } catch (FlightNotFoundException_Exception ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException_Exception ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
    
    
    //helper methods
    public static List<ws.holiday.FsPair> getListOfConnectingFlightSchedules(String origin, String destination, String date, CabinClassNameEnum cabinclassname) throws FlightNotFoundException_Exception, ParseException_Exception {
        HolidayReservationSystemWebService_Service service = new HolidayReservationSystemWebService_Service();
        HolidayReservationSystemWebService port = service.getHolidayReservationSystemWebServicePort();
        return port.getListOfConnectingFlightSchedules(origin, destination, date, cabinclassname);
    }
    
    public static List<FlightSchedule> getListOfFlightSchedules(String origin, String destination, String date, CabinClassNameEnum cabinclassname) throws FlightNotFoundException_Exception, ParseException_Exception, ParseException_Exception {
        HolidayReservationSystemWebService_Service service = new HolidayReservationSystemWebService_Service();
        HolidayReservationSystemWebService port = service.getHolidayReservationSystemWebServicePort();
        return port.getListOfFlightSchedules(origin, destination, date, cabinclassname);
    }
    
    private void printFlightSchedule(List<FlightSchedule> schedules, int numPassenger, CabinClassNameEnum cabinClassName) {
        System.out.printf("%15s%20s%30s%30s%40s%20s%20s%20s%30s%25s%25s\n", "Flight ID", 
                        "Flight Number", 
                        "Departure Airport", 
                        "Arrival Airport", 
                        "Departure Date And Time", 
                        "Duration in hours", 
                        "Arrival Date And Time", 
                        "Cabin Class", 
                        "Seats Left", 
                        "Price/Pax", 
                        "Total Price"); 
        
        for (FlightSchedule fs : schedules) {
            Calendar c = Calendar.getInstance();
            c.setTime(fs.getDepartureDateTime().toGregorianCalendar().getTime());
            double duration = fs.getFlightDuration();
            int hour = (int) duration;
            int min = (int) (duration % 1 * 60);
            int timeDiff = fs.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getGmt() - fs.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getGmt();
            c.add(Calendar.HOUR_OF_DAY, hour);
            c.add(Calendar.MINUTE, min);
            c.add(Calendar.HOUR_OF_DAY, timeDiff);
            Date arrivalDate = c.getTime();
            
            for (SeatInventory s : fs.getSeatInventory()) {
                if (s.getCabinClass().getCabinClassName().equals(cabinClassName) || cabinClassName == null)  {
                    try {
                        System.out.printf("%15s%20s%30s%30s%40s%20s%20s%20s%30s%25s%25s\n", fs.getFlightScheduleId(),
                            fs.getFlightSchedulePlan().getFlightNumber(),
                            fs.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getAirportName(),
                            fs.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getAirportName(),
                            fs.getDepartureDateTime().toString().substring(0, 19),
                            fs.getFlightDuration(),
                            arrivalDate.toString().substring(0, 19),
                            s.getCabinClass().getCabinClassName(),
                            s.getBalanceSeats(),
                            highestFare(fs, cabinClassName).getFare(),
                            highestFare(fs, s.getCabinClass().getCabinClassName()).getFare().multiply(BigDecimal.valueOf(numPassenger))
                        );

                        } catch (FlightScheduleNotFoundException_Exception ex) { 
                        Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                }
                
            }
        }
    }
    
    private void printFlightScheduleWithConnecting(List<ws.holiday.FsPair> pairs, int numPassengers, CabinClassNameEnum cabinClassName) {
        System.out.printf("%15s%20s%40s%40s%30s%20s%30s%30s%30s%25s%25s%25s%30s%45s%45s%40s%20s%30s%30s%30s%25s%25s\n", "Flight ID", 
                        "Flight Number", 
                        "Departure Airport", 
                        "Arrival Airport", 
                        "Departure Date & Time", 
                        "Duration (HRS)", 
                        "Arrival Date & Time", 
                        "Cabin Type", 
                        "Number of Seats Balanced", 
                        "Price per head", 
                        "Total Price",
                        "Connecting Flight ID", 
                        "Connecting Flight Number", 
                        "Connecting Departure Airport", 
                        "Arrival Airport", 
                        "Departure Date & Time", 
                        "Duration (HRS)", 
                        "Arrival Date & Time", 
                        "Cabin Type", 
                        "Number of Seats Balanced", 
                        "Price per head", 
                        "Total Price");
        
        for (FsPair p: pairs) {
            FlightSchedule fs1 = p.getFs1();
            FlightSchedule fs2 = p.getFs2();
            int diff1 = fs1.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getGmt() - 
                    fs2.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getGmt();
            Calendar c = Calendar.getInstance();
            c.setTime(fs1.getDepartureDateTime().toGregorianCalendar().getTime());
            double duration = fs1.getFlightDuration();
            int hour = (int) duration;
            int min = (int) (duration % 1 * 60);
            c.add(Calendar.HOUR_OF_DAY, hour);
            c.add(Calendar.MINUTE, min);
            c.add(Calendar.HOUR_OF_DAY, diff1);
            Date arrival1 = c.getTime();
            int diff2 = fs2.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getGmt() - 
                    fs2.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getGmt();
            Calendar c2 = Calendar.getInstance();
            c2.setTime(fs2.getDepartureDateTime().toGregorianCalendar().getTime());
            double duration2 = fs2.getFlightDuration();
            int hour2 = (int) duration2;
            int min2 = (int) (duration2 % 1 * 60);
            c2.add(Calendar.HOUR_OF_DAY, hour2);
            c2.add(Calendar.MINUTE, min2);
            c2.add(Calendar.HOUR_OF_DAY, diff2);
            Date arrival2 = c2.getTime();
            for (SeatInventory seats1 : fs1.getSeatInventory()) {
                for (SeatInventory seats2 : fs2.getSeatInventory()) {
                    // DO ON BOTH SIDES
                    if ((seats1.getCabinClass().getCabinClassName().equals(cabinClassName) || cabinClassName == null) 
                        && (seats2.getCabinClass().getCabinClassName().equals(cabinClassName) || cabinClassName == null)) {
                        try {
                            System.out.printf("%15s%20s%40s%40s%30s%20s%30s%30s%30s%25s%25s%25s%30s%45s%45s%40s%20s%30s%30s%30s%25s%25s\n", fs1.getFlightScheduleId(),
                                fs1.getFlightSchedulePlan().getFlightNumber(),
                                fs1.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getAirportName(),
                                fs1.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getAirportName(),
                                fs1.getDepartureDateTime().toString().substring(0, 19),
                                fs1.getFlightDuration(),
                                arrival1.toString().substring(0, 19),
                                seats1.getCabinClass().getCabinClassName(),
                                seats1.getBalanceSeats(),
                                highestFare(fs1, seats1.getCabinClass().getCabinClassName()).getFare(),
                                highestFare(fs1, seats1.getCabinClass().getCabinClassName()).getFare().multiply(BigDecimal.valueOf(numPassengers)),
                                fs2.getFlightScheduleId(),
                                fs2.getFlightSchedulePlan().getFlightNumber(),
                                fs2.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getAirportName(),
                                fs2.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getAirportName(),
                                fs2.getDepartureDateTime().toString().substring(0, 19),
                                fs2.getFlightDuration(),
                                arrival2.toString().substring(0, 19),
                                seats2.getCabinClass().getCabinClassName(),
                                seats2.getBalanceSeats(),
                                highestFare(fs2, seats1.getCabinClass().getCabinClassName()).getFare(),
                                highestFare(fs2, seats1.getCabinClass().getCabinClassName()).getFare().multiply(BigDecimal.valueOf(numPassengers))
                                
                            );  
                        } catch (FlightScheduleNotFoundException_Exception ex) {
                            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } 
    }
    
    public static Fare highestFare(FlightSchedule flightschedule, CabinClassNameEnum cabinclassname) throws FlightScheduleNotFoundException_Exception {
        HolidayReservationSystemWebService_Service service = new HolidayReservationSystemWebService_Service();
        HolidayReservationSystemWebService port = service.getHolidayReservationSystemWebServicePort();
        return port.highestFare(flightschedule, cabinclassname);
    }
    
    
    private void doReserveFlight(Long outbound1, Long outbound2, Long inbound1, Long inbound2, CabinClassNameEnum cabinClassType, int noOfPassengers) {
        //try {
            Scanner scanner = new Scanner(System.in);
        
            System.out.println("*** Flight Reservation Portal :: Reserve Flight");

            Itinerary itinerary = new Itinerary();
            BigDecimal pricePerPerson = BigDecimal.ZERO;
            List<FlightSchedule> flightSchedules = new ArrayList<>();
            List<List<String>> seatSelections = new ArrayList<>();
            List<Fare> fares = new ArrayList<>();
            List<SeatInventory> seats = new ArrayList<>();
            List<FlightReservation> reservations = new ArrayList<>();

            List<Long> flightIds = Arrays.asList(outbound1, outbound2, inbound1, inbound2);
            
            for (Long flightId : flightIds) {
                if (flightId != null) {
                    processFlightLeg(flightId, cabinClassType, noOfPassengers, flightSchedules, seatSelections, fares, seats, reservations);
                }
            }
            
            for (Fare fare : fares) {
                pricePerPerson = pricePerPerson.add(fare.getFare());
            }
            
            Itinerary finalItinerary = finaliseItinerary(itinerary, pricePerPerson, noOfPassengers, flightSchedules, seatSelections, reservations, currentPartner);
            System.out.println("Reservation Itinerary with Booking ID: " + finalItinerary.getItineraryID() + " created successfully for Customer " + "!\n");
    }
    
    
    
    private void processFlightLeg(Long flightScheduleId, CabinClassNameEnum cabinClassType, int noOfPassengers, 
                              List<FlightSchedule> flightSchedules, List<List<String>> seatSelections, 
                              List<Fare> fares, List<SeatInventory> seats, List<FlightReservation> reservations) {
        
        try {
            FlightSchedule flightSchedule = retrieveFlightScheduleById(flightScheduleId);
            flightSchedules.add(flightSchedule);
            
            System.out.println("Seat Selection for flight " + flightSchedule.getFlightSchedulePlan().getFlightNumber());
            SeatInventory seatInventory = (cabinClassType == null) ?
                    getSelectedSeatInventory(flightSchedule) :
                    getValidSeatInventory(flightSchedule, cabinClassType);
            seats.add(seatInventory);
            
            Fare fare = highestFare(flightSchedule, seatInventory.getCabinClass().getCabinClassName());
            fares.add(fare);
            
            List<String> seatSelection = getSeatBookings(seatInventory, noOfPassengers);
            seatSelections.add(seatSelection);
            
            FlightReservation reservation = new FlightReservation();
            reservation.setFareBasisCode(fare.getFareBasisCode());
            reservation.setFareAmount(fare.getFare());
            reservation.setCabinClassName(seatInventory.getCabinClass().getCabinClassName());
            
            // FlightReservation r = new FlightReservation(fare.getFareBasisCode(), fare.getFare(), seatInventory.getCabinClass().getCabinClassName());
          
            reservations.add(reservation);
        } catch (SeatInventoryNotFoundException_Exception ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FlightScheduleNotFoundException_Exception ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Itinerary finaliseItinerary(Itinerary itinerary, BigDecimal pricePerPerson, int noOfPassengers, 
                                    List<FlightSchedule> flightSchedules, List<List<String>> seatSelections, 
                                    List<FlightReservation> reservations, long customerId) {
        try {
            System.out.println("Price per person: $" + pricePerPerson + "\nTotal Amount: $" + pricePerPerson.multiply(new BigDecimal(noOfPassengers)));
            doTransaction(itinerary);
            itinerary = createNewItinerary(itinerary, customerId);
            
            List<Passenger> passengers = obtainPassengerDetails(noOfPassengers);
            
            
            for (int i = 0; i < flightSchedules.size(); i++) {
                for (int j = 0; j < passengers.size(); j++) {
                    passengers.get(j).setSeatNumber(seatSelections.get(i).get(j));
                }
                
                //System.out.println(passengers.get(0).getFirstName() + passengers.get(0).getLastName() + passengers.get(0).getPassportNumber()+ passengers.get(0).getSeatNumber());
                createNewReservation(reservations.get(i), passengers, flightSchedules.get(i).getFlightScheduleId(), itinerary.getItineraryID());
            }

        } catch (FlightReservationExistException_Exception ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownPersistenceException_Exception ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FlightScheduleNotFoundException_Exception ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SeatInventoryNotFoundException_Exception ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SeatsBookedException_Exception ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ItineraryNotFoundException_Exception ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PersonNotFoundException_Exception ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ItineraryExistException_Exception ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FlightReservationNotFoundException_Exception ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itinerary;
    }

    private void doTransaction(Itinerary itinerary) {
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Credit Card Number> ");
        String creditCardNum = scanner.nextLine().trim();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("dd/M/yyyy");

        System.out.print("Enter Expiry Date (dd/mm/yyyy)> ");
        String expiryDate = scanner.nextLine().trim();

        System.out.print("Enter cvv> ");
        String cvv = scanner.nextLine().trim();
        itinerary.setCreditCardNumber(creditCardNum); 
        itinerary.setExpiryDate(expiryDate);
        itinerary.setCvv(cvv);
    }
    
    private List<Passenger> obtainPassengerDetails(int noOfPassengers) {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Enter Passenger Details ***\n");
        List<Passenger> passengers = new ArrayList<>();
        for (int i = 1; i <= noOfPassengers; i++) {
            Passenger p = new Passenger();
            System.out.print("Enter passenger " + (i) + " first name> ");
            String firstName = sc.nextLine().trim();
            p.setFirstName(firstName);
            System.out.print("Enter passenger " + (i) + " last name> ");
            String lastName = sc.nextLine().trim();
            p.setLastName(lastName);
            System.out.print("Enter passenger " + (i) + " passport number> ");
            String passport = sc.nextLine().trim();
            p.setPassportNumber(passport);
            passengers.add(p);           

            // seat still not set here -> need to set before persisting
        }
        return passengers;
    } 
    
    private SeatInventory getSelectedSeatInventory(FlightSchedule flightSchedule) {
        Scanner sc = new Scanner(System.in);
        int i = 1;
        System.out.println(" ** Available Cabin Classes ** ");
        for (SeatInventory seats: flightSchedule.getSeatInventory()) {
            String cabinClass;
            if (seats.getCabinClass().getCabinClassName()== CabinClassNameEnum.FIRST) {
                cabinClass = "First Class"; 
            } else if (seats.getCabinClass().getCabinClassName() == CabinClassNameEnum.BUSINESS) {
                cabinClass = "Business Class";
            } else if (seats.getCabinClass().getCabinClassName() == CabinClassNameEnum.PREMIUM_ECONOMY) {
                cabinClass = "Premium Economy Class";
            } else {
                cabinClass = "Economy Class";  
            }

            System.out.println(i + ") " + cabinClass);
            i++;
        }
        while (true) {
            System.out.print("Select desired cabin class> ");
            int input = sc.nextInt();
            sc.nextLine();
            if (input <= flightSchedule.getSeatInventory().size() && input >= 1) {
                return flightSchedule.getSeatInventory().get(input - 1);
            } else {
                System.out.println("Error: Please enter a valid input");
            }
        }   
                
    }
    
    private List<String> getSeatBookings(SeatInventory seatInventory, int noOfPassengers) {
        Scanner sc = new Scanner(System.in);
        int totalAvailSeats = seatInventory.getAvailableSeats();
        int totalReservedSeats = seatInventory.getReserveSeats();
        int totalBalanceSeats = seatInventory.getBalanceSeats();
        
   
        char[][] seats = new char[seatInventory.getCabinClass().getNumRows()][seatInventory.getCabinClass().getNumSeatsAbreast()];

        for(int i = 0; i < seats.length; i++) {
            List<Integer> list = seatInventory.getSeats().get(i).getItem();
            for(int j = 0; j < seats[0].length; j++) {
                seats[i][j] = (char) list.get(j).intValue();
            }
        }
        
        String cabinClassConfig = seatInventory.getCabinClass().getSeatConfiguration();

        
        String type = "";
        if (null != seatInventory.getCabinClass().getCabinClassName()) {
            switch (seatInventory.getCabinClass().getCabinClassName()) {
                case FIRST:
                    type = "First Class";
                    break;
                case BUSINESS:
                    type = "Business Class";
                    break;
                case PREMIUM_ECONOMY:
                    type = "Premium Economy Class";
                    break;
                case ECONOMY:
                    type = "Economy Class";
                    break;
                default:
                    break;
            }
        }

        System.out.println(" -- " + type + " -- ");
        System.out.print("Row  ");
        int count = 0;
        int no = 0;
        for (int i = 0; i < cabinClassConfig.length(); i++) {
            if (Character.isDigit(cabinClassConfig.charAt(i))) {
                no += Integer.parseInt(String.valueOf(cabinClassConfig.charAt(i)));
                while (count < no) {
                    System.out.print((char) ('A' + count) + "  ");
                    count++;
                }
            } else {
                System.out.print("   ");
            }
        }
        System.out.println();

        for (int j = 0; j < seats.length; j++) {
            System.out.printf("%-5s", String.valueOf(j + 1));
            int count2 = 0;
            int no2 = 0;
            for (int i = 0; i < cabinClassConfig.length(); i++) {
                if (Character.isDigit(cabinClassConfig.charAt(i))) {
                    no2 += Integer.parseInt(String.valueOf(cabinClassConfig.charAt(i)));
                    while (count2 < no2) {
                        System.out.print(seats[j][count2] + "  ");
                        count2++;
                    }
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
        System.out.println(" --- Total --- ");
        System.out.println("Number of available seats: " + totalAvailSeats);
        System.out.println("Number of reserved seats: " + totalReservedSeats);
        System.out.println("Number of balance seats: " + totalBalanceSeats);

        List<String> seatSelection = new ArrayList<>();
      
            for (int i = 0; i < noOfPassengers; i++) {                   
                String seatNumber;
                
                while (true) {
                    System.out.print("\nEnter seat to reserve for Passenger " +  (i + 1) + "(Eg. A1)> ");
                    seatNumber = sc.nextLine().trim();
                    boolean booked = checkAvailability(seatInventory, seatNumber);
                    if (booked) {
                        System.out.println("Seat taken!\nPlease select another seat");
                    } else if (seatSelection.contains(seatNumber)) {
                        System.out.println("Duplicate seats detected!\\nPlease try again");
                    } else {
                        break;
                    }
                }
                seatSelection.add(seatNumber);
                
            }
        return seatSelection;   
    }
    
    private void doViewFlightReservation() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** View Flight Reservations ***\n");
        List<Itinerary> list = retrieveItinerariesByPartnerId(currentPartner);
        System.out.printf("%-20s%-30s%-20s%-20s%-30s%-25s\n", "Itinerary ID", "Flight Reservation ID", "Flight Number", "Trip", "Departure Date Time", "Flight Duration");
        
        for (Itinerary itinerary : list) {
//            System.out.println("Itinerary Reservation ID: " + itinerary.getItineraryID());
        
            for (FlightReservation reservation: itinerary.getReservations()) {
                String trip = reservation.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getAirportCode() + " -> " + reservation.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getAirportCode();
                String departureDateTime = reservation.getFlightSchedule().getDepartureDateTime().toString().substring(0, 19);
                String duration = String.valueOf(reservation.getFlightSchedule().getFlightDuration()) + " Hrs";
                String flightNumber = reservation.getFlightSchedule().getFlightSchedulePlan().getFlightNumber();
               
                System.out.printf("%-20s%-30s%-20s%-20s%-30s%-25s\n",
                        itinerary.getItineraryID(),
                        reservation.getFlightReservationId(),
                        flightNumber,
                        trip,
                        departureDateTime,
                        duration
                        );
            }
            System.out.println();          
        }
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
    private void doViewFlightReservationDetails() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("*** View Flight Reservations Details ***\n");     
            
            System.out.print("Enter ID of Itinerary reservation to view in detail> ");
            long id = sc.nextLong();
            sc.nextLine();
            System.out.println();
            Itinerary itinerary = retreiveItineraryById(id);
            
            BigDecimal totalPaid = new BigDecimal(0);
            int idx = 1;
//            System.out.printf("%-10s%-20s%-20s%-25s\n","Flight Number", "Trip", "Departure Date Time", "Flight Duration");
            for (FlightReservation reservation: itinerary.getReservations()) {
                totalPaid = totalPaid.add(reservation.getFareAmount().multiply(new BigDecimal(reservation.getPassengers().size())));
                String trip = reservation.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getAirportCode() + " -> " + reservation.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getAirportCode();
                String departureDateTime = reservation.getFlightSchedule().getDepartureDateTime().toString().substring(0, 19);
                String duration = String.valueOf(reservation.getFlightSchedule().getFlightDuration()) + " Hrs";
                String flightNumber = reservation.getFlightSchedule().getFlightSchedulePlan().getFlightNumber();
                String cabinClass;
                if (reservation.getCabinClassName() == CabinClassNameEnum.FIRST) {
                    cabinClass = "First Class";
                } else if (reservation.getCabinClassName() == CabinClassNameEnum.BUSINESS) {
                    cabinClass = "Business Class";
                } else if (reservation.getCabinClassName() == CabinClassNameEnum.PREMIUM_ECONOMY) {
                    cabinClass = "Premium Economy Class";
                } else {
                    cabinClass = "Economy Class";
                }
                System.out.println("** Reservation " + idx + " **");
                idx++;
                System.out.printf("%-20s%-20s%-30s%-25s\n","Flight Number", "Trip", "Departure Date Time", "Flight Duration");
                System.out.printf("%-20s%-20s%-30s%-25s\n",
                        flightNumber,
                        trip,
                        departureDateTime,
                        duration
                        );
                
                System.out.println();
                System.out.printf("%-20s%-20s%-20s\n","Passenger Name", "Cabin Class", "Seat Number");
                for (Passenger passenger: reservation.getPassengers()) {                  
                    String name = passenger.getFirstName() + " " + passenger.getLastName();           
                    String seatNumber = passenger.getSeatNumber();
                    System.out.printf("%-20s%-20s%-20s\n",
                        name,
                        cabinClass,
                        seatNumber
                        );
                }
                System.out.println();
                BigDecimal totalForThisReservation = new BigDecimal(0);
                totalForThisReservation = totalForThisReservation.add(reservation.getFareAmount()).multiply(new BigDecimal(reservation.getPassengers().size()));
                System.out.println("Total amount paid for this schedule: $" + totalForThisReservation.toString());
                System.out.println();
            }
            System.out.println("Total amount paid: $" + totalPaid.toString());
            System.out.print("Press any key to continue...> ");
            sc.nextLine();
                
        } catch (ItineraryNotFoundException_Exception ex) {
            Logger.getLogger(HolidayReservationSystemClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private static FlightSchedule retrieveFlightScheduleById(long flightscheduleid) throws FlightScheduleNotFoundException_Exception {
        HolidayReservationSystemWebService_Service service = new HolidayReservationSystemWebService_Service();
        HolidayReservationSystemWebService port = service.getHolidayReservationSystemWebServicePort();
        return port.retrieveFlightScheduleById(flightscheduleid);
    }
    
    private static long createNewReservation(ws.holiday.FlightReservation flightreservation, java.util.List<ws.holiday.Passenger> passengers, long flightscheduleid, long itineraryid) throws 
            FlightReservationExistException_Exception,
            UnknownPersistenceException_Exception,
            FlightScheduleNotFoundException_Exception,
            SeatInventoryNotFoundException_Exception,
            SeatsBookedException_Exception,
            ItineraryNotFoundException_Exception,
            FlightReservationNotFoundException_Exception {
        HolidayReservationSystemWebService_Service service = new HolidayReservationSystemWebService_Service();
        HolidayReservationSystemWebService port = service.getHolidayReservationSystemWebServicePort();
        return port.createNewReservation(flightreservation, passengers, flightscheduleid, itineraryid);
    }
    
    private static Itinerary createNewItinerary(ws.holiday.Itinerary itinerary, long partnerid) throws 
            UnknownPersistenceException_Exception,
            PersonNotFoundException_Exception,
            ItineraryExistException_Exception
            {
        HolidayReservationSystemWebService_Service service = new HolidayReservationSystemWebService_Service();
        HolidayReservationSystemWebService port = service.getHolidayReservationSystemWebServicePort();
        return port.createNewItinerary(itinerary, partnerid);
    }

    private static java.util.List<ws.holiday.Itinerary> retrieveItinerariesByPartnerId(long partnerid) {
        HolidayReservationSystemWebService_Service service = new HolidayReservationSystemWebService_Service();
        HolidayReservationSystemWebService port = service.getHolidayReservationSystemWebServicePort();
        return port.retrieveItinerariesByPartnerId(partnerid);
    }

    private static Itinerary retreiveItineraryById(long itineraryid) throws ItineraryNotFoundException_Exception {
        HolidayReservationSystemWebService_Service service = new HolidayReservationSystemWebService_Service();
        HolidayReservationSystemWebService port = service.getHolidayReservationSystemWebServicePort();
        return port.retreiveItineraryById(itineraryid);
    }
    
    private static SeatInventory getValidSeatInventory(ws.holiday.FlightSchedule flightschedule, ws.holiday.CabinClassNameEnum cabinclasstype) throws 
            SeatInventoryNotFoundException_Exception,
            FlightScheduleNotFoundException_Exception {
        HolidayReservationSystemWebService_Service service = new HolidayReservationSystemWebService_Service();
        HolidayReservationSystemWebService port = service.getHolidayReservationSystemWebServicePort();
        return port.getValidSeatInventory(flightschedule, cabinclasstype);
    }
    
    private static boolean checkAvailability(ws.holiday.SeatInventory seatinventory, java.lang.String seatnumber) {
        HolidayReservationSystemWebService_Service service = new HolidayReservationSystemWebService_Service();
        HolidayReservationSystemWebService port = service.getHolidayReservationSystemWebServicePort();
        return port.checkAvailability(seatinventory, seatnumber);
    }

    
}
