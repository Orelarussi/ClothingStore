package client.serverCommunication.decodeCMD;

import Store.Database.EmployeeDAO;
import Store.Employees.Employee;

import java.sql.SQLException;
import java.util.List;

public class DecodeExecuteCommandEmployee {
    public static String execute(String command) throws SQLException {
        EmployeeDAO DAO = new EmployeeDAO();
        Employee emp;
        int id;
        String response = Format.encodeSuccessMessage();
        switch (Format.getMethod(command)) {
            //public static String createNewEmployee(Employee emp)
            case "createNewEmployee":
                emp = Employee.deserializeFromString(Format.getFirstParam(command));
                DAO.createNewEmployee(emp);
                break;
            //public static String updateEmployee(Employee emp)
            case "updateEmployee":
                emp = Employee.deserializeFromString(Format.getFirstParam(command));
                DAO.updateEmployee(emp);
                break;
            //public static String deleteEmployee(int id)
            case "deleteEmployee":
                id = Integer.parseInt(Format.getFirstParam(command));
                DAO.deleteEmployee(id);
                break;
            //public static String getEmployeeByID(int id)
            case "getEmployeeByID":
                id = Integer.parseInt(Format.getFirstParam(command));
                emp = DAO.getEmployeeByID(id);
                if(emp == null)
                    response = Format.encodeEmpty("");
                else
                    response = emp.serializeToString();
                break;
            case "getEmployees":
                //public List<Employee> getEmployees() {
                List<Employee> employees = DAO.getEmployees();
                if( employees.size() == 0)
                    response = Format.encodeEmpty("");
                else
                    response = Format.encodeEmployees(employees);
                break;
            //public static String getEmployeesByBranch(String branch)
            case "getEmployeesByBranch":
                String branch = Format.getFirstParam(command);
                List<Employee> empList = DAO.getEmployeesByBranch(branch);
                if(empList.size() == 0)
                    response = Format.encodeEmpty("");
                else
                    response = Format.encodeEmployees(empList);
                break;
            //public static String Login(String username, String password)
            case "Login":
                String username = Format.getFirstParam(command);
                String password = Format.getSecondParam(command);
                response =  DAO.Login(username, password);
                break;
        }
        return response;
    }

}
