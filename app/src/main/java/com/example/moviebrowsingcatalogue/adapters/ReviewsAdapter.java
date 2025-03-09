package com.example.moviebrowsingcatalogue.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.moviebrowsingcatalogue.R;
import java.util.List;

import com.example.moviebrowsingcatalogue.core.Review;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private List<Review> reviews;

    public ReviewsAdapter(List<Review> reviewList) {
        this.reviews = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the individual review item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        
        holder.bind(review);  // Bind the data (review) to the ViewHolder
    }


    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviews(List<Review> newReviews) {
        this.reviews = newReviews;
        notifyDataSetChanged();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView reviewText, reviewUser, reviewRating;

        // Constructor for ViewHolder, initializing the view elements
        public ReviewViewHolder(View itemView) {
            super(itemView);
            reviewText = itemView.findViewById(R.id.reviewText);  // Ensure this matches your XML
            reviewUser = itemView.findViewById(R.id.reviewUser);  // Ensure this matches your XML
            reviewRating = itemView.findViewById(R.id.reviewRating);  // Ensure this matches your XML
        }

        // Method to bind the review data to the views
        public void bind(Review review) {
            reviewText.setText(review.getReviewText());  // Assuming Review has this method
            reviewUser.setText(review.getUser().getUsername());  // Assuming User has this method
            reviewRating.setText("Rating: " + review.getRating());  // Assuming Review has this method

        }
    }
}
