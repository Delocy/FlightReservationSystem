/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Person;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.PersonNotFoundException;

/**
 *
 * @author zares
 */
@Stateless
public class PersonSessionBean implements PersonSessionBeanRemote, PersonSessionBeanLocal {

    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Person retrievePersonById(Long personId) throws PersonNotFoundException {
        Person person = em.find(Person.class, personId);
        
        if(person != null) {
            return person;
        } else {
            throw new PersonNotFoundException("User with " + personId + " does not exist!");
        }
    }
}
