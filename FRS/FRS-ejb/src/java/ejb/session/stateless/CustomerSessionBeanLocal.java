/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Local
public interface CustomerSessionBeanLocal {
    public Long createNewCustomer(Customer customer) throws UnknownPersistenceException, CustomerUsernameExistException;
    public Customer retrieveCustomerByCustomerId(Long id) throws CustomerNotFoundException;
    public Customer retrieveCustomerByCustomerUsername(String username) throws CustomerNotFoundException;
    public Customer customerLogin(String username, String password) throws InvalidLoginCredentialException;
}
