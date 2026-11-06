package com.example.myblog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ViewPostActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private long id;
    private Post post;

    private TextView title;
    private TextView content;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        // Database
        db = new DatabaseHelper(this);

        // Get selected post ID
        id = getIntent().getLongExtra("id", -1);

        // Connect XML views
        title = findViewById(R.id.titleText);
        content = findViewById(R.id.contentText);
        image = findViewById(R.id.postImage);


        // ========================================
        // EDIT POST
        // ========================================

        findViewById(R.id.editButton).setOnClickListener(view -> {

            Intent intent =
                    new Intent(
                            ViewPostActivity.this,
                            EditPostActivity.class
                    );

            intent.putExtra("id", id);

            startActivity(intent);
        });


        // ========================================
        // SHARE POST
        // ========================================

        findViewById(R.id.shareButton).setOnClickListener(view -> {

            sharePost();

        });


        // ========================================
        // FIREBASE UPLOAD
        // ========================================

        findViewById(R.id.uploadButton).setOnClickListener(view -> {

            if (post == null) {

                Toast.makeText(
                        ViewPostActivity.this,
                        "Post not found",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }


            Toast.makeText(
                    ViewPostActivity.this,
                    "Uploading...",
                    Toast.LENGTH_SHORT
            ).show();


            FirebaseUploader uploader =
                    new FirebaseUploader();


            uploader.uploadPost(
                    post,
                    new FirebaseUploader.UploadCallback() {

                        @Override
                        public void onSuccess() {

                            // Update local SQLite status
                            db.markUploaded(id);

                            Toast.makeText(
                                    ViewPostActivity.this,
                                    "Post uploaded successfully",
                                    Toast.LENGTH_SHORT
                            ).show();

                            loadPost();
                        }


                        @Override
                        public void onFailure(Exception e) {

                            Toast.makeText(
                                    ViewPostActivity.this,
                                    "Upload failed: "
                                            + e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
            );

        });


        // ========================================
        // DELETE POST
        // ========================================

        findViewById(R.id.deleteButton).setOnClickListener(view -> {

            new AlertDialog.Builder(
                    ViewPostActivity.this
            )
                    .setTitle("Delete Post")

                    .setMessage(
                            "Are you sure you want to delete this post?"
                    )

                    .setPositiveButton(
                            "Delete",
                            (dialog, which) -> {

                                db.deletePost(id);

                                Toast.makeText(
                                        ViewPostActivity.this,
                                        "Post deleted",
                                        Toast.LENGTH_SHORT
                                ).show();

                                finish();
                            }
                    )

                    .setNegativeButton(
                            "Cancel",
                            null
                    )

                    .show();

        });

    }


    // ========================================
    // LOAD POST FROM SQLITE
    // ========================================

    private void loadPost() {

        post = db.getPost(id);

        if (post == null) {

            Toast.makeText(
                    this,
                    "Post not found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }


        title.setText(post.getTitle());

        content.setText(post.getContent());


        // Load image if available
        if (post.getImageUri() != null
                && !post.getImageUri().isEmpty()) {

            try {

                Uri imageUri =
                        Uri.parse(post.getImageUri());

                image.setImageURI(imageUri);

            } catch (Exception e) {

                image.setImageDrawable(null);
            }
        }
    }


    // ========================================
    // SHARE POST
    // ========================================

    private void sharePost() {

        if (post == null) {

            return;
        }


        Intent shareIntent =
                new Intent(Intent.ACTION_SEND);


        // Post has image
        if (post.getImageUri() != null
                && !post.getImageUri().isEmpty()) {

            shareIntent.setType("image/*");

            shareIntent.putExtra(
                    Intent.EXTRA_STREAM,
                    Uri.parse(post.getImageUri())
            );

            shareIntent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

        } else {

            // Text only
            shareIntent.setType("text/plain");
        }


        shareIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                post.getTitle()
        );


        shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                post.getTitle()
                        + "\n\n"
                        + post.getContent()
        );


        startActivity(
                Intent.createChooser(
                        shareIntent,
                        "Share Post"
                )
        );
    }


    // ========================================
    // REFRESH WHEN RETURNING FROM EDIT
    // ========================================

    @Override
    protected void onResume() {

        super.onResume();

        loadPost();
    }
}