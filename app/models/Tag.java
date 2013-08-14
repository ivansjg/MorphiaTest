package models;

import siena.*;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;

import play.Logger;

@Entity("tags")
public class Tag extends Model implements Followable {

    @Column("v")
    public String value;

    @Column("t")
    public TagType type;

    public static Tag findByValue(String value) {
        return Tag.q().filter("value", value).get();
    }

    public void handle() {
        Logger.info("[*] Handle en TAG");
    }
}