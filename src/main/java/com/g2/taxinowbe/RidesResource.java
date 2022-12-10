package com.g2.taxinowbe;

import com.g2.taxinowbe.models.Ride;
import com.g2.taxinowbe.models.RideState;
import com.g2.taxinowbe.security.jwt.JWTTokenNeeded;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Path("/rides")
public class RidesResource {


    /**
     * Get the information about rides linked to the user making the request.
     * If I'm a customer I'll get every pending, accepted or completed ride
     * I posted on the app. Otherwise, if I'm a driver I'll get all my accepted or completed rides.
     *
     * @return The information about all the rides linked to the asking customer.
     */
    @GET
    @JWTTokenNeeded
    @Path("/")
    @Produces("text/plain")
    public String getRides(@Context ContainerRequestContext context) throws ExecutionException, InterruptedException {
        CollectionReference collectionReference = FirestoreClient.getFirestore().collection("rides");

        String userType = (String) context.getProperty("userType");
        String userID = (String) context.getProperty("userID");

        Query query = null;

        if (userType.compareToIgnoreCase("customer")==0){
            //CASE - I'm a customer
            query = collectionReference.whereEqualTo("customerID", userID);

        }else if(userType.compareToIgnoreCase("driver")==0){
            //CASE - I'm a driver
            query = collectionReference.whereEqualTo("acceptedByDriverID", userID);
        }else{
            //CASE - I'm not authorized
            return "You are UNAUTHORIZED";
        }

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (querySnapshot.get().getDocuments().isEmpty()){
            return "No rides found for this user";
        }

        String result="";

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            result+=document.getData().toString()+"\n";
        }

        return result;
    }

    /**
     * Get the information about pending rides linked to the customer making the request.
     * Only customers can make this type of request.
     *
     * @return The information about all the pending rides linked to the asking customer.
     */
    @GET
    @JWTTokenNeeded
    @Path("/pending")
    @Produces("text/plain")
    public String getPendingRides(@Context ContainerRequestContext context) throws ExecutionException, InterruptedException {
        CollectionReference collectionReference = FirestoreClient.getFirestore().collection("rides");

        String userType = (String) context.getProperty("userType");
        String userID = (String) context.getProperty("userID");

        Query query = null;

        if (userType.compareToIgnoreCase("customer")==0){
            //CASE - I'm a customer
            query = collectionReference.whereEqualTo("customerID", userID);

        }else{
            //CASE - I'm not authorized
            return "You are UNAUTHORIZED";
        }

        query = query.whereEqualTo("state", "PENDING");

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (querySnapshot.get().getDocuments().isEmpty()){
            return "No rides found with the specified criteria";
        }

        String result="";

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            result+=document.getData().toString()+"\n";
        }

        return result;
    }

    /**
     * Get the information about completed rides linked to the user making the request.
     *
     * @return The information about all the completed rides linked to the asking user.
     */
    @GET
    @JWTTokenNeeded
    @Path("/completed")
    @Produces("text/plain")
    public String getCompletedRides(@Context ContainerRequestContext context) throws ExecutionException, InterruptedException {
        CollectionReference collectionReference = FirestoreClient.getFirestore().collection("rides");

        String userType = (String) context.getProperty("userType");
        String userID = (String) context.getProperty("userID");

        Query query = null;

        if (userType.compareToIgnoreCase("customer")==0){
            //CASE - I'm a customer
            query = collectionReference.whereEqualTo("customerID", userID);

        }else if(userType.compareToIgnoreCase("driver")==0){
            //CASE - I'm a driver
            query = collectionReference.whereEqualTo("acceptedByDriverID", userID);
        }else{
            //CASE - I'm not authorized
            return "You are UNAUTHORIZED";
        }

        query = query.whereEqualTo("state", "COMPLETED");

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (querySnapshot.get().getDocuments().isEmpty()){
            return "No rides found with the specified criteria";
        }

        String result="";

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            result+=document.getData().toString()+"\n";
        }

        return result;
    }

    /**
     * Get the information about last n rides linked to the user making the request.
     *
     * @param latest_n Numbers of top rides to return
     * @return The information about last n rides linked to the asking user, ordered by creation date.
     */
    @GET
    @JWTTokenNeeded
    @Path("/latest")
    @Produces("text/plain")
    public String getLastRides(@QueryParam("N") String latest_n, @Context ContainerRequestContext context) throws ExecutionException, InterruptedException {
        CollectionReference collectionReference = FirestoreClient.getFirestore().collection("rides");

        String userType = (String) context.getProperty("userType");
        String userID = (String) context.getProperty("userID");

        Query query = null;

        if (userType.compareToIgnoreCase("customer")==0){
            //CASE - I'm a customer
            query = collectionReference.whereEqualTo("customerID", userID);

        }else if(userType.compareToIgnoreCase("driver")==0){
            //CASE - I'm a driver
            query = collectionReference.whereEqualTo("acceptedByDriverID", userID);
        }else{
            //CASE - I'm not authorized
            return "You are UNAUTHORIZED";
        }

        int n = (int)Float.parseFloat(latest_n);
        query = query.orderBy("createdOn", Query.Direction.DESCENDING).limit(n);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (querySnapshot.get().getDocuments().isEmpty()){
            return "No rides found with the specified criteria";
        }

        String result="";

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            result+=document.getData().toString()+"\n";
        }

        return result;
    }

    /**
     * Get the information about rides linked to the user making the request,
     * with number of passengers greater than a specified one
     *
     * @param min_passengers Numbers of minimum passengers
     * @return The information about rides linked to the asking user, with number of passengers
     * greater than the specified one
     */
    @GET
    @JWTTokenNeeded
    @Path("/min-passengers")
    @Produces("text/plain")
    public String getMinPassengersRides(@QueryParam("MIN") String min_passengers, @Context ContainerRequestContext context) throws ExecutionException, InterruptedException {
        CollectionReference collectionReference = FirestoreClient.getFirestore().collection("rides");

        String userType = (String) context.getProperty("userType");
        String userID = (String) context.getProperty("userID");

        Query query = null;

        if (userType.compareToIgnoreCase("customer")==0){
            //CASE - I'm a customer
            query = collectionReference.whereEqualTo("customerID", userID);

        }else if(userType.compareToIgnoreCase("driver")==0){
            //CASE - I'm a driver
            query = collectionReference.whereEqualTo("acceptedByDriverID", userID);
        }else{
            //CASE - I'm not authorized
            return "You are UNAUTHORIZED";
        }

        int n = (int)Float.parseFloat(min_passengers);
        query = query.whereGreaterThan("numOfPassengers", n);
        query = query.orderBy("numOfPassengers", Query.Direction.ASCENDING);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (querySnapshot.get().getDocuments().isEmpty()){
            return "No rides found with the specified criteria";
        }

        String result="";

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            result+=document.getData().toString()+"\n";
        }

        return result;
    }

    /**
     * Get the information about the ride with the specified ID
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

    /**
     * Add a new ride request from the current customer
     *
     * @param ride the ride object
     * @return the created ride ID
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @POST
    @JWTTokenNeeded
    @Path("/")
    @Consumes(MediaType.APPLICATION_XML)
    public Response addRideRequest(@Context ContainerRequestContext context, Ride ride) throws ExecutionException, InterruptedException {
        String userID = (String) context.getProperty("userID");
        String userType = (String) context.getProperty("userType");
        if(userType.equals("customer") && ride.getCustomerID().equals(userID)) {
            ApiFuture<DocumentReference> future = FirestoreClient.getFirestore().collection("rides").add(ride);
            String rideID = future.get().get().get().getId();
            return Response.status(Response.Status.CREATED).entity(rideID).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

}
