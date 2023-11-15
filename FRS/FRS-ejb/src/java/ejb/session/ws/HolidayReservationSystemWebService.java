/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.Partner;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author xinni
 */
@WebService(serviceName = "HolidayReservationSystemWebService")
@Stateless()
public class HolidayReservationSystemWebService {

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    @WebMethod(operationName = "doLogin")
    public Long doLogin(@WebParam(name = "username") String username,
                        @WebParam(name = "password") String password) throws InvalidLoginCredentialException {
        return partnerSessionBeanLocal.partnerLogin(username, password);
    }
}
