package service;

import java.util.Arrays;
import java.util.Optional;

public enum UserChoice {
    ADD_NETWORK(1, "to add network"),
    ADD_DEVICE(2, "to add new device"),
    ADD_CONNECTION(3, "to add new connection between devices"),
    EDIT_NETWORK(4, "to edit existed network"),
    REMOVE_CONNECTION(5, "to remove connection"),
    REMOVE_NETWORK(6, "to remove network"),

    EXIT(10, "to exit");


    private final int code;
    private final String description;

    UserChoice(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<UserChoice> valueOf(int code) {
        return Arrays.stream(values())
                .filter(action -> action.getCode() == code)
                .findAny();
    }
}