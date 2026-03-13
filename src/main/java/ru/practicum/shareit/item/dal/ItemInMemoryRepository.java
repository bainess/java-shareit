package ru.practicum.shareit.item.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Repository
public class ItemInMemoryRepository {
    private final HashMap<Long, Item> items;

    public ItemInMemoryRepository() {
        this.items = new HashMap<>();
    }

    public Item saveItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    public void removeItem(Long itemId) {
        items.remove(itemId);
    }

    public Optional<Item> getItem(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    public List<Item> getAllItemsByUser(Long userId) {
        List<Item> filteredItems = items.values().stream()
                .filter(item -> Objects.equals(item.getOwner().getId(), userId))
                .toList();
        return filteredItems;
    }

    public List<Item> searchForItem(String searchedItem) {
        return items.values().stream()
                .filter(item -> item.getName().contains(searchedItem) || item.getDescription().contains(searchedItem))
                .filter(Item::isAvailable)
                .toList();
    }
}