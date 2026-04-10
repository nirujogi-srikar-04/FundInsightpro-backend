package com.fundinsight.backend.controller;

import com.fundinsight.backend.model.MutualFund;
import com.fundinsight.backend.repository.MutualFundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/funds")
public class FundController {

    private final MutualFundRepository repository;

    @Autowired
    public FundController(MutualFundRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<MutualFund>> getAllFunds() {
        return ResponseEntity.ok(repository.findAll());
    }
}
