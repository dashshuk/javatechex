package org.uk.co.imperatives.exercise.util;

import com.google.gson.Gson;
import org.uk.co.imperatives.exercise.dto.FilmRecommendationResult;

import java.util.List;

public class RecommendationUtil {

    public static final String ERROR_MSG = "Request processingerror";

    public static String toJson(List<FilmRecommendationResult> filmRecommendationResult){
        Gson gson = new Gson();
        return gson.toJson(filmRecommendationResult);
    }
}
