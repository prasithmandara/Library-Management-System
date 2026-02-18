import java.util.Scanner;

public class LibraryMain {
    private static Scanner scanner       = new Scanner(System.in);
    private static LibraryManager library = new LibraryManager();

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      LIBRARY MANAGEMENT SYSTEM       ║");
        System.out.println("╚══════════════════════════════════════╝");

        while (true) {
            printMainMenu();
            int choice = getIntInput("Enter choice: ");
            switch (choice) {
                case 1 -> bookMenu();
                case 2 -> memberMenu();
                case 3 -> borrowReturnMenu();
                case 4 -> library.showStatistics();
                case 5 -> { System.out.println("Goodbye!"); System.exit(0); }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ---- MENUS ----

    private static void printMainMenu() {
        System.out.println("\n========= MAIN MENU =========");
        System.out.println(" 1. Book Management");
        System.out.println(" 2. Member Management");
        System.out.println(" 3. Borrow / Return Book");
        System.out.println(" 4. Library Statistics");
        System.out.println(" 5. Exit");
        System.out.println("==============================");
    }

    private static void bookMenu() {
        System.out.println("\n--- Book Management ---");
        System.out.println(" 1. Add Book");
        System.out.println(" 2. View All Books");
        System.out.println(" 3. View Available Books");
        System.out.println(" 4. Search by Title");
        System.out.println(" 5. Search by Author");
        System.out.println(" 6. Search by Genre");
        System.out.println(" 7. Update Book");
        System.out.println(" 8. Delete Book");
        System.out.println(" 9. Back");

        int choice = getIntInput("Enter choice: ");
        switch (choice) {
            case 1 -> addBook();
            case 2 -> library.viewAllBooks();
            case 3 -> library.viewAvailableBooks();
            case 4 -> library.searchBookByTitle(getStringInput("Enter Title  : "));
            case 5 -> library.searchBookByAuthor(getStringInput("Enter Author : "));
            case 6 -> library.searchBookByGenre(getStringInput("Enter Genre  : "));
            case 7 -> updateBook();
            case 8 -> library.deleteBook(getIntInput("Enter Book ID to Delete: "));
            case 9 -> { /* back */ }
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void memberMenu() {
        System.out.println("\n--- Member Management ---");
        System.out.println(" 1. Register Member");
        System.out.println(" 2. View All Members");
        System.out.println(" 3. Search Member by Name");
        System.out.println(" 4. Delete Member");
        System.out.println(" 5. Back");

        int choice = getIntInput("Enter choice: ");
        switch (choice) {
            case 1 -> {
                String name  = getStringInput("Member Name  : ");
                String email = getStringInput("Member Email : ");
                library.addMember(name, email);
            }
            case 2 -> library.viewAllMembers();
            case 3 -> library.searchMemberByName(getStringInput("Enter Name: "));
            case 4 -> library.deleteMember(getIntInput("Enter Member ID to Delete: "));
            case 5 -> { /* back */ }
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void borrowReturnMenu() {
        System.out.println("\n--- Borrow / Return ---");
        System.out.println(" 1. Borrow Book");
        System.out.println(" 2. Return Book");
        System.out.println(" 3. Back");

        int choice = getIntInput("Enter choice: ");
        switch (choice) {
            case 1 -> {
                int bookId   = getIntInput("Enter Book ID   : ");
                int memberId = getIntInput("Enter Member ID : ");
                library.borrowBook(bookId, memberId);
            }
            case 2 -> {
                int bookId   = getIntInput("Enter Book ID   : ");
                int memberId = getIntInput("Enter Member ID : ");
                library.returnBook(bookId, memberId);
            }
            case 3 -> { /* back */ }
            default -> System.out.println("Invalid choice.");
        }
    }

    // ---- Book Add / Update ----

    private static void addBook() {
        System.out.println("\n--- Add New Book ---");
        String title  = getStringInput("Title  : ");
        String author = getStringInput("Author : ");
        String genre  = getStringInput("Genre  : ");
        int year      = getIntInput(   "Year   : ");
        library.addBook(title, author, genre, year);
    }

    private static void updateBook() {
        System.out.println("\n--- Update Book ---");
        int id        = getIntInput(   "Book ID : ");
        String title  = getStringInput("New Title  : ");
        String author = getStringInput("New Author : ");
        String genre  = getStringInput("New Genre  : ");
        int year      = getIntInput(   "New Year   : ");
        library.updateBook(id, title, author, genre, year);
    }

    // ---- Input Helpers ----

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}