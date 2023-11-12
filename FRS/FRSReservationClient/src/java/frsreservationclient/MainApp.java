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
import entity.FlightSchedule;
import entity.SeatInventory;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.CabinClassNameEnum;
import util.exception.CabinClassNameNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
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
            if (response == 2) {
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
        
        System.out.println("*** Welcome to Flight Reservation System ***\n");
        System.out.println("You are currently logged in as " + currentCustomer.getFirstName() + " " + currentCustomer.getLastName() + "!\n");
        System.out.println();
        System.out.println("1: Reserve Flight");
        System.out.println("2: View My Flight Reservations");
        System.out.println("3: View My Flight Reservation Details");
        System.out.println("4: Logout");

        response = 0;
        while (true) {
            while(response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                if(response == 1) {
                    //doSearchFlight();
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
            
            Date returnDate;
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
            cabinClassName = cabinClassConfigSessionBeanRemote.fetchCabinClassNameEnum(response);
            
            
            if (flightPreference == 0) { // OUTBOUND direct flight 
                searchDirectFlights(depAirport, desAirport, departureDate, cabinClassName, numPasengers);
            } else if (flightPreference == 1) { // OUTBOUND connecting flights
                
            } else if (flightPreference == 2) { // OUTBOUND any flights
                
            }

        } catch (ParseException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CabinClassNameNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void searchDirectFlights(String originAirport, String destinationAirport, Date departureDate, CabinClassNameEnum cabinClassName, int numPassenger) {
        List<FlightSchedule> outboundActual = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, originAirport, departureDate, cabinClassName);
        
        Calendar c = Calendar.getInstance();
        c.setTime(departureDate);
        c.add(Calendar.DATE, -1);
        List<FlightSchedule> outbound1Before = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, originAirport, c.getTime(), cabinClassName);
        c.add(Calendar.DATE, -1);
        List<FlightSchedule> outbound2Before = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, originAirport, c.getTime(), cabinClassName);
        c.add(Calendar.DATE, -1);
        List<FlightSchedule> outbound3Before = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, originAirport, c.getTime(), cabinClassName);
        c.add(Calendar.DATE, 1);
        List<FlightSchedule> outbound1After = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, originAirport, c.getTime(), cabinClassName);
        c.add(Calendar.DATE, 1);
        List<FlightSchedule> outbound2After = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, originAirport, c.getTime(), cabinClassName);
        c.add(Calendar.DATE, 1);
        List<FlightSchedule> outbound3After = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, originAirport, c.getTime(), cabinClassName);
        
        System.out.println("************** DIRECT FLIGHTS FROM " + originAirport + " TO " + destinationAirport + " **************");
        System.out.println("************** Flights On Selected Date **************");
        printFlightSchedule(outboundActual, numPassenger, cabinClassName);
        System.out.println("\n************** Flights 1 Day Before Selected Date **************");
        printFlightSchedule(outbound1Before, numPassenger, cabinClassName);
        System.out.println("\n************** Flights 2 Days Before Selected Date **************");
        printFlightSchedule(outbound2Before, numPassenger, cabinClassName);
        System.out.println("\n************** Flights 3 Days Before Selected Date **************");
        printFlightSchedule(outbound2Before, numPassenger, cabinClassName);
        System.out.println("\n************** Flights 1 Day After Selected Date **************");
        printFlightSchedule(outbound1After, numPassenger, cabinClassName);
        System.out.println("\n************** Flights 2 Days After Selected Date **************");
        printFlightSchedule(outbound2After, numPassenger, cabinClassName);
        System.out.println("\n************** Flights 3 Days After Selected Date **************");
        printFlightSchedule(outbound3After, numPassenger, cabinClassName);
        
    }
    /*
    public void searchConnectingFlights(String originAirport, String destinationAirport, Date departureDate, CabinClassNameEnum cabinClassName, int numPassenger) {
        List<FlightSchedule> outboundActual = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, originAirport, departureDate, cabinClassName);
        
        Calendar c = Calendar.getInstance();
        c.setTime(departureDate);
        c.add(Calendar.DATE, -1);
        List<FlightSchedule> outbound1Before = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, originAirport, c.getTime(), cabinClassName);
        c.add(Calendar.DATE, -1);
        List<FlightSchedule> outbound2Before = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, originAirport, c.getTime(), cabinClassName);
        c.add(Calendar.DATE, -1);
        List<FlightSchedule> outbound3Before = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, originAirport, c.getTime(), cabinClassName);
        c.add(Calendar.DATE, 1);
        List<FlightSchedule> outbound1After = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, originAirport, c.getTime(), cabinClassName);
        c.add(Calendar.DATE, 1);
        List<FlightSchedule> outbound2After = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, originAirport, c.getTime(), cabinClassName);
        c.add(Calendar.DATE, 1);
        List<FlightSchedule> outbound3After = flightScheduleSessionBeanRemote.retrieveListOfFlightSchedule(originAirport, originAirport, c.getTime(), cabinClassName);
        
        System.out.println("************** DIRECT FLIGHTS FROM " + originAirport + " TO " + destinationAirport + " **************");
        System.out.println("************** Flights On Selected Date **************");
        printFlightSchedule(outboundActual, numPassenger, cabinClassName);
        System.out.println("\n************** Flights 1 Day Before Selected Date **************");
        printFlightSchedule(outbound1Before, numPassenger, cabinClassName);
        System.out.println("\n************** Flights 2 Days Before Selected Date **************");
        printFlightSchedule(outbound2Before, numPassenger, cabinClassName);
        System.out.println("\n************** Flights 3 Days Before Selected Date **************");
        printFlightSchedule(outbound2Before, numPassenger, cabinClassName);
        System.out.println("\n************** Flights 1 Day After Selected Date **************");
        printFlightSchedule(outbound1After, numPassenger, cabinClassName);
        System.out.println("\n************** Flights 2 Days After Selected Date **************");
        printFlightSchedule(outbound2After, numPassenger, cabinClassName);
        System.out.println("\n************** Flights 3 Days After Selected Date **************");
        printFlightSchedule(outbound3After, numPassenger, cabinClassName);
        
    }
    */
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
                
                try {
                    System.out.printf("%15s%20s%30s%30s%40s%20s%20s%20s%30s%25s%25s\n", fs.getFlightScheduleId(),
                        fs.getFlightSchedulePlan().getFlightNumber(),
                        fs.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getAirportName(),
                        fs.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getAirportName(),
                        fs.getDepartureDateTime().toString().substring(0, 19),
                        fs.getFlightDuration(),
                        arrivalDate.toString().substring(0, 19),
                        cabinClassName,
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
