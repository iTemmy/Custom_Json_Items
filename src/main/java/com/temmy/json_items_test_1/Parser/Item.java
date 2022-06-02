package com.temmy.json_items_test_1.Parser;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.util.customItems.CustomItem;
import com.temmy.json_items_test_1.util.newCustomItem.NewCustomItem;

import javax.annotation.Nullable;

public final class Item implements ItemRepoTest {

    @Override
    public void create(String name) {
        Main.getCustomItems().put(name, new NewCustomItem().setName(name));
    }

    @Override
    @Nullable
    public NewCustomItem read(String id) {
        return Main.getCustomItems().get(id);
    }

    @Override
    public void delete(String id) {
        Main.getCustomItems().remove(id);
    }

    @Override
    @Nullable
    public NewCustomItem getByName(String name) {
        return Main.getCustomItems().get(name);
    }
}
