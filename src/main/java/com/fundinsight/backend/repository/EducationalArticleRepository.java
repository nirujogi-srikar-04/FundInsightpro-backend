package com.fundinsight.backend.repository;

import com.fundinsight.backend.model.EducationalArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationalArticleRepository extends JpaRepository<EducationalArticle, String> {
}
