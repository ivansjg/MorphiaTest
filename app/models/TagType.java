package models;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;

@Entity("tag_types")
public class TagType extends Model {

    @Column("value")
    public String value;

    public static TagType findByValue(String value) {
        return TagType.all().filter("value", value).get();
    }

}