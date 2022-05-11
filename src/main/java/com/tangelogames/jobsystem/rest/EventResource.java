package com.tangelogames.jobsystem.rest;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/async")
public class EventResource {
    @Inject
    EventBus bus;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/status")
    public Uni<String> status() {
        return bus.<String>request("custom.job.1", "data")
                .onItem().transform(Message::body);
    }
}
