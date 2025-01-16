package server.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import server.models.Branch;
import server.models.Employee;
import server.models.customer.Customer;
import server.services.AdminManager;
import server.services.BranchManager;
import server.services.EmployeeManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JsonUtils {

    public enum Files {
        Employees("employees"),
        Customers("customers"),
        Branches("branches");

        private final String fileName;

        Files(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return inDirectory(fileName) + ".json";
        }
    }

    private static final String dirPath = "src" + File.separator + "files";

    // Generic method to read a JSON file and convert it to a list of objects
    public static <T> List<T> readJsonListFromFile(
            String filePath, Class<T> clazz
    ) {
        try (FileReader reader = new FileReader(filePath)) {
            Type listType = TypeToken.getParameterized(List.class, clazz).getType();
            return new Gson().fromJson(reader, listType);
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
            // Create a GsonBuilder and enable the pretty print
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(jsonStrList, writer);
            System.out.println("JSON file updated successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to JSON file: " + e.getMessage());
        }
    }

    private static String inDirectory(String filePath) {
        return dirPath + File.separator + filePath;
    }

//    TODO call asynchronously
    public static void save() {
        System.out.println("Saving JSON files...");
        System.out.println("Saving employees...");
        saveEmployees();
        System.out.println("Saving Customers...");
        saveCustomers();
        System.out.println("Saving Branches...");
        saveBranches();
        System.out.println("JSON files saved successfully.");
    }

    //    TODO call asynchronously
    public static void load() {
        System.out.println("Loading JSON files...");
        System.out.println("Loading employees...");
        loadEmployees();
        System.out.println("Loading Customers...");
        loadCustomers();
        System.out.println("Loading Branches...");
        loadBranches();
        System.out.println("JSON files loaded successfully.");
    }

    private static void saveBranches() {
        List<Branch> branches = BranchManager.getInstance().getBranches().values().stream().toList();
        writeJsonFile(Files.Branches.getFileName(), branches);
    }

    private static void saveCustomers() {
        List<Customer> customers = EmployeeManager.getInstance().getCustomers().values().stream().toList();
        writeJsonFile(Files.Customers.getFileName(), customers);
    }

    private static void saveEmployees() {
        List<Employee> employees = AdminManager.getInstance().getAllEmployees();
        writeJsonFile(Files.Employees.getFileName(), employees);
    }

    private static void loadBranches() {
        List<Branch> branches = basicLoad(Files.Branches, Branch.class);
        if (branches != null) {
            BranchManager.getInstance().setBranches(branches);
        }
    }

    private static void loadCustomers() {
        List<JsonObject> employeesJsonList = basicLoad(Files.Customers,JsonObject.class);

        if (employeesJsonList != null) {
            List<Customer> customers = employeesJsonList.stream()
                    .map(json -> {
                        String asString = json.toString();
                        Customer customer = Customer.deserializeFromString(asString);
                        return customer;
                    }).toList();

            EmployeeManager.getInstance().setCustomers(customers);
        }
    }

    private static void loadEmployees() {
        List<Employee> employees = basicLoad(Files.Employees, Employee.class);
        if (employees != null)
            AdminManager.getInstance().setEmployees(employees);
    }

    private static <T> List<T> basicLoad(Files fileName, Class<T> objClass) {
        String jsonFileName = fileName.getFileName();
        if (new File(jsonFileName).exists()) {
            return readJsonListFromFile(jsonFileName, objClass);
        }
        System.out.println("File " + jsonFileName + " does not exist.");
        return null;
    }
}
