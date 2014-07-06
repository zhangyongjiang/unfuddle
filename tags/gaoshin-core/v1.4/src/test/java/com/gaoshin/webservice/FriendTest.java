package com.gaoshin.webservice;

import junit.framework.Assert;

import org.junit.Test;

import com.gaoshin.beans.GenericResponse;
import com.gaoshin.beans.User;
import com.gaoshin.beans.UserList;

public class FriendTest extends GaoshinTester {
    @Test
    public void testSearch() {
        User friend = createUser();
        User friend2 = createUser();

        User user = createUser();
        login(user);

        getBuilder("/user/add-friend/" + friend.getId()).post(GenericResponse.class, " ");
        UserList friends = getBuilder("/user/my-friends").get(UserList.class);
        Assert.assertEquals(1, friends.getList().size());
        Assert.assertEquals(friend.getName(), friends.getList().get(0).getName());

        getBuilder("/user/add-friend/" + friend2.getId()).post(GenericResponse.class, " ");
        friends = getBuilder("/user/my-friends").get(UserList.class);
        Assert.assertEquals(2, friends.getList().size());

        logout();
    }
}
