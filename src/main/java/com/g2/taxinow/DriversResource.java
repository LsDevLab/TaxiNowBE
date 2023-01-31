package com.g2.taxinow;

import com.g2.taxinow.models.Driver;
import com.g2.taxinow.security.jwt.JWTTokenNeeded;
import com.g2.taxinow.utils.Utils;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * The resource which provides driver related endpoints
 */
@Path("/drivers")
public class DriversResource {

    /**
     * Get the user with the specified ID
     *
     * @param id the id of the user to return
     * @return the user
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    @GET
    @Path("/{ID}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getDriver(@PathParam("ID") String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = FirestoreClient.getFirestore()
                .collection("drivers").document(id);
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // block on response
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            document.getData();
            // convert document to POJO
            Driver driver = document.toObject(Driver.class);
            driver.setID(document.getId());
            return Response.ok().entity(driver).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Edit a user
     *
     * @param context the context for the request
     * @param driver the edited driver
     * @return the edited driver
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @PUT
    @JWTTokenNeeded
    @Path("/")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response editDriver(@Context ContainerRequestContext context, Driver driver) throws ExecutionException, InterruptedException {
        String id = driver.getID();
        DocumentReference docRef = FirestoreClient.getFirestore()
                .collection("drivers").document(id);
        if (context.getProperty("userID").equals(id)){
            // asynchronously retrieve the document
            driver.setID(null);
            ApiFuture<WriteResult> future = docRef.set(Utils.removeNullValues(driver), SetOptions.merge());
            // block on response
            WriteResult document = future.get();
            driver.setID(id);
            return Response.ok().entity(driver).build();
        }else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }


}
