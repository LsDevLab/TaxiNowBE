package com.g2.taxinowbe.test;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
import java.util.concurrent.ExecutionException;

public class Trial {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, NoSuchAlgorithmException {

        String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //We will sign our JWT with our ApiKey secret
        Key signingKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                signatureAlgorithm.getJcaName());

        String jwtToken = Jwts.builder()
                .setSubject("username")
                .setIssuer("io")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(15L).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(signatureAlgorithm, signingKey)
                .compact();

        Key signingKey2 = new SecretKeySpec(Base64.getDecoder().decode(secret),
                signatureAlgorithm.getJcaName());
        Jwts.parser().setSigningKey(signingKey2).parseClaimsJws(jwtToken);

//        KeyGenerator generator = KeyGenerator.getInstance("AES");
//        generator.init(128); // The AES key size in number of bits
//        System.out.println(generator.generateKey().getEncoded().toString());

       // System.setProperty("javax.net.ssl.trustStore", "cacerts.jks");
        //System.out.println(System.getProperty("javax.net.ssl.trustStore"));


        FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);
        System.out.println("ok");


        DocumentReference docRef = FirestoreClient.getFirestore().collection("customers").document("rf5E0zXjxYaiHgO44QZn");

        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // block on response
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            // convert document to POJO
            //Customer cust = document.toObject(Customer.class);
            System.out.println(document.getData().toString());
        } else {
            System.out.println("No such document!");
        }

        InputStream stdin = Runtime.getRuntime().exec("ping 142.250.180.170").getInputStream();
        InputStreamReader isr = new InputStreamReader(stdin);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) {
            System.out.println(s);
        }


    }

}
