package core;

import database.BookDAO;
import database.UserDAO;
import gui.GUI;
import products.User;

import java.time.LocalDate;

public class Core {
    final BookDAO bookDAO = BookDAO.getInstance();
    final UserDAO userDAO = UserDAO.getInstance();
    final Authenticator authenticator = Authenticator.getInstance();
    final GUI gui = GUI.getInstance();
    private static final Core instance = new Core();


    private Core() {

    }
    boolean isRunning = false;

    public void start() {
        int counter =  0;
        boolean isLogged = false;
        while(!isLogged){
            switch (this.gui.showLoginMenu()) {
                case "1":
                    while(!isRunning && counter < 3) {
                        this.authenticator.authenticate(this.gui.readLoginAndPassword());
                        isRunning = this.authenticator.getLoggedUser() != null;
                        if(!isRunning) {
                            System.out.println("Not authorized !!");
                        }
                        else
                        {
                            isLogged = true;
                        }
                        counter++;
                    }
                    break;
                case "2":
                    this.userDAO.addUser(this.gui.register());
                    break;
                case "3":
                    isLogged = true;
                    break;
            }
        }




        while(isRunning) {
            switch (this.gui.showMenu()) {
                case "1":
                    this.gui.listBooks();
                    break;
                case "2":
                    LocalDate date = LocalDate.now();
                    this.gui.showRentResult(this.bookDAO.loanBook(this.gui.readIsbn(), this.authenticator.getLoggedUser().getLogin(), date, date.plusDays(14), this.gui.readName(), this.gui.readSurname()));
                    break;
                case "3":
                    this.gui.showReturnBookResult(this.bookDAO.returnBook(this.gui.readIsbn(), authenticator.getLoggedUser().getLogin()));
                    break;
                case "4":
                    this.gui.listSearchBooks();
                    break;
                case "5":
                    this.gui.listLoanedBooks();
                    break;
                case "6":
                    isRunning = false;
                    break;

                case "7":
                    this.authenticator.Logout();
                    isRunning = false;
                    System.out.println("Logged Out Successfully!");
                    start();
                    break;

                case "8":
                    if(this.authenticator.getLoggedUser() != null &&
                            this.authenticator.getLoggedUser().getRole() == User.Role.ADMIN) {
                        this.gui.showAddBookResult(this.bookDAO.addBook(this.gui.readNewBookData()));
                        break;
                    }

                case "9":
                    if(this.authenticator.getLoggedUser() != null &&
                            this.authenticator.getLoggedUser().getRole() == User.Role.ADMIN) {
                        this.gui.listNotGivenAwayBooks();
                        break;
                    }


                default:
                    System.out.println("Wrong choose !!");
                    break;
            }
        }
    }


    public static Core getInstance(){
        return instance;
    }
}
