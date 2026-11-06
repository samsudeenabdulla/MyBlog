package com.example.myblog;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="myblog.db";
    private static final int DB_VERSION=1;

    public DatabaseHelper(Context c){ super(c, DB_NAME, null, DB_VERSION); }

    @Override public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE posts(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "title TEXT NOT NULL, content TEXT NOT NULL, image_uri TEXT,"+
                "created_at TEXT NOT NULL, upload_status INTEGER DEFAULT 0)");
    }
    @Override public void onUpgrade(SQLiteDatabase db,int oldV,int newV){
        db.execSQL("DROP TABLE IF EXISTS posts"); onCreate(db);
    }

    public long insertPost(String title,String content,String imageUri,String createdAt){
        ContentValues v=new ContentValues();
        v.put("title",title); v.put("content",content); v.put("image_uri",imageUri);
        v.put("created_at",createdAt); v.put("upload_status",0);
        return getWritableDatabase().insert("posts",null,v);
    }

    public int updatePost(long id,String title,String content,String imageUri){
        ContentValues v=new ContentValues();
        v.put("title",title); v.put("content",content); v.put("image_uri",imageUri);
        return getWritableDatabase().update("posts",v,"id=?",new String[]{String.valueOf(id)});
    }

    public int markUploaded(long id){
        ContentValues v=new ContentValues(); v.put("upload_status",1);
        return getWritableDatabase().update("posts",v,"id=?",new String[]{String.valueOf(id)});
    }

    public int deletePost(long id){
        return getWritableDatabase().delete("posts","id=?",new String[]{String.valueOf(id)});
    }

    public void deletePosts(List<Long> ids){
        SQLiteDatabase db=getWritableDatabase();
        db.beginTransaction();
        try{
            for(long id:ids) db.delete("posts","id=?",new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } finally { db.endTransaction(); }
    }

    public Post getPost(long id){
        Cursor c=getReadableDatabase().rawQuery("SELECT * FROM posts WHERE id=?",
                new String[]{String.valueOf(id)});
        Post p=null;
        if(c.moveToFirst()) p=fromCursor(c);
        c.close(); return p;
    }

    public List<Post> getAllPosts(){
        return query("SELECT * FROM posts ORDER BY id DESC",null);
    }

    public List<Post> searchPosts(String q){
        return query("SELECT * FROM posts WHERE title LIKE ? OR content LIKE ? ORDER BY id DESC",
                new String[]{"%"+q+"%","%"+q+"%"});
    }

    private List<Post> query(String sql,String[] args){
        ArrayList<Post> list=new ArrayList<>();
        Cursor c=getReadableDatabase().rawQuery(sql,args);
        while(c.moveToNext()) list.add(fromCursor(c));
        c.close(); return list;
    }

    private Post fromCursor(Cursor c){
        return new Post(
            c.getLong(c.getColumnIndexOrThrow("id")),
            c.getString(c.getColumnIndexOrThrow("title")),
            c.getString(c.getColumnIndexOrThrow("content")),
            c.getString(c.getColumnIndexOrThrow("image_uri")),
            c.getString(c.getColumnIndexOrThrow("created_at")),
            c.getInt(c.getColumnIndexOrThrow("upload_status"))
        );
    }
}