package gui;

import core.Authenticator;
import database.BookDAO;
import database.UserDAO;
import org.apache.commons.codec.digest.DigestUtils;
import products.*;

import java.time.LocalDate;
import java.util.Scanner;

public class GUI {
    private final Scanner scanner = new Scanner(System.in);
    final Authenticator authenticator = Authenticator.getInstance();
    final BookDAO bookDAO = BookDAO.getInstance();
    final UserDAO userDAO = UserDAO.getInstance();
    private static final GUI instance = new GUI();

    private GUI() {
    }

    public String showMenu(){
        System.out.println("1. List books");
        System.out.println("2. Loan book");
        System.out.println("3. Return Book");
        System.out.println("4. Search Books");
        System.out.println("5. List Loaned Books");
        System.out.println("6. Exit");
        System.out.println("7. Logout");
        if (this.authenticator.getLoggedUser() != null &&
                this.authenticator.getLoggedUser().getRole() == User.Role.ADMIN) {
            System.out.println("8. Add book");
            System.out.println("9. List not given away Books");
        }
        return scanner.nextLine();
    }

    public String showLoginMenu(){
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        return scanner.nextLine();
    }


    public void listBooks() {
        for (Book book : this.bookDAO.getBooks()) {
            System.out.println(book);
        }
    }

    public void listSearchBooks() {
        System.out.println("Search keyword: ");
        String keyword = this.scanner.nextLine();

        for (Book book : this.bookDAO.getSearchBooks(keyword)) {
            System.out.println(book);
        }
    }


    public void listLoanedBooks() {
        for (LoanedBook loanedBook : this.bookDAO.getLoanedBooks()) {
            System.out.println(loanedBook);
        }
    }

    public void listNotGivenAwayBooks() {
        for (LoanedBook loanedBook : this.bookDAO.getNotGivenAwayBooks()) {
            System.out.println(loanedBook);
        }
    }


    public String readUser() {
        System.out.println("User:");
        return this.scanner.nextLine();
    }

    public String readRole() {
        System.out.println("Role:");

        return this.scanner.nextLine();
    }


    public void showReturnBookResult(boolean result) {
        if(result) {
            System.out.println("Book returned successfully");
        } else {
            System.out.println("You have not loaned this book actually");
        }
    }

    public void showAddBookResult(boolean result) {
        if(result) {
            System.out.println("Book added successfully");
        } else {
            System.out.println("Please check if the values are correct and isbn are unique 13 digits");
        }
    }

    public User register() {
        System.out.println("Login:");
        String login = this.scanner.nextLine();
        System.out.println("Password:");
        String password = this.scanner.nextLine();
        return new User(login, DigestUtils.md5Hex(password + Authenticator.getSeed()), User.Role.USER);
        }




    public User readLoginAndPassword() {
        User user = new User();
        System.out.println("Login:");
        user.setLogin(this.scanner.nextLine());
        System.out.println("Password:");
        user.setPassword(this.scanner.nextLine());
        return user;
    }

    public Book readNewBookData() {
        System.out.println("Title: ");
        String title = this.scanner.nextLine();
        System.out.println("Author: ");
        String author = this.scanner.nextLine();
        System.out.println("Isbn: ");
        String isbn = this.scanner.nextLine();

        return new Book(title, author, isbn);
    }


    public String readIsbn() {
        System.out.println("Isbn:");
        return this.scanner.nextLine();
    }

    public String readName() {
        System.out.println("Name:");
        return this.scanner.nextLine();
    }

    public String readSurname() {
        System.out.println("Surname:");
        return this.scanner.nextLine();
    }


    public void showRentResult(boolean result) {
        if(result) {
            System.out.println("Loan successful");
        } else {
            System.out.println("Iban does not exist or book is already loaned");
        }
    }

    public static GUI getInstance() {
        return instance;
    }
}
