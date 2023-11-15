/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package holidayreservationsystemclient;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import ws.holiday.HolidayReservationSystemWebService;
import ws.holiday.HolidayReservationSystemWebService_Service;
import ws.holiday.InvalidLoginCredentialException_Exception;

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
                    //menuMain();
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
    
    public static Long doLogin(String username, String password) throws InvalidLoginCredentialException_Exception {
        HolidayReservationSystemWebService_Service service = new HolidayReservationSystemWebService_Service();
        HolidayReservationSystemWebService port = service.getHolidayReservationSystemWebServicePort();
        return port.doLogin(username, password);
    }

}
