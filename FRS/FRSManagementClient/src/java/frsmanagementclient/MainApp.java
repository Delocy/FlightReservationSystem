/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.EmployeeUsernameExistException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zares
 */
public class MainApp {
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
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
            System.out.println("1: Create Employee");
            System.out.println("2: ");
            System.out.println("3: ");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateEmployee();
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
    
}
