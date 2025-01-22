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
                try {
                    adminManager.addEmployee(emp);
                    response.addProperty("result","success");
                }catch (IllegalArgumentException e){
                    response.addProperty("error",e.getMessage());
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
        }
        return response.toString();

    }
}
