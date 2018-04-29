package be.tabtabstudio.veganapp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.RatingBar;

import be.tabtabstudio.veganapp.R;

public class RatingDialogFragment extends DialogFragment {

    private ProductDetailsViewModel mViewModel;

    public RatingDialogFragment() {
       super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProductDetailsViewModel.class);
        mViewModel.setContext(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.rating_dialog, null))
                .setTitle(R.string.give_rating)
                .setCancelable(false)
                .setPositiveButton(R.string.rate, (dialog, id) -> {
                    // User clicked OK button
                    RatingBar ratingBar = getDialog().findViewById(R.id.rating_bar);
                    int rating = (int) ratingBar.getRating();
                    mViewModel.handleRateProduct(rating);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    // User cancelled the dialog
                });

        return builder.create();
    }
}
