package ru.practicum.shareit.item.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.*;

@Repository
public class ItemInMemoryRepository {
    private final HashMap<Long, Item> items;
    private Long id = 1L;

    public ItemInMemoryRepository() {
        this.items = new HashMap<>();
    }

    public Item saveItem(Long user, Item item) {
        item.setId(id++);
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
                .filter(item -> Objects.equals(item.getOwner(), userId))
                .toList();
        return filteredItems;
    }

    public List<Item> searchForItem(String searchedItem) {
        if (searchedItem.isBlank()) {
            return new ArrayList<>();
        }

        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(searchedItem)
                        || item.getDescription().toLowerCase().contains(searchedItem))
                .filter(Item::getAvailable)
                .toList();
    }
}