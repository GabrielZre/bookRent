package products;

public class BookTest {
    private String title;
    private String author;
    private String isbn;

    public BookTest() {
    }

    public BookTest(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
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


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(this.getTitle())
                .append(" Author: ")
                .append(this.getAuthor())
                .append(" Isbn: ")
                .append(this.getIsbn())
                .toString();

    }

}

