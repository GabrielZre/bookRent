package database;

import products.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final Connection connection;

    private static final UserDAO instance = new UserDAO();

    private UserDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bookrent",
                    "root",
                    "");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByLogin(String login) {
        try {
            String sql = "SELECT * FROM tuser WHERE login = ?";

            PreparedStatement ps = this.connection.prepareStatement(sql);

            ps.setString(1, login);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new User(
                        rs.getString("login"),
                        rs.getString("password"),
                        User.Role.valueOf(rs.getString("role")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean addUser(User user) {
        try {
            String sql = "SELECT * FROM tuser WHERE tuser.login = ?";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, user.getLogin());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return false;
            }
            String insertSql = "INSERT INTO tuser (tuser.login, tuser.password, tuser.role) VALUES (?,?,?)";

            PreparedStatement updatePs = this.connection.prepareStatement(insertSql);

            updatePs.setString(1, user.getLogin());
            updatePs.setString(2, user.getPassword());
            updatePs.setString(3, String.valueOf(user.getRole()));

            updatePs.executeUpdate();
            return true;

        } catch (SQLException e) {}
        return false;
    }

    public static UserDAO getInstance() {
        return instance;
    }

}
