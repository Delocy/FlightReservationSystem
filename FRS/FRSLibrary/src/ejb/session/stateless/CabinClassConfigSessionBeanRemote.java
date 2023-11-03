/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClassConfig;
import javax.ejb.Remote;
import util.enumeration.CabinClassNameEnum;
import util.exception.CabinClassNameNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Remote
public interface CabinClassConfigSessionBeanRemote {
    public CabinClassNameEnum fetchCabinClassNameEnum(String s) throws CabinClassNameNotFoundException;

    public Long createCabinClass(CabinClassConfig c) throws UnknownPersistenceException;
}
