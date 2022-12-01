package com.g2.taxinowbe;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

import com.google.firebase.FirebaseApp;

@Path("/customers")
public class CustomersResource {

    //private Firestore dbFirestore = FirestoreClient.getFirestore();

    @GET
    @Path("/alive")
    @Produces("text/plain")
    public String alive() {
        return "TaxiNOW BE is alive!";
    }

    @GET
    @Path("/")
    @Produces("text/plain")
    public String getCustomers() throws ExecutionException, InterruptedException, IOException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection("customers").document("rf5E0zXjxYaiHgO44QZn");

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
    @Path("/alive2")
    @Produces("text/plain")
    public String alive2() throws ExecutionException, InterruptedException, IOException {

        URL yahoo = new URL("http://www.google.com/");
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yc.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            return inputLine;

        return "ciap";
    }

}