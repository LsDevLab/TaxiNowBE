package com.g2.taxinow;

import com.g2.taxinow.models.Customer;
import com.g2.taxinow.security.jwt.JWTTokenNeeded;
import com.g2.taxinow.utils.Utils;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.ws.rs.*;

import java.util.concurrent.ExecutionException;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
            customer.setID(document.getId());
            if (context.getProperty("username").equals(customer.getUsername())){
                return Response.ok().entity(customer).build();
            }else{
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @JWTTokenNeeded
    @Path("/")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response editCustomer(@Context ContainerRequestContext context, Customer customer) throws ExecutionException, InterruptedException {
        String id = customer.getID();
        DocumentReference docRef = FirestoreClient.getFirestore()
                .collection("customers").document(id);
        if (context.getProperty("userID").equals(id)){
            customer.setID(null);
            // asynchronously retrieve the document
            ApiFuture<WriteResult> future = docRef.set(Utils.removeNullValues(customer), SetOptions.merge());
            // block on response
            WriteResult document = future.get();
            customer.setID(id);
            return Response.ok().entity(customer).build();
        }else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }

}