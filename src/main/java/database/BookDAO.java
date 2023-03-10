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

    public List<Book> getSearchBooks(String keyword) {
        ArrayList<Book> result = new ArrayList<>();
        String regex = "%" + keyword + "%"; // add wildcards to the keyword
        try {
            String sql = "SELECT tbook.title, tbook.author, tbook.isbn FROM tbook WHERE tbook.title LIKE ? OR tbook.author LIKE ? OR tbook.isbn LIKE ?";

            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, regex);
            ps.setString(2, regex);
            ps.setString(3, regex);

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
                    "tloan.end_date, tloan.loan_is_active, tuser.login, tloan.name, tloan.surname FROM tloan " +
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
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                LoanedBook loanedBook = new LoanedBook(title, author, isbn,
                        startDate, endDate, isLoan, user, name, surname);
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
                    "tuser.login, tloan.name, tloan.surname FROM tloan " +
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
                LocalDate endDate = rs.getDate("end_date").toLocalDate();
                Boolean isLoan = rs.getBoolean("loan_is_active");
                String user = rs.getString("login");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                LoanedBook loanedBook = new LoanedBook(title, author, isbn,
                        startDate, endDate, isLoan, user, name, surname);
                result.add(loanedBook);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public boolean loanBook(String isbn, String login, LocalDate startDate, LocalDate endDate, String name, String surname) {
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
                    "tloan.loan_is_active, tloan.name, tloan.surname) " +
                    "VALUES(?, ?, " +
                    "(SELECT tbook.id FROM tbook WHERE tbook.isbn=?), " +
                    "(SELECT tuser.id FROM tuser WHERE tuser.login = ?), ?, ?, ?)";

            PreparedStatement updatePs = this.connection.prepareStatement(insertSql);

            updatePs.setDate(1, Date.valueOf(startDate));
            updatePs.setDate(2, Date.valueOf(endDate));
            updatePs.setString(3, isbn);
            updatePs.setString(4, login);
            updatePs.setBoolean(5, true);
            updatePs.setString(6, name);
            updatePs.setString(7, surname);

            updatePs.executeUpdate();
            return true;

        } catch (SQLException e) {}
        return false;
    }

    public boolean returnBook(String isbn, String user) {
        try {
            String sql = "SELECT * FROM tloan INNER JOIN tbook ON tbook.id = tloan.book_id INNER JOIN tuser ON tuser.id=tloan.user_id WHERE tbook.isbn = ? AND tuser.login= ? AND tloan.loan_is_active = ?";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, isbn);
            ps.setString(2, user);
            ps.setBoolean(3, true);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String updateSql = "UPDATE tloan INNER JOIN tbook ON tbook.id = tloan.book_id I" +
                        "NNER JOIN tuser ON tuser.id = tloan.user_id SET tloan.loan_is_active = ? " +
                        "WHERE tbook.isbn = ? AND tuser.login = ?";

                PreparedStatement updatePs = this.connection.prepareStatement(updateSql);

                updatePs.setBoolean(1, false);
                updatePs.setString(2, isbn);
                updatePs.setString(3, user);

                updatePs.executeUpdate();

                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public boolean addBook(Book book) {
        if(!book.getTitle().isEmpty() && !book.getAuthor().isEmpty() && book.getIsbn().length() == 13 && book.getIsbn().matches("\\d+" )) {
            try {
                String sql = "INSERT INTO tbook " +
                        "(title, author, isbn) VALUES (?,?,?)";

                PreparedStatement ps = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, book.getTitle());
                ps.setString(2, book.getAuthor());
                ps.setString(3, book.getIsbn());

                ps.executeUpdate();


            } catch (SQLException e){
                return false; // exception occurs when isbn is not unique, maybe bad habit but it's an console app
            }
            return true;
        }
        else {
            return false;
        }
    }


    public static BookDAO getInstance() {
        return instance;
    }

}
