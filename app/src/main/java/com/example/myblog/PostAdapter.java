package com.example.myblog;

import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Holder>{
    public interface Listener { void onClick(Post p); }
    private List<Post> posts;
    private final Set<Long> selected=new HashSet<>();
    private final Listener listener;

    public PostAdapter(List<Post> posts,Listener listener){this.posts=posts;this.listener=listener;}
    public void setPosts(List<Post> p){posts=p;selected.clear();notifyDataSetChanged();}
    public List<Long> selectedIds(){return new ArrayList<>(selected);}

    @Override public Holder onCreateViewHolder(ViewGroup parent,int viewType){
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent,false);
        return new Holder(v);
    }
    @Override public void onBindViewHolder(Holder h,int pos){
        Post p=posts.get(pos);
        h.title.setText(p.getTitle());
        h.date.setText(p.getCreatedAt()+(p.getUploadStatus()==1?" • Uploaded":" • Offline"));
        h.check.setOnCheckedChangeListener(null);
        h.check.setChecked(selected.contains(p.getId()));
        h.check.setOnCheckedChangeListener((b,checked)->{
            if(checked) selected.add(p.getId()); else selected.remove(p.getId());
        });
        h.itemView.setOnClickListener(v->listener.onClick(p));
    }
    @Override public int getItemCount(){return posts.size();}

    static class Holder extends RecyclerView.ViewHolder{
        TextView title,date; CheckBox check;
        Holder(View v){super(v);title=v.findViewById(R.id.titleText);date=v.findViewById(R.id.dateText);check=v.findViewById(R.id.checkBox);}
    }
}