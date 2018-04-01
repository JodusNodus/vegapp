package be.tabtabstudio.veganapp.api.results;

import java.util.List;

import be.tabtabstudio.veganapp.model.entities.Label;

public class GetLabelsResult implements ApiResult {
    public List<Label> labels;
}
