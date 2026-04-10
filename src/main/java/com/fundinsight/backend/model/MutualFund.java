package com.fundinsight.backend.model;

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
@Table(name = "mutual_funds")
public class MutualFund {

    @Id
    private String id;
    private String name;
    private String category;
    private Double returns1Y;
    private Double returns3Y;
    private Double returns5Y;
    private String riskLevel;
    private Double aum;
    private Double expenseRatio;
    private Double minInvestment;
    private Integer rating;
    private Double nav;
    private String exitLoad;
}
