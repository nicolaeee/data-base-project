//import java.util.ArrayList;
//import java.util.List;
//
//public class LibraryManager {
//    private List<Book> books;
//    private List<Author> authors;
//
//    public LibraryManager() {
//        books = new ArrayList<>();
//        authors = new ArrayList<>();
//    }
//
//    public List<Book> getBooks() {
//        return books;
//    }
//
//    public List<Author> getAuthors() {
//        return authors;
//    }
//
//    public void addBook(Book book) {
//        books.add(book);
//    }
//
//    public void addAuthor(Author author) {
//        authors.add(author);
//    }
//
//    // Metodă pentru a obține lista cărților disponibile
//    public List<Book> listAvailableBooks() {
//        List<Book> availableBooks = new ArrayList<>();
//        for (Book book : books) {
//            if (book.isAvailable()) {
//                availableBooks.add(book);
//            }
//        }
//        return availableBooks;
//    }
//}
