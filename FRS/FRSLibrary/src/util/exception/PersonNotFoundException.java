/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author zares
 */
public class PersonNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>PersonNotFoundException</code> without
     * detail message.
     */
    public PersonNotFoundException() {
    }

    /**
     * Constructs an instance of <code>PersonNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PersonNotFoundException(String msg) {
        super(msg);
    }
}
