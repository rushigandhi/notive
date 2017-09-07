package paperbaglabs.school_android;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import paperbaglabs.school_android.R;
import paperbaglabs.school_android.models.Comment;
import paperbaglabs.school_android.models.Post;
import paperbaglabs.school_android.models.PostHolder;
import paperbaglabs.school_android.variables.Constants;
import paperbaglabs.school_android.variables.FirebaseUtils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private final String TAG = "HomeFragment";
    private View mRootView;
    private FirebaseRecyclerAdapter<Post, PostHolder> mPostAdapter;
    private RecyclerView mPostRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    int currentNumberOfElements = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        FloatingActionButton fab = (FloatingActionButton) mRootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              PostCreateDialog dialog = new PostCreateDialog();
               dialog.show(getFragmentManager(), null);
            }
        });
        getActivity().setTitle(R.string.title_activity_main);
        init();
        return mRootView;
    }

    private void init() {
        mPostRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview_post);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipeRefreshLayout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //Sets in reverse
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        mPostRecyclerView.setLayoutManager(linearLayoutManager);

        setupAdapter();
        mPostRecyclerView.setAdapter(mPostAdapter);

        //Calls for the initial load
        mPostAdapter.notifyDataSetChanged();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                mPostAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupAdapter() {
        mPostAdapter = new FirebaseRecyclerAdapter<Post, PostHolder>(
                Post.class,
                R.layout.row_post,
                PostHolder.class,
                FirebaseUtils.getPostRef()
        ) {
            @Override
            protected void populateViewHolder(PostHolder viewHolder, final Post model, int position) {
                viewHolder.setNumComments(String.valueOf(model.getNumComments()));
                viewHolder.setNumLikes(String.valueOf(model.getNumLikes()));
                viewHolder.setTime(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));
                viewHolder.setUsername(model.getUser().getUser());
                viewHolder.setPostText(model.getPostText());
                viewHolder.getSideBar().setBackgroundColor(model.getColor());

                Glide.with(getActivity())
                        .load(model.getUser().getPhotoUrl())
                        .into(viewHolder.getPostOwnerDisplayImageView());

                if (model.getPostImageUrl() != null) {
                    viewHolder.getPostDisplayImageView().setVisibility(View.VISIBLE);
                    StorageReference storageReference = FirebaseStorage.getInstance()
                            .getReference(model.getPostImageUrl());
                    Glide.with(getActivity())
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .into(viewHolder.getPostDisplayImageView());
                } else {
                    viewHolder.getPostDisplayImageView().setImageBitmap(null);
                    viewHolder.getPostDisplayImageView().setVisibility(View.GONE);
                }
                viewHolder.getPostLikeLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLikeClick(model.getPostId());
                    }
                });

                viewHolder.getPostCommentLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent intent = new Intent(getContext(), CommentActivity.class);
                      intent.putExtra(Constants.EXTRA_POST, model);
                      startActivity(intent);
                    }
                });
// --------------------------- COLOUR TRIAL ---------------------------
//                viewHolder.averageColour();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        };
    }

    private void onLikeClick(final String postId) {
        FirebaseUtils.getPostLikedRef(postId)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        //User liked
                        FirebaseUtils.getPostRef()
                                .child(postId)
                                .child(Constants.NUM_LIKES_KEY)
                                .runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        long num = (long) mutableData.getValue();
                                        mutableData.setValue(num - 1);
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                        FirebaseUtils.getPostLikedRef(postId)
                                                .setValue(null);
                                    }
                                });
                    } else {
                        FirebaseUtils.getPostRef()
                                .child(postId)
                                .child(Constants.NUM_LIKES_KEY)
                                .runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        long num = (long) mutableData.getValue();
                                        mutableData.setValue(num + 1);
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                        FirebaseUtils.getPostLikedRef(postId)
                                                .setValue(true);
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
}