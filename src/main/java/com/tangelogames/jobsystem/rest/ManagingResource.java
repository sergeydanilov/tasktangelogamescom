package com.tangelogames.jobsystem.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Path("/managing")
public class ManagingResource {

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> hello() {
        Map<String, String> result = new HashMap<>();

        // todo : serg : 202205110942, Wed : implement this
        return result;
    }


}
