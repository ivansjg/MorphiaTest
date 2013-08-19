import org.junit.*;

import java.util.*;

import play.test.*;
import play.Logger;
import play.libs.Codec;

import com.mongodb.Mongo;
import com.mongodb.DBObject;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;

import com.google.code.morphia.Datastore;

import org.bson.types.ObjectId;

import play.modules.morphia.Model.MorphiaQuery;
import play.modules.morphia.MorphiaPlugin;

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

    @Test
    public void lalala() {
        // http://stackoverflow.com/questions/6871008/mongodb-embedded-object-creation-in-java-without-morphia
        // http://docs.mongodb.org/manual/reference/operator/pull/#op._S_pull
        Mongo mongo = MorphiaPlugin.ds().getMongo();
        Datastore ds = MorphiaPlugin.ds();
        DB db = ds.getDB();

        ObjectId userId = (ObjectId) User.findByEmail("paul@gmail.com").getId();
        DBObject query = new BasicDBObject("anotherOptionForFollowing", userId);
        DBObject update = new BasicDBObject("$pull", new BasicDBObject("anotherOptionForFollowing", userId));

        DBCollection col = db.getCollection("users");
        col.update(query, update);

        /********************************/

        query = new BasicDBObject("email", new BasicDBObject("$ne", "paul@gmail.com"));
        DBCursor cursor = col.find(query);

        try {
            while (cursor.hasNext()) {
                Logger.info("-----------------------------------");
                Logger.info("%s", cursor.next());
            }
        } finally {
            cursor.close();
        }
    }

}