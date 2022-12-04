package com.g2.taxinowbe;

import com.g2.taxinowbe.security.jwt.JWTTokenNeeded;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import com.google.firebase.FirebaseApp;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static jakarta.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;

@Path("/customers")
public class CustomersResource {

    @GET
    @Path("/alive")
    @Produces("text/plain")
    public String alive() {
        return "TaxiNOW BE is alive!";
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
            return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();

        } catch (Exception e) {
            return Response.status(UNAUTHORIZED).build();
        }
    }

    @GET
    @Path("/")
    @Produces("text/plain")
    public String getCustomers() throws ExecutionException, InterruptedException, IOException {
        DocumentReference docRef = FirestoreClient.getFirestore()
                .collection("customers").document("rf5E0zXjxYaiHgO44QZn");
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // block on response
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            // convert document to POJO
            //Customer cust = document.toObject(Customer.class);
            return document.getData().toString();
        } else {
            return "No such document!";
        }

    }

    @GET
    @JWTTokenNeeded
    @Path("/login_check")
    @Produces("text/plain")
    public String secure(@Context ContainerRequestContext context) throws ExecutionException, InterruptedException, IOException {
        return "Hi " + context.getProperty("username") + ". You are logged as " + context.getProperty("userType") +
                " and your ID is " + context.getProperty("userID") + ".";
    }

    /**
     * Authenticate user and issue a JWT token
     *
     * @param username username of the user
     * @param password password of the user
     * @return
     */
    private String authenticate(String username, String password)  {
        // TODO: check if user exists in Firebase, his password and retrieve user type (customer or driver)
        String userID = "sdfsd3545432sdf";
        String userType = "customer";
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

}