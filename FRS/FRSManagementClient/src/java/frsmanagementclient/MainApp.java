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
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.AircraftConfig;
import entity.AircraftType;
import entity.Airport;
import entity.CabinClassConfig;
import entity.Employee;
import entity.Flight;
import entity.FlightRoute;
import java.awt.color.ColorSpace;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.CabinClassNameEnum;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.AircraftConfigNotFoundException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.CabinClassNameNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightNumberExistException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.MaxSeatCapacityExceededException;
import util.exception.UnknownPersistenceException;

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
    private Employee currentEmployee;
    
    public MainApp() {
        
    }

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote,AirportSessionBeanRemote airportSessionBeanRemote, AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote, CabinClassConfigSessionBeanRemote cabinClassConfigSessionBeanRemote, AircraftConfigSessionBeanRemote aircraftConfigSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.aircraftTypeSessionBeanRemote = aircraftTypeSessionBeanRemote;
        this.cabinClassConfigSessionBeanRemote = cabinClassConfigSessionBeanRemote;
        this.aircraftConfigSessionBeanRemote = aircraftConfigSessionBeanRemote;
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

                while (response < 1 || response > 4) {
                    System.out.print("> ");
 
                    response = scanner.nextInt();

                    if (response == 1) {
                        doCreateFlight();
                    } else if (response == 2) {
                        doViewAllFlights();
                    } else if (response == 3) {
                        doViewFlightDetails();
                    } else if (response == 4) {
                        // 
                    } else if (response == 5) {
                        // 
                    } else if (response == 6) {
                        // 
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

                while (response < 1 || response > 4) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        //
                    } else if (response == 2) {
                        // 
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
    
    private void doCreateFlightRoute() throws AirportNotFoundException, FlightRouteExistException, UnknownPersistenceException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** FRS Management Portal - Create Flight Route ***\n");
        System.out.println("Enter origin IATA airport code");
        String origin = scanner.nextLine().trim();
        Airport originAirport = airportSessionBeanRemote.retrieveAirportByAirportCode(origin);
        //System.out.println(airportSessionBeanRemote.retrieveAirportByAirportId(originAirport.getAirportId()).toString());
        //System.out.println(originAirport.getAirportId());
        System.out.println("Enter destination IATA airport code");
        String destination = scanner.nextLine().trim();
        Airport destinationAirport = airportSessionBeanRemote.retrieveAirportByAirportCode(destination);
        //System.out.println(airportSessionBeanRemote.retrieveAirportByAirportId(destinationAirport.getAirportId()).toString());
        //System.out.println(destinationAirport.getAirportId());
        
        FlightRoute newFlightRoute = flightRouteSessionBeanRemote.createFlightRoute(originAirport.getAirportId(), destinationAirport.getAirportId());
        System.out.println("** Flight Route: from Origin " + newFlightRoute.getOriginAirport().getAirportName() + " to Destination " + newFlightRoute.getDestinationAirport().getAirportName() + " has been successfully created **");
        System.out.println("Would you like to create a complementary return route? (Y/N)");
        if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
//            FlightRoute newReturnRoute = new FlightRoute();
              FlightRoute compFlightRoute = flightRouteSessionBeanRemote.createFlightRoute(destinationAirport.getAirportId(), originAirport.getAirportId());
              System.out.println("** Flight Route: from Origin " + compFlightRoute.getOriginAirport().getAirportName() + " to Destination " + compFlightRoute.getDestinationAirport().getAirportName() + " has been successfully created **");
        }
    }
    
    private void doViewAllFlightRoutes() {
        System.out.println("*** FRS Management Portal - View All Flight Routes ***\n");
        List<FlightRoute> allFlightRoutes = flightRouteSessionBeanRemote.viewAllFlightRoutes();
        System.out.printf("%4s%16s%24s\n", "Route ID", "Origin Airport", "Destination Airport");
        for (FlightRoute flightRoute : allFlightRoutes) {
            System.out.printf("%4s%16s%24s\n", flightRoute.getFlightRouteId(), flightRoute.getOriginAirport().getAirportName(), flightRoute.getDestinationAirport().getAirportName());
        }    
    }
    
    private void doDeleteFlightRoute() throws FlightRouteNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** FRS Management Portal - Delete Flight Route ***\n");
        System.out.println("Enter ID of flight route for deletion");
        flightRouteSessionBeanRemote.deleteFlightRoute(scanner.nextLong());
    }
    
    private void doCreateFlight() throws AircraftConfigNotFoundException, FlightRouteNotFoundException, UnknownPersistenceException, FlightExistException, FlightNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Flight newFlight = new Flight();
        System.out.println("*** FRS Management Portal - Create Flight ***\n");
        System.out.println("Enter flight number");
        newFlight.setFlightNumber("ML" + scanner.nextLine().trim());

        //if want to set flight configurations and routes at this time:
        System.out.println("Select Aircraft Configuration");
        System.out.printf("%30s%40s%25s%20s\n", "Aircraft Configuration ID", "Name", "Number of Cabin Classes", "Aircraft Type");
        List<AircraftConfig> aircraftConfigs = aircraftConfigSessionBeanRemote.retrieveAllAircraftConfig();
        
        for (AircraftConfig a : aircraftConfigs) {
            System.out.printf("%30s%40s%25s%20s\n", a.getAircraftConfigId(), a.getAircraftConfigName(), a.getNumCabinClass(), a.getAircraftType().getAircraftTypeName());
        }

        System.out.println("");
        
        AircraftConfig selectedAircraftConfig = aircraftConfigSessionBeanRemote.retrieveAircraftConfigById(scanner.nextLong());
//        newFlight.setAircraftConfig(selectedAircraftConfig); 
        //
        
        System.out.println("Select flight route");
        List<FlightRoute> allFlightRoutes = flightRouteSessionBeanRemote.viewAllFlightRoutes();
        
        for (FlightRoute flightRoute : allFlightRoutes) {
            System.out.printf("%4s%16s%24s\n", flightRoute.getFlightRouteId(), flightRoute.getOriginAirport().getAirportName(), flightRoute.getDestinationAirport().getAirportName());
        }   
        FlightRoute selectedFlightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByRouteID(scanner.nextLong());
        scanner.nextLine();
        
        Airport origin = selectedFlightRoute.getOriginAirport();
        Airport destination = selectedFlightRoute.getDestinationAirport();
//        newFlight.setFlightRoute(selectedFlightRoute);
        
        Long flightID = (long) 0;
         try {
            flightID = flightSessionBeanRemote.createNewFlight(newFlight, selectedAircraftConfig.getAircraftConfigId(),selectedFlightRoute.getFlightRouteId());
            
            System.out.println("** Flight: " + newFlight.getFlightNumber() + " has been successfully created **");
        } catch (FlightExistException ex) {
            System.out.println("Error occured when creating the new flight " + ex.getMessage());
        }

        
        for (FlightRoute flightRoute : allFlightRoutes) {
            Airport refOrigin = flightRoute.getOriginAirport();
            Airport refDestination = flightRoute.getDestinationAirport();
            if (refOrigin.equals(destination) && refDestination.equals(origin)) {
                System.out.println("Do you want to create a complementary return flight based on the same aircraft configuration but with a different flight number? Press (Y) for yes ");
                Flight returnFlight = new Flight();
                String input = scanner.nextLine().trim();
                if (input.equals("Y")) {
                    System.out.println("Enter new flight number for return flight");
                    returnFlight.setFlightNumber("ML"+ scanner.nextLine().trim());
                    
                    try {
                        Long returnFlightID = flightSessionBeanRemote.createNewFlight(returnFlight, selectedAircraftConfig.getAircraftConfigId(), flightRoute.getFlightRouteId());
                        Flight newRFlight = flightSessionBeanRemote.retrieveFlightByFlightID(returnFlightID);
//                        
                        flightSessionBeanRemote.associateOriginalFlightWithReturnFlight(flightID, returnFlightID);
                        System.out.println("** Return Flight: " + newRFlight.getFlightNumber() + " has been successfully created **");
                    } catch (FlightExistException ex) {
                        System.out.println("Error occured when creating the new flight " + ex.getMessage());
                    }
                } else if (input.equals("N")) {
                    return;
                }
            }
        }

       
        
    }
    
    private void doViewAllFlights() {
        System.out.println("*** FRS Management Portal - View All Flight ***\n");
        List<Flight> allFlights = flightSessionBeanRemote.viewAllFlights();
        System.out.printf("%4s%8s%16s%24s\n", "Flight ID", "Flight Number" ,"Origin Airport", "Destination Airport");
        for (Flight flight : allFlights) {
            System.out.printf("%4s%8s%16s%24s\n", flight.getFlightId(), flight.getFlightNumber(), flight.getFlightRoute().getOriginAirport(), flight.getFlightRoute().getDestinationAirport());
        }    
    }
    
    private void doViewFlightDetails() {
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** FRS Management Portal - View Flight Details ***\n");
        System.out.println("Enter FlightNumber: ");
        String flightNumber = scanner.nextLine().trim();
        
        try {
            Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
            FlightRoute route = flight.getFlightRoute();
            Airport origin = route.getOriginAirport();
            Airport destination = route.getDestinationAirport();
            AircraftConfig config = flight.getAircraftConfig();
            List<CabinClassConfig> cabinClassConfig = config.getCabinClassConfig();

            System.out.printf("%10s%20s%20s%35s%20s%35s%25s%30s%40s%25s%20s%20s%30s\n", "Flight ID", "Flight Number", "Flight Route ID", "Origin Airport Name", "Origin Airport IATA", "Destination Airport Name", "Destination Airport IATA", "Aircraft Configuration ID", "Name", "Cabin Class ID", "Max Seats Capacity", "Aircraft Type", "Returning Flight Number");
            for(int i = 0; i< cabinClassConfig.size(); i++) {
            System.out.printf("%10s%20s%20s%35s%20s%35s%25s%30s%40s%25s%20s%20s%30s\n", flight.getFlightId(), flight.getFlightNumber(), route.getFlightRouteId().toString(), route.getOriginAirport().getAirportName() ,route.getOriginAirport().getAirportCode(), route.getDestinationAirport().getAirportName() ,route.getDestinationAirport().getAirportCode(), config.getAircraftConfigId().toString(), config.getAircraftConfigName(), cabinClassConfig.get(i).getCabinClassConfigId(), cabinClassConfig.get(i).getMaxSeatCapacity(), config.getAircraftType().getAircraftTypeName(), flight.getReturningFlight() != null ? flight.getReturningFlight().getFlightNumber(): "None");
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
            System.out.printf("%20s%40s%20s%40s%25s\n", route.getFlightRouteId(), route.getOriginAirport().getAirportName() ,route.getOriginAirport().getAirportCode(), route.getDestinationAirport().getAirportName() ,route.getDestinationAirport().getAirportCode());
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
            System.out.println("Aircraft Configuration " + aircraftConfigId + " created successfully!");
        } catch (CabinClassNameNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownPersistenceException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MaxSeatCapacityExceededException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       
    }
    
    public CabinClassConfig doCreateCabinClass() throws CabinClassNameNotFoundException, MaxSeatCapacityExceededException {
        Scanner sc = new Scanner(System.in);
        CabinClassConfig newCabin = new CabinClassConfig();
        CabinClassNameEnum cabinClassName = CabinClassNameEnum.F;

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
            
            System.out.println("\nCabin Overview: " + aircraftConfig.getCabinClassConfig().size() + " cabin class(es) / " + maxCapacity + " total seats");
            System.out.println("");
        } catch (AircraftConfigNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
