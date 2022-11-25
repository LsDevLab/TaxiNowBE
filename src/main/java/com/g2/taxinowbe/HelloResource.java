package com.g2.taxinowbe;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/hello-world")
public class HelloResource {

    @GET
    @Produces("text/plain")
    public String alive() {
        return "TaxiNow Backend is alive!";
    }

}