package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.BaseControllerTest;
import com.example.defecttrackerserver.response.PaginatedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
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
        testactionDto = new ActionDto();
        testactionDto.setDescription("test");
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
                .andExpect(jsonPath("$.description").value("test"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetActionById() throws Exception {
        when(actionService.getActionById(any(Integer.class))).thenReturn(testactionDto);

        mockMvc.perform(get("/actions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("test"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetAllActions() throws Exception {
        Pageable pageable = PageRequest.of(0,10);
        Page<ActionDto> page = new PageImpl<>(Arrays.asList(testactionDto));
        PaginatedResponse<ActionDto> paginatedResponse = new PaginatedResponse<>(
                Arrays.asList(testactionDto), 1 ,1 , 0);

        when(actionService.getAllActions(pageable)).thenReturn(paginatedResponse);

        mockMvc.perform(get("/actions?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].description").value(testactionDto.getDescription()))
                .andExpect(jsonPath("$.totalPages").value(page.getTotalPages()))
                .andExpect(jsonPath("$.totalElements").value((int) page.getTotalElements()))
                .andExpect(jsonPath("$.currentPage").value(page.getNumber()));
    }

    @Test
    public void shouldReturnFilteredActions() throws Exception {
        String dueDateStart = "2023-01-01";
        String dueDateEnd = "2023-01-31";
        Boolean isComplete = true;
        List<Integer> assignedUserIds = Arrays.asList(1,2);
        List<Integer> defectIds = Arrays.asList(1,2);
        String createdOnStart = "2023-01-01";
        String createdOnEnd = "2023-01-31";
        List<Integer> createdByIds = Arrays.asList(1,2);
        Pageable pageable = PageRequest.of(0,10);

        PaginatedResponse<ActionDto> response = new PaginatedResponse<>(List.of(testactionDto), 1, 1, 0);

        when(actionService.getFilteredActions(dueDateStart, dueDateEnd, isComplete, assignedUserIds, defectIds,
                createdOnStart, createdOnEnd, createdByIds, pageable)).thenReturn(response);

        mockMvc.perform(get("/actions/filtered")
                        .param("dueDateStart", dueDateStart)
                        .param("dueDateEnd", dueDateEnd)
                        .param("isCompleted", String.valueOf(isComplete))
                        .param("assignedUserIds", assignedUserIds.stream().map(Object::toString).toArray(String[]::new))
                        .param("defectIds", defectIds.stream().map(Object::toString).toArray(String[]::new))
                        .param("createdOnStart", createdOnStart)
                        .param("createdOnEnd", createdOnEnd)
                        .param("createdByIds", createdByIds.stream().map(Object::toString).toArray(String[]::new))
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].description").value(testactionDto.getDescription())) // Replace this with actual checks for your ActionDto fields
                .andExpect(jsonPath("$.totalPages").value(response.getTotalPages()))
                .andExpect(jsonPath("$.totalElements").value(response.getTotalElements()))
                .andExpect(jsonPath("$.currentPage").value(response.getCurrentPage()));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldUpdateAction() throws Exception {
        when(actionService.updateAction(any(ActionDto.class))).thenReturn(testactionDto);
        mockMvc.perform(put("/actions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testactionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("test"));
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldDeleteAction() throws Exception {
        doNothing().when(actionService).deleteAction(any(Integer.class));
        mockMvc.perform(delete("/actions/1"))
                .andExpect(status().isOk());
    }
}
