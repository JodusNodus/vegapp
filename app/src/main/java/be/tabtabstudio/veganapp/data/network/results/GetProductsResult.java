package be.tabtabstudio.veganapp.data.network.results;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import be.tabtabstudio.veganapp.data.entities.Product;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetProductsResult implements ApiResult {
    public List<Product> products;
}
