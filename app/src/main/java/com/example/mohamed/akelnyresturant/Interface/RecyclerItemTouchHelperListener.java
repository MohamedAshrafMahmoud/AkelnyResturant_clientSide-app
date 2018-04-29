package com.example.mohamed.akelnyresturant.Interface;

import android.support.v7.widget.RecyclerView;

/**
 * Created by mohamed on 4/8/18.
 */

public interface RecyclerItemTouchHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder,int direction,int position);
}
