package client;

import client.handlers.AdminHandler;
import client.handlers.ChatHandler;
import client.menu.MenuItem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.command_executors.ServerDecoder;
import server.database.SocketData;
import server.models.Employee;
import server.models.Employee.Position;
import server.services.LoginResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.Arrays;
import java.util.function.Predicate;


public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final Gson gson = new Gson();
    public static final String LOG_OUT = "Log out";
    private static AdminHandler adminHandler = new AdminHandler();
    private static ChatHandler chatHandler= new ChatHandler();
    private static Integer id;

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
            String request = adminHandler.login(id, password);
            //send the request to the server
            out.println(request);
            String serverResponse = in.readLine();
            JsonObject loginResponse = ServerDecoder.convertToJsonObject(serverResponse);
            loginResult = LoginResult.valueOf(loginResponse.get("result").getAsString());
        }
        return loginResult;
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
        String request = adminHandler.logout(id);
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
                new MenuItem("Add employee", () -> {
                    createAndAddEmployee(in, out, consoleInput);
                }),
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
                    System.out.println("Displaying all employees...");
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

    private static void startInventoryMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        MenuItem[] inventoryMenu = {
                new MenuItem("Show branch inventory", () -> System.out.println("Displaying branch inventory...")),
                new MenuItem("Sale product to customer", () -> System.out.println("Processing sale to customer...")),
        };
        displayAndRunMenu(inventoryMenu, consoleInput, "Inventory Menu");
    }

    private static void startCustomerMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        MenuItem[] customerMenu = {
                new MenuItem("Add customer", () -> System.out.println("Adding customer...")),
                new MenuItem("Delete customer", () -> System.out.println("Deleting customer...")),
                new MenuItem("Show all customers in chain", () -> System.out.println("Displaying all customers in the chain...")),
        };
        displayAndRunMenu(customerMenu, consoleInput, "Customer Menu");
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
    private static void editEmployee(BufferedReader in, PrintWriter out, BufferedReader consoleInput)
            throws IOException {
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

    private static void removeEmployee(BufferedReader in, PrintWriter out, BufferedReader consoleInput)
            throws IOException {
        System.out.println("Enter the employee ID you would like to remove:");
        int employeeId;
        try {
            employeeId = Integer.parseInt(consoleInput.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a numeric value.");
            return;
        }
        final String removeEmployeeJsonReq = adminHandler.removeEmployee(employeeId);
        out.println(removeEmployeeJsonReq);

    }

    private static void createAndAddEmployee(BufferedReader in, PrintWriter out, BufferedReader consoleInput) {
        System.out.println("Enter employee details:");
        try {
            int employeeId = getInt("Employee ID: ", "Invalid ID. Please enter a numeric value.",
                    consoleInput);

            System.out.print("Employee First Name: ");
            String firstName = consoleInput.readLine();

            System.out.print("Employee Last Name: ");
            String lastName = consoleInput.readLine();

            System.out.print("Employee Phone Number: ");
            String phoneNumber = consoleInput.readLine();

            System.out.print("Employee Password Number: ");
            String password = consoleInput.readLine();

            int branchId = getInt("Employee Branch Id: ", "Invalid Branch ID. Please enter a numeric value.",
                    consoleInput);

            long accountNumber = getLong("Employee account number: ", "Invalid Branch ID. Please enter a numeric value.", consoleInput);

            // Position selection
            System.out.println("Select Employee Position:");
            Position[] positions = Position.values();
            for (int i = 0; i < positions.length; i++) {
                System.out.println((i + 1) + ". " + positions[i]);
            }

            int positionChoice;
            Position position;
            positionChoice = getInt("Enter the number corresponding to the position:",
                    "Invalid choice. Please select a valid number.", consoleInput);
            if (positionChoice < 1 || positionChoice > positions.length) {
                System.out.println("Invalid choice. Please select a valid number.");
                return;
            }
            position = positions[positionChoice - 1];

            Employee employee = new Employee(employeeId, branchId, firstName,
                    lastName, phoneNumber, password, accountNumber, position);

            final String newEmployeeJsonReq = adminHandler.createEmployee(employee);
            out.println(newEmployeeJsonReq);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private static void viewAllEmployees(BufferedReader in, PrintWriter out, BufferedReader consoleInput)
            throws IOException {

    }

    //chat functions:

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
        String request = chatHandler.openChat(selectedBranchID,myBranchID);
        //send the request to the server
        out.println(request);
        //get chat from server


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
                if (test != null)
                    if (test.test(number)) {
                        return number;
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

