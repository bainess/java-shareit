package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemDtoWithBookingDatesAndComments;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.dto.item.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldAddItem() throws Exception {
        Long userId = 1L;

        NewItemRequest request = NewItemRequest.builder()
                .name("item")
                .description("desc")
                .available(true)
                .build();

        ItemDto response = ItemDto.builder().id(1L).name("item").build();

        Mockito.when(itemService.saveItem(Mockito.eq(userId), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldUpdateItem() throws Exception {
        Long userId = 1L;
        Long itemId = 2L;

        UpdateItemRequest request = new UpdateItemRequest();
        request.setName("updated");

        ItemDto response = ItemDto.builder().id(itemId).name("updated").build();

        Mockito.when(itemService.updateItem(
                Mockito.eq(userId),
                Mockito.eq(itemId),
                Mockito.any(UpdateItemRequest.class)
        )).thenReturn(response);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId))
                .andExpect(jsonPath("$.name").value("updated"));
    }

    @Test
    void shouldGetItemsByUser() throws Exception {
        Long userId = 1L;

        ItemDtoWithBookingDatesAndComments item =
                ItemDtoWithBookingDatesAndComments.builder().id(1L).build();

        Mockito.when(itemService.findAllItemsByUser(userId))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }


}
