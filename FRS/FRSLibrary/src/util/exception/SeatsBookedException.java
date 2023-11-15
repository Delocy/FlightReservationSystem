/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author zares
 */
public class SeatsBookedException extends Exception {

    /**
     * Creates a new instance of <code>SeatsBookedException</code> without
     * detail message.
     */
    public SeatsBookedException() {
    }

    /**
     * Constructs an instance of <code>SeatsBookedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public SeatsBookedException(String msg) {
        super(msg);
    }
}
