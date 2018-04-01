package be.tabtabstudio.veganapp.model.entities;

public class Label {
    public static Label getMock() {
        Label l = new Label();
        l.labelid = 1;
        l.name = "snack";
        return l;
    }

    public int labelid;
    public String name;
}
