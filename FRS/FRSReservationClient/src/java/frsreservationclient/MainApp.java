/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsreservationclient;

import ejb.session.stateless.CabinClassConfigSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.ItinerarySessionBeanRemote;
import ejb.session.stateless.SeatInventorySessionBeanRemote;
import entity.Customer;
import entity.Flight;
import entity.FlightSchedule;
import entity.SeatInventory;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import util.enumeration.CabinClassNameEnum;
import util.exception.CabinClassNameNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
public class MainApp {
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private CabinClassConfigSessionBeanRemote cabinClassConfigSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private SeatInventorySessionBeanRemote seatInventorySessionBeanRemote;
    private FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;
    private ItinerarySessionBeanRemote itinerarySessionBeanRemote;
    private Customer currentCustomer;

    public MainApp() {
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, CabinClassConfigSessionBeanRemote cabinClassConfigSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, SeatInventorySessionBeanRemote seatInventorySessionBeanRemote, FlightReservationSessionBeanRemote flightReservationSessionBeanRemote, ItinerarySessionBeanRemote itinerarySessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.cabinClassConfigSessionBeanRemote = cabinClassConfigSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.seatInventorySessionBeanRemote = seatInventorySessionBeanRemote;
        this.flightReservationSessionBeanRemote = flightReservationSessionBeanRemote;
        this.itinerarySessionBeanRemote = itinerarySessionBeanRemote;
    }

    
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** Welcome to FRS Reservation Portal ***\n");
            System.out.println("1: Login");
            System.out.println("2: Register for an account");
            System.out.println("3: Exit\n");
            response = 0;
            while (response < 1 || response > 3) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");
                        menuMain();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credentials " + ex.getMessage());
                    }
                } else if (response == 2) {
                    doRegister();
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 3) {
                break;
            }
        }
        
    }
    
    private void doLogin() throws InvalidLoginCredentialException
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** Flight Reservation Portal :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentCustomer = customerSessionBeanRemote.customerLogin(username, password);      
            menuMain();
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    private void doRegister() {
        try {
            Scanner scanner = new Scanner(System.in);
            Customer customer = new Customer();
            System.out.println("*** Flight Reservation Portal :: Register ***\n");
            System.out.print("Enter first name> ");
            String firstName = scanner.nextLine().trim();
            customer.setFirstName(firstName);
            System.out.print("Enter last name> ");
            String lastName = scanner.nextLine().trim();
            customer.setLastName(lastName);
            System.out.print("Enter email address> ");
            String email = scanner.nextLine().trim();
            customer.setEmail(email);
            System.out.print("Enter mobile number> ");
            String mobileNumber = scanner.nextLine().trim();
            customer.setMobileNumber(mobileNumber);
            System.out.print("Enter address> ");
            String address = scanner.nextLine().trim();
            customer.setAddress(address);
            System.out.print("Enter username> ");
            String username = scanner.nextLine().trim();
            customer.setUsername(username);
            System.out.print("Enter password> ");
            String password = scanner.nextLine().trim();
            customer.setPassword(password);
            
            Long customerId = customerSessionBeanRemote.createNewCustomer(customer);
            currentCustomer = customerSessionBeanRemote.retrieveCustomerByCustomerId(customerId);
            menuMain();
        } catch (UnknownPersistenceException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomerUsernameExistException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomerNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void menuMain() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        
        while (true) {
            System.out.println("*** Welcome to Flight Reservation System ***\n");
            System.out.println("You are currently logged in as " + currentCustomer.getFirstName() + " " + currentCustomer.getLastName() + "!\n");
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
            CabinClassNameEnum cabinClassName;
            if (response.equalsIgnoreCase("A")) { cabinClassName = null; }
            else {
                cabinClassName = cabinClassConfigSessionBeanRemote.fetchCabinClassNameEnum(response);
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
                while (flightPreference != 0 || flightPreference != 1) {
                    System.out.print("Select type of flight (1. Direct, 2. Connecting)> ");
                    flightPreference = scanner.nextInt();
                }
            }
            Long o1, o2, i1, i2;
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
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CabinClassNameNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return;
    }
    
    public void searchDirectFlights(String originAirport, String destinationAirport, Date departureDate, CabinClassNameEnum cabinClassName, int numPassenger) {
        
        try {
            List<Flight> flight = flightSessionBeanRemote.retrieveFlightsByFlightRoute(originAirport, destinationAirport);
            //System.out.println("MAIN: " + flight.size());
            List<FlightSchedule> outboundActual = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, destinationAirport, departureDate, cabinClassName);
            //System.out.println("SIZE: " + outboundActual.size());
            Calendar c = Calendar.getInstance();
            c.setTime(departureDate);
            c.add(Calendar.DATE, -1);
            //System.out.println("DATE: " + c.toString());
            List<FlightSchedule> outbound1Before = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, destinationAirport, c.getTime(), cabinClassName);
            c.add(Calendar.DATE, -1);
            List<FlightSchedule> outbound2Before = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, destinationAirport, c.getTime(), cabinClassName);
            c.add(Calendar.DATE, -1);
            List<FlightSchedule> outbound3Before = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, destinationAirport, c.getTime(), cabinClassName);
            c.setTime(departureDate);
            c.add(Calendar.DATE, 1);
            List<FlightSchedule> outbound1After = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, destinationAirport, c.getTime(), cabinClassName);
            c.add(Calendar.DATE, 1);
            List<FlightSchedule> outbound2After = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, destinationAirport, c.getTime(), cabinClassName);
            c.add(Calendar.DATE, 1);
            List<FlightSchedule> outbound3After = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, destinationAirport, c.getTime(), cabinClassName);
            
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
            
        } catch (FlightNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void searchConnectingFlights(String originAirport, String destinationAirport, Date departureDate, CabinClassNameEnum cabinClassName, int numPassenger) {
        try {
            List<Pair<FlightSchedule, FlightSchedule>> outboundActual = flightScheduleSessionBeanRemote.retrieveConnectingFlightSchedules(originAirport, destinationAirport, departureDate, cabinClassName);
            
            Calendar c = Calendar.getInstance();
            c.setTime(departureDate);
            c.add(Calendar.DATE, -1);
            //System.out.println("Date: " + c.toString());
            List<Pair<FlightSchedule, FlightSchedule>> outbound1Before = flightScheduleSessionBeanRemote.retrieveConnectingFlightSchedules(originAirport, destinationAirport, c.getTime(), cabinClassName);
            c.add(Calendar.DATE, -1);
            List<Pair<FlightSchedule, FlightSchedule>> outbound2Before = flightScheduleSessionBeanRemote.retrieveConnectingFlightSchedules(originAirport, destinationAirport, c.getTime(), cabinClassName);
            c.add(Calendar.DATE, -1);
            List<Pair<FlightSchedule, FlightSchedule>> outbound3Before = flightScheduleSessionBeanRemote.retrieveConnectingFlightSchedules(originAirport, destinationAirport, c.getTime(), cabinClassName);
            c.setTime(departureDate);
            c.add(Calendar.DATE, 1);
            List<Pair<FlightSchedule, FlightSchedule>> outbound1After = flightScheduleSessionBeanRemote.retrieveConnectingFlightSchedules(originAirport, destinationAirport, c.getTime(), cabinClassName);
            c.add(Calendar.DATE, 1);
            List<Pair<FlightSchedule, FlightSchedule>> outbound2After = flightScheduleSessionBeanRemote.retrieveConnectingFlightSchedules(originAirport, destinationAirport, c.getTime(), cabinClassName);
            c.add(Calendar.DATE, 1);
            List<Pair<FlightSchedule, FlightSchedule>> outbound3After = flightScheduleSessionBeanRemote.retrieveConnectingFlightSchedules(originAirport, destinationAirport, c.getTime(), cabinClassName);
            
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
            printFlightScheduleWithConnecting(outbound2Before, numPassenger, cabinClassName);
            System.out.println("\n************** Flights 1 Day After Selected Date **************");
            printFlightScheduleWithConnecting(outbound1After, numPassenger, cabinClassName);
            System.out.println("\n************** Flights 2 Days After Selected Date **************");
            printFlightScheduleWithConnecting(outbound2After, numPassenger, cabinClassName);
            System.out.println("\n************** Flights 3 Days After Selected Date **************");
            printFlightScheduleWithConnecting(outbound3After, numPassenger, cabinClassName);
            
            System.out.println("");
        } catch (FlightScheduleNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FlightNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
            c.setTime(fs.getDepartureDateTime());
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
                            flightScheduleSessionBeanRemote.lowestFare(fs, cabinClassName).getFare(),
                            flightScheduleSessionBeanRemote.lowestFare(fs, s.getCabinClass().getCabinClassName()).getFare().multiply(BigDecimal.valueOf(numPassenger))
                        );

                        } catch (FlightScheduleNotFoundException ex) {
                        Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
        }
    }
    
    private void printFlightScheduleWithConnecting(List<Pair<FlightSchedule, FlightSchedule>> pairs, int numPassengers, CabinClassNameEnum cabinClassName) throws FlightScheduleNotFoundException {
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
        for (Pair<FlightSchedule, FlightSchedule> pair: pairs) {
            FlightSchedule fs1 = pair.getKey();
            FlightSchedule fs2 = pair.getValue();
            int diff1 = fs1.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getGmt() - 
                    fs2.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getGmt();
            Calendar c = Calendar.getInstance();
            c.setTime(fs1.getDepartureDateTime());
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
            c2.setTime(fs2.getDepartureDateTime());
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
                    System.out.printf("%15s%20s%40s%40s%30s%20s%30s%30s%30s%25s%25s%25s%30s%45s%45s%40s%20s%30s%30s%30s%25s%25s\n", fs1.getFlightScheduleId(), 
                        fs1.getFlightSchedulePlan().getFlightNumber(), 
                        fs1.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getAirportName(), 
                        fs1.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getAirportName(), 
                        fs1.getDepartureDateTime().toString().substring(0, 19), 
                        fs1.getFlightDuration(), 
                        arrival1.toString().substring(0, 19), 
                        seats1.getCabinClass().getCabinClassName(), 
                        seats1.getBalanceSeats(), 
                        flightScheduleSessionBeanRemote.lowestFare(fs1, seats1.getCabinClass().getCabinClassName()).getFare(), 
                        flightScheduleSessionBeanRemote.lowestFare(fs1, seats1.getCabinClass().getCabinClassName()).getFare().multiply(BigDecimal.valueOf(numPassengers)),
                        fs2.getFlightScheduleId(),
                        fs2.getFlightSchedulePlan().getFlightNumber(), 
                        fs2.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getAirportName(), 
                        fs2.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getAirportName(), 
                        fs2.getDepartureDateTime().toString().substring(0, 19), 
                        fs2.getFlightDuration(), 
                        arrival2.toString().substring(0, 19), 
                        seats2.getCabinClass().getCabinClassName(), 
                        seats2.getBalanceSeats(), 
                        flightScheduleSessionBeanRemote.lowestFare(fs2, seats1.getCabinClass().getCabinClassName()).getFare(), 
                        flightScheduleSessionBeanRemote.lowestFare(fs2, seats1.getCabinClass().getCabinClassName()).getFare().multiply(BigDecimal.valueOf(numPassengers))
                 
                    );
                    }
                }
            }
        }
    }
}
