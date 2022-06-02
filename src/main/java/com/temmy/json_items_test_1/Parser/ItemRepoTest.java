package com.temmy.json_items_test_1.Parser;

import com.temmy.json_items_test_1.util.customItems.CustomItem;

public interface ItemRepoTest {

    void create(String name);

    Object read(String id);

    void delete(String id);

    Object getByName(String name);
}


