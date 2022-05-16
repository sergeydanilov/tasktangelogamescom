package com.tangelogames.jobsystem.rest;

import com.tangelogames.jobsystem.base.AbstractScheduledJob;
import com.tangelogames.jobsystem.jobs.CustomJob1;
import com.tangelogames.jobsystem.jobs.CustomJob2;
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
//        bus.<String>request(name, "status").onItem().;

        final Uni<String> status1 = bus.<String>request("com.tangelogames.jobsystem.jobs.CustomJob1", "status").onItem().transform(Message::body);
        final Uni<String> status2 = bus.<String>request("com.tangelogames.jobsystem.jobs.CustomJob2", "status").onItem().transform(Message::body);

//        final Uni<String> status1 = Uni.createFrom().item("Hello1");
//        final Uni<String> status2 = Uni.createFrom().item("Hello2");


//        return Uni.combine()
//                .all().unis(status1, status2).asTuple();


        Uni<Map<String, String>> uni = Uni.combine()
                .all().unis(status1, status2).combinedWith(
                        listOfResponses -> {
                            Map<String, String> map = new LinkedHashMap<>();
                            map.put(CustomJob1.class.getCanonicalName(), (String) listOfResponses.get(0));
                            map.put(CustomJob2.class.getCanonicalName(), (String) listOfResponses.get(1));
                            return map;
                        }
                );

        return uni;

//        List<Uni<String>> unisPerJob = new ArrayList<>();
//        jobs.forEach(job -> {
//            var statusOfTheJob = bus.<String>request(name, "status").onItem().transform(Message::body);
//            unisPerJob.add(statusOfTheJob);
//        });
//        final int size = unisPerJob.size();
//        Uni[] arrayOfUnis = new Uni[size];
//
//        final Uni<List<String>> listUni = Uni.combine()
//                .all().unis(arrayOfUnis).combinedWith(
//                        listOfResponses -> {
//                            List<String> list = new LinkedList<>();
//                            for (int i = 0; i < size; i++) {
//                                list.add((String) listOfResponses.get(i));
//                            }
//                            return list;
//                        }
//                );

//        return listUni;
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
