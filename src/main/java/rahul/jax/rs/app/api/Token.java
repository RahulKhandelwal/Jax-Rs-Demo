/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rahul.jax.rs.app.api;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import rahul.jax.rs.app.security.Roles;

/**
 * @author rahul.kh
 */
@Path("token")
public class Token {

    @POST
    public Response create(@FormParam("role") String _role) {
        if (_role == null || Roles.getRole(_role) == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.CREATED).entity(Roles.getRole(_role).getRole()).build();
    }

}
