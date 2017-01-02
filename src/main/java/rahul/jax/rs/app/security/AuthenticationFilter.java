/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rahul.jax.rs.app.security;

import java.security.Principal;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author rahul.kh
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // Get the HTTP authentication header
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || authorizationHeader.trim().isEmpty()) {
            return; // We can also abort the request if required
        }

        if (!authorizationHeader.startsWith("Bearer")) {
            throw new NotAuthorizedException("Invalid Authorization header.");
        }

        // Get the token
        String token = authorizationHeader.substring("Bearer".length()).trim();

        // validate the token
        try {
            Roles role = this.validateToken(token);

            requestContext.setSecurityContext(new SecurityContext() {

                private final Roles user = role;

                @Override
                public Principal getUserPrincipal() {
                    return () -> this.user.getRole();
                }

                @Override
                public boolean isUserInRole(String role) {
                    return this.user == Roles.getRole(role);
                }

                @Override
                public boolean isSecure() {
                    return true;
                }

                @Override
                public String getAuthenticationScheme() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
            
        } catch(Exception _ex) {
            // Abort the request
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).entity(_ex.getMessage()).build());
        }
    }

    private Roles validateToken(String _token) throws Exception {

        if (_token.isEmpty()) {
            throw new Exception("Invalid token");
        }

        Roles role = Roles.getRole(_token);

        if (role == null) {
            throw new Exception("Invalid token");
        }

        return role;
    }

}
