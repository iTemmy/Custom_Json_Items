package com.temmy.json_items_test_1.attribute;

import com.temmy.json_items_test_1.Main;
import com.temmy.json_items_test_1.util.ActiveInventory;
import com.temmy.json_items_test_1.util.CustomDataTypes;
import com.temmy.json_items_test_1.util.InvalidInventoryException;
import com.temmy.json_items_test_1.util.InvalidLocationException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class MultiPageChests {

    private MultiPageChests(){
    }

    static final Logger log = Main.getPlugin().getLogger();
    /**NamepsacedKey used to hold PersistentDataContainer with page inventories.*/
    public static final NamespacedKey pageContainerKey = new NamespacedKey(Main.getPlugin(), "pages_container");
    /**NamespacedKey used to hold PersistentDataContainer with location data on page item.*/
    public static final NamespacedKey locationKey = new NamespacedKey(Main.getPlugin(), "location");
    public static final NamespacedKey worldKey = new NamespacedKey(Main.getPlugin(), "world");
    public static final NamespacedKey xKey = new NamespacedKey(Main.getPlugin(), "x");
    public static final NamespacedKey yKey = new NamespacedKey(Main.getPlugin(), "y");
    public static final NamespacedKey zKey = new NamespacedKey(Main.getPlugin(), "z");
    /** NamespacedKey used to hold page number on page item in page Inventory.*/
    public static final NamespacedKey pageNumberKey = new NamespacedKey(Main.getPlugin(), "page");

    /**
     * used to trigger all events related to MultiPageChests from Attribute.class
     * @param e Bukkit event that has been fired.
     * @param args Arguments for MultiPageChests
     */
    public static void trigger(@NotNull Event e, @NotNull String[] args) {
        try {
            if (e instanceof BlockPlaceEvent) blockPlaceEvent((BlockPlaceEvent) e, args);
            if (e instanceof BlockBreakEvent) blockBreakEvent((BlockBreakEvent) e);
            if (e instanceof InventoryOpenEvent) inventoryOpenEvent((InventoryOpenEvent) e);
            if (e instanceof InventoryCloseEvent) inventoryCloseEvent((InventoryCloseEvent) e);
            if (e instanceof InventoryClickEvent) inventoryClickEvent((InventoryClickEvent) e);
        } catch (InvalidInventoryException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Used for triggering InventoryClickEvent from another class without triggering using attribute
     * @param event  InventoryClickEvent to be parsed to inventoryClickEvent()
     * @throws InvalidInventoryException only throw is the inventory attempting to be loaded is malformed
     */
    public static void triggerInventoryClick(@NotNull InventoryClickEvent event) throws InvalidInventoryException {
        inventoryClickEvent(event);
    }

    /**
     * Used for InventoryClickEvent  logic for MultiPageChests
     * @param event The InventoryClickEvent so that we can check what slot is clicked and which inventory is clicked
     * @throws InvalidInventoryException This will only be thrown if the inventory that is loaded from the
     * PersistentDataContainer is invalid in some way
     */
    @SuppressWarnings("ConstantConditions")
    private static void inventoryClickEvent(@NotNull InventoryClickEvent event) throws InvalidInventoryException {
        Inventory inv = event.getInventory();
        if (inv.getItem(inv.getSize()-5) == null) return;
        if (!inv.getItem(inv.getSize()-5).hasItemMeta()) return;
        ItemStack pageItem = inv.getItem(inv.getSize()-5);
        if (event.getClickedInventory() == event.getView().getBottomInventory()) return;
        if (!pageItem.getItemMeta().getPersistentDataContainer().has(locationKey))
            throw new InvalidInventoryException(String.format(
                    "Unable to find persistent storage container for location data of %s's MultiPageChest \n Player was at: \"%dX, %dY, %dZ\" in world \"%s\" when this error was thrown",
                    event.getWhoClicked().getName(),
                    event.getWhoClicked().getLocation().getBlockX(),
                    event.getWhoClicked().getLocation().getBlockY(),
                    event.getWhoClicked().getLocation().getBlockZ(),
                    event.getWhoClicked().getLocation().getWorld().getName()));
        PersistentDataContainer locationContainer = pageItem.getPersistentDataContainer().get(locationKey, PersistentDataType.TAG_CONTAINER);
        Location location = new Location(
                Bukkit.getWorld(locationContainer.get(worldKey, PersistentDataType.STRING)),
                locationContainer.get(xKey, PersistentDataType.DOUBLE),
                locationContainer.get(yKey, PersistentDataType.DOUBLE),
                locationContainer.get(zKey, PersistentDataType.DOUBLE));
        if (!(location.getBlock().getState() instanceof TileState tilestate)) return;
        List<Integer> range = IntStream.rangeClosed(inv.getSize() - 9, inv.getSize()).boxed().toList();
        Integer page = pageItem.getItemMeta().getPersistentDataContainer().get(pageNumberKey, PersistentDataType.INTEGER);
        if (range.contains(event.getSlot())) {
            event.setCancelled(true);
            /*
              this is the section where the bottom row controls are processed e.g. changing to the next or previous page.
             */
            if (event.getSlot() == inv.getSize() - 1) {
                PersistentDataContainer container = tilestate.getPersistentDataContainer().get(pageContainerKey, PersistentDataType.TAG_CONTAINER);
                tilestate.getPersistentDataContainer().set(
                        pageContainerKey, PersistentDataType.TAG_CONTAINER,
                        savePage(container, inv, page));
                Player player = (Player) event.getWhoClicked();
                if (tilestate.getPersistentDataContainer().get(pageContainerKey, PersistentDataType.TAG_CONTAINER).has(new NamespacedKey(Main.getPlugin(), String.format("page_%d", page + 1)), CustomDataTypes.Inventory)) {
                    Inventory newInventory = recreateInventoryAndOpen(tilestate.getPersistentDataContainer().get(
                                    pageContainerKey, PersistentDataType.TAG_CONTAINER).
                            get(new NamespacedKey(Main.getPlugin(), String.format("page_%d",
                                    page + 1)), CustomDataTypes.Inventory));

                    page++;

                    if (!Main.newActiveInventories.containsKey(location)) {
                        ActiveInventory activeInv = new ActiveInventory(location, (Player) event.getWhoClicked()).addInventory(newInventory, page);
                        Main.newActiveInventories.put(location, activeInv);
                    } else {
                        if (Main.newActiveInventories.get(location).getPages().containsKey(page)) {
                            Main.newActiveInventories.get(location).addViewer(player);
                            event.getWhoClicked().openInventory(Main.newActiveInventories.get(location).getPages().get(page));
                        } else {
                            player.openInventory(newInventory);
                            Main.newActiveInventories.get(location).getPages().put(page, newInventory);
                            Main.newActiveInventories.get(location).addViewer(player);
                        }
                    }
                }
            } else if (event.getSlot() == inv.getSize() - 9) {
                PersistentDataContainer container = tilestate.getPersistentDataContainer().get(pageContainerKey, PersistentDataType.TAG_CONTAINER);
                tilestate.getPersistentDataContainer().set(
                        pageContainerKey, PersistentDataType.TAG_CONTAINER,
                        savePage(container, inv, page));
                Player player = (Player) event.getWhoClicked();
                if (tilestate.getPersistentDataContainer().get(pageContainerKey, PersistentDataType.TAG_CONTAINER).has(new NamespacedKey(Main.getPlugin(), String.format("page_%d", page - 1)), CustomDataTypes.Inventory)) {
                    Inventory newInventory = recreateInventoryAndOpen(tilestate.getPersistentDataContainer().get(
                                    pageContainerKey, PersistentDataType.TAG_CONTAINER).
                            get(new NamespacedKey(Main.getPlugin(), String.format("page_%d",
                                    page - 1)), CustomDataTypes.Inventory));

                    page--;

                    if (!Main.newActiveInventories.containsKey(location)) {
                        ActiveInventory activeInv = new ActiveInventory(location, (Player) event.getWhoClicked()).addInventory(newInventory, page);
                        Main.newActiveInventories.put(location, activeInv);
                    } else {
                        if (Main.newActiveInventories.get(location).getPages().containsKey(page)) {
                            event.getWhoClicked().openInventory(Main.newActiveInventories.get(location).getPages().get(page));
                            Main.newActiveInventories.get(location).addViewer(player);
                        } else {
                            player.openInventory(newInventory);
                            Main.newActiveInventories.get(location).getPages().put(page, newInventory);
                            Main.newActiveInventories.get(location).addViewer(player);
                        }
                    }
                }
            }
        }
    }

    /**
     * Used for checking if the location of the MultiPageChest is already loaded into the Map of ActiveInventories.
     * @param loc Location of the MultiPageChest.
     * @return will return ActiveInventory if location is stored or will return null if not stored yet.
     */
    private static @Nullable ActiveInventory inventoryAlreadyOpen(@NotNull Location loc){
        return Main.newActiveInventories.get(loc);
    }

    /**
     * Used for changing the name of the inventory since the name isn't saved as part of the inventory but as part of the InventoryView.
     * @param inv Inventory to be recreated.
     * @return will return the new inventory.
     */
    @SuppressWarnings("ConstantConditions")
    private static Inventory recreateInventoryAndOpen(Inventory inv){
        try {
            Component comp = Component.text(String.format("Page %d", inv.getItem(inv.getSize()-5).getItemMeta()
                .getPersistentDataContainer().get(pageNumberKey, PersistentDataType.INTEGER)+1));
            Location loc = getLocation(inv.getItem(inv.getSize()-5).getItemMeta().getPersistentDataContainer().get(locationKey, PersistentDataType.TAG_CONTAINER));
            Inventory tt = Bukkit.createInventory((InventoryHolder) loc.getBlock().getState(), inv.getSize(), comp);
            tt.setContents(inv.getContents());
            //player.openInventory(tt);
            return tt;
        } catch (InvalidLocationException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Will save the inventory parsed in to the container parsed in using a NamespacedKey with the page parsed in.
     * @param pageContainer PersistentDataContainer used to store the inventories on the MultiPageChest.
     * @param inv Inventory to be saved into the PersistentDataContainer.
     * @param page Page number of the inventory to be saved.
     * @return Will return updated PersistentDataContainer.
     */
    public static @NotNull PersistentDataContainer savePage(@NotNull PersistentDataContainer pageContainer, @NotNull Inventory inv, int page){
        pageContainer.set(new NamespacedKey(Main.getPlugin(), String.format("page_%d", page)), CustomDataTypes.Inventory, inv);
        return pageContainer;
    }

    /**
     * Called when the InventoryCloseEvent is called for MultiPageChests.
     * @param event InventoryCloseEvent fired that called this function.
     */
    @SuppressWarnings("ConstantConditions")
    private static void inventoryCloseEvent(@NotNull InventoryCloseEvent event) {
        try{
            if (event.getReason() == InventoryCloseEvent.Reason.OPEN_NEW) return;
            int page;
            ItemStack pageItem = event.getInventory().getItem(event.getInventory().getSize() - 5);
            Location loc = null;
            if (pageItem == null) return;
            if (!pageItem.hasItemMeta() || !pageItem.getItemMeta().getPersistentDataContainer().has(pageNumberKey, PersistentDataType.INTEGER))
                return;
            if (pageItem.getItemMeta().getPersistentDataContainer().has(locationKey, PersistentDataType.TAG_CONTAINER))
                loc = getLocation(Objects.requireNonNull(pageItem.getItemMeta().getPersistentDataContainer().get(locationKey, PersistentDataType.TAG_CONTAINER)));
            if (loc == null) return;
            if (!(loc.getBlock().getState() instanceof TileState state)) return;
            page = pageItem.getPersistentDataContainer().get(pageNumberKey, PersistentDataType.INTEGER);
            PersistentDataContainer container = state.getPersistentDataContainer().get(pageContainerKey, PersistentDataType.TAG_CONTAINER);
            assert container != null;
            container.set(new NamespacedKey(Main.getPlugin(), String.format("page_%d", page)), CustomDataTypes.Inventory, event.getInventory());
            state.getPersistentDataContainer().set(pageContainerKey, PersistentDataType.TAG_CONTAINER, container);
            state.update();
            if (!event.getInventory().getViewers().isEmpty()) return;
            ActiveInventory activeInv = inventoryAlreadyOpen(state.getLocation());
            if (activeInv != null) {
                activeInv.removeViewer((Player) event.getPlayer(), page);
            }
        } catch (InvalidLocationException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Called when InventoryOpenEvent is fired for MultiPageChest.
     * @param event InventoryOpenEvent that is fired everytime there is an Inventory opened.
     * @throws InvalidInventoryException Exception will only be thrown when the inventory attempting to be opened is invalid.
     */
    @SuppressWarnings("ConstantConditions")
    private static void inventoryOpenEvent(@NotNull InventoryOpenEvent event) throws InvalidInventoryException {
        try {
            TileState chest;
            if (event.getInventory().getLocation() != null)
                chest = (TileState) event.getInventory().getLocation().getBlock().getState();
            else
                chest = (TileState) getLocation(event.getInventory().getItem(event.getInventory().getSize() - 5).getPersistentDataContainer().get(locationKey, PersistentDataType.TAG_CONTAINER)).getBlock().getState();
            event.setCancelled(true);
            Inventory inv = chest.getPersistentDataContainer().get(pageContainerKey, PersistentDataType.TAG_CONTAINER).get(
                    new NamespacedKey(Main.getPlugin(), "page_0"), CustomDataTypes.Inventory);
            if (inv.getItem(inv.getSize() - 5) == null || !inv.getItem(inv.getSize() - 5).hasItemMeta()) {
                throw new InvalidInventoryException(String.format(
                        "Unable to get page number for %s's inventory at: \"%dx, %dy, %dz\"",
                        event.getPlayer().getName(),
                        chest.getLocation().getBlockX(),
                        chest.getLocation().getBlockY(),
                        chest.getLocation().getBlockZ()));
            }
            Location location = getLocation(inv.getItem(inv.getSize() - 5).getItemMeta().getPersistentDataContainer().get(locationKey, PersistentDataType.TAG_CONTAINER));
            int page = inv.getItem(inv.getSize()-5).getItemMeta().getPersistentDataContainer().get(pageNumberKey, PersistentDataType.INTEGER);
            Inventory newInv = recreateInventoryAndOpen(inv);
            if (!Main.newActiveInventories.containsKey(location)){
                Main.newActiveInventories.put(location, new ActiveInventory(location, (Player) event.getPlayer()).addInventory(newInv, page));
                event.getPlayer().openInventory(newInv);
            }else{
                if (Main.newActiveInventories.get(location).getPages().containsKey(page))
                    event.getPlayer().openInventory(Main.newActiveInventories.get(location).getPages().get(page));
                else
                    Main.newActiveInventories.get(location).getPages().put(page, newInv);
            }
        } catch (InvalidLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when MultiPageChest is placed into a world.
     * @param event BlockPlaceEvent for checking location of block and getting data to assign to TileState.
     * @param args Arguments for multiPageChest.
     */
    private static void blockPlaceEvent(@NotNull BlockPlaceEvent event, @NotNull String[] args){
        int size = 0;
        int pages = 0;
        for (String s : args) {
            s=s.replaceAll("[{}\"]", "");
            String[] s2 = s.split(",");
            for (String s3 : s2)
                try {
                    if (s3.toLowerCase().contains("size"))
                        size = Integer.parseInt(s3.toLowerCase().replaceAll("size:", ""));
                    else if (s3.toLowerCase().contains("pages"))
                        pages = Integer.parseInt(s3.toLowerCase().replaceAll("pages:", ""));
                }catch (NumberFormatException ex){
                    if (Main.debug)
                        log.log(Level.WARNING, String.format("Unable get either the size or page number from String for %s",
                                PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(event.getItemInHand().getItemMeta().displayName()))),
                                ex);
                    else
                        log.warning("Unable to get number from string in blackPlaceEvent function in MultiPageChests class");
                }
        }
        Chest chest = (Chest) event.getBlockPlaced().getState();
        if (doubleChest(chest, event)) {
            event.setCancelled(true);
            return;
        }
        chest.getPersistentDataContainer().set(Attribute.namespacedKey, PersistentDataType.STRING,
                Objects.requireNonNull(event.getItemInHand().getPersistentDataContainer().get(Attribute.namespacedKey, PersistentDataType.STRING)));
        PersistentDataAdapterContext cont = chest.getPersistentDataContainer().getAdapterContext();
        PersistentDataContainer container = cont.newPersistentDataContainer();

        List<Inventory> invList = createInventories(size, pages, event.getBlock());
        for (int i = 0; i < invList.size(); i++)
            container.set(new NamespacedKey(Main.getPlugin(), String.format("page_%d", i)), CustomDataTypes.Inventory, invList.get(i));
        chest.getPersistentDataContainer().set(pageContainerKey, PersistentDataType.TAG_CONTAINER, container);
        chest.update();
    }

    /**
     *This variable has to be outside the function doubleChest() or the ide will attempt to turn it into an atomicBoolean
     */
    private static boolean b = false;

    /**
     * Used for checking if the block is a double chest.
     * @param chest Chest block in the world to be checked.
     * @param event BlockPlaceEvent to cancel if the chest is becoming a double chest after 1 tick.
     * @return will return true if the chest is becoming a double chest.
     */
    private static boolean doubleChest(@NotNull Chest chest, @NotNull BlockPlaceEvent event){
        b = false;
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () ->{
            if (chest.getInventory().getSize() == 54){
                event.getBlockPlaced().setType(Material.AIR);
                event.getItemInHand().setAmount(event.getItemInHand().getAmount()+1);
                b = true;
            }
        }, 2L);
        return b;
    }

    /**
     * Called when a MultiPageChest is broken in the world.
     * @param event BlockBreakEvent to be canceled if the inventories are not empty.
     */
    @SuppressWarnings("ConstantConditions")
    private static void blockBreakEvent(@NotNull BlockBreakEvent event){
        if (!(event.getBlock().getState() instanceof TileState state)) return;
        PersistentDataContainer container = state.getPersistentDataContainer().get(pageContainerKey, PersistentDataType.TAG_CONTAINER);
        event.setCancelled(true);
        assert container != null;
        for (int i =0; i <= container.getKeys().size(); i++){
            NamespacedKey key = new NamespacedKey(Main.getPlugin(), String.format("page_%s", i));
            int page = Integer.parseInt(key.getKey().replaceAll("page_", "").trim());
            if (!container.has(key, CustomDataTypes.Inventory)) continue;
            Inventory inv = container.get(key, CustomDataTypes.Inventory);
            assert inv != null;
            if (isEmpty(inv)) continue;
            try {
                ActiveInventory activeInv = inventoryAlreadyOpen(getLocation(inv.getItem(inv.getSize() - 5)
                        .getPersistentDataContainer().get(locationKey, PersistentDataType.TAG_CONTAINER)));
                if (activeInv != null) {
                    for (int j : activeInv.getPages().keySet())
                        activeInv.getPages().get(j).close();

                    activeInv.getViewers().clear();
                    Main.newActiveInventories.remove(getLocation(inv.getItem(inv.getSize()-5).getPersistentDataContainer().get(locationKey, PersistentDataType.TAG_CONTAINER)));
                }
            } catch (InvalidLocationException e) {
                e.printStackTrace();
            }
            dropItems(inv, state.getLocation());
            container.set(key, CustomDataTypes.Inventory, inv);
            state.getPersistentDataContainer().set(pageContainerKey, PersistentDataType.TAG_CONTAINER, container);
            state.update();
            if (isEmpty(inv))
                return;
        }
        event.setCancelled(false);
        if (event.isDropItems())
            event.setDropItems(false);
        Collection<ItemStack> coll = event.getBlock().getDrops();
        for (ItemStack item : coll){
            if (item.getType() != event.getBlock().getType()) continue;
            ItemMeta meta = item.getItemMeta();
            meta.getPersistentDataContainer().set(Attribute.namespacedKey, PersistentDataType.STRING,
                    Objects.requireNonNull(state.getPersistentDataContainer().get(Attribute.namespacedKey, PersistentDataType.STRING)));
            item.setItemMeta(meta);
            event.getBlock().getLocation().getWorld().dropItem(event.getBlock().getLocation(), item);
        }
        Main.newActiveInventories.remove(event.getBlock().getLocation());
    }

    /**
     * Used to drop all the items from a MultiPageChest Inventory other than the bottom row of items
     * (The items used for changing page or page item).
     * @param inv Inventory to drop items from.
     * @param loc Location of the block to drop items from.
     */
    @SuppressWarnings("ConstantConditions")
    private static void dropItems(@NotNull Inventory inv, @NotNull Location loc){
        for (int i = 0; i < inv.getSize()-9; i++){
            if (inv.getItem(i) == null) continue;
            loc.getWorld().dropItem(loc, inv.getItem(i));
            inv.setItem(i, null);
        }
    }

    /**
     * Used to check if an Inventory is empty other than the bottom row of items
     * (The items used for changing page or page item).
     * @param inv Inventory to check if empty.
     * @return will return true if Inventory is empty.
     */
    private static boolean isEmpty(@NotNull Inventory inv){
        for (int i = 0; i < inv.getSize()-9; i++){
            if (inv.getItem(i) != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Used to create all the Inventories that will be saved on a block when it is placed.
     * @param size The size of the Inventories to be created.
     * @param pages The amount of Inventories to br created.
     * @param block The Block the Inventories will be saved on.
     * @return will return a List of Inventories to be saved on the Block.
     */
    private static @NotNull List<Inventory> createInventories(int size, int pages, @NotNull Block block){
        List<Inventory> invList = new ArrayList<>();
        for (int i = 0; i <= pages; i++) {
            Component comp = Component.text(String.format("Page %d", i));
            Inventory inv = Bukkit.createInventory((InventoryHolder) block.getState(), size, comp);

            ItemStack one = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemMeta oneMeta = one.getItemMeta();
            if (i == 0)
                oneMeta.displayName(Component.text(""));
            else
                oneMeta.displayName(Component.text("Previous Page."));
            one.setItemMeta(oneMeta);
            ItemStack two = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta twoMeta = two.getItemMeta();
            twoMeta.displayName(Component.text(""));
            two.setItemMeta(twoMeta);
            ItemStack three = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta threeMeta = three.getItemMeta();
            if (i == pages)
                threeMeta.displayName(Component.text(""));
            else
                threeMeta.displayName(Component.text("Next Page."));
            three.setItemMeta(threeMeta);
            ItemStack four = new ItemStack(Material.PINK_STAINED_GLASS_PANE);
            ItemMeta fourMeta = four.getItemMeta();
            fourMeta.displayName(Component.text(String.format("Page %d", i+1)));
            fourMeta.getPersistentDataContainer().set(pageNumberKey, PersistentDataType.INTEGER, i);
            PersistentDataAdapterContext context = fourMeta.getPersistentDataContainer().getAdapterContext();
            PersistentDataContainer locationContainer = context.newPersistentDataContainer();
            locationContainer.set(worldKey, PersistentDataType.STRING, block.getLocation().getWorld().getName());
            double x = block.getX();
            double y = block.getY();
            double z = block.getZ();
            locationContainer.set(xKey, PersistentDataType.DOUBLE, x);
            locationContainer.set(yKey, PersistentDataType.DOUBLE, y);
            locationContainer.set(zKey, PersistentDataType.DOUBLE, z);
            fourMeta.getPersistentDataContainer().set(locationKey, PersistentDataType.TAG_CONTAINER, locationContainer);
            four.setItemMeta(fourMeta);
            inv.setItem(size-9, one);
            inv.setItem(size-1, three);
            inv.setItem(size-5, four);
            for (int j = size-8; j < size-1; j++) {
                if (inv.getItem(j) == null) inv.setItem(j, two);
            }
            invList.add(inv);
        }
        return invList;
    }

    /**
     * Used to get the location of a MultiPageChest from the page item in the Inventory.
     * @param container PersistentDataContainer that contains the location data to create a new Location().
     * @return will return a new Location from the data stored in the PersistentDataContainer.
     * @throws InvalidLocationException will only throw if the PersistentDataContainer doesn't contain all the
     * required data to create a new Location().
     */
    @SuppressWarnings("ConstantConditions")
    public static @Nullable Location getLocation(@NotNull PersistentDataContainer container) throws InvalidLocationException {
        if (!container.has(worldKey) || !container.has(xKey) || !container.has(yKey) || !container.has(zKey)) throw new InvalidLocationException("Unable to get location from PersistentDataContainer");
        try {
            return new Location(
                    Bukkit.getWorld(Objects.requireNonNull(container.get(worldKey, PersistentDataType.STRING))),
                    container.get(xKey, PersistentDataType.DOUBLE),
                    container.get(yKey, PersistentDataType.DOUBLE),
                    container.get(zKey, PersistentDataType.DOUBLE));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
