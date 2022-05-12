package com.tangelogames.jobsystem.rest;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Log4j2
@Path("/managing")
public class ManagingResource {

    @Inject
    EventBus bus;


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/status/{id}")
    public Uni<String> status(String id) {
        return bus.<String>request("custom.job." + id, "status")
                .onItem().transform(Message::body);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/run/{name}")
    public Uni<String> run(String name) {
        return bus.<String>request(name, "run")
                .onItem().transform(Message::body);
    }
}
