package models;

import java.util.List;
import java.util.ArrayList;

import play.data.validation.Email;
import play.data.validation.Required;
import play.Logger;

import play.modules.morphia.Model;
import play.modules.morphia.CacheEntity;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Embedded;

import org.bson.types.ObjectId;

@Entity("users")
@CacheEntity("3h")
public class User extends UserBase implements Followable {

    public String fullname;

    public String fbToken;

    public List<Followable> following;

    public List<ObjectId> anotherOptionForFollowing;

    @Embedded
    public Language preferredLanguage;

/*
    OJO!! El único método que hace uso de la caché de forma automática es el getById()

    @Override
    public User save() {
        User u = super.save();

        // invalido y meto en la caché
        return u;
    }

    @Override
    public User delete() {
        User u = super.delete();

        // invalido en la caché
        return u;
    }
*/

    public static User findByEmail(String email) {
        return User.q().filter("email", email).get();
    }

    public void addFollowing(Followable thing) {
        if (following == null)
            following = new ArrayList<Followable>();

        following.add(thing);
        save();
    }

    public void addFollowing(ObjectId id) {
        if (anotherOptionForFollowing == null)
            anotherOptionForFollowing = new ArrayList<ObjectId>();

        anotherOptionForFollowing.add(id);
        save();
    }

    public static UserBase connect(String email, String password) {
        return find("byEmailAndPassword", email, password).first();
    }

    @Override
    public String toString() {
        return "[USER] fullname: " + fullname;
    }

    public void handle() {
        Logger.info("[*] Handle en USER");
    }
}