package com.axonactive.dojo.base.message;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class BaseMessage {

    public static DeleteSuccessMessage deleteSuccessMessage() {
        return new DeleteSuccessMessage();
    }
}
