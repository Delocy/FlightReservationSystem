/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import javax.ejb.Remote;
import util.exception.FlightScheduleNotFoundException;

/**
 *
 * @author zares
 */
@Remote
public interface FlightScheduleSessionBeanRemote {
    public FlightSchedule retrieveFlightScheduleById(Long flightScheduleID) throws FlightScheduleNotFoundException;
    
    public FlightSchedule createNewSchedule(FlightSchedule schedule, FlightSchedulePlan plan);
}
