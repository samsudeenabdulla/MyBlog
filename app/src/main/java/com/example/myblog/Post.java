package com.example.myblog;

public class Post {
    private long id;
    private String title, content, imageUri, createdAt;
    private int uploadStatus;

    public Post(long id, String title, String content, String imageUri, String createdAt, int uploadStatus) {
        this.id=id; this.title=title; this.content=content; this.imageUri=imageUri;
        this.createdAt=createdAt; this.uploadStatus=uploadStatus;
    }
    public long getId(){return id;}
    public String getTitle(){return title;}
    public String getContent(){return content;}
    public String getImageUri(){return imageUri;}
    public String getCreatedAt(){return createdAt;}
    public int getUploadStatus(){return uploadStatus;}
}