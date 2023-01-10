package com.g2.taxinowbe;

import com.g2.taxinowbe.models.Driver;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


@Path("/drivers")
public class DriversResource {

    @GET
    @Path("/{ID}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getDriver(@PathParam("ID") String id) throws ExecutionException, InterruptedException, IOException {
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


    @PUT
    @Path("/")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response editDriver(@Context ContainerRequestContext context, Driver driver) throws ExecutionException, InterruptedException {
        String id = driver.getID();
        DocumentReference docRef = FirestoreClient.getFirestore()
                .collection("drivers").document(id);
        if (context.getProperty("userID").equals(driver.getID())){
            // asynchronously retrieve the document
            ApiFuture<WriteResult> future = docRef.set(driver);
            // block on response
            WriteResult document = future.get();
            return Response.ok().entity(driver).build();
        }else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }


}
