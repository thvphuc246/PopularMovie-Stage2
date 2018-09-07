package com.example.vinhphuc.udacitypopularmovies.utilities;

import com.example.vinhphuc.udacitypopularmovies.api.MovieApiManager;

/**
 * Created by VINH PHUC on 25/3/2018.
 */

public class ImageUtils {
    private ImageUtils() {

    }

    public static String buildBackdropImageUrl(String backdropPath, double holderWidth) {
        String imageWidth;

        if (holderWidth > 700) {
            imageWidth = "original";
        } else if (holderWidth > 500) {
            imageWidth = "w700";
        } else if (holderWidth > 342) {
            imageWidth = "w500";
        } else if (holderWidth > 185) {
            imageWidth = "w342";
        } else if (holderWidth > 154) {
            imageWidth = "w185";
        } else if (holderWidth > 92) {
            imageWidth = "w154";
        } else {
            imageWidth = "w92";
        }

        return MovieApiManager.MOVIEDB_IMAGE_URL
                + imageWidth
                + "/"
                + backdropPath;
    }

    public static String buildPosterImageUrl(String posterPath, double holderWidth) {
        String imageWidth;

        // There is no reason to get a higher quality. Improves user experience
        if (holderWidth > 500) {
            imageWidth = "w342";
        } else {
            imageWidth = "w185";
        }

        return MovieApiManager.MOVIEDB_IMAGE_URL
                + imageWidth
                + "/"
                + posterPath;
    }
}
