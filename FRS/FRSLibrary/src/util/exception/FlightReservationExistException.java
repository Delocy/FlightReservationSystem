/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author zares
 */
public class FlightReservationExistException extends Exception {

    /**
     * Creates a new instance of <code>FlightReservationExistException</code>
     * without detail message.
     */
    public FlightReservationExistException() {
    }

    /**
     * Constructs an instance of <code>FlightReservationExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightReservationExistException(String msg) {
        super(msg);
    }
}
