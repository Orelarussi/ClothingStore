package client;

import client.handlers.AdminHandler;
import client.menu.MenuItem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.command_executors.ServerDecoder;
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
    private static AdminHandler adminHandler = new AdminHandler();
    private static LoginResult loginResult = LoginResult.FAILURE;
    private static Integer id;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Connected to server at " + SERVER_HOST + ":" + SERVER_PORT);
            //start logic
            loginAndShowAdminOrEmployeeMenu(consoleInput, out, in);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loginAndShowAdminOrEmployeeMenu(BufferedReader consoleInput, PrintWriter out, BufferedReader in)
    throws IOException {
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
        } else {
            startEmployeeMenu(in, out, consoleInput);
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
            System.out.println();
            System.exit(0);
        });

        int choice;
        while (true) {
            // display the menu
            System.out.println("\n=== "+menuTitle+" ===");
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
                if (menuItems[choice - 1].getTitle().equals("Back")) {
                    break;
                }else {
                    menuItems[choice - 1].run();
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

//all the menu functions:

    private static void startAdminMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        MenuItem[] adminMenu = {
                new MenuItem("Add employee", () -> {
                    createAndAddEmployee(in, out, consoleInput);}),
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
                }),
                new MenuItem("Log out", () -> {})
        };
        displayAndRunMenu(adminMenu, consoleInput,"Admin Menu");
    }

    private static void startEmployeeMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        MenuItem[] employeeMenu = {
                new MenuItem("Show branch info", () -> System.out.println("Displaying branch info...")),
                new MenuItem("Inventory menu", () -> {
                    try {
                        startInventoryMenu(in,out,consoleInput);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }),
                new MenuItem("Customer menu", () -> System.out.println("Opening customer menu...")),
                new MenuItem("Sales report menu", () -> System.out.println("Opening sales report menu...")),
                new MenuItem("Chats menu", () -> System.out.println("Opening chats menu...")),
                new MenuItem("Log out", () -> {
                    logout(out);
                    try {
                        loginAndShowAdminOrEmployeeMenu(consoleInput,out,in);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
        };
        displayAndRunMenu(employeeMenu, consoleInput,"Employee Menu");
    }

    private static void startInventoryMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        MenuItem[] inventoryMenu = {
                new MenuItem("Show branch inventory", () -> System.out.println("Displaying branch inventory...")),
                new MenuItem("Sale product to customer", () -> System.out.println("Processing sale to customer...")),
                new MenuItem("Return to Employee Menu", () -> {
                    try {
                        startEmployeeMenu(in, out, consoleInput);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
        };
        displayAndRunMenu(inventoryMenu, consoleInput, "Inventory Menu");
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
                req.addProperty(selectedField,val);
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

            System.out.print("Employee Password Number: ");
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
            positionChoice = getInt( "Enter the number corresponding to the position:", "Invalid choice. Please select a valid number.", consoleInput );
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

    private static void viewAllEmployees(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {

    }
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

