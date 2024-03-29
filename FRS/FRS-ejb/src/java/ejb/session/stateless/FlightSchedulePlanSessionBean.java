/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClassConfig;
import entity.Fare;
import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.SeatInventory;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.FareExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.FlightSchedulePlanExistException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFlightScheduleException;

/**
 *
 * @author zares
 */
@Stateless
public class FlightSchedulePlanSessionBean implements FlightSchedulePlanSessionBeanRemote, FlightSchedulePlanSessionBeanLocal {

    @EJB
    private FlightSessionBeanLocal flightSessionBeanLocal;

    @EJB
    private FareSessionBeanLocal fareSessionBeanLocal;

    @EJB
    private SeatInventorySessionBeanLocal seatInventorySessionBeanLocal;

    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBeanLocal;
    
    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;
    
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
   
    @Override
    public FlightSchedulePlan retrieveFlightSchedulePlanById(Long flightSchedulePlanID) throws FlightSchedulePlanNotFoundException {
        FlightSchedulePlan plan = em.find(FlightSchedulePlan.class, flightSchedulePlanID);
        
        if (plan != null && plan.isDisabled() == false) {
            return plan;
        } else {
            throw new FlightSchedulePlanNotFoundException("Flight Schedule Plan ID " + flightSchedulePlanID.toString() + " does not exist!" );
        }
    }
    
    @Override
    public FlightSchedulePlan createNewFlightSchedulePlan(FlightSchedulePlan plan, List<Fare> fares, long flightID, Pair<Date, Double> info, int recurrentDays) throws FareExistException, UnknownPersistenceException, FlightSchedulePlanExistException, FlightNotFoundException {
        try {
            em.persist(plan);

            if (recurrentDays == 0) {
                FlightSchedule schedule = new FlightSchedule(info.getKey(), info.getValue());
                flightScheduleSessionBeanLocal.createNewSchedule(schedule, plan);
            } else {
                Date currentDate = info.getKey();
                Date endDate = plan.getRecurrentEndDate();
                while(endDate.compareTo(currentDate) > 0) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(currentDate);

                    FlightSchedule scheduleNew = new FlightSchedule(c.getTime(), info.getValue());
                    scheduleNew = flightScheduleSessionBeanLocal.createNewSchedule(scheduleNew, plan);
                    c.add(Calendar.DAY_OF_MONTH, recurrentDays);         
                    currentDate = c.getTime();                 
                }
            }

            associateFlightToPlan(flightID, plan);
            
            for (FlightSchedule fs: plan.getFlightSchedule()) {               
                for (CabinClassConfig cc: plan.getFlight().getAircraftConfig().getCabinClassConfig()) {                    
                    SeatInventory seats = new SeatInventory(cc.getMaxSeatCapacity(), 0 , cc.getMaxSeatCapacity());                       
                    seatInventorySessionBeanLocal.createSeatInventory(seats, fs, cc);
                }
            }

            for (Fare fare: fares) {
                fareSessionBeanLocal.createFare(fare, plan);
            }

            
            em.flush();
            return plan;
        } catch(PersistenceException ex) {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new FlightSchedulePlanExistException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } catch (FlightNotFoundException ex) {
            throw new FlightNotFoundException(ex.getMessage());
        }
    }
    
    
    private void associateFlightToPlan(Long flightID, FlightSchedulePlan flightSchedulePlan) throws FlightNotFoundException, FlightSchedulePlanExistException {

        Flight flight = flightSessionBeanLocal.retrieveFlightByFlightID(flightID);  
        for (FlightSchedulePlan fsp: flight.getFlightSchedulePlan()) {
            for (FlightSchedule fs: fsp.getFlightSchedule()) {
                Date start1 = fs.getDepartureDateTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start1);
                double duration = fs.getFlightDuration();
                int hour = (int) duration;
                int min = (int) (duration % 1 * 60);
                calendar.add(Calendar.HOUR_OF_DAY, hour);
                calendar.add(Calendar.MINUTE, min);
                Date end1 = calendar.getTime();
                for (FlightSchedule fs2: flightSchedulePlan.getFlightSchedule()) {
                    Date start2 = fs2.getDepartureDateTime();
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(start2);
                    double duration2 = fs2.getFlightDuration();
                    int hour2 = (int) duration2;
                    int min2 = (int) (duration2 % 1 * 60);
                    calendar2.add(Calendar.HOUR_OF_DAY, hour2);
                    calendar2.add(Calendar.MINUTE, min2);
                    Date end2 = calendar2.getTime();
                    
                    if (isOverlapping(start1, end1, start2, end2)) {
                        System.out.println("calling one");
                        throw new FlightSchedulePlanExistException("Flight schedule overlaps with existing flight schedules");
                    }
                }
            }
        }
        
       
        List<FlightSchedule> fs = flightSchedulePlan.getFlightSchedule();
        System.out.println("check: " + fs.size());
        for (int i = 0; i < fs.size(); i++) {
            Date start1 = fs.get(i).getDepartureDateTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start1);
            double duration = fs.get(i).getFlightDuration();
            int hour = (int) duration;
            int min = (int) (duration % 1 * 60);
            calendar.add(Calendar.HOUR_OF_DAY, hour);
            calendar.add(Calendar.MINUTE, min);
            Date end1 = calendar.getTime();
            for (int j = i+1; j < fs.size(); j++) {
                Date start2 = fs.get(j).getDepartureDateTime();
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(start2);
                double duration2 = fs.get(j).getFlightDuration();
                int hour2 = (int) duration2;
                int min2 = (int) (duration2 % 1 * 60);
                calendar2.add(Calendar.HOUR_OF_DAY, hour2);
                calendar2.add(Calendar.MINUTE, min2);
                Date end2 = calendar2.getTime();
                 
                if (isOverlapping(start1, end1, start2, end2)) {
                    System.out.println("calling two");
                    throw new FlightSchedulePlanExistException("Flight schedule overlaps");
                 }
            }
        }
        
        flight.getFlightSchedulePlan().add(flightSchedulePlan);
        flightSchedulePlan.setFlight(flight);

    }

    private boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && start2.before(end1);
    }
    
    @Override
    public FlightSchedulePlan createNewFlightSchedulePlanMultiple(FlightSchedulePlan plan, List<Fare> fares, long flightID, List<Pair<Date, Double>> info) throws FareExistException, UnknownPersistenceException, FlightNotFoundException, FlightSchedulePlanExistException {
        
        
            try {
                em.persist(plan);
                
                int size = info.size();
                for(int i = 0; i < size; i++) {
                    FlightSchedule schedule = new FlightSchedule(info.get(i).getKey(), info.get(i).getValue());
                    flightScheduleSessionBeanLocal.createNewSchedule(schedule, plan);
              
                }
                
                associateFlightToPlan(flightID, plan);
                
                for (FlightSchedule fse: plan.getFlightSchedule()) {               
                    for (CabinClassConfig cc: plan.getFlight().getAircraftConfig().getCabinClassConfig()) {                    
                        SeatInventory seats = new SeatInventory(cc.getMaxSeatCapacity(), 0 , cc.getMaxSeatCapacity());      
                        seatInventorySessionBeanLocal.createSeatInventory(seats, fse, cc);
                    }
                }
                
                for (Fare fare: fares) {
                    fareSessionBeanLocal.createFare(fare, plan);
                }
                            
                em.flush();
                return plan;
            } catch (PersistenceException ex) {
               
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new FlightSchedulePlanExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            
            }
        }
    
    @Override
    public FlightSchedulePlan createNewFlightSchedulePlanWeekly(FlightSchedulePlan plan, List<Fare> fares, long flightID, Pair<Date, Double> pair, int recurrent) throws FareExistException, UnknownPersistenceException, FlightNotFoundException, FlightSchedulePlanExistException {
        
            try{
                em.persist(plan);
                
                    Date presentDate = pair.getKey();
                    Date endDate = plan.getRecurrentEndDate();
                    
                    
                    Calendar cal = Calendar.getInstance();
                    
                    
                    cal.setTime(presentDate);
                    FlightSchedule  schedule = new FlightSchedule(cal.getTime(), pair.getValue());
//                  
                    
                    boolean correctDay = false;
                    while(cal.get(Calendar.DAY_OF_WEEK) != recurrent) {
                        cal.add(Calendar.DATE, 1);
                        correctDay = true;
                    }       
            
                    if (correctDay) {
                        schedule = new FlightSchedule(cal.getTime(), pair.getValue());
//                      
                        flightScheduleSessionBeanLocal.createNewSchedule(schedule, plan);      
                    }
                    
                    cal.add(Calendar.DAY_OF_MONTH, 7);
                  
                    while(endDate.compareTo(cal.getTime()) > 0) {            
                        
                        schedule = new FlightSchedule(cal.getTime(), pair.getValue());
//                       
                        flightScheduleSessionBeanLocal.createNewSchedule(schedule, plan);
                        cal.add(Calendar.DAY_OF_MONTH, 7);         
                                      
                    }
                
                
                associateFlightToPlan(flightID, plan);
               
                 
                for (FlightSchedule fse: plan.getFlightSchedule()) {               
                    for (CabinClassConfig cc: plan.getFlight().getAircraftConfig().getCabinClassConfig()) {                    
                        SeatInventory seats = new SeatInventory(cc.getMaxSeatCapacity(), 0 , cc.getMaxSeatCapacity());
//                 
                        seatInventorySessionBeanLocal.createSeatInventory(seats, fse, cc);
                    }
                }
                
                for (Fare fare: fares) {
//                 
                    fareSessionBeanLocal.createFare(fare, plan);
                }
                
                em.flush();
                return plan;
            } catch(PersistenceException ex) {
                //eJBContext.setRollbackOnly();
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new FlightSchedulePlanExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
        }
    }
    
    public void associateExistingPlanToComplementaryPlan(Long sourcFlightSchedulePlanID, Long returnFlightSchedulePlanID) throws FlightSchedulePlanNotFoundException {
        FlightSchedulePlan original = retrieveFlightSchedulePlanById(sourcFlightSchedulePlanID);
        FlightSchedulePlan complementary = retrieveFlightSchedulePlanById(returnFlightSchedulePlanID);
        
        // Bidirectional association
        original.setReturnSchedulePlan(complementary);
        complementary.setOriginalSchedulePlan(original);
        
    }
    
    @Override
    public List<FlightSchedulePlan> retrieveAllFlightSchedulePlan() throws FlightSchedulePlanNotFoundException {
        Query query = em.createQuery("SELECT DISTINCT p FROM FlightSchedule f, FlightSchedulePlan p WHERE f.flightSchedulePlan.flightSchedulePlanId = p.flightSchedulePlanId AND p.disabled = false ORDER BY p.flightNumber ASC, f.departureDateTime DESC");  
        List<FlightSchedulePlan> result = query.getResultList();
        if (result.isEmpty()) {
            throw new FlightSchedulePlanNotFoundException("No flight schedule plans found");
        }
        List<FlightSchedulePlan> fResult = new ArrayList<>();
        for (FlightSchedulePlan p : result) {
            if (!fResult.contains(p)) {
                fResult.add(p);
                if (p.getReturnSchedulePlan()!= null) {
                    for (FlightSchedulePlan r : result) {
                        if (p.getReturnSchedulePlan().equals(r)) {
                            fResult.add(r);
                        }
                    }
                }
            }
        }
        return fResult;
    }
    
    @Override
    public void deleteFlightSchedulePlan(Long planID) throws FlightSchedulePlanNotFoundException {
        FlightSchedulePlan plan = retrieveFlightSchedulePlanById(planID);
        
        boolean haveReservation = false;
        for (FlightSchedule schedule : plan.getFlightSchedule()) {
            if (!schedule.getReservations().isEmpty()) {
                haveReservation = true;
                break;
            }
        }
 
        if(haveReservation == false) {
            flightScheduleSessionBeanLocal.deleteFlightSchedule(plan.getFlightSchedule());
            
            plan.getFlight().getFlightSchedulePlan().remove(plan);
            
            fareSessionBeanLocal.deleteFares(plan.getFares());
            
            if (plan.getOriginalSchedulePlan() != null) {
                plan.getOriginalSchedulePlan().setReturnSchedulePlan(null);
                plan.setOriginalSchedulePlan(null);
            }
            if (plan.getReturnSchedulePlan() != null) {
                plan.getReturnSchedulePlan().setOriginalSchedulePlan(null);
                plan.setReturnSchedulePlan(null);
            }
                       
            em.remove(plan);
            
        } else {
            plan.setDisabled(true);
        }
    }

}
