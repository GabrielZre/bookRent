package core;

import database.BookDAO;
import database.UserDAO;
import gui.GUI;
import products.User;

import java.time.LocalDate;
import java.util.Date;

public class Core {
    final BookDAO bookDAO = BookDAO.getInstance();
    final UserDAO userDB = UserDAO.getInstance();
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
                            //LocalDate date = LocalDate.now();
                            //System.out.println(date);
                            //System.out.println(date.plusDays(14));
                        }
                        counter++;
                    }
                    break;
                case "2":
                    //this.userDAO.addUser(this.gui.register());
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
                    this.gui.showRentResult(this.bookDAO.loanBook(this.gui.readIban(), this.authenticator.getLoggedUser().getLogin(), date, date.plusDays(14)));
                    break;
                case "3":
                    isRunning = false;
                    break;

                case "4":
                    this.authenticator.Logout();
                    isRunning = false;
                    System.out.println("Logged Out Successfully!");
                    start();
                    break;

                case "5":
                    if(this.authenticator.getLoggedUser() != null &&
                            this.authenticator.getLoggedUser().getRole() == User.Role.ADMIN) {
                        this.bookDAO.addBook(this.gui.readNewBookData());
                        break;
                    }
                case "6":
                    //if(this.authenticator.getLoggedUser() != null &&
                     //       this.authenticator.getLoggedUser().getRole() == User.Role.ADMIN) {
                     //   this.gui.listUsers();
                     //   this.gui.showChangeRoleResult(this.userDB.changeUserRole(this.gui.readUser(), this.gui.readRole()));
                        break;
                    //}
                case "7":
                    if(this.authenticator.getLoggedUser() != null &&
                            this.authenticator.getLoggedUser().getRole() == User.Role.ADMIN) {
                        this.gui.listLoanedBooks();
                        //this.gui.listElectricVehicles();
                        //this.gui.showAddStockResult(this.electricVehicleDB.addStock(this.gui.readCode(),this.gui.readAmount()));
                        break;
                    }
                case "8":
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
