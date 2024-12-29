package client.serverCommunication.decodeCMD;


import client.serverCommunication.Format;
import models.Employee;
import services.EmployeeManager;

import java.sql.SQLException;
import java.util.List;

public class DecodeExecuteCommandEmployee {
    public static String execute(String command) throws SQLException {
        EmployeeManager EMP_mgr = new EmployeeManager();
        Employee emp;
        int id;
        String response = Format.encodeSuccessMessage();
        switch (Format.getMethod(command)) {
            //public static String createNewEmployee(Employee emp)
            case "createNewEmployee":
                emp = new Employee(Format.getFirstParam(command));
                EMP_mgr.addEmployee(emp);
                break;
            //public static String updateEmployee(Employee emp)
            case "updateEmployee":
                emp = new Employee(Format.getFirstParam(command));
                EMP_mgr.updateEmployee(emp);
                break;
            //public static String deleteEmployee(int id)
            case "deleteEmployee":
                id = Integer.parseInt(Format.getFirstParam(command));
                EMP_mgr.deleteEmployee(id);
                break;
            //public static String getEmployeeByID(int id)
            case "getEmployeeByID":
                id = Integer.parseInt(Format.getFirstParam(command));
                emp = EMP_mgr.findEmployeeById(id);
                if(emp == null)
                    response = Format.encodeEmpty("");
                else
                    response = emp.serializeToString();
                break;
            case "getEmployees":
                //public List<Employee> getEmployees() {
                List<Employee> employees = EMP_mgr.getAllEmployees();
                if( employees.size() == 0)
                    response = Format.encodeEmpty("");
                else
                    response = Format.encodeEmployees(employees);
                break;
            //public static String getEmployeesByBranch(String branch)
            case "getEmployeesByBranch":
                String branch = Format.getFirstParam(command);
                List<Employee> empList = EMP_mgr.getEmployeesByBranch(branch);
                if(empList.size() == 0)
                    response = Format.encodeEmpty("");
                else
                    response = Format.encodeEmployees(empList);
                break;
            //public static String Login(String username, String password)
            case "Login":
                String username = Format.getFirstParam(command);
                String password = Format.getSecondParam(command);
                response =  EMP_mgr.login(username, password);
                break;
        }
        return response;
    }

}
