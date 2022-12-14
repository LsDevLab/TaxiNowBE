package com.g2.taxinowbe;

import com.g2.taxinowbe.models.Customer;
import com.g2.taxinowbe.security.jwt.JWTTokenNeeded;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import io.jsonwebtoken.Jwts;
import jakarta.ws.rs.*;

import java.io.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static jakarta.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;

@Path("/customers")
public class CustomersResource {


    /**
     * Get the information about the user with the specified ID
     *
     * @param ID ID of the user
     * @return The information about the searched customer
     */
    @GET
    @JWTTokenNeeded
    @Path("/{ID}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getCustomer(@Context ContainerRequestContext context, @PathParam("ID") String ID) throws ExecutionException, InterruptedException {
        DocumentReference docRef = FirestoreClient.getFirestore()
                .collection("customers").document(ID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // block on response
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            // convert document to POJO
            Customer customer = document.toObject(Customer.class);
            if (context.getProperty("username").equals(customer.getUsername())){
                return Response.ok().entity(customer).build();
            }else{
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}