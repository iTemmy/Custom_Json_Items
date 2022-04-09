package com.temmy.json_items_test_1.util;

import com.temmy.json_items_test_1.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveInventory {

    List<Player> viewers = new ArrayList<>();
    Location location;
    Map<Integer, Inventory> pages = new HashMap<>();

    public ActiveInventory(@NotNull Location location, @NotNull List<Player> viewers, @NotNull Map<Integer, Inventory> pages){
        this.location = location;
        this.viewers = viewers;
        this.pages = pages;
    }

    public ActiveInventory(@NotNull Location location, @NotNull Player player, @NotNull Map<Integer, Inventory> pages){
        this.location = location;
        viewers.add(player);
        this.pages = pages;
    }

    public ActiveInventory(@NotNull Location location, @NotNull Player player){
        this.location = location;
        viewers.add(player);
    }

    public @NotNull ActiveInventory addInventory(@NotNull Inventory inv, int page){
        pages.put(page, inv);
        return this;
    }

    public @NotNull ActiveInventory addViewer(@NotNull Player viewer){
        viewers.add(viewer);
        return this;
    }

    public @NotNull Map<Integer, Inventory> getPages(){
        return pages;
    }

    public @NotNull ActiveInventory removeViewer(@NotNull Player player, int page){
        pages.get(page).getViewers().remove(player);
        return this;
    }

    public @NotNull Location getLocation() {
        return location;
    }

    public @NotNull List<Player> getViewers() {
        return viewers;
    }
}
