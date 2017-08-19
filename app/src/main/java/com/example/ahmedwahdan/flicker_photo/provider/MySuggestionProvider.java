package com.example.ahmedwahdan.flicker_photo.provider;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by ahmedwahdan on 8/19/17.
 */

public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = MySuggestionProvider.class
            .getName();
    public static final int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY,MODE);
    }
}