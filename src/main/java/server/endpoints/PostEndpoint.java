package server.endpoints;



import com.google.gson.Gson;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.ArrayList;


import server.models.Post;
import server.providers.PostProvider;


import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.sql.*;
import java.util.ArrayList;


/**
 * Created by Filip on 10-10-2017.
 */
@Path("/posts")
public class PostEndpoint {


    /*
    This method returns all posts. To do so, the method creates an object of the PostProvider class
    and inserts this object in an arraylist along with the post from the models-package.

    Return response converts the arraylist getAllPosts from GSON to JSON.
     */
    @GET
    public Response getAllPosts() {

        PostProvider postProvider = new PostProvider();
        ArrayList<Post> allPosts = postProvider.getAllPosts();
        return Response.status(200).type("application/json").entity(new Gson().toJson(allPosts)).build();
    }


    @POST
    @Consumes("application/x-www-form-urlencoded")
    public Response createPostMethod(
            @FormParam("owner") int owner,
            @FormParam("content") String content,
            @DefaultValue("0") @FormParam("event") int event,
            @DefaultValue("0") @FormParam("parent") int parent) {

        PostProvider postProvider = new PostProvider();

        Post post = new Post(owner, content, event, parent);

        try {
            post.setId(postProvider.createPost(post));

            return Response.status(201).type("text/plain").entity("Post created").build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(400).type("text/plain").entity("Could not create post").build();
        }

    }

    /** This method returns one specific post chosen by id. The method creates an object of the PostProvider class and inserts
     * this object in an ArrayList and also the variables from the Post model-package.
     *
     *
     * @return It returns an response that converts the ArrayList onePost from Gson to Json.
     */
    @GET
    public Response getOnePost() {
        PostProvider postProvider = new PostProvider(); //Creates an object
        ArrayList<Post> onePost = postProvider.getOnePost();
        return Response.status(200).type("application/json").entity(new Gson().toJson(onePost)).build();
    }
}
