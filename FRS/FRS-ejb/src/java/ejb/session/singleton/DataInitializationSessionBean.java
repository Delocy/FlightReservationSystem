/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftConfigSessionBeanLocal;
import ejb.session.stateless.AircraftTypeSessionBeanLocal;
import ejb.session.stateless.AirportSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.FlightRouteSessionBeanLocal;
import ejb.session.stateless.FlightSchedulePlanSessionBeanLocal;
import ejb.session.stateless.FlightSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.AircraftConfig;
import entity.AircraftType;
import entity.Airport;
import entity.CabinClassConfig;
import entity.Employee;
import entity.Fare;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Partner;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CabinClassNameEnum;
import util.enumeration.EmployeeAccessRightEnum;
import util.enumeration.ScheduleTypeEnum;
import util.exception.AircraftConfigNotFoundException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.FareExistException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.FlightSchedulePlanExistException;
import util.exception.FlightSchedulePlanNotFoundException;
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
    private FlightSchedulePlanSessionBeanLocal flightSchedulePlanSessionBeanLocal;

    @EJB
    private FlightSessionBeanLocal flightSessionBeanLocal;

    @EJB
    private FlightRouteSessionBeanLocal flightRouteSessionBeanLocal;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

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
            
            partnerSessionBeanLocal.createNewPartner(new Partner("Holiday.com", "holidaydotcom", "password"));
            
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
            
            //CabinClassNameEnum cabinClassName, String seatConfiguration, int numRows, int numAisles, int numSeatsAbreast
            CabinClassConfig c = new CabinClassConfig(CabinClassNameEnum.ECONOMY, "3-3", 30, 1, 6);
            AircraftConfig config = new AircraftConfig("Boeing 737 All Economy", 1);
            List<CabinClassConfig> cabins = new ArrayList<>();
            cabins.add(c);
            aircraftConfigSessionBeanLocal.createAircraftConfig(config, cabins, 1l);
            
            cabins.clear();
            c = new CabinClassConfig(CabinClassNameEnum.FIRST, "1-1", 5, 1, 2);
            cabins.add(c);
            c = new CabinClassConfig(CabinClassNameEnum.BUSINESS, "2-2", 5, 1, 4);
            cabins.add(c);
            c = new CabinClassConfig(CabinClassNameEnum.ECONOMY, "3-3", 25, 1, 6);
            cabins.add(c);
            config = new AircraftConfig("Boeing 737 Three Classes", 3);
            aircraftConfigSessionBeanLocal.createAircraftConfig(config, cabins, 1l);
            
            cabins.clear();
            c = new CabinClassConfig(CabinClassNameEnum.ECONOMY, "3-4-3", 38, 2, 10);
            cabins.add(c);
            config = new AircraftConfig("Boeing 747 All Economy", 1);
            aircraftConfigSessionBeanLocal.createAircraftConfig(config, cabins, 2l);
            
            cabins.clear();
            c = new CabinClassConfig(CabinClassNameEnum.FIRST, "1-1", 5, 1, 2);
            cabins.add(c);
            c = new CabinClassConfig(CabinClassNameEnum.BUSINESS, "2-2-2", 5, 2, 6);
            cabins.add(c);
            c = new CabinClassConfig(CabinClassNameEnum.ECONOMY, "3-4-3", 32, 2, 10);
            cabins.add(c);
            config = new AircraftConfig("Boeing 747 Three Classes", 3);
            aircraftConfigSessionBeanLocal.createAircraftConfig(config, cabins, 2l);
            
            flightRouteSessionBeanLocal.createFlightRoute(airportSessionBeanLocal.retrieveAirportByAirportCode("SIN").getAirportId(), airportSessionBeanLocal.retrieveAirportByAirportCode("HKG").getAirportId());
            flightRouteSessionBeanLocal.createFlightRoute(airportSessionBeanLocal.retrieveAirportByAirportCode("HKG").getAirportId(), airportSessionBeanLocal.retrieveAirportByAirportCode("SIN").getAirportId());
            
            flightRouteSessionBeanLocal.createFlightRoute(airportSessionBeanLocal.retrieveAirportByAirportCode("SIN").getAirportId(), airportSessionBeanLocal.retrieveAirportByAirportCode("TPE").getAirportId());
            flightRouteSessionBeanLocal.createFlightRoute(airportSessionBeanLocal.retrieveAirportByAirportCode("TPE").getAirportId(), airportSessionBeanLocal.retrieveAirportByAirportCode("SIN").getAirportId());
            
            flightRouteSessionBeanLocal.createFlightRoute(airportSessionBeanLocal.retrieveAirportByAirportCode("SIN").getAirportId(), airportSessionBeanLocal.retrieveAirportByAirportCode("NRT").getAirportId());
            flightRouteSessionBeanLocal.createFlightRoute(airportSessionBeanLocal.retrieveAirportByAirportCode("NRT").getAirportId(), airportSessionBeanLocal.retrieveAirportByAirportCode("SIN").getAirportId());
            
            flightRouteSessionBeanLocal.createFlightRoute(airportSessionBeanLocal.retrieveAirportByAirportCode("HKG").getAirportId(), airportSessionBeanLocal.retrieveAirportByAirportCode("NRT").getAirportId());
            flightRouteSessionBeanLocal.createFlightRoute(airportSessionBeanLocal.retrieveAirportByAirportCode("NRT").getAirportId(), airportSessionBeanLocal.retrieveAirportByAirportCode("HKG").getAirportId());
            
            flightRouteSessionBeanLocal.createFlightRoute(airportSessionBeanLocal.retrieveAirportByAirportCode("TPE").getAirportId(), airportSessionBeanLocal.retrieveAirportByAirportCode("NRT").getAirportId());
            flightRouteSessionBeanLocal.createFlightRoute(airportSessionBeanLocal.retrieveAirportByAirportCode("NRT").getAirportId(), airportSessionBeanLocal.retrieveAirportByAirportCode("TPE").getAirportId());
            
            flightRouteSessionBeanLocal.createFlightRoute(airportSessionBeanLocal.retrieveAirportByAirportCode("SIN").getAirportId(), airportSessionBeanLocal.retrieveAirportByAirportCode("SYD").getAirportId());
            flightRouteSessionBeanLocal.createFlightRoute(airportSessionBeanLocal.retrieveAirportByAirportCode("SYD").getAirportId(), airportSessionBeanLocal.retrieveAirportByAirportCode("SIN").getAirportId());
            
            flightRouteSessionBeanLocal.createFlightRoute(airportSessionBeanLocal.retrieveAirportByAirportCode("SYD").getAirportId(), airportSessionBeanLocal.retrieveAirportByAirportCode("NRT").getAirportId());
            flightRouteSessionBeanLocal.createFlightRoute(airportSessionBeanLocal.retrieveAirportByAirportCode("NRT").getAirportId(), airportSessionBeanLocal.retrieveAirportByAirportCode("SYD").getAirportId());
        
            Flight flight = new Flight("ML111");
            Flight cFlight = new Flight("ML112");
            Long flightId = flightSessionBeanLocal.createNewFlight(flight, 2l, 1l); // 2l = 737, 1l = SIN-HKG
            Long cFlightId = flightSessionBeanLocal.createNewFlight(cFlight, 2l, 2l);
            flightSessionBeanLocal.associateOriginalFlightWithReturnFlight(flightId, cFlightId);
            
            flight = new Flight("ML211");
            cFlight = new Flight("ML212");
            flightId = flightSessionBeanLocal.createNewFlight(flight, 2l, 3l); // 2l = 737, 3l = SIN-TPE
            cFlightId = flightSessionBeanLocal.createNewFlight(cFlight, 2l, 4l);
            flightSessionBeanLocal.associateOriginalFlightWithReturnFlight(flightId, cFlightId);
            
            flight = new Flight("ML311");
            cFlight = new Flight("ML312");
            flightId = flightSessionBeanLocal.createNewFlight(flight, 4l, 5l); // 2l = 747, 5l = SIN-NRT
            cFlightId = flightSessionBeanLocal.createNewFlight(cFlight, 2l, 6l);
            flightSessionBeanLocal.associateOriginalFlightWithReturnFlight(flightId, cFlightId);
            
            flight = new Flight("ML411");
            cFlight = new Flight("ML412");
            flightId = flightSessionBeanLocal.createNewFlight(flight, 2l, 7l); // 2l = 737, 7l = HKG-NRT
            cFlightId = flightSessionBeanLocal.createNewFlight(cFlight, 2l, 8l);
            flightSessionBeanLocal.associateOriginalFlightWithReturnFlight(flightId, cFlightId);
            
            flight = new Flight("ML511");
            cFlight = new Flight("ML512");
            flightId = flightSessionBeanLocal.createNewFlight(flight, 2l, 9l); // 2l = 737, 9l = SIN-TPE
            cFlightId = flightSessionBeanLocal.createNewFlight(cFlight, 2l, 10l);
            flightSessionBeanLocal.associateOriginalFlightWithReturnFlight(flightId, cFlightId);
            
            flight = new Flight("ML611");
            cFlight = new Flight("ML612");
            flightId = flightSessionBeanLocal.createNewFlight(flight, 2l, 11l); // 2l = 737, 11l = SIN-SYD
            cFlightId = flightSessionBeanLocal.createNewFlight(cFlight, 2l, 12l);
            flightSessionBeanLocal.associateOriginalFlightWithReturnFlight(flightId, cFlightId);
            
            flight = new Flight("ML621");
            cFlight = new Flight("ML622");
            flightId = flightSessionBeanLocal.createNewFlight(flight, 1l, 11l); // 2l = 737, 11l = SIN-SYD
            cFlightId = flightSessionBeanLocal.createNewFlight(cFlight, 1l, 12l);
            flightSessionBeanLocal.associateOriginalFlightWithReturnFlight(flightId, cFlightId);
            
            flight = new Flight("ML711");
            cFlight = new Flight("ML712");
            flightId = flightSessionBeanLocal.createNewFlight(flight, 4l, 13l); // 2l = 737, 13l = SYD-NRT
            cFlightId = flightSessionBeanLocal.createNewFlight(cFlight, 4l, 14l);
            flightSessionBeanLocal.associateOriginalFlightWithReturnFlight(flightId, cFlightId);
            
            
            SimpleDateFormat recurrentFormat = new SimpleDateFormat("dd/M/yyyy");
            SimpleDateFormat scheduleFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss a");
            
            // FSP 1
            Date recurrentEnd = recurrentFormat.parse("31/12/2023");
            Flight f1 = flightSessionBeanLocal.retrieveFlightByFlightNumber("ML711");
            Date startDateTime = scheduleFormat.parse("1/12/2023 9:00:00 AM");
            Pair<Date, Double> pair = new Pair<>(startDateTime, 14.0);
            FlightSchedulePlan fsp = new FlightSchedulePlan(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, f1);
            
            List<Fare> fares = new ArrayList<>();
            fares.add(new Fare("HA", BigDecimal.valueOf(6000), CabinClassNameEnum.FIRST));
            fares.add(new Fare("HA", BigDecimal.valueOf(3000), CabinClassNameEnum.BUSINESS));
            fares.add(new Fare("HA", BigDecimal.valueOf(1000), CabinClassNameEnum.ECONOMY));
            
            fsp = flightSchedulePlanSessionBeanLocal.createNewFlightSchedulePlanWeekly(fsp, fares, f1.getFlightId(), pair, 2);
            
            Flight f2 = flightSessionBeanLocal.retrieveFlightByFlightNumber("ML712");
            FlightSchedulePlan fsp2 = new FlightSchedulePlan(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, f2);
            List<Pair<Date,Double>> info = new ArrayList<>();
            int diff = f1.getFlightRoute().getDestinationAirport().getGmt() - f1.getFlightRoute().getOriginAirport().getGmt();
            for(FlightSchedule fs : fsp.getFlightSchedule()) {
                Calendar cal = Calendar.getInstance();
                
                cal.setTime(fs.getDepartureDateTime());
                double duration = fs.getFlightDuration();
                int hour = (int) duration;
                int min = (int) (duration % 1 * 60);
                cal.add(Calendar.HOUR_OF_DAY, hour);
                cal.add(Calendar.MINUTE, min);
                cal.add(Calendar.HOUR_OF_DAY, 2 + diff);
                Date date = cal.getTime();
                info.add(new Pair<>(date, fs.getFlightDuration()));
            }
            
            List<Fare> fares2 = new ArrayList<>();
            fares2.add(new Fare("HA", BigDecimal.valueOf(6000), CabinClassNameEnum.FIRST));
            fares2.add(new Fare("HA", BigDecimal.valueOf(3000), CabinClassNameEnum.BUSINESS));
            fares2.add(new Fare("HA", BigDecimal.valueOf(1000), CabinClassNameEnum.ECONOMY));
            
            fsp2 = flightSchedulePlanSessionBeanLocal.createNewFlightSchedulePlanMultiple(fsp2, fares2, f2.getFlightId(), info);
            flightSchedulePlanSessionBeanLocal.associateExistingPlanToComplementaryPlan(fsp.getFlightSchedulePlanId(), fsp2.getFlightSchedulePlanId());
        
            // FSP 2
            f1 = flightSessionBeanLocal.retrieveFlightByFlightNumber("ML611");
            pair = new Pair<>(startDateTime, 8.0);
            fsp = new FlightSchedulePlan(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, f1);
            
            fares = new ArrayList<>();
            fares.add(new Fare("HA", BigDecimal.valueOf(3000), CabinClassNameEnum.FIRST));
            fares.add(new Fare("HA", BigDecimal.valueOf(1500), CabinClassNameEnum.BUSINESS));
            fares.add(new Fare("HA", BigDecimal.valueOf(500), CabinClassNameEnum.ECONOMY));
            
            fsp = flightSchedulePlanSessionBeanLocal.createNewFlightSchedulePlanWeekly(fsp, fares, f1.getFlightId(), pair, 1);
            
            f2 = flightSessionBeanLocal.retrieveFlightByFlightNumber("ML612");
            fsp2 = new FlightSchedulePlan(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, f2);
            info = new ArrayList<>();
            diff = f1.getFlightRoute().getDestinationAirport().getGmt() - f1.getFlightRoute().getOriginAirport().getGmt();
            for(FlightSchedule fs : fsp.getFlightSchedule()) {
                Calendar cal = Calendar.getInstance();
                
                cal.setTime(fs.getDepartureDateTime());
                double duration = fs.getFlightDuration();
                int hour = (int) duration;
                int min = (int) (duration % 1 * 60);
                cal.add(Calendar.HOUR_OF_DAY, hour);
                cal.add(Calendar.MINUTE, min);
                cal.add(Calendar.HOUR_OF_DAY, 2 + diff);
                Date date = cal.getTime();
                info.add(new Pair<>(date, fs.getFlightDuration()));
            }
            
            fares2 = new ArrayList<>();
            fares2.add(new Fare("HA", BigDecimal.valueOf(300), CabinClassNameEnum.FIRST));
            fares2.add(new Fare("HA", BigDecimal.valueOf(1500), CabinClassNameEnum.BUSINESS));
            fares2.add(new Fare("HA", BigDecimal.valueOf(500), CabinClassNameEnum.ECONOMY));
            
            fsp2 = flightSchedulePlanSessionBeanLocal.createNewFlightSchedulePlanMultiple(fsp2, fares2, f2.getFlightId(), info);
            flightSchedulePlanSessionBeanLocal.associateExistingPlanToComplementaryPlan(fsp.getFlightSchedulePlanId(), fsp2.getFlightSchedulePlanId());
        
            // FSP 3
            f1 = flightSessionBeanLocal.retrieveFlightByFlightNumber("ML621");
            pair = new Pair<>(startDateTime, 8.0);
            fsp = new FlightSchedulePlan(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, f1);
            
            fares = new ArrayList<>();
            fares.add(new Fare("HA", BigDecimal.valueOf(700), CabinClassNameEnum.ECONOMY));
            
            fsp = flightSchedulePlanSessionBeanLocal.createNewFlightSchedulePlanWeekly(fsp, fares, f1.getFlightId(), pair, 3);
            
            f2 = flightSessionBeanLocal.retrieveFlightByFlightNumber("ML622");
            fsp2 = new FlightSchedulePlan(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, f2);
            info = new ArrayList<>();
            diff = f1.getFlightRoute().getDestinationAirport().getGmt() - f1.getFlightRoute().getOriginAirport().getGmt();
            for(FlightSchedule fs : fsp.getFlightSchedule()) {
                Calendar cal = Calendar.getInstance();
                
                cal.setTime(fs.getDepartureDateTime());
                double duration = fs.getFlightDuration();
                int hour = (int) duration;
                int min = (int) (duration % 1 * 60);
                cal.add(Calendar.HOUR_OF_DAY, hour);
                cal.add(Calendar.MINUTE, min);
                cal.add(Calendar.HOUR_OF_DAY, 2 + diff);
                Date date = cal.getTime();
                info.add(new Pair<>(date, fs.getFlightDuration()));
            }
            
            fares2 = new ArrayList<>();
            fares2.add(new Fare("HA", BigDecimal.valueOf(700), CabinClassNameEnum.ECONOMY));
            
            fsp2 = flightSchedulePlanSessionBeanLocal.createNewFlightSchedulePlanMultiple(fsp2, fares2, f2.getFlightId(), info);
            flightSchedulePlanSessionBeanLocal.associateExistingPlanToComplementaryPlan(fsp.getFlightSchedulePlanId(), fsp2.getFlightSchedulePlanId());
        
            // FSP 4
            f1 = flightSessionBeanLocal.retrieveFlightByFlightNumber("ML311");
            pair = new Pair<>(startDateTime, 6.5);
            fsp = new FlightSchedulePlan(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, f1);
            
            fares = new ArrayList<>();
            fares.add(new Fare("HA", BigDecimal.valueOf(3100), CabinClassNameEnum.FIRST));
            fares.add(new Fare("HA", BigDecimal.valueOf(1600), CabinClassNameEnum.BUSINESS));
            fares.add(new Fare("HA", BigDecimal.valueOf(600), CabinClassNameEnum.ECONOMY));
            
            fsp = flightSchedulePlanSessionBeanLocal.createNewFlightSchedulePlanWeekly(fsp, fares, f1.getFlightId(), pair, 2);
            
            f2 = flightSessionBeanLocal.retrieveFlightByFlightNumber("ML612");
            fsp2 = new FlightSchedulePlan(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, f2);
            info = new ArrayList<>();
            diff = f1.getFlightRoute().getDestinationAirport().getGmt() - f1.getFlightRoute().getOriginAirport().getGmt();
            for(FlightSchedule fs : fsp.getFlightSchedule()) {
                Calendar cal = Calendar.getInstance();
                
                cal.setTime(fs.getDepartureDateTime());
                double duration = fs.getFlightDuration();
                int hour = (int) duration;
                int min = (int) (duration % 1 * 60);
                cal.add(Calendar.HOUR_OF_DAY, hour);
                cal.add(Calendar.MINUTE, min);
                cal.add(Calendar.HOUR_OF_DAY, 3 + diff);
                Date date = cal.getTime();
                info.add(new Pair<>(date, fs.getFlightDuration()));
            }
            
            fares2 = new ArrayList<>();
            fares2.add(new Fare("HA", BigDecimal.valueOf(3100), CabinClassNameEnum.FIRST));
            fares2.add(new Fare("HA", BigDecimal.valueOf(1600), CabinClassNameEnum.BUSINESS));
            fares2.add(new Fare("HA", BigDecimal.valueOf(600), CabinClassNameEnum.ECONOMY));
            
            fsp2 = flightSchedulePlanSessionBeanLocal.createNewFlightSchedulePlanMultiple(fsp2, fares2, f2.getFlightId(), info);
            flightSchedulePlanSessionBeanLocal.associateExistingPlanToComplementaryPlan(fsp.getFlightSchedulePlanId(), fsp2.getFlightSchedulePlanId());
            
            // FSP 5
            f1 = flightSessionBeanLocal.retrieveFlightByFlightNumber("ML411");
            pair = new Pair<>(startDateTime, 4.0);
            fsp = new FlightSchedulePlan(ScheduleTypeEnum.RECURRENTDAY, recurrentEnd, f1);
            
            fares = new ArrayList<>();
            fares.add(new Fare("HA", BigDecimal.valueOf(2900), CabinClassNameEnum.FIRST));
            fares.add(new Fare("HA", BigDecimal.valueOf(1400), CabinClassNameEnum.BUSINESS));
            fares.add(new Fare("HA", BigDecimal.valueOf(400), CabinClassNameEnum.ECONOMY));
            
            fsp = flightSchedulePlanSessionBeanLocal.createNewFlightSchedulePlan(fsp, fares, f1.getFlightId(), pair, 2);
            
            f2 = flightSessionBeanLocal.retrieveFlightByFlightNumber("ML412");
            fsp2 = new FlightSchedulePlan(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, f2);
            info = new ArrayList<>();
            diff = f1.getFlightRoute().getDestinationAirport().getGmt() - f1.getFlightRoute().getOriginAirport().getGmt();
            for(FlightSchedule fs : fsp.getFlightSchedule()) {
                Calendar cal = Calendar.getInstance();
                
                cal.setTime(fs.getDepartureDateTime());
                double duration = fs.getFlightDuration();
                int hour = (int) duration;
                int min = (int) (duration % 1 * 60);
                cal.add(Calendar.HOUR_OF_DAY, hour);
                cal.add(Calendar.MINUTE, min);
                cal.add(Calendar.HOUR_OF_DAY, 3 + diff);
                Date date = cal.getTime();
                info.add(new Pair<>(date, fs.getFlightDuration()));
            }
            
            fares2 = new ArrayList<>();
            fares2.add(new Fare("HA", BigDecimal.valueOf(2900), CabinClassNameEnum.FIRST));
            fares2.add(new Fare("HA", BigDecimal.valueOf(1400), CabinClassNameEnum.BUSINESS));
            fares2.add(new Fare("HA", BigDecimal.valueOf(400), CabinClassNameEnum.ECONOMY));
            
            fsp2 = flightSchedulePlanSessionBeanLocal.createNewFlightSchedulePlanMultiple(fsp2, fares2, f2.getFlightId(), info);
            flightSchedulePlanSessionBeanLocal.associateExistingPlanToComplementaryPlan(fsp.getFlightSchedulePlanId(), fsp2.getFlightSchedulePlanId());
            
            // left the mL511
        }
        catch(EmployeeUsernameExistException ex)
        {
            ex.printStackTrace();
        } catch (UnknownPersistenceException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FlightRouteExistException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AircraftConfigNotFoundException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FlightExistException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FlightNotFoundException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FareExistException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FlightSchedulePlanExistException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FlightSchedulePlanNotFoundException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
