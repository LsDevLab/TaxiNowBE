package com.g2.taxinowbe;

import com.g2.taxinowbe.models.Driver;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class DriverResource {
    @Path("/drivers")
    public static class DriversResource {

        @GET
        @Path("/{ID}")
        @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
        public Response getDriver(@PathParam("ID") String id) throws ExecutionException, InterruptedException, IOException {
            DocumentReference docRef = FirestoreClient.getFirestore()
                    .collection("drivers").document(id);
            // asynchronously retrieve the document
            ApiFuture<DocumentSnapshot> future = docRef.get();
            // block on response
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                document.getData();
                // convert document to POJO
                Driver driver = document.toObject(Driver.class);
                return Response.ok().entity(driver).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }




    }
}
