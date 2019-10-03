package com.example.saral_suvidha.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saral_suvidha.R;
import com.example.saral_suvidha.modal.Aadhar;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context ncontext;
    private List<Aadhar> ndata;
    private RecyclerOnClickListner listner;

    public RecyclerViewAdapter(Context ncontext, List<Aadhar> ndata, RecyclerOnClickListner listner) {
        this.ncontext = ncontext;
        this.ndata = ndata;
        this.listner= listner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(ncontext);
        view = inflater.inflate(R.layout.cardview_item_book,viewGroup,false);
        return new MyViewHolder(view);

    }

    public interface RecyclerOnClickListner{
        void onItemClick(int i);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {



        myViewHolder.cardView.setAnimation(AnimationUtils.loadAnimation(ncontext, R.anim.item_animation_fall_down));
        myViewHolder.tv_aadhar_title.setText(ndata.get(i).getTitle());
        myViewHolder.img_book_thumbnail.setImageResource(ndata.get(i).getThumbmail());
        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ndata.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView tv_aadhar_title;
        ImageView img_book_thumbnail;
        CardView cardView;

    public MyViewHolder(View itemView){
        super(itemView);
        tv_aadhar_title =  itemView.findViewById(R.id.Aadhar_Card_id);
        img_book_thumbnail =  itemView.findViewById(R.id.Aadhar_img_id);
        cardView =  itemView.findViewById(R.id.card_layout);

    }
}
}
