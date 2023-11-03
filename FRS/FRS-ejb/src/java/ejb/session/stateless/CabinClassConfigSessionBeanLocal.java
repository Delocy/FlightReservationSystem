/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClassConfig;
import javax.ejb.Local;
import util.enumeration.CabinClassNameEnum;
import util.exception.CabinClassNameNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Local
public interface CabinClassConfigSessionBeanLocal {

    public CabinClassNameEnum fetchCabinClassNameEnum(String s) throws CabinClassNameNotFoundException;

    public Long createCabinClass(CabinClassConfig c) throws UnknownPersistenceException;
    
}
