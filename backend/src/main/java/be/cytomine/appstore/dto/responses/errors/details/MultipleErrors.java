package be.cytomine.appstore.dto.responses.errors.details;

import java.util.List;

import be.cytomine.appstore.dto.responses.errors.AppStoreError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class MultipleErrors extends BaseErrorDetails {
    private List<AppStoreError> errors;
}
