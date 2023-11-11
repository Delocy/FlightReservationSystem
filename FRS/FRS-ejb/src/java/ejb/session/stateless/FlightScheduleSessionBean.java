/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.FlightScheduleNotFoundException;

/**
 *
 * @author zares
 */
@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanRemote, FlightScheduleSessionBeanLocal {

    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public FlightSchedule createNewSchedule(FlightSchedule schedule, FlightSchedulePlan plan) {
        em.persist(schedule);
        
        schedule.setFlightSchedulePlan(plan);
        if (!plan.getFlightSchedule().contains(schedule)) {
            plan.getFlightSchedule().add(schedule);
        }

        return schedule;
    }
    
    @Override
    public FlightSchedule retrieveFlightScheduleById(Long flightScheduleID) throws FlightScheduleNotFoundException {
        FlightSchedule schedule = em.find(FlightSchedule.class, flightScheduleID);
        
        if(schedule != null) {
            return schedule;
        } else {
            throw new FlightScheduleNotFoundException("Flight Schedule " + flightScheduleID + " not found!");      
        }
        
    }
}
