package com.axonactive.dojo.base.message;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class BaseMessage {

    public static DeleteSuccessMessage deleteSuccessMessage() {
        return new DeleteSuccessMessage();
    }
}
