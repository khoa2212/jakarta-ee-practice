package com.axonactive.dojo.base.message;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DeleteSuccessMessage {
    private Boolean success;

    public DeleteSuccessMessage() {
        this.success = true;
    }
}
