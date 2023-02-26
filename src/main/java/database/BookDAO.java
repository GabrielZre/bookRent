package database;

import products.Book;
import products.LoanedBook;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

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

    public List<LoanedBook> getLoanedBooks() {
        ArrayList<LoanedBook> result = new ArrayList<>();
        try {
            String sql = "SELECT tbook.title, tbook.author, tbook.isbn, tloan.start_date, " +
                    "tloan.end_date, tloan.loan_is_active, tuser.login FROM tloan " +
                    "INNER JOIN tbook ON tbook.id=tloan.book_id " +
                    "INNER JOIN tuser ON tuser.id=tloan.user_id " +
                    "WHERE tloan.loan_is_active = 1";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                LocalDate startDate = rs.getDate("start_date").toLocalDate();
                LocalDate endDate = rs.getDate("end_date").toLocalDate();
                Boolean isLoan = rs.getBoolean("loan_is_active");
                String user = rs.getString("login");
                LoanedBook loanedBook = new LoanedBook(title, author, isbn,
                        startDate, endDate, isLoan, user);
                result.add(loanedBook);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<LoanedBook> getNotGivenAwayBooks() {
        ArrayList<LoanedBook> result = new ArrayList<>();
        try {
            String sql = "SELECT tbook.title, tbook.author, tbook.isbn, tloan.start_date, tloan.end_date, " +
                    "(SELECT CURDATE()) AS actual_date, tloan.loan_is_active, " +
                    "tuser.login FROM tloan " +
                    "INNER JOIN tbook ON tbook.id = tloan.book_id " +
                    "INNER JOIN tuser ON tuser.id = tloan.user_id " +
                    "WHERE tloan.loan_is_active = 1 AND CURDATE() > tloan.end_date;";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                LocalDate startDate = rs.getDate("start_date").toLocalDate();
                LocalDate actualDate = rs.getDate("actual_date").toLocalDate();
                LocalDate endDate = rs.getDate("end_date").toLocalDate();
                Boolean isLoan = rs.getBoolean("loan_is_active");
                String user = rs.getString("login");
                LoanedBook loanedBook = new LoanedBook(title, author, isbn,
                        startDate, endDate, isLoan, user);
                result.add(loanedBook);
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
            while (rs.next()) {
                boolean loan = rs.getBoolean("loan_is_active");
                if(loan) {
                    return false;
                }
            }
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

        } catch (SQLException e) {}
        return false;
    }



    public void addBook(Book book) {
        try {
            String sql = "INSERT INTO tbook " +
                    "(title, author, isbn) VALUES (?,?,?)";

            PreparedStatement ps = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());

            ps.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


    public static BookDAO getInstance() {
        return instance;
    }

}
