import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class ATMExceptionDemo {
    static int readBalance(Path cardFile) throws IOException {
        String value = Files.readString(cardFile).trim();
        return Integer.parseInt(value);
    }

    static void withdraw(int balance, int amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative.");
        if (amount == 0) throw new IllegalArgumentException("Amount must be greater than zero.");
        if (amount > balance) throw new IllegalStateException("Insufficient balance.");
        System.out.println("Please collect cash: Rs." + amount);
        System.out.println("Remaining balance: Rs." + (balance - amount));
    }

    public static void main(String[] args) {
        Path cardFile = Path.of(args.length > 0 ? args[0] : "card.txt");
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Enter withdrawal amount: ");
            if (!input.hasNextInt()) {
                System.out.println("Invalid input: enter a whole number.");
                return;
            }
            int amount = input.nextInt();
            int balance = readBalance(cardFile);
            withdraw(balance, amount);
        } catch (NumberFormatException e) {
            System.out.println("Card file must contain a valid integer balance.");
        } catch (IOException e) {
            System.out.println("Card reader/file error: " + e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Transaction rejected: " + e.getMessage());
        } finally {
            System.out.println("ATM session closed (finally).");
        }
    }
}

/*
Test setup: create card.txt containing 5000.
Try inputs: 1000 (success), -5 (negative), 0 (zero), 6000 (insufficient),
abc (non-numeric), and rename/delete card.txt (checked IOException).
NumberFormatException and IllegalArgumentException are unchecked;
IOException is checked; Error represents serious JVM/system failures.
Catch specific exceptions before their superclasses. Avoid catch(Exception)
unless at a true application boundary where logging and recovery are defined.
*/