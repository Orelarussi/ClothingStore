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
import java.util.List;

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

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startAdminMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {

        MenuItem[] items = createMenu(in, out, consoleInput);
        int actionNum;
        MenuItem item;

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

    private static MenuItem[] createMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) {
        return new MenuItem[]{
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
                new MenuItem("Exit", () -> {
//                    Log out
                    System.exit(0);
                })
        };
    }

    private static void editEmployee(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException {
        int employeeId = getInt("Enter the employee ID you would like to edit:", "Invalid ID. Please enter a numeric value.", consoleInput);
        while (true) {

            System.out.println("Select the property number you would like to edit:");
            String[] fields = Arrays.stream(Employee.class.getDeclaredFields())
//                    .filter(f-> !f.getName().equals("employeesNum") && !f.getName().equals("employeeNumber"))
                    .map(Field::getName).toArray(String[]::new);


            for (int i = 0; i < fields.length; i++) {
                String fName = fields[i];
                System.out.println((i+1) + ". " + fName);
            }
            try {
                int selectedFieldNum = Integer.parseInt(consoleInput.readLine());
                String selectedField = fields[selectedFieldNum - 1];
                System.out.println("Choose the new value for "+selectedField);
                String val = consoleInput.readLine();
                JsonObject req = new JsonObject();
                req.addProperty(selectedField,val);
                //{selectedField : val } - example { firstName : "Orel" }

                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Please enter a value between 1 to "+fields.length);
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

