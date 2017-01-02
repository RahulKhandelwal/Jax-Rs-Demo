/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rahul.jax.rs.app.api;


import java.util.Collection;

import javax.inject.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import rahul.jax.rs.app.resources.Book;
import rahul.jax.rs.app.resources.BookDao;
import rahul.jax.rs.app.security.Roles;
import rahul.jax.rs.app.security.Secured;

/**
 * This class is endpoint for book resources.
 *
 * @author rahul.kh
 */
@Path("books")
public class Books {
    
    @Inject
    private BookDao bookDao; 

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll() {
        GenericEntity<Collection<Book>> books = new GenericEntity<Collection<Book>>(this.bookDao.all()){};
        return Response.ok(books).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response book(@PathParam("id") int _id) {
        return Response.ok(this.bookDao.get(_id)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Book book) {
        Book added = this.bookDao.add(book);
        String id = String.valueOf(added.getId());
        
        return Response.ok(added)
                .link(uriInfo.getAbsolutePathBuilder().path(id).build(), "self")
                .link(uriInfo.getAbsolutePathBuilder().path(id).build(), "delete")
                .build();
    }
    
    @Secured({Roles.ADMIN, Roles.NORMAL})
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int _id) {
        Book removed = this.bookDao.remove(_id);
        return Response.ok(removed).build();
    }
}
