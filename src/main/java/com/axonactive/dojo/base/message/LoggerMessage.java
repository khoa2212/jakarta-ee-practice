package com.axonactive.dojo.base.message;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoggerMessage {
    public static String findPaginatedListMessage(String info) {
        return String.format("Attempting to find all %s list with pagination", info);
    }

    public static String findPaginatedListErrorMessage(String info) {
        return String.format("Error when find all %s list with pagination", info);
    }

    public static String findAllMessage(String info) {
        return String.format("Attempting to find all %s list", info);
    }

    public static String findAllMessageError(String info) {
        return String.format("Error when find all %s list", info);
    }

    public static String findByIdMessage(String info, Long id) {
        return String.format("Attempting to find %s by id: %d", info, id);
    }

    public static String findByIdErrorMessage(String info, Long id) {
        return String.format("Error when find %s by id: %d", info, id);
    }

    public static String addMessage(String info) {
        return String.format("Adding %s", info);
    }

    public static String addErrorMessage(String info) {
        return String.format("Error when adding %s", info);
    }

    public static String updateMessage(String info) {
        return String.format("Updating %s", info);
    }

    public static String updateErrorMessage(String info) {
        return String.format("Error when updating %s", info);
    }

    public static String deleteMessage(String info) {
        return String.format("Deleting %s", info);
    }

    public static String deleteErrorMessage(String info) {
        return String.format("Error when deleting %s", info);
    }
}
