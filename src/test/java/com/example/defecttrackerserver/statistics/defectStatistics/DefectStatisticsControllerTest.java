package com.example.defecttrackerserver.statistics.defectStatistics;

import com.example.defecttrackerserver.BaseControllerTest;
import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.defect.causationCategory.CausationCategory;
import com.example.defecttrackerserver.core.defect.defect.DefectController;
import com.example.defecttrackerserver.core.defect.defect.DefectDto;
import com.example.defecttrackerserver.core.defect.defect.DefectFilterValues;
import com.example.defecttrackerserver.core.defect.defect.DefectService;
import com.example.defecttrackerserver.response.PaginatedResponse;
import com.example.defecttrackerserver.statistics.CausationCategoryCount;
import com.example.defecttrackerserver.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DefectStatisticsController.class)
@RequiredArgsConstructor
public class DefectStatisticsControllerTest extends BaseControllerTest {

    @Autowired
    private DefectStatisticsController defectStatisticsController;

    @MockBean
    private DefectStatisticsService defectStatisticsService;

    @Override
    protected Object getController() {
        return defectStatisticsController;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    @WithMockUser(username = "bill", roles = "ADMIN")
    public void shouldGetDefectStatistics() throws Exception {
        DefectStatisticsDto defectStatisticsDto = new DefectStatisticsDto();
        CausationCategoryCount causationCategoryCount = new CausationCategoryCount();
        causationCategoryCount.setName("testCausationCategory");
        causationCategoryCount.setCount(1L);

        defectStatisticsDto.setCausationCategoryCounts(List.of(causationCategoryCount));

        when(defectStatisticsService.getDefectStatistics()).thenReturn(defectStatisticsDto);

        mockMvc.perform(get("/defects/statistics").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.causationCategoryCounts[0].name").value("testCausationCategory"));
    }
}