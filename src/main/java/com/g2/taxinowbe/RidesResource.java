package com.g2.taxinowbe;

import com.g2.taxinowbe.models.Ride;
import com.g2.taxinowbe.models.RideState;
import com.g2.taxinowbe.security.jwt.JWTTokenNeeded;
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
     * @param ID ID of the ride
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
        Response response = null;
        if (document.exists()) {
            // convert document to POJO
            Ride ride = document.toObject(Ride.class);
            // return ride only if you are the customer of the driver
            String userID = (String) context.getProperty("userID");
            if (userID.equals(ride.getCustomerID()) || userID.equals(ride.getAcceptedByDriverID())) {
                response = Response.ok().entity(ride).build();
            } else {
                response = Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } else {
            response = Response.status(Response.Status.NOT_FOUND).build();
        }
        return response;
    }

    /**
     * Set the status of a ride with a specified ID to ACCEPTED by a specified Driver
     *
     * @param ID ID of the ride
     * @return The information about the accepted ride
     */
    @PUT
    @JWTTokenNeeded
    @Path("/{ID}/accept")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response acceptRide(@QueryParam("price") String price, @Context ContainerRequestContext context, @PathParam("ID") String ID) throws ExecutionException, InterruptedException, NullPointerException {
        DocumentReference docRef = FirestoreClient.getFirestore().collection("rides").document(ID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // block on response
        DocumentSnapshot document = future.get();
        Response response = null;
        if (document.exists()) {
            // convert document to POJO
            Ride ride = document.toObject(Ride.class);
            String userType = (String) context.getProperty("userType");
            String userID = (String) context.getProperty("userID");
            float acceptationPrice = Float.parseFloat(price);
            // return ride only if you are the customer of the driver
            if (userType.compareToIgnoreCase("driver") == 0) {
                if (acceptationPrice <= ride.getMaxAllowedPrice()) {
                    ride.setAccepted(userID, Float.parseFloat(price));
                    ApiFuture<WriteResult> future_write = docRef.set(ride);
                    response = Response.ok().entity(ride).build();
                } else {
                    response = Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
                }
            } else {
                response = Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } else {
            response = Response.status(Response.Status.NOT_FOUND).build();
        }

        return response;
    }


    /**
     * Set the status of a ride with a specified ID to PENDING
     *
     * @param ID ID of the ride
     * @return The information about the pending ride
     */
    @PUT
    @JWTTokenNeeded
    @Path("/{ID}/discard")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response discardRide(@Context ContainerRequestContext context, @PathParam("ID") String ID) throws ExecutionException, InterruptedException, NullPointerException {
        DocumentReference docRef = FirestoreClient.getFirestore().collection("rides").document(ID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // block on response
        DocumentSnapshot document = future.get();
        Response response = null;
        if (document.exists()) {
            // convert document to POJO
            Ride ride = document.toObject(Ride.class);
            String userType = (String) context.getProperty("userType");
            String userID = (String) context.getProperty("userID");
            // return ride only if you are the customer of the driver
            if (userType.compareToIgnoreCase("driver") == 0 && userID.compareTo(ride.getAcceptedByDriverID()) == 0) {
                if (ride.getState() != RideState.COMPLETED) {
                    ride.setPending();
                    ApiFuture<WriteResult> future_write = docRef.set(ride);
                    response = Response.ok().entity(ride).build();
                } else {
                    response = Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
                }
            } else {
                response = Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } else {
            response = Response.status(Response.Status.NOT_FOUND).build();
        }

        return response;
    }

    /**
     * Set the status of a ride with a specified ID to COMPLETED
     *
     * @param ID ID of the ride
     * @return The information about the completed ride
     */
    @PUT
    @JWTTokenNeeded
    @Path("/{ID}/complete")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response completeRide(@Context ContainerRequestContext context, @PathParam("ID") String ID) throws ExecutionException, InterruptedException, NullPointerException {
        DocumentReference docRef = FirestoreClient.getFirestore().collection("rides").document(ID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // block on response
        DocumentSnapshot document = future.get();
        Response response = null;
        if (document.exists()) {
            // convert document to POJO
            Ride ride = document.toObject(Ride.class);
            String userType = (String) context.getProperty("userType");
            String userID = (String) context.getProperty("userID");
            // return ride only if you are the customer of the driver
            if (userType.compareToIgnoreCase("driver") == 0 && userID.compareTo(ride.getAcceptedByDriverID()) == 0) {
                ride.setCompleted();
                ApiFuture<WriteResult> future_write = docRef.set(ride);
                response = Response.ok().entity(ride).build();
            } else {
                response = Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } else {
            response = Response.status(Response.Status.NOT_FOUND).build();
        }

        return response;
    }

}
