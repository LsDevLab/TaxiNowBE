package com.g2.taxinowbe.test;

import com.g2.taxinowbe.models.Ride;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.InetAddress;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Trial {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, NoSuchAlgorithmException {

        Client client = ClientBuilder.newClient();
        //WebTarget target = client.target("http://localhost:8080/REST-UserManagement/UserService/users");
        WebTarget target = client.target("http://localhost:8080/TaxiNowBE-1.0-SNAPSHOT/taxinow-api/rides");
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_XML);
        //GET -> invocationBuilder.get()



        invocationBuilder.post(Entity.xml(new Ride("ciao", 1, 232L, (float) 34.3)));

        //
//        String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
//
//        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//
//        //We will sign our JWT with our ApiKey secret
//        Key signingKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
//                signatureAlgorithm.getJcaName());
//
//        String jwtToken = Jwts.builder()
//                .setSubject("username")
//                .setIssuer("io")
//                .setIssuedAt(new Date())
//                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(15L).atZone(ZoneId.systemDefault()).toInstant()))
//                .signWith(signatureAlgorithm, signingKey)
//                .compact();
//
//        Key signingKey2 = new SecretKeySpec(Base64.getDecoder().decode(secret),
//                signatureAlgorithm.getJcaName());
//        Jwts.parser().setSigningKey(signingKey2).parseClaimsJws(jwtToken);

//        KeyGenerator generator = KeyGenerator.getInstance("AES");
//        generator.init(128); // The AES key size in number of bits
//        System.out.println(generator.generateKey().getEncoded().toString());

       // System.setProperty("javax.net.ssl.trustStore", "cacerts.jks");
        //System.out.println(System.getProperty("javax.net.ssl.trustStore"));

//
//        FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .build();
//        FirebaseApp.initializeApp(options);
//        System.out.println("ok");
//
//
//        ApiFuture<AggregateQuerySnapshot> future = FirestoreClient.getFirestore()
//                .collection("customers")
//                .whereEqualTo("username", "username1")
//                .count()
//                .get();
//        if (future.get().getCount() == 0) {
//            System.out.println("custoimers not empty");
//        } else {
//            future = FirestoreClient.getFirestore().collection("drivers").whereEqualTo("username", "username1")
//                    .count().get();
//            if (future.get().getCount() == 0) {
//                System.out.println("driverd not empty");
//            }
//        }

    }

}
