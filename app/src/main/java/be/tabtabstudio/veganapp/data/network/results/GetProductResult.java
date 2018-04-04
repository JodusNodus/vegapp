package be.tabtabstudio.veganapp.data.network.results;

import java.util.List;

import be.tabtabstudio.veganapp.data.entities.Product;
import be.tabtabstudio.veganapp.data.entities.Supermarket;

public class GetProductResult implements ApiResult {
    public Product product;
    public List<Supermarket> supermarkets;
}
