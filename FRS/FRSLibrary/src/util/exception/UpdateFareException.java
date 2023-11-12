/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author zares
 */
public class UpdateFareException extends Exception {

    /**
     * Creates a new instance of <code>UpdateFareException</code> without detail
     * message.
     */
    public UpdateFareException() {
    }

    /**
     * Constructs an instance of <code>UpdateFareException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateFareException(String msg) {
        super(msg);
    }
}
