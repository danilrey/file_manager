package org.author.demo.filemanager.search.controller;

import org.author.demo.filemanager.search.dto.SearchResultDto;
import org.author.demo.filemanager.search.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<List<SearchResultDto>> searchByQuery(
            @RequestParam String query,
            @RequestParam(required = false, defaultValue = "20") int limit) {
        List<SearchResultDto> searchResultDto = searchService.search(query, limit);

        return ResponseEntity.ok().body(searchResultDto);
    }
}
