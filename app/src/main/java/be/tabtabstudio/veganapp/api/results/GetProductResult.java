package be.tabtabstudio.veganapp.api.results;

import java.util.List;

import be.tabtabstudio.veganapp.model.entities.Product;
import be.tabtabstudio.veganapp.model.entities.Supermarket;

public class GetProductResult implements ApiResult {
    public Product product;
    public List<Supermarket> supermarkets;
}
