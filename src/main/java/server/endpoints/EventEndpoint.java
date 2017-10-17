package server.endpoints;

import com.google.gson.Gson;

import server.controllers.ContentController;


import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

import com.google.gson.JsonObject;
import server.models.Event;
import server.providers.EventProvider;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;


/**
 * Created by Filip on 10-10-2017.
 */


@Path("/events")
public class EventEndpoint {

    @GET
    @Path("{id}")
    public Response getEvent(@PathParam("id") int event_id){
        EventProvider eventProvider = new EventProvider();
        Event event = eventProvider.getEvent(event_id);
        
        return Response.status(200).type("application/json").entity(new Gson().toJson(event)).build();

    }

@Path("/events")
public class EventEndpoint {

    @POST
    public Response createEvent(String eventJson) {

        JsonObject eventData = new Gson().fromJson(eventJson, JsonObject.class);

        Event event = new Event(
                eventData.get("owner_id").getAsInt(),
                eventData.get("title").getAsString(),
                Timestamp.valueOf(eventData.get("startDate").getAsString()),
                Timestamp.valueOf(eventData.get("endDate").getAsString()),
                eventData.get("description").getAsString()
        );

        EventProvider eventProvider = new EventProvider();

        try {
            eventProvider.createEvent(event);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Response.status(200).type("application/json").entity(new Gson().toJson(event)).build();

    }


}
