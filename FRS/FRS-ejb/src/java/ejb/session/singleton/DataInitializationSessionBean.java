/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftConfigSessionBeanLocal;
import ejb.session.stateless.AircraftTypeSessionBeanLocal;
import ejb.session.stateless.AirportSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.FlightSessionBeanLocal;
import entity.AircraftConfig;
import entity.AircraftType;
import entity.Airport;
import entity.CabinClassConfig;
import entity.Employee;
import entity.Flight;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CabinClassNameEnum;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.AircraftTypeNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zares
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB
    private AircraftConfigSessionBeanLocal aircraftConfigSessionBeanLocal;

    @EJB
    private AirportSessionBeanLocal airportSessionBeanLocal;

    @EJB
    private AircraftTypeSessionBeanLocal aircraftTypeSessionBeanLocal;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    
    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public DataInitializationSessionBean()
    {
    }
    
    @PostConstruct
    public void postConstruct()
    {
        try
        {
            employeeSessionBeanLocal.retrieveEmployeeByUsername("systemadmin");
        }
        catch(EmployeeNotFoundException ex)
        {
            initializeData();
        }
    }
    
    private void initializeData()
    {
        try
        {
            employeeSessionBeanLocal.createNewEmployee(new Employee("SystemAdmin", "Default", EmployeeAccessRightEnum.SYSTEMADMIN, "systemadmin", "password"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("FleetManager", "Default", EmployeeAccessRightEnum.FLEETMANAGER, "fleetmanager", "password"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("RoutePlanner", "Default", EmployeeAccessRightEnum.ROUTEPLANNER, "routeplanner", "password"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("ScheduleManager", "Default", EmployeeAccessRightEnum.SCHEDULEMANAGER, "schedulemanager", "password"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("SalesManager", "Default", EmployeeAccessRightEnum.SALESMANAGER, "salesmanager", "password"));
            
            try {
                airportSessionBeanLocal.createNewAirport(new Airport("Changi", "SIN", "Singapore", "Singapore", "Singapore", 8));
                airportSessionBeanLocal.createNewAirport(new Airport("Melbourne", "MEL", "Melbourne", "Melbourne", "Australia",11));
                airportSessionBeanLocal.createNewAirport(new Airport("Narita", "NRT", "Tokyo", "Tokyo", "Japan", 9));
                airportSessionBeanLocal.createNewAirport(new Airport("Kansai", "KIX", "Osaka", "Osaka", "Japan", 9));
                airportSessionBeanLocal.createNewAirport(new Airport("Pudong", "PVG", "Shanghai", "Shanghai", "China", 8));
                airportSessionBeanLocal.createNewAirport(new Airport("Los Angeles", "LAX", "Los Angeles", "California", "UnitedStates", -8));
                airportSessionBeanLocal.createNewAirport(new Airport("Chek Lap Kok", "HKG", "Hong Kong", "Hong Kong", "China", 8));
                airportSessionBeanLocal.createNewAirport(new Airport("Taoyuan", "TPE", "Taoyuan", "Taipei", "Taiwan R.O.C", 8));
                airportSessionBeanLocal.createNewAirport(new Airport("Sydney", "SYD", "Sydney", "New South Wales", "Australia", 11));
                
            } catch (UnknownPersistenceException ex) {
                Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("Boeing-737", (long) 200));
                aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("Boeing-747", (long) 400));
            } catch (UnknownPersistenceException ex) {
                Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            // CabinClassNameEnum cabinClassName, String seatConfiguration, int numRows, int numAisles, int numSeatsAbreast, int maxSeatCapacity
            /*
            try {
                List<CabinClassConfig> cabins = new ArrayList<>();
                cabins.add(new CabinClassConfig(CabinClassNameEnum.ECONOMY, "3-3", 30, 1, 6));
                aircraftConfigSessionBeanLocal.createAircraftConfig(new AircraftConfig("All Economy", 1), cabins, (long)1);
                cabins = new ArrayList<>();
                cabins.add(new CabinClassConfig(CabinClassNameEnum.FIRST, "1-1", 5, 1, 2));
                cabins.add(new CabinClassConfig(CabinClassNameEnum.BUSINESS, "2-2", 5, 1, 4));
                cabins.add(new CabinClassConfig(CabinClassNameEnum.ECONOMY, "3-3", 25, 1, 6));
                aircraftConfigSessionBeanLocal.createAircraftConfig(new AircraftConfig("Three Classes", 3), cabins, (long)1);
                cabins = new ArrayList<>();
                cabins.add(new CabinClassConfig(CabinClassNameEnum.ECONOMY, "3-4-3", 38, 2, 10));
                aircraftConfigSessionBeanLocal.createAircraftConfig(new AircraftConfig("All Economy 2", 1), cabins, (long)2);
                cabins = new ArrayList<>();
                cabins.add(new CabinClassConfig(CabinClassNameEnum.FIRST, "1-1", 5, 1, 2));
                cabins.add(new CabinClassConfig(CabinClassNameEnum.BUSINESS, "2-2-2", 5, 2, 6));
                cabins.add(new CabinClassConfig(CabinClassNameEnum.ECONOMY, "3-4-3", 32, 1, 10));
                aircraftConfigSessionBeanLocal.createAircraftConfig(new AircraftConfig("All Economy 2", 3), cabins, (long)2);
            } catch (UnknownPersistenceException ex) {
                Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            } */
        }
        catch(EmployeeUsernameExistException ex)
        {
            ex.printStackTrace();
        }
    }
}
