package be.cytomine.appstore.dto.responses.errors.details;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import be.cytomine.appstore.dto.responses.errors.AppStoreError;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class MultipleErrors extends BaseErrorDetails {
    private List<AppStoreError> errors;
}
