package se.citerus.cqrs.bookstore.query;

import com.sun.jersey.api.client.Client;

public class BookCatalogClient {

  private final Client client;

  private BookCatalogClient(Client client) {
    this.client = client;
  }

  public static BookCatalogClient create(Client client) {
    return new BookCatalogClient(client);
  }

  public BookDto getBook(String bookId) {
    return client.resource("http://localhost:8080/books/" + bookId).get(BookDto.class);
  }


}
