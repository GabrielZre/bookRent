package products;

import java.util.Date;

public class Loan {
    Date startDate;
    Date endDate;
    String isLoan;

    public Loan(Date startDate, Date endDate, String isLoan) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.isLoan = isLoan;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getIsLoan() {
        return isLoan;
    }

    public void setIsLoan(String isLoan) {
        this.isLoan = isLoan;
    }
}
