package server.endpoints;

import com.google.gson.Gson;
import server.controllers.UserController;
import server.models.Event;
import server.providers.EventProvider;

import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

import server.controllers.ContentController;


import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

import com.google.gson.JsonObject;
import server.models.Event;
import server.providers.EventProvider;
import server.providers.PostProvider;

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

    EventProvider eventProvider = new EventProvider();
    ContentController contentController = new ContentController();

    /*
    This method returns all events. To do so, the method creates an object of the EventProvider-class
    and inserts this object in an arraylist along with the user from the models-package.

    Return response converts the arraylist allEvents from GSON to JSON
     */
    @GET
    public Response getAllEvents(){

        EventProvider eventProvider = new EventProvider();

        ArrayList<Event> allEvents = eventProvider.getAllEvents();



        return Response.status(200).type("text/plain").entity(new Gson().toJson(allEvents)).build();


    }

    @GET
    @Path("{id}")
    public Response getEvent(@PathParam("id") int event_id){
        EventProvider eventProvider = new EventProvider();
        PostProvider postProvider = new PostProvider();
        UserController userController = new UserController();

        Event event = eventProvider.getEvent(event_id);

        event.getPosts().addAll(postProvider.getAllPostsByEventId(event_id));

        //Get all participants in the event
        event.getParticipants().addAll(userController.getParticipants(event_id));

        return Response.status(200).type("application/json").entity(new Gson().toJson(event)).build();

    }

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
            /**
             * validateEventInput is called to make sure the timestamp for event equals or is after current time.
             * This way you can't create an event that happens before current time.
             */

            event = contentController.validateEventCreation(event.getId(), event.getTitle(),
                    event.getCreated(), event.getOwner(), event.getStartDate(),
                    event.getEndDate(),event.getDescription());
        }catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
            return Response.status(400).build();
        }

        try {
            eventProvider.createEvent(event);
        }catch (SQLException e){
            return Response.status(501).type("text/plain").entity("Server could not store the validated event object (SQL Error) ").build();
        }

        return Response.status(201).type("text/plain").entity("Event Created").build();


    }

    @POST
    @Path("/subscribe")
    public Response subscribeToEvent(String jsonData){

        JsonObject jsonObj = new Gson().fromJson(jsonData, JsonObject.class);
        int user_id = jsonObj.get("user_id").getAsInt();
        int event_id = jsonObj.get("event_id").getAsInt();

        EventProvider eventProvider = new EventProvider();

        eventProvider.subscribeToEvent(user_id, event_id);

        return Response.status(200).type("text/plain").entity("User subscribed to event").build();

            }


}
