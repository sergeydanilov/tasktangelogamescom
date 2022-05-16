package com.tangelogames.jobsystem.rest;

import com.tangelogames.jobsystem.base.AbstractScheduledJob;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Log4j2
@Path("/managing")
public class ManagingResource {

    @Inject
    EventBus bus;

    @Inject
    Instance<AbstractScheduledJob> jobs;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/status")
    public Uni<List<String>> statusAll(String name) {
//        bus.<String>request(name, "status").onItem().;

        List<Uni<String>> unisPerJob = new ArrayList<>();
        jobs.forEach(job -> {
            var statusOfTheJob = bus.<String>request(name, "status").onItem().transform(Message::body);
            unisPerJob.add(statusOfTheJob);
        });
        final int size = unisPerJob.size();
        Uni[] arrayOfUnis = new Uni[size];

        return Uni.combine()
                .all().unis(arrayOfUnis).combinedWith(
                        listOfResponses -> {
                            List<String> list = new LinkedList<String>();
                            for (int i = 0; i < size; i++) {
                                list.add((String) listOfResponses.get(i));
                            }
                            return list;
                        }
                );
//
//
//        List<String> strings = new ArrayList<>();
//        Uni<String> uni = null;
//
//
//        Multi.createFrom().iterable(jobs)
//                .onItem().invoke(v -> {
//                    bus.<String>request(v.getName(), "status")
//                            .onItem().transform(Message::body).subscribe().with(s -> {
//                                strings.add(s);
//                            });
//                })
//                .onCompletion().invoke(() -> uni.onItem().transform(
//                                s -> strings.stream().collect(Collectors.joining("."))
//                        )
//                );
//
//
//        return uni;
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
