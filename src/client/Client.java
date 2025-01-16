package client;

import client.handlers.AdminHandler;
import client.menu.MenuItem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.command_executors.ServerDecoder;
import server.models.Employee;
import server.models.Employee.Position;
import server.services.LoginResult;
import server.utils.JsonUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final Gson gson = new Gson();
    private static final AdminHandler adminHandler = new AdminHandler();
    private static LoginResult loginResult = LoginResult.FAILURE;
    private static Integer id;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Connected to server at " + SERVER_HOST + ":" + SERVER_PORT);

            while (loginResult == LoginResult.FAILURE) {
                // id
                System.out.print("Username id: ");
                id = Integer.parseInt(consoleInput.readLine());
                // password
                System.out.print("Password: ");
                String password = consoleInput.readLine();
                String request = adminHandler.login(id, password);
                //send the request to the server
                out.println(request);
                JsonObject loginResponse = ServerDecoder.convertToJsonObject(in.readLine());
                loginResult = LoginResult.valueOf(loginResponse.get("result").getAsString());
                if (loginResult == LoginResult.FAILURE) {
                    System.out.println("User id or password are incorrect, please try again");
                }
            }
            if (loginResult == LoginResult.ADMIN) {
                startAdminMenu(in, out, consoleInput);
            } else {

    private static void loginAndShowAdminOrEmployeeMenu(BufferedReader consoleInput, PrintWriter out, BufferedReader in)
    throws IOException {
        while (true) {
            while (loginResult == LoginResult.FAILURE) {
                // id
                id = getInt("Username id: ", "Invalid id. Please enter a numeric value.", consoleInput);
                // password
                System.out.print("Password: ");
                String password = consoleInput.readLine();
                String request = adminHandler.login(id, password);
                //send the request to the server
                out.println(request);
                JsonObject loginResponse = ServerDecoder.convertToJsonObject(in.readLine());
                loginResult = LoginResult.valueOf(loginResponse.get("result").getAsString());
                if (loginResult == LoginResult.FAILURE) {
                    System.out.println("User id or password are incorrect, please try again");
                }
            }
            if (loginResult == LoginResult.ADMIN) {
                startAdminMenu(in, out, consoleInput);

                loginResult = LoginResult.FAILURE;
            } else {
                startEmployeeMenu(in, out, consoleInput);
                loginResult = LoginResult.FAILURE;
            }
        }
    }

    private static void logout(PrintWriter out) {
        String request = adminHandler.logout(id);
        out.println(request);
    }
    //display And Run Menu : to use in the start ... menu

    private static void displayAndRunMenu(MenuItem[] menuItems, BufferedReader consoleInput,String menuTitle) throws IOException {
        menuItems = Arrays.copyOf(menuItems, menuItems.length + 2);
        menuItems[menuItems.length - 2] = new MenuItem("Back", null);
        menuItems[menuItems.length - 1] = new MenuItem("Exit", () -> {
            System.out.println("Exiting the client. Goodbye!");
            try {
                consoleInput.close(); // Close console input
            } catch (IOException e) {
                System.out.println("Error closing console input: " + e.getMessage());
            }
            System.exit(0); // Terminate the client process
        });


        while (true) {
            // Build JSON request
            JsonObject requestJson = new JsonObject();
            System.out.println("Enter action : ");
            for (int i = 0; i < items.length; i++) {
                item = items[i];
                System.out.println((1 + i) + ". " + item);
            }
            try {
                actionNum = Integer.parseInt(consoleInput.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Try again , choose a number.");
                continue;
            }
            if (actionNum > 0 && actionNum <= items.length) {
                //Good number
                items[actionNum - 1].run();
                System.out.println(items[actionNum - 1].getTitle() + " success");
            } else {
//                Invalid
                System.out.println("Try again , choose a valid number (1-" + items.length + ").");
                continue;
            }

            // Send JSON request
            // out.println(gson.toJson(requestJson));

            // Read JSON response
//                String response = in.readLine();
//                JsonObject responseJson = gson.fromJson(response, JsonObject.class);
//                System.out.println("Server response: " + responseJson);
        }
    }

    //all menus functions:

    private static void startAdminMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        MenuItem[] adminMenu = {

   
                new MenuItem("Add employee", () -> {
                    try {
                        createAndAddEmployee(in, out, consoleInput);
                    } catch (IOException e) {
                        System.out.println("An error occurred while adding the employee.");
                    }
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
                    // You can implement additional logic for this option here.
                })
        };
        displayAndRunMenu(adminMenu, consoleInput,"Admin Menu");
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
                })
        };
        displayAndRunMenu(employeeMenu, consoleInput, "Employee Menu");
    }

    private static void startInventoryMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        MenuItem[] inventoryMenu = {
                new MenuItem("Show branch inventory", () -> System.out.println("Displaying branch inventory...")),
                new MenuItem("Sale product to customer", () -> System.out.println("Processing sale to customer...")),

        };
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
        final String removeEmployeeJsonReq = adminHandler.removeEmployee(employeeId);
        out.println(removeEmployeeJsonReq);

    }

    private static void createAndAddEmployee(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        System.out.println("Enter employee details:");
        int employeeId;
        while (true) {
            System.out.print("Employee ID: ");

            try {
                employeeId = Integer.parseInt(consoleInput.readLine());
                break;

            } catch (NumberFormatException e) {
                System.out.println("Invalid ID. Please enter a numeric value.");

            }
        }
        System.out.print("Employee First Name: ");
        String firstName = consoleInput.readLine();

        System.out.print("Employee Last Name: ");
        String lastName = consoleInput.readLine();

        System.out.print("Employee Phone Number: ");
        String phoneNumber = consoleInput.readLine();

        System.out.print("Employee Password Number: ");
        String password = consoleInput.readLine();

        int branchId = getInt("Employee Branch Id: ", "Invalid Branch ID. Please enter a numeric value.", consoleInput);


        long accountNumber;
        while (true) {
            System.out.print("Employee account number: ");
            try {
                accountNumber = Long.parseLong(consoleInput.readLine());
                break; //

            } catch (NumberFormatException e) {
                System.out.println("Invalid Branch ID. Please enter a numeric value.");
            }

        }

        // Position selection
        System.out.println("Select Employee Position:");
        Position[] positions = Position.values();
        for (int i = 0; i < positions.length; i++) {
            System.out.println((i + 1) + ". " + positions[i]);
        }

        int positionChoice;
        Position position;
        try {
            System.out.print("Enter the number corresponding to the position: ");
            positionChoice = Integer.parseInt(consoleInput.readLine());
            if (positionChoice < 1 || positionChoice > positions.length) {
                System.out.println("Invalid choice. Please select a valid number.");
                return;
            }
            position = positions[positionChoice - 1];
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }


        Employee employee = new Employee(employeeId, branchId, firstName,
                lastName, phoneNumber, password, accountNumber, position);

        final String newEmployeeJsonReq = adminHandler.createEmployee(employee);
        out.println(newEmployeeJsonReq);
    }


    //input halp functions:

    private static int getInt(String msg, String errMsg, BufferedReader consoleInput) {
        while (true) {
            System.out.print(msg);
            try {
                return Integer.parseInt(consoleInput.readLine());
            } catch (NumberFormatException e) {
                System.out.println(errMsg);
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }
}

