package paperbaglabs.school_android.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import paperbaglabs.school_android.R;

public class PostHolder extends RecyclerView.ViewHolder {
    private ImageView postOwnerDisplayImageView;
    private TextView postOwnerUsernameTextView;
    private TextView postTimeCreatedTextView;
    private ImageView postDisplayImageView;
    private TextView postTextTextView;
    private LinearLayout postLikeLayout;
    private LinearLayout postCommentLayout;
    private TextView postNumLikesTextView;
    private TextView postNumCommentsTextView;

    public PostHolder(View itemView) {
        super(itemView);
        postOwnerDisplayImageView = (ImageView) itemView.findViewById(R.id.iv_post_owner_display);
        postOwnerUsernameTextView = (TextView) itemView.findViewById(R.id.tv_post_username);
        postTimeCreatedTextView = (TextView) itemView.findViewById(R.id.tv_time);
        postDisplayImageView = (ImageView) itemView.findViewById(R.id.iv_post_display);
        postLikeLayout = (LinearLayout) itemView.findViewById(R.id.like_layout);
        postCommentLayout = (LinearLayout) itemView.findViewById(R.id.comment_layout);
        postNumLikesTextView = (TextView) itemView.findViewById(R.id.tv_likes);
        postNumCommentsTextView = (TextView) itemView.findViewById(R.id.tv_comments);
        postTextTextView = (TextView) itemView.findViewById(R.id.tv_post_text);
    }

    public ImageView getPostDisplayImageView(){
        return postDisplayImageView;
    }

    public ImageView getPostOwnerDisplayImageView(){
        return postOwnerDisplayImageView;
    }

    public LinearLayout getPostLikeLayout(){
        return postLikeLayout;
    }

    public LinearLayout getPostCommentLayout(){
        return postCommentLayout;
    }

    public void setUsername(String username) {
        postOwnerUsernameTextView.setText(username);
    }

    public void setTime(CharSequence time) {
        postTimeCreatedTextView.setText(time);
    }

    public void setNumLikes(String numLikes) {
        postNumLikesTextView.setText(numLikes);
    }

    public void setNumComments(String numComments) {
        postNumCommentsTextView.setText(numComments);
    }

    public void setPostText(String text) {
        postTextTextView.setText(text);
    }

}