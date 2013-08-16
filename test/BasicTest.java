import org.junit.*;

import java.util.*;

import play.test.*;
import play.Logger;
import play.libs.Codec;

import org.bson.types.ObjectId;

import play.modules.morphia.Model.MorphiaQuery;

import models.*;

public class BasicTest extends UnitTest {

    @Test
    public void aVeryImportantThingToTest() {
        User u = User.q().filter("fullname", "Bob").get();

        assertEquals(u.email, "bob@gmail.com");

        Logger.info("[*] %s", u.preferredLanguage);

        u.addFollowing(Tag.findByValue("Economy"));
        u.addFollowing(User.findByEmail("paul@gmail.com"));

        u.addFollowing((ObjectId) User.findByEmail("paul@gmail.com").getId());

        User user = new User();
        user.email = "imanol@gmail.com";
        user.password = "secret";
        user.fullname = "Imanol Pérez";
        user.save();

        List<User> allUsers = User.findAll();
        List<User> imanolUsers = User.find("email", "imanol@gmail.com").asList();

        user = User.q().filter("preferredLanguage.languageCode", "es").get();
        Logger.info("name preflang(es): %s", user.preferredLanguage.name);

        MorphiaQuery q = User.q();
        q.or(q.criteria("email").containsIgnoreCase("bOb"), 
             q.criteria("fullname").containsIgnoreCase("jeff"));
        List<User> lista = q.asList();
        assertEquals(lista.size(), 2);

/*
        for (int i = 1 ; i <= 5000000 ; i++) {
            user = new User();
            user.email = Codec.UUID() + "@gmail.com";
            user.password = "secret";
            user.fullname = user.email;
            user.save();
        }
        */
    }

}