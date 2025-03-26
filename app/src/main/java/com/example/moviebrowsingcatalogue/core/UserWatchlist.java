package com.example.moviebrowsingcatalogue.core;

import java.util.List;

public class UserWatchlist {
    private List<Watchlist> userWatchlists;
    private boolean isOwnProfile;

    public List<Watchlist> getUserWatchlists() {
        return userWatchlists;
    }

    public void setUserWatchlists(List<Watchlist> userWatchlists) {
        this.userWatchlists = userWatchlists;
    }

    public boolean isOwnProfile() {
        return isOwnProfile;
    }

    public void setOwnProfile(boolean ownProfile) {
        isOwnProfile = ownProfile;
    }
}
