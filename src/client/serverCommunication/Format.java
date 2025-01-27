package client.serverCommunication;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import server.database.Chat;
import server.models.Employee;
import server.models.Product;
import server.models.customer.Customer;
import server.models.PurchasedItem;
import server.services.AdminManager;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// [TYPE] | [METHOD_NAME] | [PARAMETERS]
public class Format {
    //DON'T USE `%` format
    public static String typeSeparator = "@@@";
    public static String methodSeparator = "###";
    public static String paramsSeparator = "&&&";
    public static String objectSeparator = "~~~";
    public static String fieldSeparator = "!!!";

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static DateTimeFormatter formatter_get = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String encode(ClassType type, String methodName) {
        return type + typeSeparator + methodName + methodSeparator;
    }

    public static String encode(ClassType type, String methodName, String param) {
        return encode(type, methodName) + param + paramsSeparator;
    }

    public static String encode(ClassType type, String methodName, String param1, String param2) {
        return encode(type, methodName, param1) + param2 + paramsSeparator;
    }

    public static ClassType getType(String str) {
        String typeStr = str.split(typeSeparator)[0];
        try {
            return ClassType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            return ClassType.OBJECT;
        }
    }

    public static String getMethod(String str) {
        String temp = str.split(typeSeparator)[1];
        return temp.split(methodSeparator)[0];
    }

    public static String getFirstParam(String str) {
        String temp = str.split(paramsSeparator)[0];
        return temp.split(methodSeparator)[1];
    }

    public static String getSecondParam(String str) {
        String temp = str.split(paramsSeparator)[1];
        return temp.split(paramsSeparator)[0];
    }

    public static String encodeException(String message) {
        return encode(ClassType.EXCEPTION, "", message);
    }

    public static String encodeEmpty(String optionalString) {
        return encode(ClassType.EMPTY, "", "");
    }

    public static String encodeSuccessMessage() {
        return encode(ClassType.SUCCESS, "", "הפעולה בוצעה בהצלחה!");
    }

    public static List<Customer> decodeCustomers(String jsonString) {
        Gson gson = new Gson();
        JsonArray jarr = gson.fromJson(jsonString,JsonArray.class);

        List<Customer> arr = new ArrayList<>();

        for (JsonElement json : jarr) {
            arr.add(Customer.deserializeFromString(json.getAsString()));
        }
        return arr;
    }

    public static LocalDateTime stringToDate(String str) {
        return LocalDateTime.parse(str, formatter);
    }

    public static String dateToString(LocalDateTime date) {
        return formatter.format(date);

    }

    public static LocalDateTime stringToDateDB(String str) {
        return LocalDateTime.parse(str, formatter_get);
    }

    public static String encodeCustomers(List<Customer> arr) {
        StringBuilder result = new StringBuilder();
        for (Customer customer : arr) {
            result.append(customer.serializeToString());
            result.append(objectSeparator);
        }
        return result.toString();
    }

    public static List<Employee> decodeEmployees(String jsonString) {
        Type listType = new TypeToken<List<Employee>>(){}.getType();
        List<Employee> employees = new Gson().fromJson(jsonString, listType);
        return employees;
    }

    public static String encodeEmployees(List<Employee> arr) {
        StringBuilder result = new StringBuilder();
        for (Employee employee : arr) {
            result.append(employee.serializeToString());
            result.append(objectSeparator);
        }
        return result.toString();
    }

    public static List<Product> decodeProducts(String jsonString) {
        Type listType = new TypeToken<List<Product>>(){}.getType();
        List<Product> products = new Gson().fromJson(jsonString, listType);
        return products;
    }

    public static String encodeProducts(List<Product> arr) {
        StringBuilder result = new StringBuilder();
        for (Product Product : arr) {
            result.append(Product.serializeToString());
            result.append(objectSeparator);
        }
        return result.toString();
    }

    public static List<PurchasedItem> decodePurchasedItems(String str) {
        String[] objects = str.split(objectSeparator);
        List<PurchasedItem> arr = new ArrayList<>();

        for (String objectString : objects) {
            PurchasedItem pi = new PurchasedItem(objectString);
            arr.add(pi);
        }
        return arr;
    }

    public static String encodePurchasedItems(List<PurchasedItem> arr) {
        StringBuilder result = new StringBuilder();
        for (PurchasedItem PurchasedItem : arr) {
            result.append(PurchasedItem.serializeToString());
            result.append(objectSeparator);
        }
        return result.toString();
    }

    public static Set<String> decodeAvailableBranches(String str) {
        String[] objects = str.split(objectSeparator);
        Set<String> branches = new HashSet<>();

        for (String objectString : objects) {
            branches.add(objectString);
        }
        return branches;
    }

    public static String encodeAvailableBranches(Set<String> branches) {
        StringBuilder result = new StringBuilder();

        for (String branch : branches) {
            result.append(branch);
            result.append(objectSeparator);
        }
        return result.toString();
    }

    public static List<Object[]> decodeAvailableChats(String str) {
        String[] objects = str.split(objectSeparator);
        List<Object[]> tableLines = new ArrayList<>();

        for (String objectString : objects) {
            String[] fields = objectString.split(fieldSeparator);
            Object[] object = {fields[0], fields[1], fields[2], fields[3]};

            tableLines.add(object);
        }
        return tableLines;
    }

    public static String encodeAvailableChats(Set<Chat> chats) {
        JsonArray result = new JsonArray(chats.size());
        JsonObject object;
        AdminManager manager = AdminManager.getInstance();

        for (Chat chat : chats) {
            object = new JsonObject();
            object.addProperty("sessionID", chat.getSessionID());

            int employeeID = chat.getReceiverID();
            Employee employee = manager.findEmployeeById(employeeID);

            object.addProperty("branchID", employee.getBranchID());
            object.addProperty("fullName", employee.getFullName());

            employeeID = chat.getCreatorID();
            employee = manager.findEmployeeById(employeeID);
            object.addProperty("fullName", employee.getFullName());

            result.add(object);
        }
        return result.getAsString();
    }
}
