package transaction;

import java.time.LocalDateTime;

public class BorrowedBookTransaction {

    private String transactionID;
    private String userID;
    private Long bookID;
    private String title;
    private String author;
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;
    private TransactionType transactionType;


    public BorrowedBookTransaction(String transactionID, String userID, Long bookID, String title, String author, LocalDateTime borrowTime, LocalDateTime returnTime, TransactionType transactionType) {
        this.transactionID = transactionID;
        this.userID = userID;
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.borrowTime = borrowTime;
        this.returnTime = returnTime;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(LocalDateTime borrowTime) {
        this.borrowTime = borrowTime;
    }

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDateTime returnTime) {
        this.returnTime = returnTime;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public static TransactionType parseTransactionType(String typeStr) {
        return switch (typeStr) {
            case "BORROW" -> TransactionType.BORROW;
            case "RETURN" -> TransactionType.RETURN;
            default -> throw new IllegalArgumentException("Unknown TransactionType: " + typeStr);
        };
    }

}
