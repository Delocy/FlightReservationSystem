/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author zares
 */
public class FlightNumberExistException extends Exception {

    /**
     * Creates a new instance of <code>FlightNumberExistException</code> without
     * detail message.
     */
    public FlightNumberExistException() {
    }

    /**
     * Constructs an instance of <code>FlightNumberExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightNumberExistException(String msg) {
        super(msg);
    }
}
