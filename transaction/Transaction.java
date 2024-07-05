package transaction;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {

    private String transactionID;
    private String userID;
    private Long bookID;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private TransactionType transactionType;


    public Transaction(String transactionID, String userID, Long bookID, LocalDateTime borrowDate, LocalDateTime returnDate, TransactionType transactionType) {
        this.transactionID = transactionID;
        this.userID = userID;
        this.bookID = bookID;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.transactionType = transactionType;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Long getBookID() {
        return bookID;
    }

    public void setBookID(Long bookID) {
        this.bookID = bookID;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return Objects.equals(transactionID, that.transactionID) && Objects.equals(userID, that.userID) && Objects.equals(bookID, that.bookID) && Objects.equals(borrowDate, that.borrowDate) && Objects.equals(returnDate, that.returnDate) && transactionType == that.transactionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionID, userID, bookID, borrowDate, returnDate, transactionType);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionID='" + transactionID + '\'' +
                ", userID='" + userID + '\'' +
                ", bookID=" + bookID +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                ", transactionType=" + transactionType +
                '}';
    }
}

