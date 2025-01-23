package server.command_executors;

import com.google.gson.JsonObject;
import server.models.Employee;
import server.services.AdminManager;
import server.services.LoginResult;

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
                int id = data.get("id").getAsInt();
                String password = data.get("password").getAsString();
                LoginResult result = adminManager.login(id,password);
                response.addProperty ("id", id);
                response.addProperty("result", result.toString());
                break;
            case ADD_EMP:
                Employee emp= new Employee(data.toString());
                adminManager.addEmployee(emp);
                response.addProperty("result","success");
                break;
            case IS_SHIFT_MANAGER: // New case for checking role
                employeeId = data.get("employeeId").getAsInt();
                employee = adminManager.findEmployeeById(employeeId);
                boolean isShiftManager = employee != null && employee.getPosition() == Employee.Position.SHIFTMGR;
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
