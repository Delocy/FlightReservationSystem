/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.AircraftConfig;
import entity.Employee;
import entity.Flight;
import entity.FlightRoute;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.EmployeeUsernameExistException;
import util.exception.FlightNumberExistException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zares
 */
public class MainApp {
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private Employee currentEmployee;
    
    public MainApp() {
        
    }

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
    }
    
    public void runApp() {
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
    
    private void menuMain()
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
                        //doViewAllFlights();
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
        System.out.print("Enter username> ");
        newEmployee.setLastName(scanner.nextLine().trim());
        System.out.print("Enter password> ");
        newEmployee.setLastName(scanner.nextLine().trim());
        try {
            Long employeeID = employeeSessionBeanRemote.createNewEmployee(newEmployee);
            System.out.println("** Employee " + newEmployee.getFirstName() + newEmployee.getLastName() + " with role : " + newEmployee.getAccessRightEnum() +" has been successfully created **");
        } catch (EmployeeUsernameExistException ex) {
            System.out.println("Error occured when creating the new customer " + ex.getMessage());
        }
    }
    
    private void doCreateFlight() {
        Scanner scanner = new Scanner(System.in);
        Flight newFlight = new Flight();
        System.out.println("*** FRS Management Portal - Create Flight ***\n");
        System.out.println("Enter flight number");
        newFlight.setFlightNumber(scanner.nextLine().trim());

        //if want to set flight configurations and routes at this time:
        newFlight.setAircraftConfig(new AircraftConfig());
        newFlight.setFlightRoute(new FlightRoute());

        try {
            Long employeeID = flightSessionBeanRemote.createNewFlight(newFlight);
            System.out.println("** Flight: " + newFlight.getFlightNumber() + " has been successfully created **");
        } catch (FlightNumberExistException ex) {
            System.out.println("Error occured when creating the new flight " + ex.getMessage());
        }
    }
    
}
