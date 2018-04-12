package be.tabtabstudio.veganapp.data.entities;

public class Label {
    public static Label getMock() {
        Label l = new Label();
        l.labelid = 1;
        l.name = "snack";
        return l;
    }

    public int labelid;
    public String name;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Label) {
            return ((Label) obj).labelid == labelid;
        } else {
            return false;
        }
    }
}
