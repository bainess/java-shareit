package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "requests")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;

    private LocalDateTime created;

    @Column(nullable = false)
    private String description;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Request request)) return false;

        return Objects.equals(id, request.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
