import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String email;
    private List<String> borrowedBooks; // list of book titles

    public Member(int id, String name, String email) {
        this.id           = id;
        this.name         = name;
        this.email        = email;
        this.borrowedBooks = new ArrayList<>();
    }

    // Getters
    public int getId()                   { return id; }
    public String getName()              { return name; }
    public String getEmail()             { return email; }
    public List<String> getBorrowedBooks() { return borrowedBooks; }

    // Setters
    public void setName(String name)   { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    public void addBorrowedBook(String bookTitle)    { borrowedBooks.add(bookTitle); }
    public void removeBorrowedBook(String bookTitle) { borrowedBooks.remove(bookTitle); }

    @Override
    public String toString() {
        return String.format("| %-4d | %-20s | %-25s | %-5d books |",
                id, name, email, borrowedBooks.size());
    }
}