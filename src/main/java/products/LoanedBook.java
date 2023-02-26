package products;

import java.sql.Date;
import java.time.LocalDate;

public final class LoanedBook extends Book{
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isLoan;
    private String user;

    public LoanedBook(String title, String author, String isbn,
                      LocalDate startDate, LocalDate endDate, Boolean isLoan, String user) {
        super(title, author, isbn);
        this.startDate = startDate;
        this.endDate = endDate;
        this.isLoan = isLoan;
        this.user = user;
    }

    public LoanedBook() {
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getUser() { return user; }

    public void setUser(String user) { this.user = user; }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getLoan() {
        return isLoan;
    }

    public void setLoan(Boolean loan) {
        isLoan = loan;
    }

    @Override
    public String toString() {
        return new StringBuilder(super.toString())
                .append(" Loan date: ")
                .append(this.startDate)
                .append(" End date: ")
                .append(this.endDate)
                .append(" Loaned: ")
                .append(this.isLoan)
                .append(" To user: ")
                .append(this.user)
                .toString();
    }
}
