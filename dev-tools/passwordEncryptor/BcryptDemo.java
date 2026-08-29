import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Console;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BcryptDemo {

    public static void main(String[] args) throws IOException {
        String password = getPassword(args);
        System.out.println(new BCryptPasswordEncoder().encode(password));
    }

    private static String getPassword(String[] args) throws IOException {
        if (args.length > 0) {
            return String.join(" ", args);
        }

        Console console = System.console();
        if (console != null) {
            char[] password = console.readPassword("Password: ");
            return new String(password);
        }

        System.out.print("Password: ");
        return new BufferedReader(new InputStreamReader(System.in)).readLine();
    }
}