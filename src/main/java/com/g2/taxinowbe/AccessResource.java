package com.g2.taxinowbe;

import com.g2.taxinowbe.exceptions.UserNotExistsException;
import com.g2.taxinowbe.exceptions.WrongPasswordException;
import com.g2.taxinowbe.models.Customer;
import com.g2.taxinowbe.models.Driver;
import com.g2.taxinowbe.models.UserBody;
import com.g2.taxinowbe.security.jwt.JWTTokenNeeded;
import com.g2.taxinowbe.utils.Utils;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import io.jsonwebtoken.Jwts;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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
                                     @FormParam("password") String password) {
        try {
            // Authenticate and issue a token for the user
            String token = authenticate(username, password);
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
                             @FormParam("password") String password,
                             @FormParam("name") String name,
                             @FormParam("surname") String surname,
                             @FormParam("userType") String userType) throws ExecutionException, InterruptedException, NoSuchAlgorithmException {
        if (userExists(username)){
            return Response.status(Response.Status.CONFLICT).build();
        } else if(userType.equals("customer") || userType.equals("driver")){
            String userID = createUser(userType+"s", username, password, name, surname);
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
     * @param password password of the user
     * @return the jwt token
     */
    private String authenticate(String username, String password) throws ExecutionException, InterruptedException, UserNotExistsException, WrongPasswordException, NoSuchAlgorithmException {
        // TODO: add salt
        String hashedPassword;
        String userID;
        String userType;
        // retrieve the user of Firebase db
        ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore().collection("customers").whereEqualTo("username", username).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            hashedPassword = (String) document.getData().get("hashedPassword");
            userID = document.getId();
            userType = "customer";
        } else {
            future = FirestoreClient.getFirestore().collection("drivers").whereEqualTo("username", username).get();
            documents = future.get().getDocuments();
            if (!documents.isEmpty()) {
                DocumentSnapshot document = documents.get(0);
                hashedPassword = (String) document.getData().get("hashedPassword");
                userID = document.getId();
                userType = "driver";
            } else {
                throw new UserNotExistsException();
            }
        }
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        if (!hashedPassword.equals(Utils.bytesToHex(encodedHash))){
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
     * @param password password of the user
     * @param name name of the user
     * @param surname surname of the user
     * @return the ID of the created user
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws NoSuchAlgorithmException
     */
    private String createUser(String userType, String username, String password, String name, String surname) throws ExecutionException, InterruptedException, NoSuchAlgorithmException {
        // hashing the password
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String hashedPassword = Utils.bytesToHex(digest.digest(password.getBytes(StandardCharsets.UTF_8)));
        // creating a new user
        UserBody newUser = new UserBody(username, name, surname, hashedPassword);
        ApiFuture<DocumentReference> future = FirestoreClient.getFirestore().collection(userType).add(newUser);
        String userID = future.get().get().get().getId();
        return userID;
    }

}
