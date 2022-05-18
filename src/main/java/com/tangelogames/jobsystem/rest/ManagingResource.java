package com.tangelogames.jobsystem.rest;

import com.tangelogames.jobsystem.base.AbstractScheduledJob;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import lombok.extern.log4j.Log4j2;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Log4j2
@Path("/managing")
public class ManagingResource {

    @Inject
    EventBus bus;

    @Inject
    Instance<AbstractScheduledJob> jobs;

    @Blocking
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/status")
    public Uni<Map<String, String>> statusAll(String name) {
        // todo : serg : 202205180451, Wed : refactoring and unit testing
        Map<String, Uni<String>> unisPerJob = new HashMap<>();
        jobs.forEach(job -> {
            final String address = job.getName();
            var statusOfTheJob = bus.<String>request(address, "status").onItem().transform(Message::body);
            unisPerJob.put(address, statusOfTheJob);
        });


        Uni<String>[] uins = new Uni[unisPerJob.keySet().size()];
        String[] names = new String[unisPerJob.keySet().size()];
        int counter = 0;
        for (String address : unisPerJob.keySet()) {
            names[counter] = address;
            uins[counter] = unisPerJob.get(address);
            counter++;
        }

        Uni<Map<String, String>> uni = Uni.combine()
                .all().unis(uins).combinedWith(
                        listOfResponses -> {
                            Map<String, String> map = new LinkedHashMap<>();
                            for (int index = 0; index < names.length; index++) {
                                map.put(names[index], (String) listOfResponses.get(index));
                            }
                            return map;
                        }
                );

        return uni;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/status/{name}")
    public Uni<String> status(String name) {
        return bus.<String>request(name, "status")
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
