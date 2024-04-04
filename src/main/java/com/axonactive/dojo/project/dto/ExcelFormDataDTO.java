package com.axonactive.dojo.project.dto;

import lombok.*;

import javax.ws.rs.FormParam;
import java.io.File;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelFormDataDTO {

    @FormParam("file")
    private File file;
}
