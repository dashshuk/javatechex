package org.uk.co.imperatives.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Class used to hold film data
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmRecommendationResult {

    private Integer Id;
    private String title;
    private String description;
    private String category;
    private String rating;
    private Integer length;
}
