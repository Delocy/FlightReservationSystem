/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfig;
import entity.Employee;
import entity.Flight;
import entity.FlightRoute;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import util.exception.AircraftConfigNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightNumberExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zares
 */
@Stateless
public class FlightSessionBean implements FlightSessionBeanRemote, FlightSessionBeanLocal {

    @EJB
    private FlightRouteSessionBeanLocal flightRouteSessionBeanLocal;

    @EJB
    private AircraftConfigSessionBeanLocal aircraftConfigSessionBeanLocal;

    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;
   
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long createNewFlight(Flight newFlight, Long configId, Long routeId) throws FlightExistException, UnknownPersistenceException, FlightRouteNotFoundException, AircraftConfigNotFoundException {
   
         try { 
            
            
            FlightRoute route = flightRouteSessionBeanLocal.retrieveFlightRouteByRouteID(routeId);
            List<Flight> flightsInRoute = route.getFlights();
            
            AircraftConfig config = aircraftConfigSessionBeanLocal.retrieveAircraftConfigById(configId);
            List<Flight> flightsInConfig = config.getFlights();  
            
            flightsInRoute.add(newFlight);
            route.setFlights(flightsInRoute);
            
            flightsInConfig.add(newFlight);
            config.setFlights(flightsInConfig);
            
            em.persist(newFlight);
            
            newFlight.setAircraftConfig(config);
            newFlight.setFlightRoute(route);
            
            em.flush();

        } catch (PersistenceException ex) {              
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                     throw new FlightExistException("Flight " + newFlight.getFlightNumber() + " already exist");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }                          
        }
           
        return newFlight.getFlightId();
        
    }
    
    
    @Override
    public void associateOriginalFlightWithReturnFlight(Long originalFlightID, Long returnFlightID) throws FlightNotFoundException {
        Flight originalFlight = retrieveFlightByFlightID(originalFlightID);
        Flight returnFlight = retrieveFlightByFlightID(returnFlightID);

        // Bidirection association
        originalFlight.setReturningFlight(returnFlight);
        returnFlight.setOriginalFlight(originalFlight);
    }
    
    @Override
    public Flight retrieveFlightByFlightNumber(String flightNo) throws FlightNotFoundException {
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :inFlightnumber");
        query.setParameter("inFlightnumber", "ML" + flightNo);
        
        try
        {
            return (Flight)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new FlightNotFoundException("Flight " + flightNo + " does not exist!");
        }
    }
    
    @Override
    public Flight retrieveFlightByFlightID(Long id) throws FlightNotFoundException {
        Flight flight = em.find(Flight.class, id);
        
        if(flight != null) {
            return flight;
        } else {
            throw new FlightNotFoundException("Flight " + id + " not found!");
        }
    }
     
    @Override
    public List<Flight> viewAllFlights() {
        Query query = em.createQuery("SELECT f FROM Flight f ORDER BY f.flightNumber ASC");
        List<Flight> allFlights = query.getResultList();
        
        List<Flight> orderedFlights = new ArrayList<>();
        
        for (Flight flight : allFlights) {
           if (!orderedFlights.contains(flight)) {
               orderedFlights.add(flight);
               Flight compFlight = flight.getReturningFlight();
               if (compFlight != null) {
                   orderedFlights.add(compFlight);
               }  
           }
        }
        return orderedFlights;
    }
    
    @Override
    public void updateFlight(Long existingFlightID, Flight newFlight) {
        try {
            Flight existingFlight = retrieveFlightByFlightID(existingFlightID);
            
            FlightRoute existingRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByRouteID(existingFlight.getFlightRoute().getFlightRouteId());
            
            FlightRoute newRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByRouteID(newFlight.getFlightRoute().getFlightRouteId());
            
            if (!existingRoute.getOriginAirport().equals(newRoute.getOriginAirport()) || !existingRoute.getDestinationAirport().equals(newRoute.getDestinationAirport()) ) {
                if (existingFlight.getReturningFlight() != null) {
                    existingFlight.getReturningFlight().setOriginalFlight(null);
                    existingFlight.setReturningFlight(null);
                    existingRoute.getFlights().remove(existingFlight);
                }
                existingFlight.setFlightRoute(newRoute);
                newRoute.getFlights().add(existingFlight);
            }
            
            AircraftConfig existingConfig = aircraftConfigSessionBeanLocal.retrieveAircraftConfigById(existingFlight.getAircraftConfig().getAircraftConfigId());
                    
            AircraftConfig newConfig = aircraftConfigSessionBeanLocal.retrieveAircraftConfigById(newFlight.getAircraftConfig().getAircraftConfigId());
            
            if(!existingConfig.equals(newConfig)) {
                existingConfig.getFlights().remove(existingFlight);
                
                existingFlight.setAircraftConfig(newConfig);
                newConfig.getFlights().add(existingFlight);
            }
            em.flush();
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(FlightSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FlightNotFoundException ex) {
            Logger.getLogger(FlightSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AircraftConfigNotFoundException ex) {
            Logger.getLogger(FlightSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void deleteFlight(Long flightId) {
        try {
            Flight flight = retrieveFlightByFlightID(flightId);
            if (flight.getFlightSchedulePlan().isEmpty()) {
                if (flight.getReturningFlight() != null) {
                    flight.getReturningFlight().setOriginalFlight(null);
                    flight.setReturningFlight(null);
                }
                FlightRoute route = flight.getFlightRoute();
                List<Flight> flightsInRoute = route.getFlights();
                flightsInRoute.remove(flight);
                route.setFlights(flightsInRoute);

                AircraftConfig config = flight.getAircraftConfig();
                List<Flight> flightsInConfig = config.getFlights();
                flightsInConfig.remove(flight);
                config.setFlights(flightsInConfig);
                em.remove(flight);
            } else {
                flight.setIsDisabled(true);
            }
        } catch (FlightNotFoundException ex) {
            System.out.println("Flight with flight ID " + flightId + " does not exist");
        }
        
    }
    
    @Override
    public List<Flight> retrieveFlightsByFlightRoute(String origin, String destination) throws FlightNotFoundException {
        Query query = em.createQuery("SELECT f FROM Flight f "
            + "WHERE f.flightRoute.originAirport.airportCode = :inOrigin "
            + "AND f.flightRoute.destinationAirport.airportCode = :inDestination "
            + "AND f.disabled = false "
            + "ORDER BY SUBSTRING(f.flightNum, 3) ASC");
        query.setParameter("inOrigin", origin);
        query.setParameter("inDestination", destination);
        
        List<Flight> result = query.getResultList();
        if (result != null) {
            return result;
        } else {
            throw new FlightNotFoundException("Flights from " + origin + " to " + destination + " does not exist!");
        }
    }
    
    @Override
    public List<Flight[]> retrieveConnectingFlightsByFlightRoute(String origin, String destination) throws FlightNotFoundException {
        Query query = em.createQuery("SELECT f1, f2 FROM Flight f1, Flight f2 "
            + "WHERE f1.flightRoute.originAirport.airportCode = :inOrigin "
            + "AND f2.flightRoute.destinationAirport.airportCode = :inDestination "
            + "AND f1.flightRoute.destinationAirport.airportCode = f2.flightRoute.originAirport.airportCode "
            + "AND f1.disabled = false "
            + "AND f2.disabled = false "
            + "ORDER BY SUBSTRING(f.flightNum, 3) ASC");
        query.setParameter("inOrigin", origin);
        query.setParameter("inDestination", destination);
        
        List<Flight[]> result = query.getResultList();
        if (result != null) {
            return result;
        } else {
            throw new FlightNotFoundException("Flights from " + origin + " to " + destination + " does not exist!");
        }
    }
}
