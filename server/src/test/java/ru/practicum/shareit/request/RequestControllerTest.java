package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @Autowired
    private ObjectMapper objectMapper;

    private RequestDto requestDto;
    private NewRequestDto newRequest;

    @BeforeEach
    void setUp() {
        requestDto = RequestDto.builder()
                .id(1L)
                .description("Request for item")
                .build();

        newRequest = NewRequestDto.builder()
                .description("Request for item")
                .build();
    }

    @Test
    void shouldGetRequest() throws Exception {
        Long userId = 1L;
        Long requestId = 1L;

        Mockito.when(requestService.getRequest(userId, requestId))
                .thenReturn(requestDto);

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestId))
                .andExpect(jsonPath("$.description").value("Request for item"));
    }

    @Test
    void shouldGetAllRequests() throws Exception {
        Long userId = 1L;

        List<RequestDto> requests = List.of(
                requestDto,
                RequestDto.builder().id(2L).description("Another request").build()
        );

        Mockito.when(requestService.getAllRequestsByUser(userId))
                .thenReturn(requests);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].description").value("Another request"));
    }
}