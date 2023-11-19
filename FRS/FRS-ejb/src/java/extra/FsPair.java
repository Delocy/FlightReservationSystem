/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package extra;

import entity.FlightSchedule;

/**
 *
 * @author xinni
 */
public class FsPair {
    private FlightSchedule fs1;
    private FlightSchedule fs2;

    public FsPair() {
    }
    
    public FsPair(FlightSchedule fs1, FlightSchedule fs2) {
        this.fs1 = fs1;
        this.fs2 = fs2;
    }

    public FlightSchedule getFs1() {
        return fs1;
    }

    public void setFs1(FlightSchedule fs1) {
        this.fs1 = fs1;
    }

    public FlightSchedule getFs2() {
        return fs2;
    }

    public void setFs2(FlightSchedule fs2) {
        this.fs2 = fs2;
    }
    
    
}
