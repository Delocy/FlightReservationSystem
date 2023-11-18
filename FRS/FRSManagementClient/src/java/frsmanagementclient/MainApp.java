/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import ejb.session.stateless.AircraftConfigSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CabinClassConfigSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FareSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.AircraftConfig;
import entity.AircraftType;
import entity.Airport;
import entity.CabinClassConfig;
import entity.Employee;
import entity.Fare;
import entity.Flight;
import entity.FlightReservation;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Passenger;
import entity.SeatInventory;
import static java.awt.SystemColor.info;
import java.awt.color.ColorSpace;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import util.enumeration.CabinClassNameEnum;
import util.enumeration.EmployeeAccessRightEnum;
import util.enumeration.ScheduleTypeEnum;
import util.exception.AircraftConfigNotFoundException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.CabinClassNameNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.FareExistException;
import util.exception.FareNotFoundException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightNumberExistException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.FlightSchedulePlanExistException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.MaxSeatCapacityExceededException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFareException;
import util.exception.UpdateFlightScheduleException;

/**
 *
 * @author zares
 */
public class MainApp {
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private AirportSessionBeanRemote airportSessionBeanRemote;
    private AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote;
    private CabinClassConfigSessionBeanRemote cabinClassConfigSessionBeanRemote;
    private AircraftConfigSessionBeanRemote aircraftConfigSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FareSessionBeanRemote fareSessionBeanRemote;
    private Employee currentEmployee;
    
    public MainApp() {
        
    }

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote, AirportSessionBeanRemote airportSessionBeanRemote, AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote, CabinClassConfigSessionBeanRemote cabinClassConfigSessionBeanRemote, AircraftConfigSessionBeanRemote aircraftConfigSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FareSessionBeanRemote fareSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.aircraftTypeSessionBeanRemote = aircraftTypeSessionBeanRemote;
        this.cabinClassConfigSessionBeanRemote = cabinClassConfigSessionBeanRemote;
        this.aircraftConfigSessionBeanRemote = aircraftConfigSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.fareSessionBeanRemote = fareSessionBeanRemote;
    }

   
    public void runApp() throws AirportNotFoundException, FlightRouteExistException, UnknownPersistenceException, FlightRouteNotFoundException, AircraftConfigNotFoundException, FlightExistException, FlightNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** Welcome to FRS Management Portal ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            while (response < 1 || response > 2) {
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
        
        System.out.println("*** Management Portal :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentEmployee = employeeSessionBeanRemote.employeeLogin(username, password);      
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    private void menuMain() throws AirportNotFoundException, FlightRouteExistException, UnknownPersistenceException, FlightRouteNotFoundException, AircraftConfigNotFoundException, FlightExistException, FlightNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true)
        {
            System.out.println("*** FRS Management Portal ***\n");
            System.out.println("You are login as " + currentEmployee.getFirstName() + " with " + currentEmployee.getAccessRightEnum().toString() + " rights\n");
            
            EmployeeAccessRightEnum currentAccessRightEnum = currentEmployee.getAccessRightEnum();
            if (currentAccessRightEnum == EmployeeAccessRightEnum.SYSTEMADMIN) {
                System.out.println("1: Create Employee");
                System.out.println("2: Logout\n");
                response = 0;

                while (response < 1 || response > 4) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        doCreateEmployee();
                    } else if (response == 2) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }

                if (response == 2) {
                    break;
                }
                
            } else if (currentAccessRightEnum == EmployeeAccessRightEnum.FLEETMANAGER) {
                System.out.println("1: Create Aircraft Configuration");
                System.out.println("2: View All Aircraft Configuration");
                System.out.println("3: View Aircraft Configuration Details");
                System.out.println("4: Logout\n");
                
                response = 0;

                while (response < 1 || response > 4) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        doCreateAircraftConfig();
                    } else if (response == 2) {
                        try {
                            System.out.println("*** FRS Management Portal - View All Aircraft Configurations ***\n");
                            doViewAllAircraftConfig();
                        } catch (AircraftConfigNotFoundException ex) {
                            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (response == 3) {
                        doViewAircraftConfigDetails();
                    } else if (response == 4) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }

                if (response == 4) {
                    break;
                }
                
            } else if (currentAccessRightEnum == EmployeeAccessRightEnum.ROUTEPLANNER) {
                System.out.println("1: Create Flight Route");
                System.out.println("2: View All Flight Routes");
                System.out.println("3: Delete Flight Route");
                System.out.println("4: Logout\n");
                
                response = 0;

                while (response < 1 || response > 4) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        doCreateFlightRoute();
                    } else if (response == 2) {
                        doViewAllFlightRoutes();
                    } else if (response == 3) {
                        doDeleteFlightRoute();
                    } else if (response == 4) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }

                if (response == 4) {
                    break;
                }
                
            } else if (currentAccessRightEnum == EmployeeAccessRightEnum.SCHEDULEMANAGER) {
                System.out.println("1: Create Flight");
                System.out.println("2: View All Flights");
                System.out.println("3: View Flight Details"); // this part then have options to update or delete the flight
                System.out.println("4: Create Flight Schedule Plan");
                System.out.println("5: View All Flight Schedule Plans");
                System.out.println("6: View Flight Schedule Plan Details"); // have options to update or delete the flight schedule plan
                System.out.println("7: Logout\n");
                
                response = 0;

                while (response < 1 || response > 7) {
                    System.out.print("> ");
 
                    response = scanner.nextInt();

                    if (response == 1) {
                        doCreateFlight();
                    } else if (response == 2) {
                        doViewAllFlights();
                    } else if (response == 3) {
                        doViewFlightDetails();
                    } else if (response == 4) {
                        doCreateFlightSchedulePlan();
                    } else if (response == 5) {
                        doViewAllFlightSchedulePlan(); 
                    } else if (response == 6) {
                        doViewFlightSchedulePlanDetails(); 
                    } else if (response == 7) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }

                if (response == 7) {
                    break;
                }
                
            } else if (currentAccessRightEnum == EmployeeAccessRightEnum.SALESMANAGER) {
                System.out.println("1: View Seat Inventory");
                System.out.println("2: View Flight Reservations");
                System.out.println("3: Logout\n");
                
                response = 0;

                while (response < 1 || response > 3) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        doViewSeatsInventory();
                    } else if (response == 2) {
                        doViewFlightReservations(); 
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
    }
    
    private void doCreateEmployee() {
        Scanner scanner = new Scanner(System.in);
        Employee newEmployee = new Employee();
        System.out.println("*** FRS Management Portal - Create Customer ***\n");
        System.out.print("Enter first name> ");
        newEmployee.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter last name> ");
        newEmployee.setLastName(scanner.nextLine().trim());
        System.out.println("Select employeeAccessRight \n 1: Fleet Manager \n 2: Route Planner \n 3: Schedule Manager \n 4: Sales Manager");
       
        Integer accessRightInt = scanner.nextInt();
        if (accessRightInt == 1) {
            newEmployee.setAccessRightEnum(EmployeeAccessRightEnum.FLEETMANAGER);
        } else if (accessRightInt == 2) {
            newEmployee.setAccessRightEnum(EmployeeAccessRightEnum.ROUTEPLANNER);
        } else if (accessRightInt == 3) {
            newEmployee.setAccessRightEnum(EmployeeAccessRightEnum.SCHEDULEMANAGER);
        } else if (accessRightInt == 4) {
            newEmployee.setAccessRightEnum(EmployeeAccessRightEnum.SALESMANAGER);
        } else {
            System.out.println("Invalid input , please try again!\n");
        }
        scanner.nextLine();
        System.out.print("Enter username> ");
        newEmployee.setUsername(scanner.nextLine().trim());
        System.out.print("Enter password> ");
        newEmployee.setPassword(scanner.nextLine().trim());
        try {
            Long employeeID = employeeSessionBeanRemote.createNewEmployee(newEmployee);
            System.out.println("** Employee " + newEmployee.getFirstName() + newEmployee.getLastName() + " with role : " + newEmployee.getAccessRightEnum() +" has been successfully created **");
        } catch (EmployeeUsernameExistException ex) {
            System.out.println("Error occured when creating the new customer " + ex.getMessage());
        }
    }
    
    private void doCreateFlightRoute() throws FlightRouteExistException {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("*** FRS Management Portal - Create Flight Route ***\n");
            System.out.println("Enter origin IATA airport code");
            String origin = scanner.nextLine().trim();
            Airport originAirport = airportSessionBeanRemote.retrieveAirportByAirportCode(origin);
            System.out.println("Enter destination IATA airport code");
            String destination = scanner.nextLine().trim();
            Airport destinationAirport = airportSessionBeanRemote.retrieveAirportByAirportCode(destination);

            List<FlightRoute> allFlightRoutes = flightRouteSessionBeanRemote.viewAllFlightRoutes();
            boolean inside = false;
            for (FlightRoute route : allFlightRoutes) {
                Airport o = route.getOriginAirport();
                Airport d = route.getDestinationAirport();
                if (originAirport.equals(o) && destinationAirport.equals(d)) {
                    inside = true;
                    break;
                }
            }
            if (inside == false) {
                FlightRoute newFlightRoute = flightRouteSessionBeanRemote.createFlightRoute(originAirport.getAirportId(), destinationAirport.getAirportId());
                System.out.println("** Flight Route: from Origin " + newFlightRoute.getOriginAirport().getAirportName() + " to Destination " + newFlightRoute.getDestinationAirport().getAirportName() + " has been successfully created **");
                boolean insideC = false;
                for (FlightRoute route : allFlightRoutes) {
                    Airport o = route.getOriginAirport();
                    Airport d = route.getDestinationAirport();
                    if (destinationAirport.equals(o) && originAirport.equals(d)) {
                        insideC = true;
                        break;
                    }
                }

                if (insideC == false) {
                    System.out.println("Would you like to create a complementary return route? (Y/N)");
                    if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                          FlightRoute compFlightRoute = flightRouteSessionBeanRemote.createFlightRoute(destinationAirport.getAirportId(), originAirport.getAirportId());
                          System.out.println("** Flight Route: from Origin " + compFlightRoute.getOriginAirport().getAirportName() + " to Destination " + compFlightRoute.getDestinationAirport().getAirportName() + " has been successfully created **");
                    }
                }
            } else {
                System.out.println("Flight Route exists! \n");
            }
        } catch (AirportNotFoundException | UnknownPersistenceException ex) {
            System.out.println("Error creating flight route");  
        }
        
        
    }
    
    private void doViewAllFlightRoutes() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** FRS Management Portal - View All Flight Routes ***\n");
        List<FlightRoute> allFlightRoutes = flightRouteSessionBeanRemote.viewAllFlightRoutes();
        System.out.printf("%4s%16s%24s\n", "Route ID", "Origin Airport", "Destination Airport");
        for (FlightRoute flightRoute : allFlightRoutes) {
            System.out.printf("%4s%16s%24s\n", flightRoute.getFlightRouteId(), flightRoute.getOriginAirport().getAirportName(), flightRoute.getDestinationAirport().getAirportName());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    private void doDeleteFlightRoute() throws FlightRouteNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** FRS Management Portal - Delete Flight Route ***\n");
        System.out.println("Enter ID of flight route for deletion");
        Long routeIdToDelete = scanner.nextLong();
        flightRouteSessionBeanRemote.deleteFlightRoute(routeIdToDelete);
        System.out.println("Flight route with ID: " + routeIdToDelete + " successfully deleted!");
    }
    
    private void doCreateFlight() throws AircraftConfigNotFoundException, FlightRouteNotFoundException, UnknownPersistenceException, FlightExistException, FlightNotFoundException {
        try {
            Scanner scanner = new Scanner(System.in);
            Flight newFlight = new Flight();
            System.out.println("*** FRS Management Portal - Create Flight ***\n");
            System.out.println("Enter flight number (Numbers only)> ");
            newFlight.setFlightNumber("ML" + scanner.nextLine().trim());

            //if want to set flight configurations and routes at this time:
            System.out.printf("%-30s%-40s%-25s%-20s\n", "Aircraft Configuration ID", "Name", "Number of Cabin Classes", "Aircraft Type");
            List<AircraftConfig> aircraftConfigs = aircraftConfigSessionBeanRemote.retrieveAllAircraftConfig();

            for (AircraftConfig a : aircraftConfigs) {
                System.out.printf("%-30s%-40s%-25s%-20s\n", a.getAircraftConfigId(), a.getAircraftConfigName(), a.getNumCabinClass(), a.getAircraftType().getAircraftTypeName());
            }
            System.out.print("Select Aircraft Configuration BY ID> ");
  

            AircraftConfig selectedAircraftConfig = aircraftConfigSessionBeanRemote.retrieveAircraftConfigById(scanner.nextLong());
    //        newFlight.setAircraftConfig(selectedAircraftConfig); 
            //

            System.out.println("Select flight route");
            List<FlightRoute> allFlightRoutes = flightRouteSessionBeanRemote.viewAllFlightRoutes();

            for (FlightRoute flightRoute : allFlightRoutes) {
                System.out.printf("%-8s%-16s%-24s\n",
                        flightRoute.getFlightRouteId(),
                        flightRoute.getOriginAirport().getAirportCode(),
                        flightRoute.getDestinationAirport().getAirportCode());
            }   
            
            System.out.print("Select flight route BY ID> ");
            FlightRoute selectedFlightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByRouteID(scanner.nextLong());
            scanner.nextLine();

            Airport origin = selectedFlightRoute.getOriginAirport();
            Airport destination = selectedFlightRoute.getDestinationAirport();
    //        newFlight.setFlightRoute(selectedFlightRoute);

            Long flightID = (long) 0;
    //         try {
            flightID = flightSessionBeanRemote.createNewFlight(newFlight, selectedAircraftConfig.getAircraftConfigId(),selectedFlightRoute.getFlightRouteId());

            System.out.println("** Flight: " + newFlight.getFlightNumber() + " has been successfully created **");
    //        } catch (FlightExistException ex) {
    //            System.out.println("Error occured when creating the new flight " + ex.getMessage());
    //        }


            for (FlightRoute flightRoute : allFlightRoutes) {
                Airport refOrigin = flightRoute.getOriginAirport();
                Airport refDestination = flightRoute.getDestinationAirport();
                if (refOrigin.equals(destination) && refDestination.equals(origin)) {
                    System.out.println("Do you want to create a complementary return flight based on the same aircraft configuration but with a different flight number? Press (Y) for yes | Press (N) for no ");
                    Flight returnFlight = new Flight();
                    String input = scanner.nextLine().trim();
                    if (input.equals("Y")) {
                        System.out.println("Enter new flight number for return flight");
                        returnFlight.setFlightNumber("ML"+ scanner.nextLine().trim());

    //                    try {
                        Long returnFlightID = flightSessionBeanRemote.createNewFlight(returnFlight, selectedAircraftConfig.getAircraftConfigId(), flightRoute.getFlightRouteId());
                        Flight newRFlight = flightSessionBeanRemote.retrieveFlightByFlightID(returnFlightID);
//                        
                        flightSessionBeanRemote.associateOriginalFlightWithReturnFlight(flightID, returnFlightID);
                        System.out.println("** Return Flight: " + newRFlight.getFlightNumber() + " has been successfully created **");
                        return;
    //                    } catch (FlightExistException ex) {
    //                        System.out.println("Error occured when creating the new flight " + ex.getMessage());
    //                    }
                    } else if (input.equals("N")) {
                        System.out.println("Unable to create a complementary return flight with a different aircraft configuration or same flight number!");
                        return;
                    }
                }
            }
        }catch (FlightExistException ex ) {
            System.out.println("Error occured when creating the new flight " + ex.getMessage());
        }
        
    }

    
    private void doViewAllFlights() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** FRS Management Portal - View All Flight ***\n");
        List<Flight> allFlights = flightSessionBeanRemote.viewAllFlights();
//        System.out.printf("%10s%15s%20s%20s\n", "FlightID", "Flight Number" ,"Origin Airport", "Destination Airport");
//        for (Flight flight : allFlights) {
//            System.out.printf("%10s%15s%20s%20s\n", flight.getFlightId(), flight.getFlightNumber(), flight.getFlightRoute().getOriginAirport().getAirportCode(), flight.getFlightRoute().getDestinationAirport().getAirportCode());
//        }    
        
        System.out.printf("%-10s %-15s %-20s %-20s\n", "FlightID", "Flight Number", "Origin Airport", "Destination Airport");
        for (Flight flight : allFlights) {
            System.out.printf("%-10s %-15s %-20s %-20s\n", 
                flight.getFlightId(), 
                flight.getFlightNumber(), 
                flight.getFlightRoute().getOriginAirport().getAirportCode(), 
                flight.getFlightRoute().getDestinationAirport().getAirportCode());
        }
        
        System.out.print("Press any key to continue> ");
        scanner.nextLine();
    }
    
    private void doViewFlightDetails() {
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** FRS Management Portal - View Flight Details ***\n");
        System.out.println("Enter FlightNumber (Numbers Only): ");
        String flightNumber = scanner.nextLine().trim();
        
        try {
            Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
            FlightRoute route = flight.getFlightRoute();
            Airport origin = route.getOriginAirport();
            Airport destination = route.getDestinationAirport();
            AircraftConfig config = flight.getAircraftConfig();
            List<CabinClassConfig> cabinClassConfig = config.getCabinClassConfig();

            System.out.printf("%-10s%-20s%-20s%-20s%-35s%-20s%-35s%-25s%-30s%-40s%-25s%-20s%-20s%-30s\n", "Flight ID", "Flight Number", "Cabin Class", "Cabin Class ID", "Flight Route ID", "Origin Airport Name", "Origin Airport IATA", "Destination Airport Name", "Destination Airport IATA", "Aircraft Configuration ID", "Name", "Max Seats Capacity", "Aircraft Type", "Returning Flight Number");
            for(int i = 0; i< cabinClassConfig.size(); i++) {
            System.out.printf("%-10s%-20s%-20s%-20s%-35s%-20s%-35s%-25s%-30s%-40s%-25s%-20s%-20s%-30s\n", flight.getFlightId(), flight.getFlightNumber(), cabinClassConfig.get(i).getCabinClassName().toString(),cabinClassConfig.get(i).getCabinClassConfigId(), route.getFlightRouteId().toString(), route.getOriginAirport().getAirportName() ,route.getOriginAirport().getAirportCode(), route.getDestinationAirport().getAirportName() ,route.getDestinationAirport().getAirportCode(), config.getAircraftConfigId().toString(), config.getAircraftConfigName(), cabinClassConfig.get(i).getMaxSeatCapacity(), config.getAircraftType().getAircraftTypeName(), flight.getReturningFlight() != null ? flight.getReturningFlight().getFlightNumber(): "None");
            }
            
            System.out.println("--------------------------");
            System.out.println("1: Update Flight");
            System.out.println("2: Delete Flight");
            System.out.println("3: Back\n");
            
            System.out.print("> ");
            int response = scanner.nextInt();
            
            if(response == 1) {
                doUpdateFlight(flight.getFlightId());
            }
            else if(response == 2) {
                doDeleteFlight(flight.getFlightId());
            }
            
        } catch (FlightNotFoundException ex) {
            System.out.println("Flight with number: " + flightNumber + "does not exist" );
        }
    }
    
    private void doUpdateFlight(Long flightId) throws FlightNotFoundException {
        Scanner sc = new Scanner(System.in);
        Flight flight = flightSessionBeanRemote.retrieveFlightByFlightID(flightId);
        
        System.out.println("*** Update Flight " + flight.getFlightNumber() + "***");
        
        Flight newFlight = new Flight(); //dummy
        boolean updated = false;
        
        List<FlightRoute> routes;
        routes = flightRouteSessionBeanRemote.viewAllFlightRoutes();
        
        System.out.printf("%20s%40s%20s%40s%25s\n", "Flight Route ID", "Origin Airport Name", "Origin Airport IATA", "Destination Airport Name", "Destination Airport IATA");
        for (FlightRoute route : routes) {
            System.out.printf("%20s%40s%20s%40s%25s\n",
                    route.getFlightRouteId(),
                    route.getOriginAirport().getAirportName(),
                    route.getOriginAirport().getAirportCode(),
                    route.getDestinationAirport().getAirportName(),
                    route.getDestinationAirport().getAirportCode());
        }
        System.out.print("Enter ID of the flight route you want this flight to change to) (Press 0 if no change)>  ");
        Long chosenRouteId = sc.nextLong();
        sc.nextLine();
        
        if(chosenRouteId > 0) {
            try {
                FlightRoute existingFlightRoute = flight.getFlightRoute();
                FlightRoute flightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByRouteID(chosenRouteId);
                if (flightRoute.getOriginAirport() != flight.getFlightRoute().getOriginAirport() && flightRoute.getDestinationAirport() != flight.getFlightRoute().getDestinationAirport()) {
//                    List<Flight> flightsInExistingFlightRoute = existingFlightRoute.getFlights();
//                    flightsInExistingFlightRoute.remove(flight);
//                    existingFlightRoute.setFlights(flightsInExistingFlightRoute);
                    
                    newFlight.setFlightRoute(flightRoute);
//                    if (flight.getReturningFlight() != null) {
//                        
//                    }
                    updated = true;
//                    flightSessionBeanRemote.deleteFlight(flightId);
                }
            } catch (FlightRouteNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
                return;
            }
        } else if (chosenRouteId == 0) {
            newFlight.setFlightRoute(flight.getFlightRoute());
        }
        
        List<AircraftConfig> aircraftConfig;
        try {
            aircraftConfig = aircraftConfigSessionBeanRemote.retrieveAllAircraftConfig();
        } catch (AircraftConfigNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
            return;
        }
        
        System.out.printf("%30s%40s%25s%30s\n", "Aircraft Configuration ID", "Name", "Number of Cabin Class", "Aircraft Type");
        for (AircraftConfig config : aircraftConfig) {
            System.out.printf("%30s%40s%25s%30s\n", config.getAircraftConfigId().toString(), config.getAircraftConfigName(), config.getNumCabinClass(), config.getAircraftType().getAircraftTypeName());
        }
        System.out.print("Enter Aircraft Configuration ID (Press 0 if no change)>  ");
        Long chosenConfig = sc.nextLong();
        sc.nextLine();
        if(chosenConfig > 0) {
            try {
                AircraftConfig config = aircraftConfigSessionBeanRemote.retrieveAircraftConfigById(chosenConfig);
                newFlight.setAircraftConfig(config);
                updated = true;
            } catch (AircraftConfigNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
                return;
            }
        } else if (chosenConfig == 0) {
            newFlight.setAircraftConfig(flight.getAircraftConfig());
        }
        
        if (updated) {
            flightSessionBeanRemote.updateFlight(flight.getFlightId(), newFlight);
        } 
        
    }
    
    private void doDeleteFlight(Long flightId) throws FlightNotFoundException {
        Flight flight = flightSessionBeanRemote.retrieveFlightByFlightID(flightId);
        flightSessionBeanRemote.deleteFlight(flightId);
        System.out.println("Successfully deleted flight " + flight.getFlightNumber());
    }
    
    private void doCreateAircraftConfig() {
        try {
            Scanner sc = new Scanner(System.in);
            AircraftConfig newAircraftConfig = new AircraftConfig();
            
            System.out.println("*** FRS Management Portal - Create New Aircraft Configuration ***\n");
            System.out.printf("%20s%25s%20s\n", "Aircraft Type ID", "Name", "Max Seat Capacity");
            List<AircraftType> aircraftTypes = aircraftTypeSessionBeanRemote.retrieveAllAircraftType();
            for (AircraftType a : aircraftTypes) {
                System.out.printf("%20s%25s%20s\n", a.getAircraftTypeId().toString(), a.getAircraftTypeName(), a.getMaxCapacity());
            }
            
            System.out.print("Enter Aircraft Type ID> ");
            Long aircraftTypeId = sc.nextLong();
            sc.nextLine();
            
            //AircraftType aircraftType = aircraftTypeSessionBeanRemote.retrieveAircraftTypeByAircraftTypeId(aircraftTypeId);
            //newAircraftConfig.setAircraftType(aircraftType);
            //do i need to set aircraftconfig on aircraft type..?
            System.out.print("Enter Aircraft Configuration Name> ");
            String name = sc.nextLine().trim();
            newAircraftConfig.setAircraftConfigName(name);
            System.out.print("Enter number of cabin classes> ");
            int numCabinClass = sc.nextInt();
            newAircraftConfig.setNumCabinClass(numCabinClass);
            
            int seatCapacity = 0;
            
            System.out.println("*** FRS Management Portal - Create New Aircraft Configuration :: Cabin Configuration ***\n");
            List<CabinClassConfig> cabins = new ArrayList<>();
            for (int i = 0; i < numCabinClass; i++) {
                CabinClassConfig cabin = doCreateCabinClass();
                seatCapacity += cabin.getMaxSeatCapacity();
                cabins.add(cabin);
            }
            
            Long aircraftConfigId = aircraftConfigSessionBeanRemote.createAircraftConfig(newAircraftConfig, cabins, aircraftTypeId);
            
            /*
            if (seatCapacity <= aircraftType.getMaxCapacity()) {
                aircraftType.getAircraftConfig().add(newAircraftConfig);
                for (CabinClassConfig c : cabins) {
                    Long id = cabinClassConfigSessionBeanRemote.createCabinClass(c, newAircraftConfig);
                    //c.setAircraftConfig(newAircraftConfig);
                }
                newAircraftConfig.setCabinClassConfig(cabins);
            } else {
                throw new MaxSeatCapacityExceededException("Max seat capacity of cabin class exceeds the aircraft type!");
            }
            
            Long aircraftConfigId = aircraftConfigSessionBeanRemote.createAircraftConfig(newAircraftConfig);
            */
            System.out.println("Aircraft Configuration " + name + " created successfully!");
        } catch (CabinClassNameNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownPersistenceException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MaxSeatCapacityExceededException ex) {
            System.out.println("Max Seat Capacity Exceeded! \n");
        } catch (AircraftTypeNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public CabinClassConfig doCreateCabinClass() throws CabinClassNameNotFoundException, MaxSeatCapacityExceededException {
        Scanner sc = new Scanner(System.in);
        CabinClassConfig newCabin = new CabinClassConfig();
        CabinClassNameEnum cabinClassName = CabinClassNameEnum.FIRST;

       System.out.print("Select cabin class (First (F), Business (J), Premium Economy (W), Economy (Y)> ");
       String response = sc.nextLine().trim();
       cabinClassName = cabinClassConfigSessionBeanRemote.fetchCabinClassNameEnum(response);
       newCabin.setCabinClassName(cabinClassName);

       System.out.print("Enter number of aisles> ");
       int numAisles = sc.nextInt();
       newCabin.setNumAisles(numAisles);

       System.out.print("Enter number of rows> ");
       int numRows = sc.nextInt();
       newCabin.setNumRows(numRows);

       System.out.print("Enter number of seats abreast> ");
       int seatAbreast = sc.nextInt();
       sc.nextLine();
       newCabin.setNumSeatsAbreast(seatAbreast);

       System.out.print("Enter seat configuration (eg. 3-4-3 or 2-2)> ");
       String seatConfig = sc.nextLine().trim();
       int seats = 0;
       for (char c : seatConfig.toCharArray()) {
           if (Character.isDigit(c)) {
               seats += c - '0';
           }
       }
       if (seats != seatAbreast) {
           throw new MaxSeatCapacityExceededException("Seat capacity does not correspond to number of seats abreast!");
       }
       newCabin.setSeatConfiguration(seatConfig);

       int cabinSeatCapacity = seatAbreast * numRows;
       newCabin.setMaxSeatCapacity(cabinSeatCapacity);
       
       return newCabin;
    }
    
    public void doViewAllAircraftConfig() throws AircraftConfigNotFoundException {
        
        System.out.printf("%30s%40s%25s%20s\n", "Aircraft Configuration ID", "Name", "Number of Cabin Classes", "Aircraft Type");
        List<AircraftConfig> aircraftConfigs = aircraftConfigSessionBeanRemote.retrieveAllAircraftConfig();
        
        for (AircraftConfig a : aircraftConfigs) {
            System.out.printf("%30s%40s%25s%20s\n", a.getAircraftConfigId(), a.getAircraftConfigName(), a.getNumCabinClass(), a.getAircraftType().getAircraftTypeName());
        }

        System.out.println("");
        
    }
    
    public void doViewAircraftConfigDetails() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("*** FRS Management Portal - View Aircraft Configuration Details ***\n");
            doViewAllAircraftConfig();
            
            System.out.print("Select Aircraft Configuration ID to view details> ");
            Long id = sc.nextLong();
            
            AircraftConfig aircraftConfig = aircraftConfigSessionBeanRemote.retrieveAircraftConfigById(id);
            System.out.println("");
            System.out.println("Aircraft Configuration ID: " + id);
            System.out.println("Aircraft Configuration Name: " + aircraftConfig.getAircraftConfigName());
            System.out.println("Number of Cabin Classes: " + aircraftConfig.getNumCabinClass());
            System.out.println("Aircraft Type: " + aircraftConfig.getAircraftType().getAircraftTypeName());
            
            int maxCapacity = 0;
            for (CabinClassConfig c : aircraftConfig.getCabinClassConfig()) {
                System.out.println("\t" + c.getCabinClassName() + ", " + c.getNumAisles()+ " aisle(s), " + c.getNumRows()+ " row(s), " + c.getNumSeatsAbreast()+ " seats abreast, " + c.getSeatConfiguration()+ " seating configuration, " + c.getMaxSeatCapacity()+ " total seats");
                maxCapacity += c.getMaxSeatCapacity();
            }
            
            System.out.println("\nCabin Overview: " + aircraftConfig.getCabinClassConfig().size() + " cabin class(es) / " + maxCapacity + " total seats \n");
//            System.out.print("Press any key to continue...> ");
         
        } catch (AircraftConfigNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    private Pair<Date, Double> getFlightScheduleInfo() throws ParseException {
        Date departure;
        double duration;
        
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy hh:mm:ss a");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("dd/M/yyyy hh:mm:ss a");
        
        System.out.print("Enter departure Date and Time (dd/mm/yyyy hh:mm:ss AM/PM)> ");
        String input = sc.nextLine().trim();
        departure = formatter.parse(input);
        System.out.print("Enter estimated flight duration (HRS)> ");
        duration = sc.nextDouble();
        return new Pair<>(departure, duration);
    }
    
    private Fare createFare(CabinClassConfig cabinclass) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter fare basis code (2 to 6 numbers)> ");
        String code = sc.next().trim();
        System.out.print("Enter fare amount> ");
        BigDecimal cost = sc.nextBigDecimal();
        Fare fare = new Fare(code, cost, cabinclass.getCabinClassName());
        return fare;
    }
    
    public void doCreateFlightSchedulePlan() {
        try {
            Scanner scanner = new Scanner(System.in);
            FlightSchedulePlan plan = new FlightSchedulePlan();
            SimpleDateFormat recurrentFormat = new SimpleDateFormat("dd/M/yyyy");
            List<Pair<Date, Double>> listOfScheduleInfo = new ArrayList<>();
            Pair<Date, Double> info = null;
            int recurrentDay = 0;
            
            
            System.out.println("*** FRS Management Portal - Create Flight Schedule Plan ***\n");
            
            List<Flight> flights = flightSessionBeanRemote.viewAllFlights();
            System.out.printf("%10s%20s%20s%25s\n", "Flight ID", "Flight Number", "Flight Route", "Aircraft Configuration");
            for (Flight flight : flights) {
                System.out.printf("%10s%20s%20s%25s\n", flight.getFlightId() ,flight.getFlightNumber(), flight.getFlightRoute().getOriginAirport().getAirportCode() + " -> " + flight.getFlightRoute().getDestinationAirport().getAirportCode() , flight.getAircraftConfig().getAircraftConfigName());
            }
            System.out.println("\n Press (1) to enter Flight ID, Press (2) to enter Flight Number to select flight");
            int select = scanner.nextInt();
            Long idSelected = null;
            String flightNoSelected = null;
            if (select == 1) {
                System.out.print("Choose a flight to create a schedule plan for (INPUT ID)> ");
                idSelected = scanner.nextLong();
            } else {
                System.out.print("Choose a flight to create a schedule plan for (INPUT Flight Number - numbers only)> ");
                scanner.nextLine();
                flightNoSelected = scanner.nextLine().trim();
            }
            Flight selectedF = null;
            if (idSelected != null) {
                selectedF = flightSessionBeanRemote.retrieveFlightByFlightID(scanner.nextLong());
            } else if (flightNoSelected != null) {
                selectedF = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNoSelected);
            }
            
            plan.setFlightNumber(selectedF.getFlightNumber());
            
            System.out.print("Enter Schedule Type \n 1: Single \n 2: Multiple \n 3: Recurrent n day \n 4: Recurrent weekly \n ");
            int typeSelected = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Create new flight schedule for flight " + selectedF.getFlightNumber());
            switch (typeSelected) {
                case 1:
                    plan.setScheduleType(ScheduleTypeEnum.SINGLE);
                    info = getFlightScheduleInfo();
                    break;
                case 2:
                    plan.setScheduleType(ScheduleTypeEnum.MULTIPLE);
                    System.out.print("Enter number of schedule to be created> ");
                    int num = scanner.nextInt();
                    scanner.nextLine();
                    for(int i = 0; i < num; i++) {
                        Pair pair1 = getFlightScheduleInfo();
                        listOfScheduleInfo.add(pair1);
                    }
                    break;
                case 3:
                    plan.setScheduleType(ScheduleTypeEnum.RECURRENTDAY);
                    info = getFlightScheduleInfo();
                    System.out.print("Enter recurrent end date (dd/mm/yyyy)> ");
                    String date = scanner.nextLine().trim();
                    Date dailyEnd = recurrentFormat.parse(date);
                    plan.setRecurrentEndDate(dailyEnd);
                    break;
                case 4:
                    plan.setScheduleType(ScheduleTypeEnum.RECURRENTWEEK);
                    info = getFlightScheduleInfo();
                    System.out.print("Enter recurrent day (1 -> Sunday , 7 -> Saturday)> ");
                    recurrentDay = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter recurrent end date (dd/mm/yyyy)> ");
                    String date1 = scanner.nextLine().trim();
                    Date weekEnd = recurrentFormat.parse(date1);
                    plan.setRecurrentEndDate(weekEnd);
                    break;
            }
            
            List<CabinClassConfig> cabinClass = selectedF.getAircraftConfig().getCabinClassConfig();
            System.out.println("Aircraft Configuration for flight " + selectedF.getFlightNumber() + " contains " + cabinClass.size() + " cabins");
            System.out.println("Please enter fares for each cabin class!\n");

            List<Fare> fares = new ArrayList<>();
            for (CabinClassConfig cc: cabinClass) {
                String type = "";
                if (cc.getCabinClassName() != null) {
                    switch (cc.getCabinClassName()) {
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
                
                
                    System.out.println("** Creating fare for " + type + " **");
                    while (true) {
                        fares.add(createFare(cc));
                        System.out.print("Would you like to add more fares to this cabin class? (Y/N)> ");
                        String reply = scanner.nextLine().trim();
                        if (!reply.equalsIgnoreCase("Y")) {
                            break;
                        }               
                    }
                }
            }
            System.out.println("Fares for the cabin classes successfully created!\n");
            
            if (plan.getScheduleType().equals(ScheduleTypeEnum.MULTIPLE)) {
                    plan = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlanMultiple(plan, fares, selectedF.getFlightId(), listOfScheduleInfo);
                } else if (plan.getScheduleType().equals(ScheduleTypeEnum.RECURRENTDAY)) {
                    System.out.print("Enter interval of recurrence (1-6)> ");
                    int days = scanner.nextInt();
                    scanner.nextLine();
                    plan = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(plan, fares, selectedF.getFlightId(), info, days);
                } else if (plan.getScheduleType().equals(ScheduleTypeEnum.RECURRENTWEEK)) {
                    plan = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlanWeekly(plan, fares, selectedF.getFlightId(), info, recurrentDay);
                } else {
                    plan = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(plan, fares, selectedF.getFlightId(), info, 0);
                }
                System.out.println("New Flight Schedule Plan for Flight " + plan.getFlightNumber() + " created successfully!\n");

            if (selectedF.getReturningFlight() != null) {
                Flight returnFlight = selectedF.getReturningFlight();
                System.out.println("Complementary return flight has been found for flight " + selectedF.getFlightNumber());
                System.out.print("Would you like to create a complementary return flight schedule plan? (Y/N)> ");
                String reply = scanner.nextLine().trim();
                if (reply.equalsIgnoreCase("Y")) {
                    System.out.print("Enter layover duration in Hrs> ");
                    int layoverHrs;
                    while (true) {
                        try {
                            layoverHrs = Integer.parseInt(scanner.nextLine().trim());
                            break;
                        } catch (NumberFormatException ex) {
                            System.out.print("Please enter a valid value!\n> ");
                        }
                    }
                    FlightSchedulePlan returnPlan = new FlightSchedulePlan();
                    returnPlan.setFlightNumber(returnFlight.getFlightNumber());
                    returnPlan.setScheduleType(plan.getScheduleType());
                    if (plan.getRecurrentEndDate() != null) {                 
                        returnPlan.setRecurrentEndDate(plan.getRecurrentEndDate());
                    }

                    int diff = selectedF.getFlightRoute().getDestinationAirport().getGmt() - selectedF.getFlightRoute().getOriginAirport().getGmt();
                    List<Pair<Date,Double>> listOfFlightScheduleInfoReturn = new ArrayList<>();
                    for (FlightSchedule fs: plan.getFlightSchedule()) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(fs.getDepartureDateTime());
                        double duration = fs.getFlightDuration();
                        int hour = (int) duration;
                        int min = (int) (duration % 1 * 60);
                        c.add(Calendar.HOUR_OF_DAY, hour + layoverHrs + diff);
                        c.add(Calendar.MINUTE, min);

                        Date newDeparture = c.getTime();
                        listOfFlightScheduleInfoReturn.add(new Pair<>(newDeparture, fs.getFlightDuration()));
                    }
                    
//                    System.out.println("size of now: " + plan.getFlightSchedule().size());
//                    System.out.println("size of return: " + listOfFlightScheduleInfoReturn.size());
                    
                    List<CabinClassConfig> returnCabinClass = returnFlight.getAircraftConfig().getCabinClassConfig();
//                    System.out.println("Aircraft Configuration for flight " + returnFlight.getFlightNumber() + " contains " + returnCabinClass.size() + " cabins");
//                    System.out.println("Please enter fares for each cabin class!\n");

                    List<Fare> faresReturn = fares;
//                    for (CabinClassConfig cc: returnCabinClass) {
//                        String type = "";
//                        if (cc.getCabinClassName() != null) switch (cc.getCabinClassName()) {
//                            case FIRST:
//                                type = "First Class";
//                                break;
//                            case BUSINESS:
//                                type = "Business Class";
//                                break;
//                            case PREMIUM_ECONOMY:
//                                type = "Premium Economy Class";
//                                break;
//                            case ECONOMY:
//                                type = "Economy Class";
//                                break;
//                            default:
//                                break;
//                        }
//                        System.out.println("** Creating fare for " + type + " **");
//                        while (true) {
//                            faresReturn.add(createFare(cc));
//                            System.out.print("Would you like to add more fares to this cabin class? (Y/N)> ");
//                            String reply2 = scanner.nextLine().trim();
//                            if(!reply.equalsIgnoreCase("N")) {
//                                break;
//                            }                         
//                        }
//                    }
//                    System.out.println("Fares for the cabin classes successfully created!\n");
                    
                    returnPlan = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlanMultiple(returnPlan, faresReturn, returnFlight.getFlightId(), listOfFlightScheduleInfoReturn);
                    System.out.println("New Flight Schedule Plan for Return Flight " + returnPlan.getFlightNumber() + " created successfully!\n");

                    flightSchedulePlanSessionBeanRemote.associateExistingPlanToComplementaryPlan(plan.getFlightSchedulePlanId(), returnPlan.getFlightSchedulePlanId()); 
                 
                } else {
                    System.out.println();
                }
            }           
        } catch (FlightNotFoundException ex) {
            System.out.println("Flight does not exist! \n");
        } catch (ParseException | FareExistException | UnknownPersistenceException | FlightSchedulePlanExistException | FlightSchedulePlanNotFoundException  ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    private void doViewAllFlightSchedulePlan() {
        System.out.println("*** FRS Management Portal - View All Flight Schedule Plans***\n");
        Scanner scanner = new Scanner(System.in);
        try {
            List<FlightSchedulePlan> allPlans = flightSchedulePlanSessionBeanRemote.retrieveAllFlightSchedulePlan();
            System.out.printf("%-10s %-15s %-20s %-40s %-30s\n", "Plan ID", "Flight Number", "Type Plan", "Recurrent End Date", "Number of Flight Schedule");
            for (FlightSchedulePlan plan : allPlans) {
                System.out.printf("%-10s %-15s %-20s %-40s %-30s\n",
                        plan.getFlightSchedulePlanId() ,
                        plan.getFlightNumber(),
                        plan.getScheduleType(),
                        plan.getRecurrentEndDate(),
                        plan.getFlightSchedule().size());
            }
        } catch (FlightSchedulePlanNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
            
        System.out.print("Press any key to continue> ");
        scanner.nextLine();     
    }
    
    private void doViewFlightSchedulePlanDetails() {
        try {
            Scanner scanner = new Scanner(System.in);
        
            System.out.println("*** FRS Management Portal - View Flight Schedule Plan Details ***\n");
            
            System.out.print("Enter Flight Schedule Plan ID> ");
            Long id = scanner.nextLong();
            
            FlightSchedulePlan plan = flightSchedulePlanSessionBeanRemote.retrieveFlightSchedulePlanById(id);
            Flight flight = plan.getFlight();
            FlightRoute route = flight.getFlightRoute();
            List<FlightSchedule> schedule = plan.getFlightSchedule();
            List<Fare> fare = plan.getFares();
            
            System.out.printf("%-10s %-15s %-20s %-25s %-30s %-25s %-40s %-40s %-20s %-30s\n", "Plan ID", "Flight Number", "Type Plan", "Flight Schedule ID", "Departure Date", "Duration", "Origin", "Destination", "Cabin Class Type", "Fare");
            
            for (FlightSchedule list : schedule) {
                for (Fare fares : fare) {
                    System.out.printf("%-10s %-15s %-20s %-25s %-30s %-25s %-40s %-40s %-20s %-30s\n", 
                            plan.getFlightSchedulePlanId(),
                            plan.getFlightNumber(),
                            plan.getScheduleType(),
                            list.getFlightScheduleId(),
                            list.getDepartureDateTime().toString().substring(0, 19),
                            list.getFlightDuration(),
                            route.getOriginAirport().getAirportName(),
                            route.getDestinationAirport().getAirportName(),
                            fares.getCabinClassName(),
                            fares.getFare());
                }
            }
            System.out.println("--------------------------");
            System.out.println("1: Update Flight Schedule Plan");
            System.out.println("2: Delete Flight Schedule Plan");
            System.out.println("3: Back\n");
            
            System.out.print("> ");
            int response = scanner.nextInt();
            
            if(response == 1) {
                doUpdateFlightSchedulePlan(plan);
            }
            else if(response == 2) {
                doDeleteFlightSchedulePlan(plan);
            }
        } catch (FlightSchedulePlanNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    private void doUpdateFlightSchedulePlan(FlightSchedulePlan plan) {
       Scanner sc = new Scanner(System.in);
       System.out.println("*** Update Flight Schedule Plan " + plan.getFlightSchedulePlanId() + " ***");

       System.out.println("1: Update Fares");
       System.out.println("2: Update Flight Schedules");
       System.out.println("3: Back\n");

       System.out.print("> ");
       int response = sc.nextInt();

       if(response == 1) {
           updateFares(plan);
       }
       else if(response == 2) {
           try {
               updateFlightSchedule(plan);
               System.out.println("Plan successfully updated!");
           } catch (FlightScheduleNotFoundException | UpdateFlightScheduleException ex) {
               System.out.println("Error: " + ex.getMessage() + "\n");
           }
       } 
    }
    
    private void updateFares(FlightSchedulePlan plan) {
        try {
            Scanner sc =  new Scanner(System.in);
            int i = 1;
            System.out.println(" ** All Fares **");
            for (Fare fare: plan.getFares()) {
                System.out.println(i+ ". " + fare.getFareBasisCode() + ", $" + fare.getFare());
                i++;
            }
            System.out.print("Enter fare you would like to update > ");
            int choice = sc.nextInt();
            sc.nextLine();
            if (choice < 1 || choice > plan.getFares().size()) {
                System.out.println("Error: Invalid option\nPlease try again!\n");
                return;
            }
            Fare fare = plan.getFares().get(choice - 1);
            System.out.print("Enter new fare amount> ");
            BigDecimal newAmt = sc.nextBigDecimal();
            fareSessionBeanRemote.updateFare(fare.getFareId(), newAmt);
            System.out.println("Fare updated successfully!\n");
        } catch (FareNotFoundException | UpdateFareException ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again\n");
        }
    }
        
    private void updateFlightSchedule(FlightSchedulePlan plan) throws FlightScheduleNotFoundException, UpdateFlightScheduleException {
        Scanner sc =  new Scanner(System.in);
        System.out.printf("%-30s %-30s %-20s\n", "Flight Schedule ID", "Departure Date Time", "Duration");
        for (FlightSchedule flightSchedule: plan.getFlightSchedule()) {
            System.out.printf("%-30s %-30s %-20s\n",
                    flightSchedule.getFlightScheduleId(),
                    flightSchedule.getDepartureDateTime().toString().substring(0, 19),
                    flightSchedule.getFlightDuration()
            );
        }
        System.out.print("Enter ID of flight schedule to update > ");
        int flightScheduleId = sc.nextInt();
        sc.nextLine();
        System.out.println();
        System.out.println("1: Update information");
        System.out.println("2: Delete flight schedule");
        System.out.println("3: Cancel\n");

        System.out.print("> ");
        int response = sc.nextInt();

        if(response == 1) {
            try {
                Date departure;
                double duration;
                
                SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy hh:mm:ss a");
                
                try {
                    System.out.print("Enter new departure Date and Time (dd/mm/yyyy hh:mm:ss AM/PM)> ");
                    String input = sc.nextLine().trim();
                    departure = formatter.parse(input);
                } catch (ParseException ex) {
                    System.out.println("Error: Invalid date and time\nPlease try again\n");
                    return;
                }
                System.out.print("Enter new estimated flight duration (HRS)> ");
                duration = sc.nextDouble();
                flightScheduleSessionBeanRemote.updateFlightSchedule(flightScheduleId, departure, duration);
                System.out.println("Flight Schedule " + flightScheduleId + " successfully updated!\n");
            } catch (FlightScheduleNotFoundException | UpdateFlightScheduleException ex) {
                System.out.println("Error: " + ex.getMessage() +"\nPlease try again!\n");
            }
        }
        else if(response == 2) {
            try {
                flightScheduleSessionBeanRemote.deleteFlightSchedule(flightScheduleId);
                System.out.println("Flight Schedule " + flightScheduleId + " successfully removed!\n");
            } catch (FlightScheduleNotFoundException | UpdateFlightScheduleException ex) {
                System.out.println("Error: " + ex.getMessage() +"\n");
            }
        } 
    }
    
    private void doDeleteFlightSchedulePlan(FlightSchedulePlan plan) throws FlightSchedulePlanNotFoundException {
        System.out.println("** Delete Flight Schedule Plan **");

        try {
            flightSchedulePlanSessionBeanRemote.deleteFlightSchedulePlan(plan.getFlightSchedulePlanId());
            System.out.println("Deletion successful!");
        } catch (FlightSchedulePlanNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\n");
        }
        
    }
    
    private void doViewSeatsInventory() {
       try {
            Scanner sc = new Scanner(System.in);
            System.out.println("*** View Seats Inventory ***");
            System.out.print("Enter Flight Number (Numbers Only)> ");
            String flightNumber = sc.nextLine().trim();
            Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
            List<FlightSchedulePlan> plans = flight.getFlightSchedulePlan();
            if (plans.isEmpty()) {
                System.out.println("The selected flight has no flight schedule plans associated with it\n");
                return;
            }
            System.out.println("All flight schedules for Flight " + flightNumber + ": " + flight.getFlightRoute().getOriginAirport().getAirportCode() + " -> " + flight.getFlightRoute().getDestinationAirport().getAirportCode());
            System.out.printf("%-25s%-30s%-20s\n", "Flight Schedule ID", "Departure Date Time", "Duration (HRS)");
            for (FlightSchedulePlan fsp: plans) {
                for (FlightSchedule fs: fsp.getFlightSchedule()) {
                    System.out.printf("%-25s%-30s%-20s\n",
                            fs.getFlightScheduleId().toString(),
                            fs.getDepartureDateTime().toString().substring(0, 19),
                            String.valueOf(fs.getFlightDuration()));
                }
            }
            System.out.print("Enter ID of flight schedule (Enter (0) to exit)>  ");
            Long id = sc.nextLong();
            if (id == 0) {
                return;
            }
            Long chosenFlightScheduleId = sc.nextLong();
            sc.nextLine();
            
//            FlightSchedule flightSchedule = null;
//            for (FlightSchedulePlan fsp: plans) {
//                for (FlightSchedule fs: fsp.getFlightSchedule()) {
//                    if (Objects.equals(fs.getFlightScheduleId(), chosenFlightScheduleId)) {
//                        flightSchedule = fs;
//                    }
//                }
//            }

            FlightSchedule flightSchedule = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(chosenFlightScheduleId);
           
            int totalAvailSeats = 0;
            int totalReservedSeats = 0;
            int totalBalanceSeats = 0;
            for (SeatInventory seatInventory: flightSchedule.getSeatInventory()) {
                totalAvailSeats += seatInventory.getAvailableSeats();
                totalReservedSeats += seatInventory.getReserveSeats();
                totalBalanceSeats += seatInventory.getBalanceSeats();
                
                char[][] seats = seatInventory.getSeats();
                String cabinClassConfig = seatInventory.getCabinClass().getSeatConfiguration();
                
                String type = "";
                if (seatInventory.getCabinClass().getCabinClassName() != null)
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
                
                System.out.println(" -- " + type + " -- ");
                System.out.print("Row  ");
                int count = 0;
                int no = 0;
                for (int i = 0; i < cabinClassConfig.length(); i++) {
                    if (Character.isDigit(cabinClassConfig.charAt(i))) {
                        no += Integer.parseInt(String.valueOf(cabinClassConfig.charAt(i)));
                        while (count < no) {
                            System.out.print((char)('A' + count) + "  ");
                            count++;
                        }
                    } else {
                        System.out.print("   ");
                    }
                }
                System.out.println();
                
                for (int j = 0; j < seats.length; j++) {
                    System.out.printf("%-5s", String.valueOf(j+1));
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
                
                System.out.println("\nNumber of available seats: " + seatInventory.getAvailableSeats());
                System.out.println("Number of reserved seats: " + seatInventory.getReserveSeats());
                System.out.println("Number of balance seats: " + seatInventory.getBalanceSeats() + "\n");
                
            }
            
            System.out.println("<<< Total >>> ");
            System.out.println("Number of available seats: " + totalAvailSeats);
            System.out.println("Number of reserved seats: " + totalReservedSeats);
            System.out.println("Number of balance seats: " + totalBalanceSeats);
            System.out.print("Press any key to continue...> ");
            sc.nextLine();
            
        } catch (FlightNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
        } catch (FlightScheduleNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    private void doViewFlightReservations() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("*** View Flight Reservations ***");
            System.out.print("Enter Flight Number (Numbers Only)> ");
            String flightNumber = sc.nextLine().trim();
            Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
            if (flight.getFlightSchedulePlan().isEmpty()) {
                System.out.println("Error: The selected flight has no flight schedule plans associated with it\n");
                return;
            }
            System.out.println("Displaying all flight schedules for Flight " + flightNumber + ": " + flight.getFlightRoute().getOriginAirport().getAirportCode() + " -> " + flight.getFlightRoute().getDestinationAirport().getAirportCode());
            
            System.out.printf("%-25s%-30s%-20s\n", "Flight Schedule ID", "Departure Date Time", "Flight Duration");
            for (FlightSchedulePlan fsp: flight.getFlightSchedulePlan()) {
                for (FlightSchedule fs: fsp.getFlightSchedule()) {
                    System.out.printf("%-25s%-30s%-20s\n", fs.getFlightScheduleId().toString(), fs.getDepartureDateTime().toString().substring(0, 19), String.valueOf(fs.getFlightDuration()));
                }
            }
            System.out.print("Enter ID of flight schedule (Enter (0) to exit)>  ");
            Long id = sc.nextLong();
            if (id == 0) {
                return;
            }
            Long chosenFlightScheduleId = id;
            sc.nextLine();
        
//            FlightScheduleEntity flightSchedule = null;
//            for (FlightSchedulePlanEntity fsp: flight.getFlightSchedulePlan()) {
//                for (FlightScheduleEntity fs: fsp.getFlightSchedule()) {
//                    if (Objects.equals(fs.getFlightScheduleID(), chosenFlightScheduleId)) {
//                        flightSchedule = fs;
//                    }
//                }
//            }
    
            FlightSchedule flightSchedule = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(chosenFlightScheduleId);
            
            //available cabin class types that is booked for this schedule
            List<CabinClassNameEnum> cabinTypes = new ArrayList<>();
            for (FlightReservation reservation: flightSchedule.getReservations()) {
                if (!cabinTypes.contains(reservation.getCabinClassName())) {
                    cabinTypes.add(reservation.getCabinClassName());
                }
            }
            
            List<List<Pair<Passenger, String>>> passengerInfoForEachCabinType = new ArrayList<>();
            for (int i = 0; i < cabinTypes.size(); i++) {
                passengerInfoForEachCabinType.add(new ArrayList<>());
                for (FlightReservation reservation: flightSchedule.getReservations()) {
                    if (reservation.getCabinClassName()== cabinTypes.get(i)) {
                        String fareBasisCode = reservation.getFareBasisCode();
                        for (Passenger passenger: reservation.getPassengers()) {
                            passengerInfoForEachCabinType.get(i).add(new Pair<>(passenger, fareBasisCode));
                        }
                    }
                }
            }
            
            for (int i = 0; i < cabinTypes.size(); i++) {
                Collections.sort(passengerInfoForEachCabinType.get(i), (Pair<Passenger, String> p1, Pair<Passenger, String> p2) -> {
                    if (p1.getKey().getSeatNumber().compareTo(p2.getKey().getSeatNumber()) > 0) {
                        return 1;
                    } else if (p1.getKey().getSeatNumber().compareTo(p2.getKey().getSeatNumber()) < 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                });
            }
            
            System.out.println("\n ** All Reservations for Flight Schedule ID: " + chosenFlightScheduleId + " **\n");
            if (cabinTypes.isEmpty()) {
                System.out.println("No existing reservations for this flight schedule\n");
            } else {
                for (int i = 0; i < cabinTypes.size(); i++) {
                    String type;
                    switch (cabinTypes.get(i)) {
                    case FIRST:
                        type = "First Class";
                        break;
                    case BUSINESS:
                        type = "Business Class";
                        break;
                    case PREMIUM_ECONOMY:
                        type = "Premium Economy Class";
                        break;
                    default:
                        type = "Economy Class";
                        break;
                    }
                    System.out.println(" -- " + type + " -- ");
                    System.out.println();
                    System.out.printf("%-20s%-20s%-30s\n", "Passenger Name", "Seat Number", "Fare Basis Code");
                    for (Pair<Passenger, String> pair: passengerInfoForEachCabinType.get(i)) {
                        Passenger pass = pair.getKey();
                        String fareCode = pair.getValue();
                        System.out.printf("%-30s%-20s%-30s\n",
                                pass.getFirstName() + " " + pass.getLastName(),
                                pass.getSeatNumber(),
                                fareCode);
                    }
                    System.out.println();
                }
            }
            
            System.out.print("Press any key to continue...> ");
            sc.nextLine();
            
            
        } catch (FlightNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
        } catch (FlightScheduleNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
