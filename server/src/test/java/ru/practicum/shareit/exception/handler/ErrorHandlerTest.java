package ru.practicum.shareit.exception.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {ErrorHandlerTest.TestConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ErrorHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    @Import(ErrorHandler.class)
    static class TestConfig {

        @Bean
        public TestController testController() {
            return new TestController();
        }
    }

    @RestController
    static class TestController {

        @GetMapping("/not-found")
        public void notFound() {
            throw new NotFoundException("not found message");
        }

        @GetMapping("/conflict")
        public void conflict() {
            throw new DuplicatedDataException("duplicate");
        }

        @GetMapping("/forbidden")
        public void forbidden() {
            throw new ForbiddenException("forbidden");
        }

        @GetMapping("/bad-request")
        public void badRequest() throws IllegalAccessException {
            throw new IllegalAccessException("bad request");
        }

        @GetMapping("/error")
        public void error() {
            throw new RuntimeException("boom");
        }
    }

    @Test
    void shouldHandleNotFound() throws Exception {
        mockMvc.perform(get("/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("not found"))
                .andExpect(jsonPath("$.message").value("not found message"));
    }

    @Test
    void shouldHandleConflict() throws Exception {
        mockMvc.perform(get("/conflict"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("insufficient data"))
                .andExpect(jsonPath("$.message").value("duplicate"));
    }

    @Test
    void shouldHandleForbidden() throws Exception {
        mockMvc.perform(get("/forbidden"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("access forbidden"))
                .andExpect(jsonPath("$.message").value("forbidden"));
    }

    @Test
    void shouldHandleBadRequest() throws Exception {
        mockMvc.perform(get("/bad-request"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("illegal argument"))
                .andExpect(jsonPath("$.message").value("bad request"));
    }

    @Test
    void shouldHandleGenericException() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("internal error"))
                .andExpect(jsonPath("$.message").value("boom"));
    }
}