import java.util.regex.Pattern;

public class RegexValidation {
    private static final Pattern EMAIL=Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE=Pattern.compile("^[6-9][0-9]{9}$");
    public static void main(String[] args) {
        String email="sneha@example.com", phone="9876543210";
        System.out.println("Email valid: "+EMAIL.matcher(email).matches());
        System.out.println("Indian mobile valid: "+PHONE.matcher(phone).matches());
    }
}
