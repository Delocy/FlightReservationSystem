/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author zares
 */
public class SeatInventoryNotFoundException extends Exception{

    /**
     * Creates a new instance of <code>SeatInventoryNotFoundException</code>
     * without detail message.
     */
    public SeatInventoryNotFoundException() {
    }

    /**
     * Constructs an instance of <code>SeatInventoryNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SeatInventoryNotFoundException(String msg) {
        super(msg);
    }
}
