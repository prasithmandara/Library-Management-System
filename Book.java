import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String author;
    private String genre;
    private int year;
    private boolean isAvailable;
    private String borrowedBy;  // member name who borrowed the book

    public Book(int id, String title, String author, String genre, int year) {
        this.id          = id;
        this.title       = title;
        this.author      = author;
        this.genre       = genre;
        this.year        = year;
        this.isAvailable = true;
        this.borrowedBy  = null;
    }

    // Getters
    public int getId()           { return id; }
    public String getTitle()     { return title; }
    public String getAuthor()    { return author; }
    public String getGenre()     { return genre; }
    public int getYear()         { return year; }
    public boolean isAvailable() { return isAvailable; }
    public String getBorrowedBy(){ return borrowedBy; }

    // Setters
    public void setTitle(String title)   { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setGenre(String genre)   { this.genre = genre; }
    public void setYear(int year)        { this.year = year; }

    // Borrow / Return
    public void borrow(String memberName) {
        this.isAvailable = false;
        this.borrowedBy  = memberName;
    }

    public void returnBook() {
        this.isAvailable = true;
        this.borrowedBy  = null;
    }

    @Override
    public String toString() {
        String status = isAvailable ? "Available" : "Borrowed by: " + borrowedBy;
        return String.format("| %-4d | %-30s | %-20s | %-12s | %-4d | %-20s |",
                id, title, author, genre, year, status);
    }
}