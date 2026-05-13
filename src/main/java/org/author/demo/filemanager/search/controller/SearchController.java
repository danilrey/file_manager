package org.author.demo.filemanager.search.controller;

import jakarta.validation.Valid;
import org.author.demo.filemanager.search.dto.SearchRequestDto;
import org.author.demo.filemanager.search.dto.SearchResponseDto;
import org.author.demo.filemanager.search.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity<List<SearchResponseDto>> searchByQuery(@Valid @ModelAttribute SearchRequestDto request) {
        List<SearchResponseDto> searchResponseDto = searchService.search(request.getQuery(), request.getLimit());

        return ResponseEntity.ok().body(searchResponseDto);
    }
}
