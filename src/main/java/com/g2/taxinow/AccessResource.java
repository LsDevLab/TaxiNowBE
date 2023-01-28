package com.g2.taxinow;

import com.g2.taxinow.exceptions.UserNotExistsException;
import com.g2.taxinow.exceptions.WrongPasswordException;
import com.g2.taxinow.models.UserBody;
import com.g2.taxinow.security.jwt.JWTTokenNeeded;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import io.jsonwebtoken.Jwts;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static jakarta.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;

@Path("/access")
public class AccessResource {

    @GET
    @Path("/alive")
    @Produces("text/plain")
    public String alive() {
        return "TaxiNOW BE is alive!";
    }

    @GET
    @JWTTokenNeeded
    @Path("/login")
    @Produces("text/plain")
    public String check(@Context ContainerRequestContext context) {
        return "Hi " + context.getProperty("username") + ". You are logged as " + context.getProperty("userType") +
                " and your ID is " + context.getProperty("userID") + ".";
    }

    @POST
    @Path("/login")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("username") String username,
                                     @FormParam("hashedPassword") String hashedPassword) {
        try {
            // Authenticate and issue a token for the user
            String token = authenticate(username, hashedPassword);
            // Return the token on the response
            return Response.ok().header(AUTHORIZATION, "Bearer " + token).entity(token).build();
        } catch (Exception e) {
            //TODO: more meaningful error messages
            return Response.status(UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("/register")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public Response register(@FormParam("username") String username,
                             @FormParam("hashedPassword") String hashedPassword,
                             @FormParam("name") String name,
                             @FormParam("surname") String surname,
                             @FormParam("userType") String userType) throws ExecutionException, InterruptedException, NoSuchAlgorithmException {
        if (userExists(username)){
            return Response.status(Response.Status.CONFLICT).build();
        } else if(userType.equals("customer") || userType.equals("driver")){
            String userID = createUser(userType+"s", username, hashedPassword, name, surname);
            String jwtToken = Jwts.builder()
                    .setSubject(username)
                    .setIssuer("TaxiNOW")
                    .setIssuedAt(new Date())
                    .setExpiration(Date.from(Instant.now().plus(360l, ChronoUnit.MINUTES)))
                    .claim("userType", userType)
                    .claim("userID", userID)
                    .signWith(TaxiNowService.SIGNATURE_ALGORITHM, TaxiNowService.API_KEY)
                    .compact();
            return Response.ok().header(AUTHORIZATION, "Bearer " + jwtToken).entity(jwtToken).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * Authenticate user and issue a JWT token
     *
     * @param username username of the user
     * @param hashedPassword hashed password of the user
     * @return the jwt token
     */
    private String authenticate(String username, String hashedPassword) throws ExecutionException, InterruptedException, UserNotExistsException, WrongPasswordException, NoSuchAlgorithmException {
        // TODO: add salt
        String hPassword;
        String userID;
        String userType;
        int numOfSeats = 0;
        // retrieve the user of Firebase db
        ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore().collection("customers").whereEqualTo("username", username).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            hPassword = (String) document.getData().get("hashedPassword");
            userID = document.getId();
            userType = "customer";
        } else {
            future = FirestoreClient.getFirestore().collection("drivers").whereEqualTo("username", username).get();
            documents = future.get().getDocuments();
            if (!documents.isEmpty()) {
                DocumentSnapshot document = documents.get(0);
                hPassword = (String) document.getData().get("hashedPassword");
                userID = document.getId();
                userType = "driver";
                numOfSeats = ((Double)document.getData().get("numOfSeats")).intValue();
            } else {
                throw new UserNotExistsException();
            }
        }
        if (!hPassword.equals(hashedPassword)){
            throw new WrongPasswordException();
        }
        // Build the JWT token
        String jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuer("TaxiNOW")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(360l, ChronoUnit.MINUTES)))
                .claim("userType", userType)
                .claim("userID", userID)
                .claim("numOfSeats", numOfSeats)
                .signWith(TaxiNowService.SIGNATURE_ALGORITHM, TaxiNowService.API_KEY)
                .compact();
        return jwtToken;
    }

    /**
     * @param username the username of the user
     * @return true if a user with the given username already exists
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private boolean userExists(String username) throws ExecutionException, InterruptedException {
        // retrieve the user of Firebase db
        ApiFuture<AggregateQuerySnapshot> future = FirestoreClient.getFirestore()
                .collection("customers")
                .whereEqualTo("username", username)
                .count()
                .get();
        if (future.get().getCount() != 0) {
            return true;
        } else {
            future = FirestoreClient.getFirestore()
                    .collection("drivers")
                        .whereEqualTo("username", username)
                    .count()
                    .get();
            return future.get().getCount() != 0;
        }
    }

    /**
     *
     * @param userType type of the user ("customer" or "driver")
     * @param username username of the user
     * @param hashedPassword hashed password of the user
     * @param name name of the user
     * @param surname surname of the user
     * @return the ID of the created user
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws NoSuchAlgorithmException
     */
    private String createUser(String userType, String username, String hashedPassword, String name, String surname) throws ExecutionException, InterruptedException {
        // creating a new user
        UserBody newUser = new UserBody(username, name, surname, hashedPassword);
        ApiFuture<DocumentReference> future = FirestoreClient.getFirestore().collection(userType).add(newUser);
        String userID = future.get().get().get().getId();
        return userID;
    }

}
