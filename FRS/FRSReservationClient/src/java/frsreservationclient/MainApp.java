/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.Customer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
public class MainApp {
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private Customer currentCustomer;

    public MainApp() {
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
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
        
        System.out.println("*** Welcome to Merlion Airlines ***\n");
        System.out.println("You are currently logged in as " + currentCustomer.getFirstName() + " " + currentCustomer.getLastName() + "!\n");
        System.out.println();
        System.out.println("*** What would you like to do ***");
        System.out.println("1: Reserve Flight");
        System.out.println("2: View My Flight Reservations");
        System.out.println("3: View My Flight Reservation Details");
        System.out.println("4: Log Out");

        response = 0;
        while (true) {
            while(response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                if(response == 1) {
                    //searchFlight();
                } else if(response == 2) {
                    //viewFlightReservation();
                } else if (response == 3) {
                    //viewFlightReservationDetails();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                return;
            }
        }
        
    }
}
