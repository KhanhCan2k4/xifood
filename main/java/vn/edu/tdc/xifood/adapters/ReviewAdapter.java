package vn.edu.tdc.xifood.adapters;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.tdc.xifood.API;
import vn.edu.tdc.xifood.ImageStorageReference;
import vn.edu.tdc.xifood.databinding.ReviewItemLayoutBinding;
import vn.edu.tdc.xifood.mydatamodels.Review;
import vn.edu.tdc.xifood.mydatamodels.User;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<Review> reviews;

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public ReviewAdapter(Activity context, ArrayList<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ReviewItemLayoutBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviews.get(position);
        API<User> userAPI = new API<>(User.class, API.USER_TABLE_NAME);

        userAPI.find(review.getUserKey(), new API.FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                if (object != null) {
                    User user = (User) object;
                    holder.binding.userName.setText(user.getFullName());
                    ImageStorageReference.setImageInto(holder.binding.avatar, "avatars/" + user.getKey());
                }
            }
        });
        holder.binding.reviewContent.setText(review.getContent());
        holder.binding.rating.setText("Đánh giá: " + review.getRating());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ReviewItemLayoutBinding binding;

        public ViewHolder(@NonNull ReviewItemLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}