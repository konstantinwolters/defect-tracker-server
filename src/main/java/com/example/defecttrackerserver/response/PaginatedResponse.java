package com.example.defecttrackerserver.response;

import com.example.defecttrackerserver.core.action.ActionFilterValues;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginatedResponse<T> {
    private List<T> content;
    private int totalPages;
    private int totalElements;
    private int currentPage;
    private ActionFilterValues actionFilterValuesDto;

    public PaginatedResponse(List<T> content, int totalPages, int totalElements,
                             int currentPage, ActionFilterValues actionFilterValues) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.actionFilterValuesDto = actionFilterValues;
    }
}
