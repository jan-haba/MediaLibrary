package module;

import java.time.LocalDateTime;

public class Book extends Module{
    private String author;
    private int pageCount;
    private String publisher;
    private String isbn;

    public Book(int id, String title, String genre, int year, int rating, String imageUrl, String description, boolean favorite, LocalDateTime dateAdded, String author, int pageCount, String publisher, String isbn) {
        super(id, title, genre, year, rating, imageUrl, description);
        this.author = author;
        this.pageCount = pageCount;
        this.publisher = publisher;
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
