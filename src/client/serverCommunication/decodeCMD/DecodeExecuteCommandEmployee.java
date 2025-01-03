package client.serverCommunication.decodeCMD;


import client.serverCommunication.Format;
import models.Employee;
import server.services.AdminManager;
import server.services.EmployeeManager;

import java.sql.SQLException;
import java.util.List;

public class DecodeExecuteCommandEmployee {
    public static String execute(String command) throws SQLException {
        AdminManager manager = new AdminManager();
        Employee emp;
        int id;
        String response = Format.encodeSuccessMessage();
        switch (Format.getMethod(command)) {
            //public static String createNewEmployee(Employee emp)
            case "createNewEmployee":
                emp = new Employee(Format.getFirstParam(command));
                manager.addEmployee(emp);
                break;
            //public static String updateEmployee(Employee emp)
            case "updateEmployee":
                emp = new Employee(Format.getFirstParam(command));
                manager.updateEmployee(emp);
                break;
            //public static String deleteEmployee(int id)
            case "deleteEmployee":
                id = Integer.parseInt(Format.getFirstParam(command));
                manager.deleteEmployee(id);
                break;
            //public static String getEmployeeByID(int id)
            case "getEmployeeByID":
                id = Integer.parseInt(Format.getFirstParam(command));
                emp = manager.findEmployeeById(id);
                if(emp == null)
                    response = Format.encodeEmpty("");
                else
                    response = emp.serializeToString();
                break;
            case "getEmployees":
                //public List<Employee> getEmployees() {
                List<Employee> employees = manager.getAllEmployees();
                if(employees.isEmpty())
                    response = Format.encodeEmpty("");
                else
                    response = Format.encodeEmployees(employees);
                break;
            //public static String getEmployeesByBranch(String branch)
            case "getEmployeesByBranch":
                String branch = Format.getFirstParam(command);
                List<Employee> empList = manager.getEmployeesByBranch(branch);
                if(empList.isEmpty())
                    response = Format.encodeEmpty("");
                else
                    response = Format.encodeEmployees(empList);
                break;
            //public static String Login(String uid, String password)
            case "Login":
                int uid = Integer.parseInt(Format.getFirstParam(command));
                String password = Format.getSecondParam(command);
                response = manager.login(uid, password).serializeToString();
                break;
        }
        return response;
    }

}
