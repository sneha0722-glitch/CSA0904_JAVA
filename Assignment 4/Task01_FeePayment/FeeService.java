import java.sql.*;
import java.util.Objects;

/** Application/service layer: validates input and performs payment persistence. */
public class FeeService {
    private final String url, user, password;

    public FeeService(String url, String user, String password) {
        this.url = Objects.requireNonNull(url);
        this.user = Objects.requireNonNull(user);
        this.password = Objects.requireNonNull(password);
    }

    public String pay(String regNo, String amountText) throws Exception {
        if (regNo == null || regNo.isBlank()) throw new IllegalArgumentException("Register number is required.");
        final double amount;
        try { amount = Double.parseDouble(amountText.trim()); }
        catch (RuntimeException ex) { throw new IllegalArgumentException("Enter a valid amount."); }
        if (!Double.isFinite(amount) || amount <= 0) throw new IllegalArgumentException("Amount must be positive.");

        String sql = "INSERT INTO fees(reg_no, amount, paid_at) VALUES (?, ?, CURRENT_TIMESTAMP)";
        try (Connection c = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, regNo.trim());
            ps.setDouble(2, amount);
            ps.executeUpdate();
        }
        return String.format("Payment recorded: Rs. %.2f", amount);
    }
}