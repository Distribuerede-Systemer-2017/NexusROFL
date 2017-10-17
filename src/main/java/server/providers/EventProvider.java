package server.providers;

import server.models.Event;

import server.models.User;
import server.util.DBManager;



import server.models.User;
import server.util.DBManager;

import javax.ws.rs.Path;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import java.sql.PreparedStatement;
import java.sql.Statement;



/**
 * Created by Filip on 10-10-2017.
 */
public class EventProvider {

    /*
    PreparedStatetement for getting all events ordered by id from DB cafe_nexus
     */
    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> allEvents = new ArrayList<>();

        ResultSet resultSet = null;

        try {
            PreparedStatement getAllEventsStmt = null;

            getAllEventsStmt = DBManager.getConnection().
                    prepareStatement("SELECT * FROM events ORDER BY event_id");


            resultSet = getAllEventsStmt.executeQuery();

         /*
         Getting variables from Models_Event class
         and adding events to ArrayList
         */
            while (resultSet.next()) {
                Event event = new Event(
                        resultSet.getInt("event_id"),
                        resultSet.getString("title"),
                        resultSet.getTimestamp("created"),
                        new User(resultSet.getInt("owner_id")),
                        resultSet.getTimestamp("start"),
                        resultSet.getTimestamp("end"),
                        resultSet.getString("description"));

                allEvents.add(event);


            }

            resultSet.close();

            getAllEventsStmt.close();

        } catch (
                SQLException e)

        {
            e.printStackTrace();
        }

        return allEvents;

    }

    public Event getEvent(int event_id) {
        ArrayList<Event> getEvent = new ArrayList<>();
        Event event = null;
        ResultSet resultSet = null;

        try {
           PreparedStatement getEventStmt = DBManager.getConnection()
                    .prepareStatement("SELECT * FROM events WHERE event_id = ?");

           getEventStmt.setInt(1, event_id);

            resultSet = getEventStmt.executeQuery();

            while (resultSet.next()) {
                event = new Event(
                    resultSet.getInt("event_id"),
                    resultSet.getString("title"),
                    resultSet.getTimestamp("created"),
                    new User(resultSet.getInt("owner_id")),
                    resultSet.getTimestamp("start"),
                    resultSet.getTimestamp("end"),
                    resultSet.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return event;
    }


    public void createEvent(Event event) throws SQLException {

        PreparedStatement createEventStmt = DBManager.getConnection().prepareStatement("INSERT INTO events (title, description, start, events.end, owner_id) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

        createEventStmt.setString(1, event.getTitle());
        createEventStmt.setString(2, event.getDescription());
        createEventStmt.setTimestamp(3, event.getStartDate());
        createEventStmt.setTimestamp(4, event.getEndDate());
        createEventStmt.setInt(5, event.getOwner().getId());

        createEventStmt.executeUpdate();

    }

}

