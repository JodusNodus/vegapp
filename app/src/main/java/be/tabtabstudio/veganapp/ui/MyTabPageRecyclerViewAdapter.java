package be.tabtabstudio.veganapp.ui;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import be.tabtabstudio.veganapp.R;
import be.tabtabstudio.veganapp.data.entities.ProductListItem;
import be.tabtabstudio.veganapp.ui.TabPageFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class MyTabPageRecyclerViewAdapter extends RecyclerView.Adapter<MyTabPageRecyclerViewAdapter.ViewHolder> {

    private List<ProductListItem> mProducts;
    private final OnListFragmentInteractionListener mListener;

    public MyTabPageRecyclerViewAdapter(OnListFragmentInteractionListener listener) {
        mProducts = new ArrayList<>();
        mListener = listener;
    }

    public void updateList(List products) {
        mProducts = products;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tabpage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.productListItem = mProducts.get(position);
        Picasso
                .get()
                .load(holder.productListItem.thumbPicture)
                .into(holder.mThumbImageView);
        holder.mContentView.setText(mProducts.get(position).getProductName());

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
        return mProducts.size();
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
