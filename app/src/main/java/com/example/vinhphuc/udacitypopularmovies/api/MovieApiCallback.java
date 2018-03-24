package com.example.vinhphuc.udacitypopularmovies.api;

/**
 * Created by VINH PHUC on 24/3/2018.
 */

public interface MovieApiCallback<T> {
    void onResponse(T result);
    void onCancel();
}
