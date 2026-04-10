package ru.practicum.shareit.item.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByRequest_Id(Long requestId);

    List<Item> findByOwnerId(Long ownerId);

    List<Item> findByAvailableTrueAndNameContainingIgnoreCaseOrAvailableTrueAndDescriptionContainingIgnoreCase(
            String name, String description
    );
}