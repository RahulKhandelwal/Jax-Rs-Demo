/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rahul.jax.rs.app.api;


import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import rahul.jax.rs.app.resources.Book;
import rahul.jax.rs.app.resources.BookDao;

/**
 * This class is endpoint for book resources.
 *
 * @author rahul.kh
 */
@Path("books")
public class Books {

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll() {
        GenericEntity<Collection<Book>> books = new GenericEntity<Collection<Book>>(BookDao.INSTANCE.all()){};
        return Response.ok(books).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response book(@PathParam("id") int _id) {
        return Response.ok(BookDao.INSTANCE.get(_id)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Book book) {
        Book added = BookDao.INSTANCE.add(book);
        String id = String.valueOf(added.getId());
        
        return Response.ok(added).
                link(uriInfo.getAbsolutePathBuilder().path(id).build(), "self").
                build();
    }

}
