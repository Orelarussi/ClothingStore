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
import server.models.chat.Message;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;


public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    public static final String LOG_OUT = "Log out";
    public static final AdminHandler admin_handler = AdminHandler.getInstance();
    private static Integer id;
    private static int myBranchID;


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
            String request = AdminHandler.getInstance().login(id, password);
            //send the request to the server
            out.println(request);
            //server response
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
        String request = AdminHandler.getInstance().logout(id);
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
                        viewAllEmployees(in, out, consoleInput);
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

    private static void showCustomers(BufferedReader in, PrintWriter out) {
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


//all the functions that inside of the menu

    //admin menu functions:
    private static void editEmployee(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        int employeeId = getInt("Enter the employee ID you would like to edit:", "Invalid ID. Please enter a numeric value.", consoleInput);
        while (true) {

            System.out.println("Select the property number you would like to edit:");
            String[] fields = Arrays.stream(Employee.class.getDeclaredFields())
//                    .filter(f-> !f.getName().equals("employeesNum") && !f.getName().equals("employeeNumber"))
                    .map(Field::getName).toArray(String[]::new);


            for (int i = 0; i < fields.length; i++) {
                String fName = fields[i];
                System.out.println((i + 1) + ". " + fName);
            }
            try {
                int selectedFieldNum = Integer.parseInt(consoleInput.readLine());
                String selectedField = fields[selectedFieldNum - 1];
                System.out.println("Choose the new value for " + selectedField);
                String val = consoleInput.readLine();
                JsonObject req = new JsonObject();
                req.addProperty(selectedField, val);
                //{selectedField : val } - example { firstName : "Orel" }

                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Please enter a value between 1 to " + fields.length);
            }

        }

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
        final String removeEmployeeJsonReq = AdminHandler.getInstance().removeEmployee(employeeId);
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
        System.out.println("Enter employee details:");
        try {
            int employeeId = getInt("Employee ID: ", "Invalid ID. Please enter a numeric value.", consoleInput);

            System.out.print("Employee First Name: ");
            String firstName = consoleInput.readLine();

            System.out.print("Employee Last Name: ");
            String lastName = consoleInput.readLine();

            System.out.print("Employee Phone Number: ");
            String phoneNumber = consoleInput.readLine();

            System.out.print("Employee Password : ");
            String password = consoleInput.readLine();

            int branchId = getInt("Employee Branch Id: ", "Invalid Branch ID. Please enter a numeric value.", consoleInput);

            long accountNumber = getLong("Employee account number: ", "Invalid Branch ID. Please enter a numeric value.", consoleInput);

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

            Employee employee = new Employee(employeeId, branchId, firstName,
                    lastName, phoneNumber, password, accountNumber, position);

            final String newEmployeeJsonReq = AdminHandler.getInstance().createEmployee(employee);
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

    private static void viewAllEmployees(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {

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
                System.out.println("Chat started with ID: " + chatId);
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
            System.out.println("Chat started with ID: " + chatId);
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
        try {
            while (true) {
                //check if the client enter input
                if (Objects.equals(userInput.get(), "exit")) {
                    if (isWaiting) {
                        removeFromWaitingList(out, branchId);
                    } else {
                        removeFromAvailableList(out);
                    }
                    break;
                }
                // Check server response
                if (in.ready()) {
                    String response = in.readLine();
                    JsonObject serverResponse = ServerDecoder.convertToJsonObject(response);//גיל
                    int chatId = serverResponse.get("chatId").getAsInt();
                    System.out.println("Chat is now available! Chat ID: " + chatId);
                    joinChat(chatId, in, out, consoleInput);
                    break;
                }
            }
        } finally {
            // Ensure the executor is shut down in all cases
            executor.shutdownNow();
        }
    }

    private static void removeFromWaitingList(PrintWriter out, int branchId) {
        String request = ChatHandler.getInstance().removeFromWaitingList(id, branchId);
        out.println(request);
    }

    private static void removeFromAvailableList(PrintWriter out) {
        String request = ChatHandler.getInstance().removeFromAvailableList(id);
        out.println(request);
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
        //TODO: להכין פונקציית תצוגה תפריט שהיא לא לולאתית כי זה כל פעם מציג את אותם צאטים גם אם השתנה ואת הלולאה לשים בפונקציה הזאת
        displayAndRunMenu(OptionalChat, consoleInput, "Chose available chat");
    }

    private static void joinExistingChat(int chatId, BufferedReader in, PrintWriter out, BufferedReader consoleInput)  {
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

    private static void joinChat(int chatId, BufferedReader in, PrintWriter out, BufferedReader consoleInput)  {
        //log in to chat message
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
        System.out.println("\n" + loginMessage);

        // משתנה לניהול קלט משתמש
        AtomicReference<String> userInput = new AtomicReference<>(null);
        // יצירת Thread להאזנה לקלט המשתמש
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                while (true) {
                    if (consoleInput.ready()) {
                        String input = consoleInput.readLine().trim().toLowerCase();
                        userInput.set(input); // עדכון הקלט
                        if ("bye bye".equals(input)) {
                            break; // יציאה מהצ'אט אם המשתמש הקליד "bye bye"
                        }
                    }
                    Thread.sleep(500); // מניעת לולאה הדוקה
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
// האזנה להודעות ולטיפול בקלט
        try {
            boolean running = true;
            while (running) {
                // שליחת הודעה אם המשתמש הקליד
                String input = userInput.getAndSet(null);
                if (input != null) {
                    LocalDateTime timestamp = LocalDateTime.now();
                    String messageRequest = ChatHandler.getInstance().sendMessage(chatId, id,input,timestamp);
                    out.println(messageRequest);
                    serverResponse = in.readLine();
                    if ("bye bye".equals(input)) {
                        System.out.println("Exiting chat...");
                        String endChatRequest = ChatHandler.getInstance().closeChat(chatId);//delete chat from active chats
                        out.println(endChatRequest);
                        break;
                    }
                }

                // קבלת הודעות מהשרת
                if (in.ready()) {
                    String response = in.readLine();
                    JsonObject messageJson = ServerDecoder.convertToJsonObject(response);
                    Message message = new Message(
                            messageJson.get("senderId").getAsInt(),
                            messageJson.get("content").getAsString(),
                            LocalDateTime.parse(messageJson.get("timestamp").getAsString()));
                    message.setSenderName(messageJson.get("senderName").getAsString());

                    // בדיקת תוכן ההודעה לסיום השיחה
                    String messageContent = message.getContent().trim().toLowerCase();
                    if ("bye bye".equals(messageContent)) {
                        System.out.println("Chat ended by the other participant.");
                        break; // סיום השיחה
                    }

                    // הדפסת ההודעה
                    System.out.println(message.toString());
                }
            }
        }catch (IOException e){
            System.out.println(e.toString());
        }
        finally {
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


    //brunch info functions:
    private static int getBranchId(PrintWriter out, BufferedReader in) throws IOException {
        String request = AdminHandler.getInstance().getBranchId(id);

        out.println(request);
        String serverResponse = in.readLine();

        JsonObject jsonResponse = ServerDecoder.convertToJsonObject(serverResponse).getAsJsonObject();
        return jsonResponse.get("branchId").getAsInt();
    }


    //input help functions:
    private static int getInt(String msg, String errMsg, BufferedReader consoleInput) {
        return getInt(msg, errMsg, consoleInput, null);
    }

    private static int getInt(String msg, String errMsg, BufferedReader consoleInput, Predicate<Integer> test) {
        while (true) {
            System.out.print(msg);
            try {
                int number = Integer.parseInt(consoleInput.readLine());
                if (test != null) {
                    if (test.test(number)) {
                        return number;
                    }
                } else {
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

