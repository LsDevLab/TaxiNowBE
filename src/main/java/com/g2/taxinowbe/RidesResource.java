package com.g2.taxinowbe;

import com.g2.taxinowbe.models.Customer;
import com.g2.taxinowbe.models.Ride;
import com.g2.taxinowbe.security.jwt.JWTTokenNeeded;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Path("/rides")
public class RidesResource {

//    @GET
//    @Path("/")
//    @Produces("text/plain")
//    public String getRides() {
//
//
//    }


    /**
     * Get the information about the user with the specified ID
     *
     * @param ID ID of the user
     * @return The information about the searched ride
     */
    @GET
    @JWTTokenNeeded
    @Path("/{ID}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getRide(@Context ContainerRequestContext context, @PathParam("ID") String ID) throws ExecutionException, InterruptedException {
        DocumentReference docRef = FirestoreClient.getFirestore().collection("rides").document(ID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // block on response
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            // convert document to POJO
            Ride ride = document.toObject(Ride.class);
            // return ride only if you are the customer of the driver
            String username = (String) context.getProperty("username");
            if (username.equals(ride.getCustomerID()) || username.equals(ride.getAcceptedByDriverID())){
                return Response.ok().entity(ride).build();
            }else{
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
