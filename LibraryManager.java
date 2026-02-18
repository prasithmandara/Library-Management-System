import java.io.*;
import java.util.*;

public class LibraryManager {

    private List<Book>   books   = new ArrayList<>();
    private List<Member> members = new ArrayList<>();

    private final String BOOKS_FILE   = "books.dat";
    private final String MEMBERS_FILE = "members.dat";

    private int nextBookId   = 1;
    private int nextMemberId = 1;

    public LibraryManager() {
        loadFromFile();
        for (Book b   : books)   if (b.getId()   >= nextBookId)   nextBookId   = b.getId()   + 1;
        for (Member m : members) if (m.getId()   >= nextMemberId) nextMemberId = m.getId()   + 1;
    }

    // ==============================
    //        BOOK OPERATIONS
    // ==============================

    public void addBook(String title, String author, String genre, int year) {
        books.add(new Book(nextBookId++, title, author, genre, year));
        saveToFile();
        System.out.println("Book added successfully!");
    }

    public void viewAllBooks() {
        if (books.isEmpty()) { System.out.println("No books in library."); return; }
        printBookHeader();
        for (Book b : books) System.out.println(b);
        printBookFooter();
    }

    public void viewAvailableBooks() {
        List<Book> available = new ArrayList<>();
        for (Book b : books) if (b.isAvailable()) available.add(b);
        if (available.isEmpty()) { System.out.println("No available books."); return; }
        System.out.println("Available Books:");
        printBookHeader();
        for (Book b : available) System.out.println(b);
        printBookFooter();
    }

    public void searchBookByTitle(String title) {
        List<Book> results = new ArrayList<>();
        for (Book b : books)
            if (b.getTitle().toLowerCase().contains(title.toLowerCase())) results.add(b);
        printSearchResults(results, "title: " + title);
    }

    public void searchBookByAuthor(String author) {
        List<Book> results = new ArrayList<>();
        for (Book b : books)
            if (b.getAuthor().toLowerCase().contains(author.toLowerCase())) results.add(b);
        printSearchResults(results, "author: " + author);
    }

    public void searchBookByGenre(String genre) {
        List<Book> results = new ArrayList<>();
        for (Book b : books)
            if (b.getGenre().toLowerCase().contains(genre.toLowerCase())) results.add(b);
        printSearchResults(results, "genre: " + genre);
    }

    public void updateBook(int id, String title, String author, String genre, int year) {
        Book b = findBookById(id);
        if (b != null) {
            b.setTitle(title); b.setAuthor(author); b.setGenre(genre); b.setYear(year);
            saveToFile();
            System.out.println("Book updated successfully!");
        } else {
            System.out.println("Book ID " + id + " not found.");
        }
    }

    public void deleteBook(int id) {
        Book b = findBookById(id);
        if (b == null) { System.out.println("Book ID " + id + " not found."); return; }
        if (!b.isAvailable()) { System.out.println("Cannot delete a borrowed book."); return; }
        books.remove(b);
        saveToFile();
        System.out.println("Book deleted successfully!");
    }

    // ==============================
    //       MEMBER OPERATIONS
    // ==============================

    public void addMember(String name, String email) {
        members.add(new Member(nextMemberId++, name, email));
        saveToFile();
        System.out.println("Member registered successfully!");
    }

    public void viewAllMembers() {
        if (members.isEmpty()) { System.out.println("No members registered."); return; }
        System.out.println("+------+----------------------+---------------------------+-------------+");
        System.out.println("| ID   | Name                 | Email                     | Borrowed    |");
        System.out.println("+------+----------------------+---------------------------+-------------+");
        for (Member m : members) System.out.println(m);
        System.out.println("+------+----------------------+---------------------------+-------------+");
    }

    public void searchMemberByName(String name) {
        for (Member m : members) {
            if (m.getName().toLowerCase().contains(name.toLowerCase())) {
                System.out.println("Member Found:");
                System.out.println("   ID     : " + m.getId());
                System.out.println("   Name   : " + m.getName());
                System.out.println("   Email  : " + m.getEmail());
                System.out.println("   Books  : " + (m.getBorrowedBooks().isEmpty() ? "None" : m.getBorrowedBooks()));
                return;
            }
        }
        System.out.println("Member not found: " + name);
    }

    public void deleteMember(int id) {
        Member m = findMemberById(id);
        if (m == null) { System.out.println("Member ID " + id + " not found."); return; }
        if (!m.getBorrowedBooks().isEmpty()) { System.out.println("Member has borrowed books. Return them first."); return; }
        members.remove(m);
        saveToFile();
        System.out.println("Member deleted successfully!");
    }

    // ==============================
    //     BORROW & RETURN
    // ==============================

    public void borrowBook(int bookId, int memberId) {
        Book b   = findBookById(bookId);
        Member m = findMemberById(memberId);
        if (b == null)  { System.out.println("Book ID " + bookId + " not found.");     return; }
        if (m == null)  { System.out.println("Member ID " + memberId + " not found."); return; }
        if (!b.isAvailable()) {
            System.out.println("Book is already borrowed by: " + b.getBorrowedBy());
            return;
        }
        b.borrow(m.getName());
        m.addBorrowedBook(b.getTitle());
        saveToFile();
        System.out.printf("'%s' borrowed by %s successfully!%n", b.getTitle(), m.getName());
    }

    public void returnBook(int bookId, int memberId) {
        Book b   = findBookById(bookId);
        Member m = findMemberById(memberId);
        if (b == null)  { System.out.println("Book ID " + bookId + " not found.");     return; }
        if (m == null)  { System.out.println("Member ID " + memberId + " not found."); return; }
        if (b.isAvailable()) { System.out.println("This book was not borrowed."); return; }
        b.returnBook();
        m.removeBorrowedBook(b.getTitle());
        saveToFile();
        System.out.printf("'%s' returned successfully!%n", b.getTitle());
    }

    // ==============================
    //       STATISTICS
    // ==============================

    public void showStatistics() {
        long available = books.stream().filter(Book::isAvailable).count();
        long borrowed  = books.size() - available;
        System.out.println("\n ===== Library Statistics =====");
        System.out.println("  Total Books     : " + books.size());
        System.out.println("  Available Books : " + available);
        System.out.println("  Borrowed Books  : " + borrowed);
        System.out.println("  Total Members   : " + members.size());
        System.out.println("================================\n");
    }

    // ==============================
    //          HELPERS
    // ==============================

    private Book findBookById(int id) {
        for (Book b : books) if (b.getId() == id) return b;
        return null;
    }

    private Member findMemberById(int id) {
        for (Member m : members) if (m.getId() == id) return m;
        return null;
    }

    private void printSearchResults(List<Book> results, String query) {
        if (results.isEmpty()) { System.out.println("No books found for " + query); return; }
        System.out.println("Search Results for " + query + ":");
        printBookHeader();
        for (Book b : results) System.out.println(b);
        printBookFooter();
    }

    private void printBookHeader() {
        System.out.println("+------+--------------------------------+----------------------+--------------+------+----------------------+");
        System.out.println("| ID   | Title                          | Author               | Genre        | Year | Status               |");
        System.out.println("+------+--------------------------------+----------------------+--------------+------+----------------------+");
    }

    private void printBookFooter() {
        System.out.println("+------+--------------------------------+----------------------+--------------+------+----------------------+");
    }

    // ---- FILE: Save ----
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKS_FILE))) {
            oos.writeObject(books);
        } catch (IOException e) { System.out.println("Error saving books: " + e.getMessage()); }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MEMBERS_FILE))) {
            oos.writeObject(members);
        } catch (IOException e) { System.out.println("Error saving members: " + e.getMessage()); }
    }

    // ---- FILE: Load ----
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File bf = new File(BOOKS_FILE);
        if (bf.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKS_FILE))) {
                books = (List<Book>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) { System.out.println("Could not load books."); }
        }

        File mf = new File(MEMBERS_FILE);
        if (mf.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MEMBERS_FILE))) {
                members = (List<Member>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) { System.out.println("Could not load members."); }
        }
    }
}