package be.tabtabstudio.veganapp.ui;

import android.app.Activity;
import android.content.Context;

public class ViewModel extends android.arch.lifecycle.ViewModel {
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return context;
    }
}
