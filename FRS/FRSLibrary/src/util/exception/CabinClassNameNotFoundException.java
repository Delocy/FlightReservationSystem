/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author xinni
 */
public class CabinClassNameNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>CabinClassNameNotFoundException</code>
     * without detail message.
     */
    public CabinClassNameNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CabinClassNameNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CabinClassNameNotFoundException(String msg) {
        super(msg);
    }
}
