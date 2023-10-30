/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.Employee;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zares
 */
@Singleton
@LocalBean
@Startup

public class DataInitializationSessionBean {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public DataInitializationSessionBean()
    {
    }
    
    @PostConstruct
    public void postConstruct()
    {
        try
        {
            employeeSessionBeanLocal.retrieveEmployeeByUsername("systemadmin");
        }
        catch(EmployeeNotFoundException ex)
        {
            initializeData();
        }
    }
    
    private void initializeData()
    {
        try
        {
            employeeSessionBeanLocal.createNewEmployee(new Employee("SystemAdmin", "Default", EmployeeAccessRightEnum.SYSTEMADMIN, "systemadmin", "password"));
//            employeeSessionBeanLocal.createNewEmployee(new Employee("FleetManager", "Default", EmployeeAccessRightEnum.FLEETMANAGER, "fleetmanager", "password"));
//            employeeSessionBeanLocal.createNewEmployee(new Employee("RoutePlanner", "Default", EmployeeAccessRightEnum.ROUTEPLANNER, "routeplanner", "password"));
//            employeeSessionBeanLocal.createNewEmployee(new Employee("ScheduleManager", "Default", EmployeeAccessRightEnum.SCHEDULEMANAGER, "schedulemanager", "password"));
//            employeeSessionBeanLocal.createNewEmployee(new Employee("SalesManager", "Default", EmployeeAccessRightEnum.SALESMANAGER, "salesmanager", "password"));
        }
        catch(EmployeeUsernameExistException ex)
        {
            ex.printStackTrace();
        }
    }
}
