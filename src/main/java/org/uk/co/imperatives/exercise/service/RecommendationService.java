package org.uk.co.imperatives.exercise.service;

import org.uk.co.imperatives.exercise.dto.FilmRecommendationResult;

import java.util.List;

public interface RecommendationService {
    List<FilmRecommendationResult> getRecommendations(Integer customerI);
}
