package com.example.grey.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.grey.R;

import java.util.List;

public class EMUserAdapter extends RecyclerView.Adapter<EMUserAdapter.ViewHolder>{
    private List<EMUser>list;
    private Context context;

    public EMUserAdapter(List<EMUser>list,Context context){
        this.list=list;
        this.context=context;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchfriend,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener  {
        void onItemClick(View v, int position);
        void onItemLongClick(View v);
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView username;
        private Button buttonMessage,buttonAddFriends;

        public ViewHolder(View itemView){
            super(itemView);
            username=itemView.findViewById(R.id.username);
            buttonMessage=itemView.findViewById(R.id.message);
            buttonAddFriends=itemView.findViewById(R.id.add);

            buttonMessage.setOnClickListener(this);
            buttonAddFriends.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener!=null){
                onItemClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
