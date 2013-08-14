package models;

import play.data.validation.Required;
import play.data.validation.MaxSize;

import play.modules.morphia.Model;
import play.modules.morphia.validation.Unique;

import com.google.code.morphia.annotations.Entity;

@Entity("languages")
public class Language extends Model {

    static final int NAME_MAX = 255;
    static final int LANGUAGE_CODE_MAX = 4;

    @Required
    @MaxSize(NAME_MAX)
    @Unique("name")
    public String name;

    @MaxSize(LANGUAGE_CODE_MAX)
    @Column("lc")
    public String languageCode;

}