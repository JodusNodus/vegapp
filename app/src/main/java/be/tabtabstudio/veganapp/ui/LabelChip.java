package be.tabtabstudio.veganapp.ui;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tylersuehr.chips.Chip;

public class LabelChip extends Chip {

    private String label;

    public LabelChip(String label) {
        this.label = label;
    }

    @Nullable
    @Override
    public Object getId() {
        return label;
    }

    @NonNull
    @Override
    public String getTitle() {
        return label;
    }

    @Nullable
    @Override
    public String getSubtitle() {
        return null;
    }

    @Nullable
    @Override
    public Uri getAvatarUri() {
        return null;
    }

    @Nullable
    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }
}
