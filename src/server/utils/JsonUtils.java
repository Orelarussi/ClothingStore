package server.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import server.models.Employee;
import server.models.customer.Customer;
import server.services.AdminManager;
import server.services.EmployeeManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class JsonUtils {

    private static final Gson gson = new Gson();
    private static final String dirPath = "files";

    // Generic method to read a JSON file and convert it to a list of objects
    public static <T> List<T> readJsonListFromFile(
            String filePath, Class<T> clazz
    ) {
        try (FileReader reader = new FileReader(filePath)) {
            Type listType = TypeToken.getParameterized(List.class, clazz).getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
            return null;
        }
    }

    // Generic method to write a list of objects to a JSON file
    public static <T extends JsonSerializable> void writeJsonFile(
            String filePath, List<T> objects
    ) {
        File f = new File(filePath);
        f.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(filePath)) {
            List<JsonObject> jsonStrList = objects.stream().map(JsonSerializable::toJson).toList();
            gson.toJson(jsonStrList, writer);
            System.out.println("JSON file updated successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to JSON file: " + e.getMessage());
        }
    }

    private static String inDirectory(String filePath) {
        return dirPath + File.separator + filePath;
    }

    public enum Files {
        Employees("employees"),
        Customers("customers"),;

        private final String fileName;

        Files(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return inDirectory(fileName) + ".json";
        }
    }

    public static void save() {
        List<Employee> employees = AdminManager.getInstance().getAllEmployees();
        writeJsonFile(Files.Employees.getFileName(), employees);

        List<Customer> customers = EmployeeManager.getInstance().getCustomers().values().stream().toList();
        writeJsonFile(Files.Customers.getFileName(), customers);
    }

    public static void load() {
        String employeesFileName = Files.Employees.getFileName();
        if (new File(employeesFileName).exists()) {
            List<Employee> employees = readJsonListFromFile(employeesFileName, Employee.class);
            if (employees != null)
                AdminManager.getInstance().setEmployees(employees);
        } else {
            System.out.println("File " + employeesFileName + " does not exist.");
        }

        String customersFileName = Files.Customers.getFileName();
        if (new File(customersFileName).exists()) {
            List<JsonObject> employeesJsonList = readJsonListFromFile(customersFileName, JsonObject.class);
            if (employeesJsonList != null) {
                List<Customer> customers = employeesJsonList.stream()
                        .map(json -> {
                            String asString = json.toString();
                            Customer customer = Customer.deserializeFromString(asString);
                            return customer;
                        }).toList();

                EmployeeManager.getInstance().setCustomers(customers);
            }
        } else {
            System.out.println("File " + customersFileName + " does not exist.");
        }
    }
}
