package com.g2.taxinow.security.jwt;

import com.g2.taxinow.TaxiNowService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

//import javax.crypto.KeyGenerator;
import java.io.IOException;

/**
 * A filter executed on each requests on endpoints marked with the annotation @JWTTokenNeeded.
 * It checks the authorization JWT token in the requests, allows/blocks requests and set the context
 * for the requests incoming to the backend API.
 */
@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenNeededFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        try {

            // Get the HTTP Authorization header from the request
            String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

            // Extract the token from the HTTP Authorization header
            String token = authorizationHeader.substring("Bearer".length()).trim();

            // Validate the token
            Jws<Claims> claims = Jwts.parser().setSigningKey(TaxiNowService.API_KEY).parseClaimsJws(token);
            if(!claims.getBody().containsKey("userID") || !claims.getBody().containsKey("userType")){
                throw new Exception();
            }

            // setting the context properties (to be used in requests)
            requestContext.setProperty("username", claims.getBody().getSubject());
            requestContext.setProperty("userID", claims.getBody().get("userID"));
            requestContext.setProperty("userType", claims.getBody().get("userType"));
            requestContext.setProperty("numOfSeats", claims.getBody().get("numOfSeats"));

        } catch (Exception e) {
            // if exception occur returns an UNAUTHORIZED response rejecting the request0
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }

    }

}