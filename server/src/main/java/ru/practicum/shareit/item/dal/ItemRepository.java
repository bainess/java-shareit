package ru.practicum.shareit.item.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByRequest_Id(Long requestId);
    List<Item> findByOwnerId(Long ownerId);

    List<Item> findByAvailableTrueAndNameContainingIgnoreCaseOrAvailableTrueAndDescriptionContainingIgnoreCase(
            String name, String description
    );
    Optional<Item> findByIdAndAvailableTrue(Long id);
}