package be.tabtabstudio.veganapp.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import be.tabtabstudio.veganapp.R;
import be.tabtabstudio.veganapp.data.entities.ProductListItem;
import be.tabtabstudio.veganapp.ui.TabPageFragment.OnListFragmentInteractionListener;

import java.util.List;

public class MyTabPageRecyclerViewAdapter extends RecyclerView.Adapter<MyTabPageRecyclerViewAdapter.ViewHolder> {

    private final List<ProductListItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyTabPageRecyclerViewAdapter(List<ProductListItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tabpage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.productListItem = mValues.get(position);
        Picasso
                .get()
                .load(holder.productListItem.getThumbnail())
                .into(holder.mThumbImageView);
        holder.mContentView.setText(mValues.get(position).getProductName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.productListItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final ImageView mThumbImageView;
        public ProductListItem productListItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = view.findViewById(R.id.product_card_title);
            mThumbImageView = view.findViewById(R.id.product_card_thumb);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
