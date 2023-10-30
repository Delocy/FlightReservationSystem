/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author zares
 */
public class EmployeeUsernameExistException extends Exception {

    /**
     * Creates a new instance of <code>EmployeeUsernameExistException</code>
     * without detail message.
     */
    public EmployeeUsernameExistException() {
    }

    /**
     * Constructs an instance of <code>EmployeeUsernameExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public EmployeeUsernameExistException(String msg) {
        super(msg);
    }
}
