package com.fundinsight.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "educational_articles")
public class EducationalArticle {

    @Id
    private String id;
    private String title;
    private String category;
    
    @Column(length = 1000)
    private String content;
    
    @Column(length = 1000)
    private String aiInsight;
    
    private Double confidenceScore;
    private String readTime;
    private String imageUrl;
}
