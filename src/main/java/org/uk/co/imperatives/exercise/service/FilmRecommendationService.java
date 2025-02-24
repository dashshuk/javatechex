package org.uk.co.imperatives.exercise.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.uk.co.imperatives.exercise.dto.FilmRecommendationResult;

import java.util.List;

@Component
public class FilmRecommendationService implements RecommendationService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FilmRecommendationResult> getRecommendations(Integer customerId){

        return entityManager
                .createNamedQuery("FilmRecommendation")
                .setParameter(1, customerId)
                .setParameter(2, customerId)
                .getResultList();
    }
}
