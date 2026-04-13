package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dal.RequestRepository;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private RequestService requestService;

    private User owner;
    private User requestor;
    private NewRequestDto newRequest;
    private Request request;
    private Request request2;
    private Item requestedItem;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .name("owner")
                .email("owner@email.ru")
                .build();

        requestor = User.builder()
                .id(2L)
                .name("requestor")
                .email("requestor@email.com")
                .build();

        request = Request.builder()
                .id(2L)
                .requestor(requestor)
                .description("Request for a new item")
                .created(LocalDateTime.now())
                .build();

        request2 = Request.builder()
                .id(2L)
                .requestor(requestor)
                .description("Request 2 for a new item")
                .created(LocalDateTime.now())
                .build();

        newRequest = NewRequestDto.builder()
                .description("Request for a new item")
                .build();

        requestedItem = Item.builder()
                .id(4L)
                .name("item")
                .description("item description")
                .available(true)
                .owner(owner)
                .build();
    }


    @Test
    void shouldThrowNotFoundExceptionWhenUserNotExists() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> requestService.saveRequest(requestor.getId(), newRequest));
    }

    @Test
    void shouldSaveRequest() {
        when(userRepository.findById(requestor.getId()))
                .thenReturn(Optional.of(requestor));

        when(requestRepository.save(any(Request.class)))
                .thenAnswer(invocation -> {
                    Request saved = invocation.getArgument(0);
                    saved.setId(10L);
                    return saved;
                });

        RequestDto result = requestService.saveRequest(requestor.getId(), newRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Request for a new item", result.getDescription());
        Assertions.assertNotNull(result.getId());

        verify(userRepository).findById(requestor.getId());
        verify(requestRepository).save(any(Request.class));
    }

    @Test
    void shouldThrowExceptionRequestNotFound() {
        Long requestId = 99L;

        when(requestRepository.findById(requestId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class,
                () -> requestService.getRequest(requestor.getId(), requestId));
    }

    @Test
    void shouldReturnRequest() {
        when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(request));

        when(itemRepository.findAllByRequest_Id(anyLong()))
                .thenReturn(List.of(requestedItem));

        RequestDto result = requestService.getRequest(requestor.getId(), requestedItem.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(request.getId(), result.getId());
        Assertions.assertEquals("Request for a new item", result.getDescription());
        Assertions.assertNotNull(result.getItems());
        Assertions.assertEquals(1, result.getItems().size());

        verify(requestRepository).findById(requestedItem.getId());
        verify(itemRepository).findAllByRequest_Id(requestedItem.getId());
    }

    @Test
    void shouldReturnListOfRequestsByUser() {
        when(requestRepository.findAllByRequestor_Id(requestor.getId()))
                .thenReturn(List.of(request, request2));

        List<RequestDto> result = requestService.getAllRequestsByUser(requestor.getId());

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Request for a new item", result.get(0).getDescription());
        Assertions.assertEquals("Request 2 for a new item", result.get(1).getDescription());

        verify(requestRepository).findAllByRequestor_Id(requestor.getId());
    }
}
