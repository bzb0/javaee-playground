package com.bzb.javaee.resource;

import com.bzb.javaee.dao.BookDAO;
import com.bzb.javaee.jpa.entity.Book;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/books")
public class BookResource {

  private final static String NAME_PARAM = "name";

  @EJB
  BookDAO bookDAO;

  @POST
  @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
  public Response save(MultivaluedMap<String, String> formData) {
    Book book = bookDAO.save(formData.getFirst(NAME_PARAM));
    return Response.created(URI.create("/books/" + book.getId())).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getBook(@PathParam("id") long id) {
    Optional<Book> book = bookDAO.getBook(id);
    return book.map(b -> Response.ok(b).build()).orElse(Response.status(Status.NOT_FOUND).build());
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Book> getAllBooks() {
    return bookDAO.getAllBooks();
  }
}