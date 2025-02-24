package org.uk.co.imperatives.exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.uk.co.imperatives.exercise.dto.FilmRecommendationResult;
import org.uk.co.imperatives.exercise.service.RecommendationService;
import org.uk.co.imperatives.exercise.util.RecommendationUtil;

import java.util.List;

@RestController
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;


    @GetMapping("/{customerId}")
    public String recommendation(@PathVariable Integer customerId)  {
        List<FilmRecommendationResult> recommendations;
        try {
            recommendations = recommendationService.getRecommendations(customerId);
            return RecommendationUtil.toJson(recommendations);
        }catch (Exception e){
            //For now
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, RecommendationUtil.ERROR_MSG, e);
        }
    }
}
