package com.example.grey.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.grey.R;
import com.example.grey.model.Post;

import java.util.List;

import cn.bmob.v3.BmobUser;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Post>postList;
    private Context context;


    public MyRecyclerViewAdapter(List<Post> postList,Context context){
        this.postList=postList;
        this.context=context;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textContent;
        private Button buttonAgree;
        private Button buttonDisagree;
        private Button buttonTrush;
        private TextView textName;

//        private int agree=1;
//        private int disagree=2;

        public ViewHolder(View itemView){
            super(itemView);
            textName=itemView.findViewById(R.id.name);
            textContent=itemView.findViewById(R.id.content);
            buttonAgree=itemView.findViewById(R.id.agree);
            buttonDisagree=itemView.findViewById(R.id.disagree);
            buttonTrush=itemView.findViewById(R.id.trush);

            itemView.setOnClickListener(this);
            buttonAgree.setOnClickListener(this);
            buttonDisagree.setOnClickListener(this);
            buttonTrush.setOnClickListener(this);


//            buttonAgree.setText( String.valueOf(agree));
//            buttonDisagree.setText(String.valueOf(disagree));
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }

//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post,viewGroup,false);
//        return new PostViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        //
//        Post post=postList.get(position);
//        holder.buttonAgree.setText();
//    }


    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        Post post=postList.get(i);
//        viewHolder.textName.setText("111");
        viewHolder.textContent.setText(post.getContent());
        viewHolder.buttonAgree.setText(String.valueOf(post.getAgree()));
        viewHolder.buttonDisagree.setText(String.valueOf(post.getDisagree()));

        String name1=post.getName();
        String name2=BmobUser.getCurrentUser().getUsername();

        if (post.isFoundAgree()){
            viewHolder.buttonAgree.setText(String.valueOf("已赞同"));
            viewHolder.buttonAgree.setEnabled(false);
        }else {
            viewHolder.buttonAgree.setText(String.valueOf(post.getAgree()));
            viewHolder.buttonAgree.setEnabled(true);
        }

        if (post.isFoundDisagree()){
            viewHolder.buttonDisagree.setText(String.valueOf("已反对"));
            viewHolder.buttonDisagree.setEnabled(false);
        }else {
            viewHolder.buttonDisagree.setText(String.valueOf(post.getDisagree()));
            viewHolder.buttonDisagree.setEnabled(true);
        }

        if (!name2.equals(name1))
        {
            viewHolder.buttonTrush.setText("转发");
//            viewHolder.buttonTrush.setCompoundDrawables(buttonImage,null,null,null);
        }else {
            viewHolder.buttonTrush.setText("删除");
        }

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public interface OnItemClickListener  {
        void onItemClick(View v, int position);
        void onItemLongClick(View v);
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }

}
