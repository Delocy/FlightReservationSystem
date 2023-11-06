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
                airportSessionBeanLocal.createNewAirport(new Airport("Changi", "SIN", "Singapore", "Singapore", "Singapore"));
                airportSessionBeanLocal.createNewAirport(new Airport("Adelaide", "ADL", "Adelaide", "Adelaide", "Australia"));
                airportSessionBeanLocal.createNewAirport(new Airport("Narita", "NRT", "Tokyo", "Tokyo", "Japan"));
                airportSessionBeanLocal.createNewAirport(new Airport("Kansai", "KIX", "Osaka", "Osaka", "Japan"));
                airportSessionBeanLocal.createNewAirport(new Airport("Pudong", "PVG", "Shanghai", "Shanghai", "China"));
            } catch (UnknownPersistenceException ex) {
                Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("Boeing-737", (long) 204));
            } catch (UnknownPersistenceException ex) {
                Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            // CabinClassNameEnum cabinClassName, String seatConfiguration, int numRows, int numAisles, int numSeatsAbreast, int maxSeatCapacity
            List<CabinClassConfig> cabins = new ArrayList<>();
            cabins.add(new CabinClassConfig(CabinClassNameEnum.FIRST, "1-2-1", 2, 2, 4));
            cabins.add(new CabinClassConfig(CabinClassNameEnum.BUSINESS, "2-2-2", 10, 2, 6));
            cabins.add(new CabinClassConfig(CabinClassNameEnum.ECONOMY, "3-4-3", 12, 2, 10));
            AircraftType at = aircraftTypeSessionBeanLocal.retrieveAircraftTypeByAircraftTypeId((long)1);
            aircraftConfigSessionBeanLocal.createAircraftConfig(new AircraftConfig("SQ", 3, at), cabins, (long)1);
            
            
            
        }
        catch(EmployeeUsernameExistException ex)
        {
            ex.printStackTrace();
        } catch (AircraftTypeNotFoundException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownPersistenceException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
