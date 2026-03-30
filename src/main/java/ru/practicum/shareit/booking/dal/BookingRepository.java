package ru.practicum.shareit.booking.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findByIdAndBookerId(Long id, Long userId);
    
    List<Booking> findByItem_Owner_IdOrderByStartDesc(Long userId);

    List<Booking> findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId, LocalDateTime now1, LocalDateTime now2);

    List<Booking> findByItem_Owner_IdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findByItem_Owner_IdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime now);


    List<Booking> findByItem_Owner_IdAndStatusOrderByStartDesc(Long userId, State state);


    List<Booking> findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId, LocalDateTime now1, LocalDateTime now2);

    List<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findByBooker_IdOrderByStartDesc(Long userId);

    List<Booking> findByBooker_IdAndStatusOrderByStartDesc(Long userId, State state);

    List<Booking> findByBooker_Id(Long userId);

    Booking findByIdAndItem_Owner_Id(Long bookingId, Long userId);

    Optional<Booking> findFirstByItem_IdAndEndAfterOrderByEndDesc(Long itemId, LocalDateTime now);

    Optional<Booking> findFirstByItem_IdAndStartAfterOrderByStartAsc(Long ItemId, LocalDateTime now);

    boolean existsByItem_IdAndBooker_IdAndEndBefore(Long itemId, Long userId, LocalDateTime created);
}
