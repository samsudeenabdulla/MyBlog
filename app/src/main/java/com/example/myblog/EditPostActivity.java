package com.example.myblog;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class EditPostActivity extends AppCompatActivity {
    DatabaseHelper db; long id; EditText title,content; ImageView image; String imageUri="";
    @Override protected void onCreate(Bundle b){
        super.onCreate(b); setContentView(R.layout.activity_edit_post);
        db=new DatabaseHelper(this); id=getIntent().getLongExtra("id",-1);
        title=findViewById(R.id.titleInput);content=findViewById(R.id.contentInput);image=findViewById(R.id.postImage);
        Post p=db.getPost(id);
        if(p!=null){title.setText(p.getTitle());content.setText(p.getContent());imageUri=p.getImageUri();}
        findViewById(R.id.galleryButton).setEnabled(false);
        findViewById(R.id.cameraButton).setEnabled(false);
        findViewById(R.id.saveButton).setOnClickListener(v->{
            db.updatePost(id,title.getText().toString().trim(),content.getText().toString().trim(),imageUri);
            Toast.makeText(this,"Post updated",Toast.LENGTH_SHORT).show();finish();
        });
    }
}