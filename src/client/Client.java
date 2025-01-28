package client;

import client.handlers.*;
import client.menu.MenuItem;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import server.command_executors.ServerDecoder;
import server.database.SocketData;
import server.models.Employee;
import server.models.Employee.Position;
import server.models.chat.Message;
import server.models.customer.Customer;
import server.models.customer.NewCustomer;
import server.services.LoginResult;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;


public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    public static final String LOG_OUT = "Log out";
    public static final AdminHandler admin_handler = AdminHandler.getInstance();
    private static Integer myBranchID;
    private static Integer id;
    private static boolean isOnline = true;

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

        MenuItem[] generalMenu = new MenuItem[]{
                new MenuItem("Login", () -> connectToServer(consoleInput)),
                new MenuItem("Exit", Client::exitClient)
        };

        new Thread(()->{
            while (isOnline) {
                System.out.println("\nWelcome to Clothing Store\n");
                displayAndRunMenu(generalMenu, consoleInput, "main menu", false);
            }
        }).start();
    }

    private static void connectToServer(BufferedReader consoleInput) {

        //try with resources will close the socket when done
        try (SocketData socketData = new SocketData(new Socket(SERVER_HOST, SERVER_PORT))) {

            System.out.println("Connected to server at " + SERVER_HOST + ":" + SERVER_PORT);
            //start logic
            PrintWriter out = socketData.getOutputStream();
            BufferedReader in = socketData.getInputStream();
            LoginResult loginResult = login(consoleInput, out, in);//should be admin or employee with fullName as message
            showAdminOrEmployeeMenu(loginResult, consoleInput, out, in);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static LoginResult login(BufferedReader consoleInput, PrintWriter out, BufferedReader in)
            throws IOException {

        LoginResult loginResult = LoginResult.FAILURE;

        System.out.println("\n=== Login ===");

        id = getInt("Username id: ", "Invalid id. Please enter a numeric value.", consoleInput);
        // password
        System.out.print("Password: ");
        String password = consoleInput.readLine();
        String request = SessionHandler.getInstance().login(id, password);
        //send the request to the server
        out.println(request);
        String serverResponse = in.readLine();
        JsonObject loginResponse = ServerDecoder.convertToJsonObject(serverResponse);
        loginResult = LoginResult.fromJson(loginResponse.get("result").getAsString());

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
            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            String inventory = json.get("result").getAsString();
            System.out.println(!inventory.isEmpty() ? inventory : "Inventory is empty");
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


    private static void showAdminOrEmployeeMenu(LoginResult loginResult, BufferedReader consoleInput,
                                                PrintWriter out, BufferedReader in) throws IOException {
        switch (loginResult) {
            case ADMIN -> startAdminMenu(in, out, consoleInput, loginResult.getMessage());
            case EMPLOYEE -> startEmployeeMenu(in, out, consoleInput, loginResult.getMessage());
            case FAILURE -> System.out.println(loginResult.getMessage());
        }
    }

    private static void logout(BufferedReader in, PrintWriter out) {
        String request = SessionHandler.getInstance().logout(id);
        out.println(request);

        try {
            String response = in.readLine();
            if (response != null) {
                JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
                if (jsonObject.has("error"))
                    System.out.println(jsonObject.get("error").getAsString());
                else
                    System.out.println("Logged out successfully");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //display And Run Menu : to use in the start ... menu

    private static void displayAndRunMenu(MenuItem[] menuItems, BufferedReader consoleInput, String title) {
        displayAndRunMenu(menuItems, consoleInput, title, true);
    }

    private static void displayAndRunMenu(MenuItem[] menuItems, BufferedReader consoleInput,
                                          String menuTitle, boolean addBack) {
        if (addBack) {
            menuItems = Arrays.copyOf(menuItems, menuItems.length + 1);
            menuItems[menuItems.length - 1] = new MenuItem("Back", null);
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (choice > 0 && choice <= menuItems.length) {
                MenuItem item = menuItems[choice - 1];
                String title = item.getTitle();
                if (title.equals("Back") || title.equals(LOG_OUT)) {
                    return;
                }else if (title.equals("Exit")) {
                    item.run();
                    return;
                }else {
                    item.run();
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void exitClient() {
        isOnline = false;
        System.out.println("Exiting the client. Goodbye!");
    }

    //all menus functions:

    private static void startAdminMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput, String userName) throws IOException {
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
                new MenuItem(LOG_OUT, () -> logout(in, out))
        };
        System.out.println("\nHello "+userName);

        displayAndRunMenu(adminMenu, consoleInput, "Admin Menu");
    }

    private static void startEmployeeMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput, String userName) throws IOException {
        MenuItem[] employeeMenu = {
                new MenuItem("Branch Menu", () ->{
                    try {
                        startBranchInfoMenu(in, out, consoleInput);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }),
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
                new MenuItem(LOG_OUT, () -> logout(in, out))
        };
        System.out.println("\nHello "+userName);
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
            JsonObject object = JsonParser.parseString(response).getAsJsonObject();

            if (object.get("success").getAsBoolean()) {
                JsonArray jsonArray = object.get("customers").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    Customer customer = Customer.deserializeFromString(jsonElement.toString());
                    customers.add(customer);
                }

                System.out.println(String.join("\n",customers.stream().map(Customer::toString).toList()));
            }
            else {
                System.out.println("failed to fetched customers");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        NewCustomer customer;
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

        return new NewCustomer(cid, first, last, phone, 0);
    }


    private static void startSalesReportMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        MenuItem[] salesReportMenu = {
                new MenuItem("Show sales amount by branch", () -> showSalesByBranch(in, out, consoleInput)),
                new MenuItem("Show sales amount by product ID", () -> showSalesByProduct(in, out, consoleInput)),
                new MenuItem("Show sales amount by date", () -> showSalesByDate(in, out, consoleInput)),
        };
        displayAndRunMenu(salesReportMenu, consoleInput, "Sales Report Menu");
    }

    private static void startBranchInfoMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        MenuItem[] BranchInfoMenu = {
                new MenuItem("Show branch employees", () -> showBranchEmployee(in, out, consoleInput)),
                new MenuItem("Add product to branch", () -> addProductToBranch(in, out, consoleInput)),
        };
        displayAndRunMenu(BranchInfoMenu, consoleInput, "Branch Menu");
    }

    private static void addProductToBranch(BufferedReader in, PrintWriter out, BufferedReader consoleInput) {
        int branchId = getInt("Enter branch id: ","Invalid id",consoleInput);
        int productId = getInt("Enter product id: ","Invalid id",consoleInput);
        int amount = getInt("Enter amount: ","Invalid amount",consoleInput);

        String request = EmployeeHandler.getInstance().addProductToBranch(branchId, productId, amount);
        out.println(request);

        try {
            String response = in.readLine();
            System.out.println(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showBranchEmployee(BufferedReader in, PrintWriter out, BufferedReader consoleInput) {

        int branchId = getInt("Branch Id: ", "Invalid Branch ID. Please enter a numeric value.", consoleInput);


        String request = EmployeeHandler.getInstance().showBranchEmployee(branchId);
        out.println(request);

        try {

            String response = in.readLine();
            if (response == null || response.isEmpty()) {
                System.out.println("No response received from the server.");
                return;
            }
            JsonObject object = JsonParser.parseString(response).getAsJsonObject();
            String employees = object.get("result").getAsString();
            System.out.println(!employees.isEmpty() ? employees : "No employees found");
        } catch (IOException e) {
            System.out.println("An error occurred while fetching branch employees: " + e.getMessage());
        }
    }

    private static void basicSalesAction(BufferedReader in, PrintWriter out, String req) {
        out.println(req);
        try {
            String response = in.readLine();
            JsonObject obj = JsonParser.parseString(response).getAsJsonObject();
            String sales = obj.get("sales").getAsString();
            System.out.println(!sales.isEmpty() ? sales : "No sales found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void showSalesByBranch(BufferedReader in, PrintWriter out, BufferedReader consoleInput) {
        int branchId = getInt("Employee Branch Id: ", "Invalid Branch ID. Please enter a numeric value.", consoleInput);
        String request = SalesHandler.getInstance().showSalesByBranch(branchId);
        basicSalesAction(in, out, request);
    }

    private static void showSalesByProduct(BufferedReader in, PrintWriter out, BufferedReader consoleInput) {
        int productId = getInt("Product Id: ", "Invalid Product ID. Please enter a numeric value.", consoleInput);
        String request = SalesHandler.getInstance().showSalesByProduct(productId);
        basicSalesAction(in, out, request);
    }

    private static void showSalesByDate(BufferedReader in, PrintWriter out, BufferedReader consoleInput) {
        LocalDate date = getDate(consoleInput);
        String request = SalesHandler.getInstance().showSalesByDate(date);
        basicSalesAction(in, out, request);
    }

    private static void startChatsMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        boolean isManager = isShiftManager(out, in);
        MenuItem[] chatsMenu;

        if (isManager) {
            chatsMenu = new MenuItem[]{
                    new MenuItem("Start a new chat with another branch", () -> {
                        try {
                            chooseBranchMenu(in, out, consoleInput);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }),
                    new MenuItem("Wait for an incoming chat request", () -> {
                        try {
                            availableForChat(in, out, consoleInput);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }),
                    new MenuItem("Join an ongoing chat", () -> {
                        try {
                            showOptionalChatSM(in, out, consoleInput);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
            };
        } else {
            chatsMenu = new MenuItem[]{
                    new MenuItem("Open chat", () -> {
                        try {
                            chooseBranchMenu(in, out, consoleInput);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }),
                    new MenuItem("Wait to chat request", () -> {
                        try {
                            availableForChat(in, out, consoleInput);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
            };
        }
        displayAndRunMenu(chatsMenu, consoleInput, "Chats Menu");
    }



    //admin menu functions:
    private static void editEmployee(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        System.out.println("Enter employee id from the list:");

        viewAllEmployees(in, out);

        int employeeId = getInt("Enter the employee ID you would like to edit:",
                "Invalid ID. Please enter a numeric value.", consoleInput);

        String employeeExistReq = admin_handler.isEmployeeExist(employeeId);
        out.println(employeeExistReq);

        String response = in.readLine();
        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        boolean exists = json.get("exists").getAsBoolean();
        if (!exists){
            System.out.println("Employee with id "+employeeId+" does not exist,please try again");
            editEmployee(in, out, consoleInput);
            return;
        }

        OnEmployeeFieldSelectedListener listener = fieldName -> {

            System.out.println("Choose the new value for " + fieldName);

            if (fieldName.equalsIgnoreCase("position")){
                MenuItem[] positionMenu = new MenuItem[Position.values().length];
                Position[] values = Position.values();
                for (int i = 0; i < values.length; i++) {
                    String name = values[i].name();
                    positionMenu[i] = new MenuItem(name, () -> {
                        try {
                            requestEditEmployeeAndResponse(in, out, fieldName, employeeId, name);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                displayAndRunMenu(positionMenu, consoleInput, "Choose a position");//TODO check invalid choice
            }
            else {
                try {
                    String value = consoleInput.readLine();
                    requestEditEmployeeAndResponse(in, out, fieldName, employeeId, value);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        MenuItem[] menu = createEditEmployeeMenu(listener);

        displayAndRunMenu(menu, consoleInput, "Select the property number you would like to edit:");
    }

    private static void requestEditEmployeeAndResponse(BufferedReader in, PrintWriter out,
                                                       String fieldName, int employeeId, String value) throws IOException {
        String request = admin_handler.editEmployee(employeeId, fieldName, value);
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
    }

    private static MenuItem[] createEditEmployeeMenu(OnEmployeeFieldSelectedListener listener) {
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
            JsonObject res = JsonParser.parseString(response).getAsJsonObject();
            if (res.has("error")) {
                System.out.println("Employee " + employeeId + " failed to remove.");
                System.out.println(res.get("error"));
            }else if (res.has("result")) {
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

    private static int getEmployeeId(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        boolean doesEmployeeExist = true;
        int employeeId = -1;
        while (doesEmployeeExist) {
            employeeId = getInt("Employee ID: ", "Invalid ID. Please enter a numeric value.", consoleInput);
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

    private static void viewAllEmployees(BufferedReader in, PrintWriter out) throws IOException {
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

    /////////////
    //chat functions:

    private static void chooseBranchMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        myBranchID = getBranchId(out, in);
        MenuItem[] customerMenu = {
                new MenuItem("Tel Aviv", () -> tryOpenChat(in, out, consoleInput, 1)),
                new MenuItem("Jerusalem", () -> tryOpenChat(in, out, consoleInput, 2)),
                new MenuItem("Haifa", () -> tryOpenChat(in, out, consoleInput, 3)),
                new MenuItem("Beer sheba", () -> tryOpenChat(in, out, consoleInput, 4)),
        };
        displayAndRunMenu(customerMenu, consoleInput, "Chose branch");
    }

    private static void tryOpenChat(BufferedReader in, PrintWriter out, BufferedReader consoleInput, int selectedBranchID) {
        if (selectedBranchID == myBranchID) {
            System.out.println("You cannot select your own branch, try again.");
            return;
        }
        String request = ChatHandler.getInstance().waitingForChatRequest(selectedBranchID, id);
        out.println(request);
        // Receive and process the server's response
        try {
            String response = in.readLine();
            JsonObject serverResponse = ServerDecoder.convertToJsonObject(response);
            if (serverResponse.get("chatId").isJsonNull()) {
                System.out.println("No chat available at the moment. Waiting for available employee...");
                waitForChat(in, out, consoleInput, true, selectedBranchID);
            } else {
                int chatId = serverResponse.get("chatId").getAsInt();
                joinChat(chatId, in, out, consoleInput);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void availableForChat(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {

        // build and send the request
        String request = ChatHandler.getInstance().availableForChat(id);
        out.println(request);
        // Receive and process the server's response
        String response = in.readLine();
        JsonObject serverResponse = ServerDecoder.convertToJsonObject(response);
        if (serverResponse.get("chatId").isJsonNull()) {
            System.out.println("\nNo chat requests at the moment. Waiting for chat request.");
            waitForChat(in, out, consoleInput, false, null);
        } else {
            int chatId = serverResponse.get("chatId").getAsInt();
            joinChat(chatId, in, out, consoleInput);
        }
    }

    private static void waitForChat(BufferedReader in, PrintWriter out, BufferedReader consoleInput, boolean isWaiting, Integer branchId) throws IOException {
        System.out.println("Type 'exit' to leave wait mode at any time.");

        AtomicReference<String> userInput = new AtomicReference<>(null);// Shared resource for input

        // Creates a single-thread executor to run a separate thread for non-blocking user input handling
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while (true) {
                try {
                    if (consoleInput.ready()) {
                        String input = consoleInput.readLine().trim();
                        userInput.set(input); // Read input if available
                        if ("exit".equalsIgnoreCase(input)) {
                            break;
                        }
                    }
                    Thread.sleep(500);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        while (true) {
            //check if the client enter input
            if (Objects.equals(userInput.get(), "exit")) {
                if (isWaiting) {
                    removeFromWaitingList(in,out,consoleInput, branchId);
                } else {
                    removeFromAvailableList(in,out,consoleInput);
                }
                break;
            }
            // Check server response
            if (in.ready()) {
                String response = in.readLine();
                JsonObject serverResponse = ServerDecoder.convertToJsonObject(response);//גיל
                int chatId = serverResponse.get("chatId").getAsInt();
                executor.shutdownNow();
                joinChat(chatId, in, out, consoleInput);
                break;
            }
        }

    }

    private static void removeFromWaitingList(BufferedReader in, PrintWriter out,BufferedReader consoleInput, int branchId) {
        String request = ChatHandler.getInstance().removeFromWaitingList(id, branchId);
        out.println(request);
        try {
            in.readLine();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private static void removeFromAvailableList(BufferedReader in, PrintWriter out,BufferedReader consoleInput) {
        String request = ChatHandler.getInstance().removeFromAvailableList(id);
        out.println(request);
        try {
            in.readLine();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private static void showOptionalChatSM(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        String request = ChatHandler.getInstance().getOptionalChat(id);
        out.println(request);

        String serverResponse = in.readLine();
        JsonObject jsonResponse = ServerDecoder.convertToJsonObject(serverResponse).getAsJsonObject();
        JsonArray chatSessions = jsonResponse.getAsJsonArray("chatSessionsArray");
        if (chatSessions == null || chatSessions.size() == 0) {
            System.out.println("No available chats at the moment.");
            return;
        }
        System.out.println("There is available chats at the moment.");

        MenuItem[] OptionalChat = new MenuItem[chatSessions.size()];
        for (int i = 0; i < chatSessions.size(); i++) {
            JsonObject chat = chatSessions.get(i).getAsJsonObject();
            int chatId = chat.get("chatId").getAsInt();
            String description = chat.get("description").getAsString();

            OptionalChat[i] = new MenuItem(description, () -> {
                joinExistingChat(chatId, in, out, consoleInput); // התחברות לצ'אט
            });
        }
        displayAndRunMenuOnce(OptionalChat, consoleInput, "Chose available chat",true);
    }

    private static void joinExistingChat(int chatId, BufferedReader in, PrintWriter out, BufferedReader consoleInput) {
        String request = ChatHandler.getInstance().joinExistChat(chatId, id);
        out.println(request);
        String serverResponse = null;
        try {
            serverResponse = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonObject jsonResponse = ServerDecoder.convertToJsonObject(serverResponse).getAsJsonObject();
        if (jsonResponse.get("success").getAsBoolean()) {
            System.out.println("\njoinExistingChat success");
            joinChat(chatId, in, out, consoleInput);
            //TODO:לעדכן את חברי הצאט על ההצטרפות של אותו המנהל
        } else {
            System.out.println("\njoinExistingChat not success");
        }
    }

    private static void joinChat(int chatId, BufferedReader in, PrintWriter out, BufferedReader consoleInput) {
        //log in to chat title
        System.out.println("\n=== Welcome to chat ===");
        String request = ChatHandler.getInstance().startChatMessage(chatId, id);
        out.println(request);
        String serverResponse = null;
        try {
            serverResponse = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonObject jsonResponse = ServerDecoder.convertToJsonObject(serverResponse).getAsJsonObject();
        String loginMessage = jsonResponse.get("message").getAsString();
        System.out.println( loginMessage);
        System.out.println("To exit the chat, type 'bye bye' and press Enter.");

        // Variable to manage user input
        AtomicReference<String> userInput = new AtomicReference<>(null);
        // Create a thread to listen for user input
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                while (true) {
                    // Check if there is user input
                    if (consoleInput.ready()) {
                        String input = consoleInput.readLine().trim();
                        userInput.set(input); // Update the user input
                        if ("bye bye".equalsIgnoreCase(input)) {
                            break; // // Exit the chat if the user typed 'bye bye'
                        }
                    }
                    Thread.sleep(500); // Prevent a tight loop
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        // Listen for messages and handle input
        try {
            while (true) {
                // Send a message if the user typed input
                String input = userInput.getAndSet(null);
                if (input != null) {
                    LocalDateTime timestamp = LocalDateTime.now();
                    String messageRequest = ChatHandler.getInstance().sendMessage(chatId, id, input, timestamp);
                    out.println(messageRequest);
                    serverResponse = in.readLine();//clean the buffer
                    if ("bye bye".equalsIgnoreCase(input)) {
                        System.out.println("Exiting chat...");
                        String endChatRequest = ChatHandler.getInstance().closeChat(chatId);//delete chat from active chats
                        out.println(endChatRequest);
                        serverResponse = in.readLine();//clean the buffer
                        break;
                    }
                }

                // קבלת הודעות מהשרת
                if (in.ready()) {
                    String response = in.readLine();
                    JsonObject messageJson = ServerDecoder.convertToJsonObject(response);
                    Message message = new Message(
                            messageJson.get("employeeId").getAsInt(),
                            messageJson.get("content").getAsString(),
                            LocalDateTime.parse(messageJson.get("timestamp").getAsString()),
                            messageJson.get("senderName").getAsString());

                    // בדיקת תוכן ההודעה לסיום השיחה
                    String messageContent = message.getContent().trim();
                    if ("bye bye".equalsIgnoreCase(messageContent)) {
                        System.out.println("Chat ended by the other participant.");
                        break; // סיום השיחה
                    }

                    // הדפסת ההודעה
                    System.out.println(message.toString());
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            executor.shutdownNow(); // סגירת ה-Thread
        }
    }


    private static boolean isShiftManager(PrintWriter out, BufferedReader in) throws IOException {
        String request = AdminHandler.getInstance().isShiftManger(id);
        //send the request to the server
        out.println(request);

        String serverResponse = in.readLine();
        JsonObject jsonResponse = ServerDecoder.convertToJsonObject(serverResponse);
        return jsonResponse.get("isShiftManager").getAsBoolean();
    }

    private static void displayAndRunMenuOnce(MenuItem[] menuItems, BufferedReader consoleInput,
                                          String menuTitle, boolean addBack) {
        if (addBack) {
            menuItems = Arrays.copyOf(menuItems, menuItems.length + 1);
            menuItems[menuItems.length - 1] = new MenuItem("Back", null);
        }

        int choice;

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
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (choice > 0 && choice <= menuItems.length) {
                MenuItem item = menuItems[choice - 1];
                String title = item.getTitle();
                if (title.equals("Back") || title.equals(LOG_OUT)) {
                    return;
                }else if (title.equals("Exit")) {
                    item.run();
                    return;
                }else {
                    item.run();
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
            }

    }


    //brunch info functions:
    private static int getBranchId(PrintWriter out, BufferedReader in) throws IOException {
        String request = AdminHandler.getInstance().getBranchId(id);

        out.println(request);
        String serverResponse = in.readLine();

        JsonObject jsonResponse = ServerDecoder.convertToJsonObject(serverResponse).getAsJsonObject();
        return jsonResponse.get("branchId").getAsInt();
    }
    /// ////////////


    //input help functions:
    private static int getInt(String msg, String errMsg, BufferedReader consoleInput) {
        return getInt(msg,errMsg,consoleInput, x-> x > 0);
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

    public static LocalDate getDate(BufferedReader reader) {
        LocalDate date = null;

        while (date == null) {
            System.out.print("Enter a date in the format yyyy-MM-dd: ");

            try {
                date = LocalDate.parse(reader.readLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Attempt to parse the date
            } catch (Exception e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }

        return date;
    }
}

