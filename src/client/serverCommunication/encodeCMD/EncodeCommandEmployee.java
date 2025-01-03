package client.serverCommunication.encodeCMD;


import client.serverCommunication.ClassType;
import client.serverCommunication.Format;
import server.models.Employee;

public class EncodeCommandEmployee {
    public static String createNewEmployee(Employee emp)
    {
        return Format.encode(ClassType.EMPLOYEE, "createNewEmployee", emp.serializeToString());
    }
    public static String updateEmployee(Employee emp)
    {
        return Format.encode(ClassType.EMPLOYEE, "updateEmployee", emp.serializeToString());
    }
    public static String deleteEmployee(int id)
    {
        return Format.encode(ClassType.EMPLOYEE, "deleteEmployee", Integer.toString(id));
    }
    public static String getEmployeeByID(int id)
    {
        return Format.encode(ClassType.EMPLOYEE, "getEmployeeByID", Integer.toString(id));
    }
    public static String getEmployees()
    {
        return Format.encode(ClassType.EMPLOYEE, "getEmployees");
    }
    public static String getEmployeesByBranch(String branch)
    {
        return Format.encode(ClassType.EMPLOYEE, "getEmployeesByBranch", branch);
    }
    public static String Login(String username, String password)
    {
        return Format.encode(ClassType.EMPLOYEE, "Login", username, password);
    }

}
