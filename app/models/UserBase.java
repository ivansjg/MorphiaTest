package models;

import play.data.validation.Email;
import play.data.validation.Required;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Indexed;

public abstract class UserBase extends Model {

    public enum Type {
        POLITICIAN,
        CITIZEN,
        ADMIN
    };

    @Email
    @Required
    // Aunque esta clase sea abstracta, y en teoría no debería crear una colección en la DB, la creará, porque
    // si tiene un índice crea una colección aunque no tenga sentido ninguno.
    // Para solucionarlo habría que decirle al ensureIndexes() que no crease el índice si la clase es abstracta.
    @Indexed(unique = true)
    public String email;

    @Required
    public String password;

}