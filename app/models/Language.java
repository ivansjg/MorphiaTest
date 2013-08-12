package models;

import play.data.validation.*;

import play.modules.morphia.Model;
import play.modules.morphia.validation.Unique;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Indexed;

@Entity
public class Language extends Model {

    static final int NAME_MAX = 255;
    static final int LANGUAGE_CODE_MAX = 4;

    @Required
    @MaxSize(NAME_MAX)
    @Indexed(name = "language_name_index", unique = true)
    @Unique("name")
    public String name;

    @MaxSize(LANGUAGE_CODE_MAX)
    @com.google.code.morphia.annotations.Property("lc")
    public String languageCode;

    @Embedded
    public static class EmbededLanguage {

        public String name;

        @com.google.code.morphia.annotations.Property("lc")
        public String languageCode;

    }

}