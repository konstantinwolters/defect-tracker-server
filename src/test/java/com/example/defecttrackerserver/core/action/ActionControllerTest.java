package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.BaseControllerTest;
import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.user.user.userDtos.UserDto;
import com.example.defecttrackerserver.response.PaginatedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ActionController.class)
public class ActionControllerTest extends BaseControllerTest {

    @Autowired
    private ActionController actionController;

    @MockBean
    private ActionService actionService;

    private ActionDto testactionDto;

    @Override
    protected Object getController() {
        return actionController;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        testactionDto = TestHelper.setUpActionDto();
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldSaveAction() throws Exception {
        when(actionService.saveAction(any(ActionDto.class))).thenReturn(testactionDto);

        mockMvc.perform(post("/actions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testactionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("testDescription"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetActionById() throws Exception {
        when(actionService.getActionById(any(Integer.class))).thenReturn(testactionDto);

        mockMvc.perform(get("/actions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("testDescription"));
    }

    @Test
    public void shouldReturnFilteredActions() throws Exception {
        String searchTerm = "test";
        LocalDate dueDateStart = LocalDate.now();
        LocalDate dueDateEnd = LocalDate.now();
        Boolean isComplete = true;
        String assignedUserIds = "1,2";
        String defectIds = "1,2";
        LocalDate createdAtStart = LocalDate.now();
        LocalDate createdAtEnd = LocalDate.now();
        LocalDate changedAtStart = LocalDate.now();
        LocalDate changedAtEnd = LocalDate.now();
        Integer page = 0;
        Integer size = 10;
        String createdByIds = "1,2";
        String changedByIds = "1,2";
        String sort = "id,desc";

        PaginatedResponse<ActionDto> response = new PaginatedResponse<>(List.of(testactionDto), 1,
                1, 0, new ActionFilterValues());

        when(actionService.getActions(searchTerm, dueDateStart, dueDateEnd, isComplete, assignedUserIds, defectIds,
                createdAtStart, createdAtEnd, changedAtStart, changedAtEnd, createdByIds, changedByIds, page, size, sort)).thenReturn(response);

        mockMvc.perform(get("/actions")
                        .param("search", searchTerm)
                        .param("dueDateStart", String.valueOf(dueDateStart))
                        .param("dueDateEnd", String.valueOf(dueDateEnd))
                        .param("isCompleted", String.valueOf(isComplete))
                        .param("assignedUserIds", assignedUserIds)
                        .param("defectIds", defectIds)
                        .param("createdAtStart", String.valueOf(createdAtStart))
                        .param("createdAtEnd", String.valueOf(createdAtEnd))
                        .param("changedAtStart", String.valueOf(createdAtStart))
                        .param("changedAtEnd", String.valueOf(createdAtEnd))
                        .param("createdByIds", createdByIds)
                        .param("changedByIds", changedByIds)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sort))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].description").value(testactionDto.getDescription()))
                .andExpect(jsonPath("$.totalPages").value(response.getTotalPages()))
                .andExpect(jsonPath("$.totalElements").value(response.getTotalElements()))
                .andExpect(jsonPath("$.currentPage").value(response.getCurrentPage()));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldCloseAction() throws Exception {
        doNothing().when(actionService).closeAction(any(Integer.class));

        mockMvc.perform(patch("/actions/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldUpdateAction() throws Exception {
        when(actionService.updateAction(any(Integer.class), any(ActionDto.class))).thenReturn(testactionDto);
        mockMvc.perform(put("/actions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testactionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("testDescription"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteAction() throws Exception {
        doNothing().when(actionService).deleteAction(any(Integer.class));
        mockMvc.perform(delete("/actions/1"))
                .andExpect(status().isNoContent());
    }
}
