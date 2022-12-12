package com.g2.taxinowbe;

import com.g2.taxinowbe.models.Driver;
import com.g2.taxinowbe.notifier.Notifier;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

import static jakarta.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;

@ApplicationPath("/taxinow-api")
public class TaxiNowService extends Application {

    public static Key API_KEY;
    public static SignatureAlgorithm SIGNATURE_ALGORITHM;

    public static String MULTICAST_NOTIFIER_ADDRESS = "230.0.0.0";
    public static int MULTICAST_NOTIFIER_PORT = 7778;

    public TaxiNowService() throws IOException {
        // Initialize Firebase Database
        FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);
        // Load API secret
        String keyString = new BufferedReader(new FileReader("apiSecret.txt")).readLine();
        SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
        API_KEY = new SecretKeySpec(Base64.getDecoder().decode(keyString), SIGNATURE_ALGORITHM.getJcaName());
        // notifier init
        Notifier.initialize(MULTICAST_NOTIFIER_ADDRESS, MULTICAST_NOTIFIER_PORT);
    }


}
