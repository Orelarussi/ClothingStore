package client.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.command_executors.MethodType;
import server.command_executors.ServiceType;
import server.models.Employee;

public class AdminHandler extends BaseHandler {

    private static final AdminHandler instance = new AdminHandler();

    public static AdminHandler getInstance() {
        return instance;
    }

    private AdminHandler() {
        super(ServiceType.ADMIN);
    }

    public String login(int id, String password){
        JsonObject data = new JsonObject();
        data.addProperty("id",id);
        data.addProperty("password",password);
        return super.encodeRequest(MethodType.LOGIN,data);
    }

    public String logout(Integer id) {
        JsonObject data = new JsonObject();
        data.addProperty("id", id);
        return super.encodeRequest(MethodType.LOGOUT,data);
    }

    public String createEmployee(Employee employee){
        JsonObject data = new Gson().fromJson(employee.serializeToString(), JsonObject.class);
        return super.encodeRequest(MethodType.ADD_EMP,data);
    }

    public String removeEmployee(int id){
        JsonObject data = new JsonObject();
        data.addProperty("id", id);
        return super.encodeRequest(MethodType.REMOVE_EMP,data);
    }

    public String getAllEmployees(){
        JsonObject data = new JsonObject();
        return super.encodeRequest(MethodType.GET_ALL_EMP,data);
    }

    public String isEmployeeExist(int id) {
        JsonObject data = new JsonObject();
        data.addProperty("id", id);
        return super.encodeRequest(MethodType.IS_EMPLOYEE_EXISTS,data);
    }

    public String isShiftManger(Integer employeeId) {
        JsonObject data = new JsonObject();
        data.addProperty("employeeId", employeeId);
        return super.encodeRequest(MethodType.IS_SHIFT_MANAGER, data);
    }

    public String getBranchId(Integer employeeId) {
        JsonObject data = new JsonObject();
        data.addProperty("employeeId", employeeId);
        return super.encodeRequest(MethodType.GET_BRANCH_ID, data);
    }

    public String editEmployee(int employeeId, String fieldName, String value) {
        JsonObject data = new JsonObject();
        data.addProperty("id", employeeId);
        data.addProperty("fieldName", fieldName);
        data.addProperty("value", value);
        return super.encodeRequest(MethodType.EDIT_EMP,data);
    }
}
