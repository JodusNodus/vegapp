package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.tabtabstudio.veganapp.R;
import be.tabtabstudio.veganapp.data.entities.Label;
import be.tabtabstudio.veganapp.data.entities.Product;
import be.tabtabstudio.veganapp.data.entities.ProductListItem;

import java.util.List;

public class TabPageFragment extends Fragment {

    public static final String ARG_COLUMN_COUNT = "column-count";
    public static final String ARG_DATA = "data";
    public static final String NEW_PAGE = "new";
    public static final String SEARCH_PAGE = "search";
    public static final String POPULAR_PAGE = "popular";
    public static final String RATING_PAGE = "rating";
    private int mColumnCount;
    private OnListFragmentInteractionListener mListener;
    private TabPageViewModel mViewModel;
    private MyTabPageRecyclerViewAdapter adapter;

    public TabPageFragment() {
    }

    @SuppressWarnings("unused")
    public static TabPageFragment newInstance(int columnCount) {
        TabPageFragment fragment = new TabPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TabPageViewModel.class);
        mViewModel.setContext(getContext());

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tabpage_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyTabPageRecyclerViewAdapter(mListener);
            recyclerView.setAdapter(adapter);
        }

        Observer<List<Product>> observer = new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                if (products != null && adapter != null) {
                    adapter.updateList(products);
                }
            }
        };

        String data = getArguments().getString(ARG_DATA);
        switch (data) {
            case SEARCH_PAGE:
                mViewModel.getSearchProductsObservable().observe(this, observer);
                break;
            case NEW_PAGE:
                mViewModel.getNewProductsObservable().observe(this, observer);
                mViewModel.fetchNewProducts();
                break;
            case RATING_PAGE:
                mViewModel.getHighestRatedProductsObservable().observe(this, observer);
                mViewModel.fetchHighestRatedProducts();
                break;
            case POPULAR_PAGE:
                mViewModel.getMostPopularProductsObservable().observe(this, observer);
                mViewModel.fetchMostPopularProducts();
                break;
            default:
                mViewModel.getProductsObservableWithLabel(data).observe(this, observer);
                mViewModel.fetchProductsWithLabel(data);
                break;
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ProductListItem item);
    }
}
