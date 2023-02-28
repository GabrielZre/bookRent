
package database;

import org.junit.jupiter.api.*;
import products.BookTest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookDAOTest {

    private final Connection connection;


    private static final BookDAOTest instance = new BookDAOTest();

    private BookDAOTest() {
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

    @BeforeAll
    public static void createTable() {
        try {
            Connection connection;

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bookrent",
                    "root",
                    "");

            String sql = "CREATE TABLE tbooktest LIKE tbook";

            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void clearTable() {
        try {
            String sql = "DELETE FROM tbooktest";

            PreparedStatement ps = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void deleteTable() {
        try {
            Connection connection;

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bookrent",
                    "root",
                    "");

            String sql = "DROP table tbooktest";

            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAddBook() {
        ArrayList<BookTest> expectedResult = new ArrayList<>();
        String title = "Test Book";
        String author = "Test Author";
        String isbn = "1234567890123";
        BookTest expectedBook = new BookTest(title, author, isbn);
        expectedResult.add(expectedBook);

        addBook(expectedBook);

        assertEquals(expectedResult.size(), getSearchBooks(isbn).size());

        List<BookTest> actualResult = getSearchBooks(isbn);
        IntStream.range(0, expectedResult.size())
                .forEach(i -> {
                    BookTest expected = expectedResult.get(i);
                    BookTest actual = actualResult.get(i);
                    assertEquals(expected.getTitle(), actual.getTitle());
                    assertEquals(expected.getAuthor(), actual.getAuthor());
                    assertEquals(expected.getIsbn(), actual.getIsbn());
                });
    }

    @Test
    public void testAddEmptyBook() {
        ArrayList<BookTest> expectedResult = new ArrayList<>();
        String title = "";
        String author = "";
        String isbn = "";
        BookTest expectedBook = new BookTest(title, author, isbn);
        expectedResult.add(expectedBook);

        addBook(expectedBook);

        Assertions.assertTrue(getSearchBooks(isbn).isEmpty());
    }

    @Test
    public void testAddBadIsbnBook() {
        ArrayList<BookTest> expectedResult = new ArrayList<>();
        String title = "Harry Potter";
        String author = "Hagrid";
        String isbn = "abc123abc123a";
        BookTest expectedBook = new BookTest(title, author, isbn);
        expectedResult.add(expectedBook);

        addBook(expectedBook);

        Assertions.assertTrue(getSearchBooks(isbn).isEmpty());
    }


    public boolean addBook(BookTest bookTest) {
        if (!bookTest.getTitle().isEmpty() && !bookTest.getAuthor().isEmpty() && bookTest.getIsbn().length() == 13 && bookTest.getIsbn().matches("\\d+")) {
            try {
                String sql = "INSERT INTO tbooktest " +
                        "(title, author, isbn) VALUES (?,?,?)";

                PreparedStatement ps = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, bookTest.getTitle());
                ps.setString(2, bookTest.getAuthor());
                ps.setString(3, bookTest.getIsbn());

                ps.executeUpdate();


            } catch (SQLException e) {
                return false; //  exception occurs when isbn is not unique, maybe bad habit but it's an console app
            }
            return true;
        } else {
            return false;
        }
    }

    public List<BookTest> getSearchBooks(String keyword) {
        ArrayList<BookTest> result = new ArrayList<>();
        String regex = "%" + keyword + "%"; // add wildcards to the keyword
        try {
            String sql = "SELECT tbooktest.title, tbooktest.author, tbooktest.isbn FROM tbooktest " +
                    "WHERE tbooktest.title LIKE ? OR tbooktest.author LIKE ? OR tbooktest.isbn LIKE ?";

            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, regex);
            ps.setString(2, regex);
            ps.setString(3, regex);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                BookTest bookTest = new BookTest(title, author, isbn);
                result.add(bookTest);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}

