/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import util.exception.AirportNotFoundException;
import util.exception.FlightRouteExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zares
 */
@Stateless
public class FlightRouteSessionBean implements FlightRouteSessionBeanRemote, FlightRouteSessionBeanLocal {

    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;
    
    private AirportSessionBeanLocal airportSessionBeanLocal;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public FlightRoute createFlightRoute(Long originAirportID, Long destinationAirportID) throws AirportNotFoundException, FlightRouteExistException, UnknownPersistenceException {
        try {
            
            Airport originAirport = airportSessionBeanLocal.retrieveAirportByAirportId(originAirportID);
            Airport destinationAirport = airportSessionBeanLocal.retrieveAirportByAirportId(destinationAirportID);

            FlightRoute route = new FlightRoute();
            route.setOriginAirport(originAirport);
            route.setDestinationAirport(destinationAirport);

            em.persist(route);
            
            return route;
        } catch (AirportNotFoundException ex) {
            throw new AirportNotFoundException("Airport does not exist in system");
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new FlightRouteExistException("This flight route already exists");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    } 
    
    
    @Override
    public List<FlightRoute> viewAllFlightRoutes() {
        // we sorted it by airport id
        TypedQuery<FlightRoute> query = em.createQuery("SELECT fr FROM FlightRoute fr ORDER BY fr.originairport.airportname ASC", FlightRoute.class);
        List<FlightRoute> allFlightRoutes = query.getResultList();

        List<FlightRoute> orderedFlightRoutes = new ArrayList<>();
        for (FlightRoute route : allFlightRoutes) {
            if (!orderedFlightRoutes.contains(route)) {
                orderedFlightRoutes.add(route);

                FlightRoute complementaryReturnRoute = null;

                for (FlightRoute routeReturn : allFlightRoutes) {
                    if (routeReturn.getOriginAirport().equals(route.getDestinationAirport()) && routeReturn.getDestinationAirport().equals(route.getOriginAirport())) {
                        complementaryReturnRoute = routeReturn;
                    }
                }
                if (complementaryReturnRoute != null) {
                    orderedFlightRoutes.add(complementaryReturnRoute);
                }
            }
        }
        return orderedFlightRoutes;
    }

}
