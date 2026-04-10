package ru.practicum.shareit.request.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.Request;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findByIdAndRequestor_Id(Long requestId, Long userId);
}
