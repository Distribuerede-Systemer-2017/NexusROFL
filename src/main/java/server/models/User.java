package server.models;

import java.util.ArrayList;

/**
 * Created by Filip on 10-10-2017.
 */
public class User {

    private int id;
    private String password;
    private String salt;
    private String firstName;
    private String lastName;
    private String email;
    private String description;
    private char gender;
    private String major;
    private int semester;
    private ArrayList<Event> events;
    private ArrayList<Post> posts;

    public User(int id, String password, String salt, String firstName, String lastName, String email, String description, char gender, String major, int semester) {
        this.id = id;
        this.password = password;
        this.salt = salt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.description = description;
        this.gender = gender;
        this.major = major;
        this.semester = semester;

        this.events = new ArrayList<Event>();
        this.posts = new ArrayList<Post>();
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public char getGender() {
        return gender;
    }

    public String getMajor() {
        return major;
    }

    public int getSemester() {
        return semester;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }
}