package be.tabtabstudio.veganapp.data.network.results;

import java.util.List;

import be.tabtabstudio.veganapp.data.entities.Label;

public class GetLabelsResult implements ApiResult {
    public List<Label> labels;
}
