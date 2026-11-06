package com.example.myblog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private PostAdapter adapter;

    private EditText searchInput;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ==========================
        // FIREBASE AUTH
        // ==========================

        firebaseAuth =
                FirebaseAuth.getInstance();


        // User not logged in
        if (firebaseAuth.getCurrentUser() == null) {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            LoginActivity.class
                    );

            startActivity(intent);

            finish();

            return;
        }


        setContentView(R.layout.activity_main);


        // ==========================
        // DATABASE
        // ==========================

        db = new DatabaseHelper(this);


        // ==========================
        // RECYCLER VIEW
        // ==========================

        RecyclerView recyclerView =
                findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );


        adapter =
                new PostAdapter(
                        db.getAllPosts(),
                        post -> {

                            Intent intent =
                                    new Intent(
                                            MainActivity.this,
                                            ViewPostActivity.class
                                    );

                            intent.putExtra(
                                    "id",
                                    post.getId()
                            );

                            startActivity(intent);
                        }
                );


        recyclerView.setAdapter(adapter);


        // ==========================
        // SEARCH
        // ==========================

        searchInput =
                findViewById(R.id.searchInput);


        findViewById(R.id.searchButton)
                .setOnClickListener(view -> {

                    String query =
                            searchInput
                                    .getText()
                                    .toString()
                                    .trim();


                    if (query.isEmpty()) {

                        refreshPosts();

                    } else {

                        adapter.setPosts(
                                db.searchPosts(query)
                        );
                    }
                });


        // ==========================
        // CREATE NEW POST
        // ==========================

        findViewById(R.id.newPostButton)
                .setOnClickListener(view -> {

                    Intent intent =
                            new Intent(
                                    MainActivity.this,
                                    CreatePostActivity.class
                            );

                    startActivity(intent);
                });


        // ==========================
        // DELETE SELECTED POSTS
        // ==========================

        findViewById(R.id.deleteSelectedButton)
                .setOnClickListener(view -> {

                    List<Long> ids =
                            adapter.selectedIds();


                    if (ids.isEmpty()) {

                        Toast.makeText(
                                MainActivity.this,
                                "Please select posts first",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        db.deletePosts(ids);

                        Toast.makeText(
                                MainActivity.this,
                                "Selected posts deleted",
                                Toast.LENGTH_SHORT
                        ).show();

                        refreshPosts();
                    }
                });


        // ==========================
        // LOGOUT
        // ==========================

        Button logoutButton =
                findViewById(R.id.logoutButton);


        logoutButton.setOnClickListener(view -> {

            firebaseAuth.signOut();


            Intent intent =
                    new Intent(
                            MainActivity.this,
                            LoginActivity.class
                    );


            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );


            startActivity(intent);

            finish();
        });
    }


    private void refreshPosts() {

        adapter.setPosts(
                db.getAllPosts()
        );
    }


    @Override
    protected void onResume() {

        super.onResume();

        if (adapter != null) {

            refreshPosts();
        }
    }
}