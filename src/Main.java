import utils.PasswordValidator;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a password to validate: ");
        String password = scanner.nextLine();

        if (PasswordValidator.validate(password)) {
            System.out.println("Password is valid!");
        } else {
            System.out.println("Password is invalid.");
        }
    }
}
