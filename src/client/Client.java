package client;

import client.handlers.AdminHandler;
import client.handlers.ChatHandler;
import client.handlers.CustomerHandler;
import client.handlers.EmployeeHandler;
import client.menu.MenuItem;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import server.command_executors.ServerDecoder;
import server.database.SocketData;
import server.models.Employee;
import server.models.Employee.Position;
import server.models.customer.Customer;
import server.models.customer.NewCustomer;
import server.services.LoginResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;


public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    public static final String LOG_OUT = "Log out";
    public static final AdminHandler admin_handler = AdminHandler.getInstance();

    private Integer id;

    public static void main(String[] args) {
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

        MenuItem[] generalMenu = new MenuItem[]{
                new MenuItem("Login", () -> connectToServer(consoleInput)),
                new MenuItem("Exit", () -> exitClient(consoleInput))
        };

        while (true) {
            System.out.println("\nWelcome to Clothing Store\n");
            try {
                displayAndRunMenu(generalMenu, consoleInput, "main menu", false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void connectToServer(BufferedReader consoleInput) {

        //try with resources will close the socket when done
        try (SocketData socketData = new SocketData(new Socket(SERVER_HOST, SERVER_PORT))) {

            System.out.println("Connected to server at " + SERVER_HOST + ":" + SERVER_PORT);
            //start logic
            PrintWriter out = socketData.getOutputStream();
            BufferedReader in = socketData.getInputStream();
            LoginResult loginResult = login(consoleInput, out, in);
            showAdminOrEmployeeMenu(loginResult, consoleInput, out, in);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static LoginResult login(BufferedReader consoleInput, PrintWriter out, BufferedReader in)
            throws IOException {

        LoginResult loginResult = LoginResult.FAILURE;

        System.out.println("\n=== Login ===");

        while (!(loginResult == LoginResult.ADMIN || loginResult == LoginResult.EMPLOYEE)) {
            // id
            id = getInt("Username id: ", "Invalid id. Please enter a numeric value.", consoleInput);
            // password
            System.out.print("Password: ");
            String password = consoleInput.readLine();
            String request = admin_handler.login(id, password);
            //send the request to the server
            out.println(request);
            String serverResponse = in.readLine();
            JsonObject loginResponse = ServerDecoder.convertToJsonObject(serverResponse);
            loginResult = LoginResult.valueOf(loginResponse.get("result").getAsString());
        }
        return loginResult;
    }

    private static void showBranchInventory(PrintWriter out, BufferedReader in, BufferedReader consoleInput) throws IOException {
        System.out.print("Enter the branch ID to display its inventory: ");
        int branchID = Integer.parseInt(consoleInput.readLine());

        String request = EmployeeHandler.getInstance().showInventory(branchID);

        // Send the request to the server
        out.println(request);

        // Wait for the server's response
        String response = in.readLine();
        if (response != null) {
            System.out.println("Inventory for Branch ID " + branchID + ":");
            System.out.println(response);
        } else {
            System.out.println("No response received from the server.");
        }
    }

    private static void saleProduct(PrintWriter out, BufferedReader in, BufferedReader consoleInput) throws IOException {
        System.out.print("Enter the Customer ID to purchase: ");
        int customerID = Integer.parseInt(consoleInput.readLine());

        System.out.print("Enter the Product Id: ");
        int productId = Integer.parseInt(consoleInput.readLine());

        System.out.print("Enter the Amount: ");
        int amount = Integer.parseInt(consoleInput.readLine());

        String request = EmployeeHandler.getInstance().saleProduct(customerID, productId, amount);

        // Send the request to the server
        out.println(request);

        // Wait for the server's response
        String response = in.readLine();
        if (response != null) {
            System.out.println("Sale added Successfully");
            System.out.println(response);
        } else {
            System.out.println("No response received from the server.");
        }
    }

    private static void startInventoryMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        MenuItem[] inventoryMenu = {
                new MenuItem("Show branch inventory", () -> {
                    try {
                        showBranchInventory(out, in, consoleInput);
                    } catch (IOException e) {
                        System.out.println("An error occurred while displaying the inventory: " + e.getMessage());
                    }
                }),
                new MenuItem("Sale product to customer", () -> {
                    try {
                        saleProduct(out, in, consoleInput);
                    } catch (IOException e) {
                        System.out.println("An error occurred while displaying the inventory: " + e.getMessage());
                    }


                    System.out.println("Processing sale to customer...");
                })
        };
        displayAndRunMenu(inventoryMenu, consoleInput, "Inventory Menu");
    }


    private static void showAdminOrEmployeeMenu(LoginResult loginResult, BufferedReader consoleInput, PrintWriter out, BufferedReader in) throws IOException {
        switch (loginResult) {
            case ADMIN:
                startAdminMenu(in, out, consoleInput);
                break;
            case EMPLOYEE:
                startEmployeeMenu(in, out, consoleInput);
                break;
            case FAILURE:
                System.out.println("User id or password are incorrect, please try again");
                break;
        }
    }

    private static void logout(PrintWriter out) {
        String request = admin_handler.logout(id);
        out.println(request);
    }
    //display And Run Menu : to use in the start ... menu

    private static void displayAndRunMenu(MenuItem[] menuItems, BufferedReader consoleInput, String title) throws IOException {
        displayAndRunMenu(menuItems, consoleInput, title, true);
    }

    private static void displayAndRunMenu(MenuItem[] menuItems, BufferedReader consoleInput, String menuTitle,
                                   boolean addBack_ExitOpt) throws IOException {
        if (addBack_ExitOpt) {
            menuItems = Arrays.copyOf(menuItems, menuItems.length + 2);
            menuItems[menuItems.length - 2] = new MenuItem("Back", null);
            menuItems[menuItems.length - 1] = new MenuItem("Exit", () -> exitClient(consoleInput));
        }

        int choice;
        while (true) {
            // display the menu
            System.out.println("\n=== " + menuTitle + " ===");
            for (int i = 0; i < menuItems.length; i++) {
                System.out.println((i + 1) + ". " + menuItems[i].getTitle());
            }

            System.out.print("Choose an option: ");
            try {
                choice = Integer.parseInt(consoleInput.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                continue;
            }

            if (choice > 0 && choice <= menuItems.length) {
                String title = menuItems[choice - 1].getTitle();
                if (title.equals("Back") || title.equals(LOG_OUT)) {
                    return;
                } else {
                    menuItems[choice - 1].run();
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void exitClient(BufferedReader consoleInput) {
        System.out.println("Exiting the client. Goodbye!");
        try {
            consoleInput.close(); // Close console input
        } catch (IOException e) {
            System.out.println("Error closing console input: " + e.getMessage());
        }
        System.exit(0); // Terminate the client process
    }

    //all menus functions:

    private static void startAdminMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        MenuItem[] adminMenu = {
                new MenuItem("Add employee", () -> createAndAddEmployee(in, out, consoleInput)),
                new MenuItem("Remove employee", () -> {
                    try {
                        removeEmployee(in, out, consoleInput);
                    } catch (IOException e) {
                        System.out.println("An error occurred while removing the employee.");
                    }
                }),
                new MenuItem("Edit employee", () -> {
                    try {
                        editEmployee(in, out, consoleInput);
                    } catch (IOException e) {
                        System.out.println("An error occurred while editing the employee.");
                    }
                }),
                new MenuItem("View all employees", () -> {
                    try {
                        viewAllEmployees(in, out);
                    } catch (IOException e) {
                        System.out.println("An error occurred while viewing all employees.");
                    }
                }),
                new MenuItem(LOG_OUT, () -> {
                })
        };
        displayAndRunMenu(adminMenu, consoleInput, "Admin Menu");
    }

    private static void startEmployeeMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        MenuItem[] employeeMenu = {
                new MenuItem("Show branch info", () -> System.out.println("Displaying branch info...")),
                new MenuItem("Inventory menu", () -> {
                    try {
                        startInventoryMenu(in, out, consoleInput);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }),
                new MenuItem("Customer menu", () -> {
                    try {
                        startCustomerMenu(in, out, consoleInput);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }),
                new MenuItem("Sales report menu", () -> {
                    try {
                        startSalesReportMenu(in, out, consoleInput);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }),
                new MenuItem("Chats menu", () -> {
                    try {
                        startChatsMenu(in, out, consoleInput);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }),
                new MenuItem(LOG_OUT, () -> {
                })
        };
        displayAndRunMenu(employeeMenu, consoleInput, "Employee Menu");
    }

    private static void startCustomerMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        MenuItem[] customerMenu = {
                new MenuItem("Add customer", () -> addCustomer(in, out, consoleInput)),
                new MenuItem("Delete customer", () -> deleteCustomer(in, out, consoleInput)),
                new MenuItem("Show all customers in chain", () -> showCustomers(in, out)),
        };
        displayAndRunMenu(customerMenu, consoleInput, "Customer Menu");
    }

    private static  void showCustomers(BufferedReader in, PrintWriter out) {
        String request = CustomerHandler.getInstance().getAllCustomers();
        out.println(request);

        List<Customer> customers = new ArrayList<>();
        try {
            String response = in.readLine();
            JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                Customer customer = Customer.deserializeFromString(jsonElement.toString());
                customers.add(customer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        customers.forEach(System.out::println);
    }

    private static void deleteCustomer(BufferedReader in, PrintWriter out, BufferedReader consoleInput) {
        int cid = getInt("Enter customer id to delete:", "Invalid id", consoleInput);
        String request = CustomerHandler.getInstance().deleteCustomer(cid);
        out.println(request);

        try {
            String response = in.readLine();
            System.out.println(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addCustomer(BufferedReader in, PrintWriter out, BufferedReader consoleInput) {
        NewCustomer customer = null;
        try {
            customer = getNewCustomer(consoleInput);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String request = CustomerHandler.getInstance().addCustomer(customer);
        out.println(request);

        try {
            String response = in.readLine();
            System.out.println(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static NewCustomer getNewCustomer(BufferedReader consoleInput) throws IOException {
        int cid = getInt("Enter customer id: ", "Invalid id", consoleInput);
        System.out.println("Enter first customer name: ");
        String first = consoleInput.readLine();
        System.out.println("Enter last customer name: ");
        String last = consoleInput.readLine();
        System.out.println("Enter phone number: ");
        String phone = consoleInput.readLine();

        return new NewCustomer(cid, first, last, phone);
    }


    private static void startSalesReportMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        MenuItem[] salesReportMenu = {
                new MenuItem("Show sales amount by branch", () -> System.out.println("Displaying sales by branch...")),
                new MenuItem("Show sales amount by product ID", () -> System.out.println("Displaying sales by product ID...")),
                new MenuItem("Show sales amount by category", () -> System.out.println("Displaying sales by category (לא זמין)...")),
        };
        displayAndRunMenu(salesReportMenu, consoleInput, "Sales Report Menu");
    }

    private static void startChatsMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        MenuItem[] chatsMenu = {
                new MenuItem("Open chat", () -> System.out.println("Opening chat...")),
                new MenuItem("Connect to available chat", () -> System.out.println("Connecting to available chat...")),
                new MenuItem("Write to chat request", () -> System.out.println("Writing to chat request...")),
        };
        displayAndRunMenu(chatsMenu, consoleInput, "Chats Menu");
    }


//all the functions that inside of the menu

    //admin menu functions:
    private static void editEmployee(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        int employeeId = getInt("Enter the employee ID you would like to edit:",
                "Invalid ID. Please enter a numeric value.", consoleInput);

        OnEmployeeFieldSelectedListener listener = fieldName -> {
            try {
                System.out.println("Choose the new value for " + fieldName);
                AtomicReference<String> value = new AtomicReference<>();

                if (fieldName.equalsIgnoreCase("position")){
                    MenuItem[] positionMenu = new MenuItem[Position.values().length];
                    Position[] values = Position.values();
                    for (int i = 0; i < values.length; i++) {
                        String name = values[i].name();
                        positionMenu[i] = new MenuItem(name, () -> value.set(name));
                    }
                    displayAndRunMenu(positionMenu, consoleInput, "Choose a position");
                }
                else
                    value.set(consoleInput.readLine());

                String request = admin_handler.editEmployee(employeeId, fieldName, value.get());
                out.println(request);

                String response = in.readLine();
                if (response != null) {
                    JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
                    if (jsonObject.has("error")) {
                        System.out.println(jsonObject.get("error").getAsString());
                    } else if (jsonObject.has("result") && jsonObject.get("result").getAsString().equals("success")) {
                        System.out.println("Employee " + employeeId + " has been edited.");
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        MenuItem[] menu = createEditEmployeeMenu(listener);

        displayAndRunMenu(menu, consoleInput, "Select the property number you would like to edit:");
    }

    private MenuItem[] createEditEmployeeMenu(OnEmployeeFieldSelectedListener listener) {
        List<Field> fields = Employee.getAllFields();

        MenuItem[] menu = new MenuItem[fields.size()];

        for (int i = 0; i < fields.size(); i++) {
            String fName = fields.get(i).getName();
            menu[i] = new MenuItem(fName, () -> listener.onEmployeeFieldSelected(fName));
        }

        return menu;
    }

    private static void removeEmployee(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        System.out.println("Enter the employee ID you would like to remove:");
        int employeeId;
        try {
            employeeId = Integer.parseInt(consoleInput.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a numeric value.");
            return;
        }
        final String removeEmployeeJsonReq = admin_handler.removeEmployee(employeeId);
        out.println(removeEmployeeJsonReq);
        String response = in.readLine();
        if (response != null) {
            JsonObject res = new JsonObject();
            if (res.has("error")) {
                System.out.println("Employee " + employeeId + " failed to remove.");
                System.out.println(res.get("error"));
            }
            if (res.has("result")) {
                System.out.println("Employee " + employeeId + " was successfully removed.");
            }
        }
    }

    private static void createAndAddEmployee(BufferedReader in, PrintWriter out, BufferedReader consoleInput) {
        try {
            System.out.println("Enter employee details:");
            int employeeId = getEmployeeId(in, out, consoleInput);

            System.out.print("Employee First Name: ");
            String firstName = consoleInput.readLine();

            System.out.print("Employee Last Name: ");
            String lastName = consoleInput.readLine();

            System.out.print("Employee Phone Number: ");
            String phoneNumber = consoleInput.readLine();

            System.out.print("Employee Password : ");
            String password = consoleInput.readLine();
//            Map<Integer, Branch> branches = BranchManager.getInstance().getBranches();
//            for (Branch branch : branches.values()) {
//                System.out.println(branch);
//            }

            int branchId = getInt("Employee Branch Id: ",
                    "Invalid Branch ID. Please enter a numeric value.",
                    consoleInput);

            long accountNumber = getLong("Employee account number: ",
                    "Invalid Branch ID. Please enter a numeric value.",
                    consoleInput);

            // Position selection
            System.out.println("Select Employee Position:");
            Position[] positions = Position.values();
            for (int i = 0; i < positions.length; i++) {
                System.out.println((i + 1) + ". " + positions[i]);
            }

            int positionChoice;
            Position position;
            positionChoice = getInt("Enter the number corresponding to the position:", "Invalid choice. Please select a valid number.", consoleInput);
            if (positionChoice < 1 || positionChoice > positions.length) {
                System.out.println("Invalid choice. Please select a valid number.");
                return;
            }
            position = positions[positionChoice - 1];

            Employee employee = new Employee(employeeId, branchId, firstName, lastName,
                    phoneNumber, password, accountNumber, position);

            final String newEmployeeJsonReq = admin_handler.createEmployee(employee);
            out.println(newEmployeeJsonReq);

            String result = in.readLine();
            if (result != null) {
                JsonObject res = JsonParser.parseString(result).getAsJsonObject();
                if (res.has("error")) {
                    System.out.println("Employee " + employee.getFullName() + " failed to add.");
                    String error = res.get("error").toString();
                    System.out.println(error);
                }
                if (res.has("result")) {
                    System.out.println("Employee " + employee.getFullName() + " successfully added.");
                }
            }

        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void openChat(BufferedReader in, PrintWriter out, BufferedReader consoleInput) {
        int myBranchID = 1;//need to use get brunch id by em id
        String[] branches = {"Tel Aviv", "Jerusalem", "Haifa", "Beersheba"};

        System.out.println("Branches:");
        for (int i = 0; i < branches.length; i++) {
            if (i + 1 != myBranchID) {
                System.out.println("Branch ID: " + (i + 1) + ", Address: " + branches[i]);
            }
        }
        int selectedBranchID = getInt("Enter branch ID : ",
                "Invalid input. Please enter a valid branch number.", consoleInput,
                branch -> branch < 1 || branch > branches.length || branch == myBranchID);
        // build the request
        String request = ChatHandler.getInstance().openChat(selectedBranchID,myBranchID);
        //send the request to the server
        out.println(request);
        //get chat from server


    }

    private int getEmployeeId(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        boolean doesEmployeeExist = true;
        int employeeId = -1;
        while (doesEmployeeExist) {
            employeeId = getInt("Employee ID: ", "Invalid ID. Please enter a numeric value.", consoleInput);
            if (employeeId <= 0) {
                System.out.println("ID must be a positive number!");
                continue;
            }
            String req = admin_handler.isEmployeeExist(employeeId);
            out.println(req);

            String result = in.readLine();
            if (result != null) {
                JsonObject obj = JsonParser.parseString(result).getAsJsonObject();
                doesEmployeeExist = obj.get("exists").getAsBoolean();
                if (doesEmployeeExist)
                    System.out.println("Employee with id " + employeeId + " already exists!");
            }
        }
        return employeeId;
    }

    private void viewAllEmployees(BufferedReader in, PrintWriter out) throws IOException {
        String request = admin_handler.getAllEmployees();
        out.println(request);//send request to server

        String result = in.readLine();

        if (result != null) {
            JsonObject res = JsonParser.parseString(result).getAsJsonObject();
            JsonElement element = res.get("employees");
            if (element != null) {
                //Convert to list of employees
                Type type = new TypeToken<List<Employee>>() {
                }.getType();
                List<Employee> employees = new Gson().fromJson(element.getAsString(), type);
                employees.forEach(System.out::println);
            }
        }
    }


    //input help functions:
    private static int getInt(String msg, String errMsg, BufferedReader consoleInput) {
        return getInt(msg,errMsg,consoleInput, null);
    }

    private static int getInt(String msg, String errMsg, BufferedReader consoleInput, Predicate<Integer> test) {
        while (true) {
            System.out.print(msg);
            try {
                int number = Integer.parseInt(consoleInput.readLine());
                if (test != null){
                    if (test.test(number)) {
                        return number;
                    }
                }else {
                    return number;
                }
            } catch (NumberFormatException e) {
                System.out.println(errMsg);
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    private static long getLong(String msg, String errMsg, BufferedReader consoleInput) {
        while (true) {
            System.out.print(msg);
            try {
                return Long.parseLong(consoleInput.readLine());
            } catch (NumberFormatException e) {
                System.out.println(errMsg);
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }
}

