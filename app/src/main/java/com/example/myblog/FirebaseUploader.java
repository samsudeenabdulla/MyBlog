package com.example.myblog;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUploader {

    private final FirebaseFirestore db;

    public FirebaseUploader() {
        db = FirebaseFirestore.getInstance();
    }

    public void uploadPost(Post post, UploadCallback callback) {

        Map<String, Object> data = new HashMap<>();

        data.put("localId", post.getId());
        data.put("title", post.getTitle());
        data.put("content", post.getContent());
        data.put("imageUri", post.getImageUri());
        data.put("createdAt", post.getCreatedAt());
        data.put("uploaded", true);

        db.collection("posts")
                .add(data)
                .addOnSuccessListener(documentReference -> {

                    callback.onSuccess();

                })
                .addOnFailureListener(e -> {

                    callback.onFailure(e);

                });
    }

    public interface UploadCallback {

        void onSuccess();

        void onFailure(Exception e);
    }
}