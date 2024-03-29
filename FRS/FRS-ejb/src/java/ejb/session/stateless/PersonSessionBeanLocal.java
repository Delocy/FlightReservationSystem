/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Person;
import javax.ejb.Local;
import util.exception.PersonNotFoundException;

/**
 *
 * @author zares
 */
@Local
public interface PersonSessionBeanLocal {

    public Person retrievePersonById(Long personId) throws PersonNotFoundException;
    
}
