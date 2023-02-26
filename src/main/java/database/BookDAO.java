package database;

import products.Book;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BookDAO {

    private final Connection connection;


    private static final BookDAO instance = new BookDAO();

    private BookDAO() {
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

    public List<Book> getBooks() {
        ArrayList<Book> result = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tbook";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                Book book = new Book(title, author, isbn);
                result.add(book);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    public boolean loanBook(String isbn, String login, LocalDate startDate, LocalDate endDate) {
        try {
            String sql = "SELECT * FROM tloan INNER JOIN tbook ON tbook.id = tloan.book_id WHERE tbook.isbn = ?";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                boolean loan = rs.getBoolean("loan_is_active");
                if(!loan) {
                    String updateSql = "UPDATE tvehicle SET rent = ? WHERE id = ?";
                    String insertSql = "INSERT INTO tloan " +
                            "(tloan.start_date, tloan.end_date, tloan.book_id, tloan.user_id, " +
                            "tloan.loan_is_active) " +
                            "VALUES(?, ?, " +
                            "(SELECT tbook.id FROM tbook WHERE tbook.isbn=?), " +
                            "(SELECT tuser.id FROM tuser WHERE tuser.login = ?), ?)";

                    PreparedStatement updatePs = this.connection.prepareStatement(insertSql);

                    updatePs.setDate(1, Date.valueOf(startDate));
                    updatePs.setDate(2, Date.valueOf(endDate));
                    updatePs.setString(3, isbn);
                    updatePs.setString(4, login);
                    updatePs.setBoolean(5, true);

                    updatePs.executeUpdate();
                    return true;
                }
            }
        } catch (SQLException e) {}
        return false;
    }

    /*public boolean loanBook(String isbn, String login) {
        try {
            String sql = "INSERT INTO tloan " +
                    "(tloan.start_date, tloan.end_date, tloan.book_id, tloan.user_id, " +
                    "tloan.loan_is_active) " +
                    "VALUES(?, ?, " +
                    "(SELECT tbook.id FROM tbook WHERE tbook.isbn=?), " +
                    "(SELECT tuser.id FROM tuser WHERE tuser.login = ?), 1)";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, isbn);
            ps.setString(1, isbn);
            ps.setString(1, isbn);
            ps.setString(1, isbn);
            ps.setString(1, login);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
          //      boolean loan = rs.getBoolean()
            }
        } catch (SQLException e) {
            return false;
        }
        return false;

    }*/

    public void addBook(Book book) {
        try {
            String sql = "INSERT INTO tbook " +
                    "(title, author, isbn) VALUES (?,?,?)";

            PreparedStatement ps = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());

            ResultSet rs = ps.getGeneratedKeys();
            //rs.next();
            //int bookId = rs.getInt(1);
            //String sql = "INSERT INTO tloan " +
            //        "(start_date, end_date, book_id, user_id, loan_is_active)";
            //ps.setString(1, start_date);
            //ps.setString(2, end_date);
            //ps.setString(3, book_id);
            //ps.setString(4, user_id);
            //ps.setString(5, loan_is_active);
            ps.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


    public static BookDAO getInstance() {
        return instance;
    }

}
