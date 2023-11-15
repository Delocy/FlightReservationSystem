/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Remote;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Remote
public interface PartnerSessionBeanRemote {
    public Long createNewPartner(Partner partner) throws UnknownPersistenceException;

    public Long partnerLogin(String username, String password) throws InvalidLoginCredentialException;

    public Partner retrievePartnerByPartnerUsername(String username) throws PartnerNotFoundException;

}
