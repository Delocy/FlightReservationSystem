/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author xinni
 */
public class CustomerUsernameExistException extends Exception {

    /**
     * Creates a new instance of <code>CustomerUsernameExistException</code>
     * without detail message.
     */
    public CustomerUsernameExistException() {
    }

    /**
     * Constructs an instance of <code>CustomerUsernameExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomerUsernameExistException(String msg) {
        super(msg);
    }
}
