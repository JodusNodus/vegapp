package be.tabtabstudio.veganapp.ui.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.tabtabstudio.veganapp.data.entities.ProductListItem;

public class DummyContent {

    public static final List<ProductListItem> ITEMS = new ArrayList<>();

    public static final Map<Long, ProductListItem> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(ProductListItem.getMock());
        }
    }

    private static void addItem(ProductListItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.ean, item);
    }
}
