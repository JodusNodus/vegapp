package be.tabtabstudio.veganapp.ui;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.pchmn.materialchips.model.Chip;
import com.pchmn.materialchips.model.ChipInterface;

import be.tabtabstudio.veganapp.data.entities.Label;
import be.tabtabstudio.veganapp.utilities.StringUtils;

public class LabelChip implements ChipInterface {

    private String label;

    public LabelChip(String label) {
        this.label = StringUtils.capitize(label);
    }

    @Override
    public Object getId() {
        return label;
    }

    @Override
    public Uri getAvatarUri() {
        return null;
    }

    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getInfo() {
        return null;
    }
}
