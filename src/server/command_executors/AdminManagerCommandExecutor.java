package server.command_executors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.models.Employee;
import server.services.AdminManager;
import server.services.LoginResult;

import java.util.List;

public class AdminManagerCommandExecutor implements IExecute{
    private AdminManager adminManager;

    @Override
    public String execute(Integer userId,LoginResult loginResult, String request) {
        this.adminManager = AdminManager.getInstance();
        MethodType method = ServerDecoder.getMethodType(request);
        JsonObject data = ServerDecoder.getData(request);
        JsonObject response = new JsonObject();
        int employeeId;
        Employee employee;

        switch (method){
            case LOGIN:
                employeeId = data.get("id").getAsInt();
                String password = data.get("password").getAsString();
                LoginResult result = adminManager.login(employeeId,password);
                response.addProperty ("id", employeeId);
                response.addProperty("result", result.toString());
                break;
            case ADD_EMP:
                Employee emp= new Employee(data.toString());
                try {
                    adminManager.addEmployee(emp);
                    response.addProperty("result","success");
                } catch (Exception e){
                    response.addProperty("error",e.getMessage());
                }
                break;
            case REMOVE_EMP:
                employeeId = data.get("id").getAsInt();
                try {
                    adminManager.deleteEmployee(employeeId);
                    response.addProperty("result","success");
                } catch (Exception e){
                    response.addProperty("error",e.getMessage());
                }
                break;
            case EDIT_EMP:
                employeeId = data.get("id").getAsInt();
                String attr = data.get("fieldName").getAsString();
                String value = data.get("value").getAsString();
                try {
                    adminManager.editEmployee(employeeId,attr,value);
                    response.addProperty("result","success");
                }catch (Exception e){
                    response.addProperty("error",e.getMessage());
                }
                break;
            case GET_ALL_EMP:
                List<Employee> employees = adminManager.getAllEmployees();
                String json = new Gson().toJson(employees);
                response.addProperty("employees",json);
                response.addProperty("result","success");
                break;
            case IS_EMPLOYEE_EXISTS:
                employeeId = data.get("id").getAsInt();
                employee = adminManager.findEmployeeById(employeeId);
                response.addProperty("exists",employee != null);
                break;
            case IS_SHIFT_MANAGER: // New case for checking role
                employeeId = data.get("employeeId").getAsInt();
                employee = adminManager.findEmployeeById(employeeId);
                boolean isShiftManager = employee != null && employee.getPosition() == Employee.Position.SHIFT_MANAGER;
                response.addProperty("isShiftManager", isShiftManager);
                break;
            case GET_BRANCH_ID:
                employeeId = data.get("employeeId").getAsInt();
                employee = adminManager.findEmployeeById(employeeId);
                response.addProperty("branchId", employee.getBranchID());
                break;
        }
        return response.toString();

    }
}
