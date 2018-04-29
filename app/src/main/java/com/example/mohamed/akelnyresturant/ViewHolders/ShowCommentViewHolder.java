package com.example.mohamed.akelnyresturant.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mohamed.akelnyresturant.Interface.ItemClickListner;
import com.example.mohamed.akelnyresturant.R;

/**
 * Created by mohamed on 4/7/18.
 */

public class ShowCommentViewHolder extends RecyclerView.ViewHolder {

    public TextView phone, comment;
    public RatingBar rate;


    public ShowCommentViewHolder(View itemView) {
        super(itemView);

        phone = (TextView) itemView.findViewById(R.id.txtuserphone);
        comment = (TextView) itemView.findViewById(R.id.txtusercomment);
        rate = (RatingBar) itemView.findViewById(R.id.Rating);



    }


}