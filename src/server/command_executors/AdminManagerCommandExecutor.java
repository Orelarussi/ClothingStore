package server.command_executors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.exceptions.IllegalFieldValueException;
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
        int id;
        switch (method){
            case LOGIN:
                id = data.get("id").getAsInt();
                String password = data.get("password").getAsString();
                LoginResult result = adminManager.login(id,password);
                response.addProperty ("id", id);
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
                id = data.get("id").getAsInt();
                try {
                    adminManager.deleteEmployee(id);
                    response.addProperty("result","success");
                } catch (Exception e){
                    response.addProperty("error",e.getMessage());
                }
                break;
            case EDIT_EMP:
                id = data.get("id").getAsInt();
                String attr = data.get("fieldName").getAsString();
                String value = data.get("value").getAsString();
                try {
                    adminManager.editEmployee(id,attr,value);
                    response.addProperty("result","success");
                }catch (IllegalFieldValueException|IllegalAccessException e){
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
                id = data.get("id").getAsInt();
                Employee employee = adminManager.findEmployeeById(id);
                response.addProperty("exists",employee != null);
                break;
        }
        return response.toString();

    }
}
