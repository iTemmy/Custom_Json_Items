package com.temmy.json_items_test_1.Parser;

import java.util.Set;

public interface ItemRepoTest {

    void create(String name);

    Object read(String id);

    void delete(String id);

    Object getByName(String name);

    Set<String> getAllItems();
}


