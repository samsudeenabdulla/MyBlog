package com.example.myblog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreatePostActivity extends AppCompatActivity {
    EditText title,content; ImageView image; String imageUri="";
    DatabaseHelper db;

    ActivityResultLauncher<String> gallery=registerForActivityResult(
        new ActivityResultContracts.GetContent(), uri->{if(uri!=null){imageUri=uri.toString();image.setImageURI(uri);}}
    );

    ActivityResultLauncher<Intent> camera=registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(), r->{
            if(r.getResultCode()==RESULT_OK && r.getData()!=null && r.getData().getData()!=null){
                Uri uri=r.getData().getData(); imageUri=uri.toString(); image.setImageURI(uri);
            } else Toast.makeText(this,"Camera preview may not return a persistent URI on this device. Use Gallery for persistent storage.",Toast.LENGTH_LONG).show();
        });

    @Override protected void onCreate(Bundle b){
        super.onCreate(b); setContentView(R.layout.activity_create_post);
        db=new DatabaseHelper(this);
        title=findViewById(R.id.titleInput);content=findViewById(R.id.contentInput);image=findViewById(R.id.postImage);
        findViewById(R.id.galleryButton).setOnClickListener(v->gallery.launch("image/*"));
        findViewById(R.id.cameraButton).setOnClickListener(v->camera.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)));
        findViewById(R.id.saveButton).setOnClickListener(v->save());
    }
    void save(){
        String t=title.getText().toString().trim(), c=content.getText().toString().trim();
        if(t.isEmpty()||c.isEmpty()){Toast.makeText(this,"Enter title and content",Toast.LENGTH_SHORT).show();return;}
        String date=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault()).format(new Date());
        db.insertPost(t,c,imageUri,date);
        Toast.makeText(this,"Saved offline",Toast.LENGTH_SHORT).show(); finish();
    }
}