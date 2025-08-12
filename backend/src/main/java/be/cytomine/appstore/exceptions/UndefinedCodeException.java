package be.cytomine.appstore.exceptions;

import be.cytomine.appstore.dto.responses.errors.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class UndefinedCodeException extends RuntimeException {
    private ErrorCode code;
}
