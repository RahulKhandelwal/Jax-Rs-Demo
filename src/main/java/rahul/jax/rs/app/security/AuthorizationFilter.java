/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rahul.jax.rs.app.security;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author rahul.kh
 */
@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // Get the resource class which matches with the requested URI
        // Extract the roles declared by it
        Class<?> resourceClass = this.resourceInfo.getResourceClass();
        List<Roles> classRoles = this.extractRoles(resourceClass);

        // Get the resource method which matches with the requested URI
        // Extract the roles declared by it
        Method resourceMethod = this.resourceInfo.getResourceMethod();
        List<Roles> methodRoles = this.extractRoles(resourceMethod);

        try {

            // Check if the user is allowed to execute the method
            // The method annotations override the class annotations
            if (methodRoles.isEmpty()) {
                this.checkPermissions(classRoles, requestContext.getSecurityContext());
            } else {
                this.checkPermissions(methodRoles, requestContext.getSecurityContext());
            }

        } catch (Exception _ex) {
            requestContext.abortWith(
                Response.status(Response.Status.FORBIDDEN).entity(_ex.getMessage()).build());
        }
    }

    // Extract the roles from the annotated element
    private List<Roles> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new ArrayList<>();
        } else {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured == null) {
                return new ArrayList<>();
            } else {
                Roles[] allowedRoles = secured.value();
                return Arrays.asList(allowedRoles);
            }
        }
    }

    private void checkPermissions(List<Roles> allowedRoles, SecurityContext securityContext) throws Exception {
        // Check if the user contains one of the allowed roles
        // Throw an Exception if the user has not permission to execute the method

        for (Roles role : allowedRoles) {
            if (securityContext.isUserInRole(role.getRole())) {
                return;
            }
        }

        throw new Exception(String.format("User [%s] not authorized.", securityContext.getUserPrincipal()));
    }
}
