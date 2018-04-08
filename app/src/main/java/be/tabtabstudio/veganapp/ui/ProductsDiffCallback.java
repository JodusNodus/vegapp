package be.tabtabstudio.veganapp.ui;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import be.tabtabstudio.veganapp.data.entities.ProductListItem;

public class ProductsDiffCallback extends DiffUtil.Callback {
    List<ProductListItem> oldProducts;
    List<ProductListItem> newProducts;

    public ProductsDiffCallback(List<ProductListItem> oldProducts, List<ProductListItem> newProducts) {
        this.oldProducts = oldProducts;
        this.newProducts = newProducts;
    }

    @Override
    public int getOldListSize() {
        return oldProducts.size();
    }

    @Override
    public int getNewListSize() {
        return newProducts.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldProducts.get(oldItemPosition).equals(newProducts.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldProducts.get(oldItemPosition).equals(newProducts.get(newItemPosition));
    }

}
