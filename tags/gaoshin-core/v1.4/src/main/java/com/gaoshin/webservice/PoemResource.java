package com.gaoshin.webservice;

import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.gaoshin.beans.Post;

@Path("/poem")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json;charset=utf-8" })
public class PoemResource extends PredefinedGroupResource {
    public static final String PoemGroupName = "Love Poem";

    public PoemResource() {
        super(PoemGroupName);
    }

    @GET
    @Path("random")
    public Post random() {
        Post post = groupService.randomPost(group.getId());
        if (post == null) {
            post = new Post();
            String poems[] = {
                    "You said we'd always be together\n"
                    + "And that gave me so much pleasure\n"
                    + "\n"
                    + "You said you'd never tell me a lie\n"
                    + "And that made me want to cry\n"
                    + "......",

            "If love asks a question, do not deceive"
                    + "\nFor the truth is the answer, I only believe"
                    + "\n"
                    + "\nIf my heart should open, love do not fear"
                    + "\nOr if my eyes in happiness shed a tear"
                    + "\n"
                    + "\nLet the wind in my soul blow you away"
                    + "\nAnd the sun in my heart brighten your day"
                    + "\n"
                    + "\n"
                    + "\nby -Nicola Edmanson"
            };
            post.setContent(poems[new Random(System.currentTimeMillis()).nextInt(poems.length)]);
        }
        return post;
    }
}
