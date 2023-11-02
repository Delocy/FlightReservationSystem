/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.AircraftConfig;
import entity.AircraftType;
import entity.Airport;
import entity.Employee;
import entity.Flight;
import entity.FlightRoute;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.AircraftTypeNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.FlightNumberExistException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.InvalidLoginCredentialException;
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
    private Employee currentEmployee;
    
    public MainApp() {
        
    }

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote,AirportSessionBeanRemote airportSessionBeanRemote, AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.aircraftTypeSessionBeanRemote = aircraftTypeSessionBeanRemote;
    }

    
    
    public void runApp() throws AirportNotFoundException, FlightRouteExistException, UnknownPersistenceException, FlightRouteNotFoundException {
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
    
    private void menuMain() throws AirportNotFoundException, FlightRouteExistException, UnknownPersistenceException, FlightRouteNotFoundException
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
                        //
                    } else if (response == 2) {
                        // 
                    } else if (response == 3) {
                        // 
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
                        //doCreateFlight();
                    } else if (response == 2) {
                        // doViewAllFlights();
                    } else if (response == 3) {
                        // 
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

                if (response == 6) {
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
        System.out.println("** Flight Route: from Origin " + newFlightRoute.getOriginAirport() + " to Destination " + newFlightRoute.getDestinationAirport() + " has been successfully created **");
        System.out.println("Would you like to create a complementary return route? (Y/N)");
        if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
//            FlightRoute newReturnRoute = new FlightRoute();
              flightRouteSessionBeanRemote.createFlightRoute(destinationAirport.getAirportId(), originAirport.getAirportId());
              System.out.println("** Flight Route: from Origin " + newFlightRoute.getOriginAirport() + " to Destination " + newFlightRoute.getDestinationAirport() + " has been successfully created **");
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
    
//    private void doCreateFlight() {
//        Scanner scanner = new Scanner(System.in);
//        Flight newFlight = new Flight();
//        System.out.println("*** FRS Management Portal - Create Flight ***\n");
//        System.out.println("Enter flight number");
//        newFlight.setFlightNumber(scanner.nextLine().trim());
//
//        //if want to set flight configurations and routes at this time:
//        newFlight.setAircraftConfig(new AircraftConfig());
//        System.out.println("Select flight route");
//        //
//        FlightRoute flightRoute = new FlightRoute();
//        newFlight.setFlightRoute(flightRoute);
//
//        try {
//            Long flightID = flightSessionBeanRemote.createNewFlight(newFlight);
//            System.out.println("** Flight: " + newFlight.getFlightNumber() + " has been successfully created **");
//        } catch (FlightNumberExistException ex) {
//            System.out.println("Error occured when creating the new flight " + ex.getMessage());
//        }
//        
//        if (flightRoute.isHasComplementaryReturnRoute()) {
//            System.out.println("Do you want to create a complementary return flight based on the same aircraft configuration but with a different flight number?");
//            Flight returnFlight = new Flight();
//            String input = scanner.nextLine().trim();
//            if (input.equals("Y")) {
//                System.out.println("Enter new flight number for return flight");
//                returnFlight.setFlightNumber(scanner.nextLine().trim());
//                returnFlight.setAircraftConfig(newFlight.getAircraftConfig());
//                // returnFlight.setFlightRoute(flightRoute.getReturnRoute());
//                try {
//                    Long flightID = flightSessionBeanRemote.createNewFlight(returnFlight);
//                    System.out.println("** Return Flight: " + newFlight.getFlightNumber() + " has been successfully created **");
//                } catch (FlightNumberExistException ex) {
//                    System.out.println("Error occured when creating the new flight " + ex.getMessage());
//                }
//            } else if (input.equals("N")) {
//                returnFlight.setFlightNumber(newFlight.getFlightNumber());
//                returnFlight.setAircraftConfig(newFlight.getAircraftConfig());
//                // returnFlight.setFlightRoute(flightRoute.getReturnRoute());
//                try {
//                    Long flightID = flightSessionBeanRemote.createNewFlight(returnFlight);
//                    System.out.println("** Flight: " + newFlight.getFlightNumber() + " has been successfully created **");
//                } catch (FlightNumberExistException ex) {
//                    System.out.println("Error occured when creating the new flight " + ex.getMessage());
//                }
//            } else {
//                //
//            }
//                   
//        }
//    }
    
    private void doCreateAircraftConfig() throws AircraftTypeNotFoundException {
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
        
        AircraftType aircraftType = aircraftTypeSessionBeanRemote.retrieveAircraftTypeByAircraftTypeId(aircraftTypeId);
        String name = sc.nextLine().trim();
        newAircraftConfig.setAircraftConfigName(name);
        int numCabinClass = sc.nextInt();
        newAircraftConfig.setNumCabinClass(numCabinClass);
        
        
        // next i needa add da cabin class details T_T
        
    }
    
}
