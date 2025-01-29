package server.utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import server.models.Branch;
import server.models.Employee;
import server.models.Product;
import server.models.SaleReport;
import server.models.customer.Customer;
import server.services.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static server.logger.Logger.log;

public class JsonUtils {

    public enum Files {
        Employees("employees"),
        Customers("customers"),
        Branches("branches"),
        Products("products"),
        Sales("sales");

        private final String fileName;

        Files(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return inDirectory(fileName) + ".json";
        }
    }

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
    private static final String dirPath = "src" + File.separator + "files";

    // Generic method to read a JSON file and convert it to a list of objects
    public static <T> List<T> readJsonFile(String filePath, Class<T> clazz) {
        try (FileReader reader = new FileReader(filePath)) {
            Type listType = TypeToken.getParameterized(List.class, clazz).getType();
            List<T> list = gson.fromJson(reader, listType);
            log("Read file "+filePath+" successfully");
            return list;
        } catch (IOException e) {
            String err = "Error reading JSON file: " + e.getMessage();
            System.out.println(err);
            log(err);
            return null;
        }
    }

    // Generic method to write a list of objects to a JSON file
    public static <T> void writeJsonFile(String filePath, List<T> objects) {
        File file = new File(filePath);
        if (!file.exists()) {
            boolean created = file.getParentFile().mkdirs();//create folder
            if (!created) {
                throw new RuntimeException("Couldn't create directory: " + file.getParent());
            }
        }
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(objects, writer);
            log("JSON file "+filePath+ " updated successfully.");
            System.out.println("JSON file updated successfully.");
        } catch (IOException e) {
            String s = "Error writing to JSON file: " + e.getMessage();
            System.out.println(s);
            log(s);
        }
    }

    private static String inDirectory(String filePath) {
        return dirPath + File.separator + filePath;
    }

    public static void save() {
        System.out.println("Saving JSON files...");
        System.out.println("Saving employees...");
        saveEmployees();
        System.out.println("Saving Customers...");
        saveCustomers();
        System.out.println("Saving Branches...");
        saveBranches();
        System.out.println("Saving Products...");
        saveProducts();
        System.out.println("Saving Sales...");
        saveSaleReports();
        System.out.println("JSON files saved successfully.");
    }

    public static void load() {
        System.out.println("Loading JSON files...");
        System.out.println("Loading employees...");
        loadEmployees();
        System.out.println("Loading Customers...");
        loadCustomers();
        System.out.println("Loading Branches...");
        loadBranches();
        System.out.println("Loading Products...");
        loadProducts();
        System.out.println("Loading Sales...");
        loadSales();
        System.out.println("JSON files loaded successfully.");
    }

    public static void saveBranches() {
        List<Branch> branches = BranchManager.getInstance().getBranches().values().stream().toList();
        writeJsonFile(Files.Branches.getFileName(), branches);
    }

    public static void saveProducts() {
        List<Product> products = ProductManager.getInstance().getProducts().values().stream().toList();
        writeJsonFile(Files.Products.getFileName(), products);
    }

    private static void saveSaleReports() {
        writeJsonFile(Files.Sales.getFileName(), SalesManager.getInstance().getAllSaleReports());
    }

    public static void saveCustomers() {
        List<Customer> customers = EmployeeManager.getInstance().getCustomers().values().stream().toList();
        writeJsonFile(Files.Customers.getFileName(), customers);
    }

    public static void saveEmployees() {
        List<Employee> employees = AdminManager.getInstance().getAllEmployees();
        writeJsonFile(Files.Employees.getFileName(), employees);
    }

    public static void loadBranches() {
        List<Branch> branches = basicLoad(Files.Branches, Branch.class);
        BranchManager.getInstance().setBranches(branches);
    }

    public static void loadSales() {
        List<SaleReport> sales = basicLoad(Files.Sales, SaleReport.class);
        if (sales != null) {
            SalesManager.getInstance().setSaleReports(sales);
        }
    }

    public static void loadProducts() {
        List<Product> products = basicLoad(Files.Products, Product.class);
        ProductManager.getInstance().setProducts(products);
    }

    public static void loadCustomers() {
        List<JsonObject> employeesJsonList = basicLoad(Files.Customers, JsonObject.class);
        EmployeeManager.getInstance().setCustomersFromJson(employeesJsonList);
    }

    public static void loadEmployees() {
        List<Employee> employees = basicLoad(Files.Employees, Employee.class);
        AdminManager.getInstance().setEmployees(employees);
    }

    public static <T> List<T> basicLoad(Files fileName, Class<T> objClass) {
        String jsonFileName = fileName.getFileName();
        if (new File(jsonFileName).exists()) {
            return readJsonFile(jsonFileName, objClass);
        }
        System.out.println("File " + jsonFileName + " does not exist.");
        return new ArrayList<>();
    }
}
