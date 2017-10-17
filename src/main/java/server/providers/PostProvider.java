package server.providers;


import server.models.Event;
import server.models.Post;
import server.models.User;

import server.util.DBManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.ArrayList;


/**
 * Created by Filip on 10-10-2017.
 */
public class PostProvider {

    /*
    PreparedStatement for getting all posts from posts.
    */

    public ArrayList<Post> getAllPosts() {
        ArrayList<Post> allPosts = new ArrayList<>();

        ResultSet resultSet = null;

        PreparedStatement getAllPostsStmt = null;
        try {
            getAllPostsStmt = DBManager.getConnection().prepareStatement("SELECT * FROM posts");

            resultSet = getAllPostsStmt.executeQuery();


            /*
            Getting all variables from the model class Post
            and adding all posts to the ArrayList
             */
             while (resultSet.next()){
                Post post = new Post(
                        resultSet.getInt("post_id"),
                        resultSet.getTimestamp("created"),
                        new User(resultSet.getInt("user_id")),
                        resultSet.getString("content"),
                        new Event(resultSet.getInt("event_id")),
                        new Post(resultSet.getInt("parent_id"))
                        );

                allPosts.add(post);

             }
             resultSet.close();
             getAllPostsStmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allPosts;

        }

    public int createPost(Post post) throws SQLException {

        //Creating prepared statement
        PreparedStatement createPostStatement =
                DBManager.getConnection().prepareStatement("INSERT INTO posts " + "(content, event_id, parent_id, user_id)" +
                        "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

        //Inserting values into the prepared statement
        createPostStatement.setString(1, post.getContent());

        if (post.getEvent().getId() == 0){
            createPostStatement.setNull(2, 1);
        } else {
            createPostStatement.setInt(2, post.getEvent().getId());
        }

        if (post.getParent().getId() == 0) {
            createPostStatement.setNull(3,1);
        } else {
            createPostStatement.setInt(3, post.getParent().getId());
        }

        createPostStatement.setInt(4, post.getOwner().getId());

        //Execute update
        int rowsUpdated = createPostStatement.executeUpdate();

        //Checking if a row has been updated
        if(rowsUpdated !=1) {
            throw new SQLException("Error with creating a post, no rows are affected");
        }

        //Collect generated Post id
        ResultSet generatedKeys = createPostStatement.getGeneratedKeys();

        //Checking if primary key has been created
        if(generatedKeys.next()) {
            post.setId(generatedKeys.getInt(1));
        } else {
            throw new SQLException("Error with creating post, could not retrieve ID");
        }

        //Closing query
        createPostStatement.close();

        //Returning Post id
        return post.getId();
    }


    //Creating method for getting one post
    public ArrayList<Post> getOnePost() {
     ArrayList<Post> onePost = new ArrayList<>(); //Creating object of ArrayList

        ResultSet resultSet = null;

        //Creating prepared statement for getting one post
        PreparedStatement getOnePostStatement = null;
        try {
            getOnePostStatement = DBManager.getConnection().prepareStatement("SELECT FROM posts WHERE posts = post_id");

            resultSet = getOnePostStatement.executeQuery();

            while (resultSet.next()) {
                Post post = new Post(
                    resultSet.getInt("post_id"),
                    resultSet.getTimestamp("created"),
                    new User(resultSet.getInt("user_id")), //Creating an owner to the post
                    resultSet.getString("content"),
                    new Event(resultSet.getInt("event_id")), //Creating an event to the post
                    new Post(resultSet.getInt("parent_id")) //Creating an parent to the post
                );

            } //Closing query
            resultSet.close();
            getOnePostStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();

        } //Returning one post
        return onePost;

    }

}


