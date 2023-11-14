/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author zares
 */
public class ItineraryExistException extends Exception {

    /**
     * Creates a new instance of <code>ItineraryExistException</code> without
     * detail message.
     */
    public ItineraryExistException() {
    }

    /**
     * Constructs an instance of <code>ItineraryExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ItineraryExistException(String msg) {
        super(msg);
    }
}
