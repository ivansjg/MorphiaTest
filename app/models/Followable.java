package models;

import siena.*;

public interface Followable {

    public enum Type {
        TAG,
        CONVERSATION,
        USER
    };

    public void handle();

}