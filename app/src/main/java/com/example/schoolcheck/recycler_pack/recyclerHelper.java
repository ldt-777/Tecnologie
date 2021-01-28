package com.example.schoolcheck.recycler_pack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolcheck.R;
import com.example.schoolcheck.adapter_pack.note_adapter;

public class recyclerHelper extends ItemTouchHelper.SimpleCallback {

    private note_adapter adapter;

    public recyclerHelper(note_adapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        final int pos = viewHolder.getAdapterPosition();

        if(direction == ItemTouchHelper.LEFT){

            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());

            builder.setTitle("Deleting");
            builder.setMessage("Vuoi eliminare la nota?");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    adapter.deleteNote(pos);

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
        else{
            adapter.editNote(pos);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable backgroundColor;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 60;

        if(dX > 0){
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.edit_);               //swipe a destra
            backgroundColor = new ColorDrawable((ContextCompat.getColor(adapter.getContext(), R.color.app_main_color)));
        }
        else {                                                                                      //swipe a sinistra
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable._delete);
            backgroundColor = new ColorDrawable(Color.RED);
        }

        assert icon != null;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) {                                                                               //swipe a destra
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            backgroundColor.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left                                                 //swipe a sinistra
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            backgroundColor.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else {                                                                                       // view is unSwiped
            backgroundColor.setBounds(0, 0, 0, 0);
        }

        backgroundColor.draw(c);
        icon.draw(c);

    }
}
