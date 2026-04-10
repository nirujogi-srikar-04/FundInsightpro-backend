package com.fundinsight.backend.controller;

import com.fundinsight.backend.model.EducationalArticle;
import com.fundinsight.backend.repository.EducationalArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final EducationalArticleRepository repository;

    @Autowired
    public ArticleController(EducationalArticleRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<EducationalArticle>> getAllArticles() {
        return ResponseEntity.ok(repository.findAll());
    }
}
