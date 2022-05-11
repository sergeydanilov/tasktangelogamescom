package com.tangelogames.jobsystem.rest;

import io.vertx.core.Vertx;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
@Path("/managing")
public class ManagingResource {

    @Inject
    Vertx vertx;

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> hello() {
        Map<String, String> result = new HashMap<>();

        // todo : serg : 202205110942, Wed : implement this
        AtomicBoolean aBoolean = new AtomicBoolean(true);
        vertx.eventBus().request("custom.job.1", "Yay! Someone kicked a ball across a patch of grass", ar -> {
            if (ar.succeeded()) {
                log.info("Received reply: " + ar.result().body());
                result.put("answer", ar.result().body().toString());
                aBoolean.getAndSet(false);
            }
        });
        while (aBoolean.get()) { // todo : serg : 202205111233, Wed : get rid of this
            log.info("waiting for answer from eventBus ");
        }

        log.info("return result: " + result);
        return result;
    }
}
