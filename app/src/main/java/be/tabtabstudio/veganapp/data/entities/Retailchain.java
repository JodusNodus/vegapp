package be.tabtabstudio.veganapp.data.entities;

public class Retailchain {
    public static Retailchain getMock() {
        Retailchain r = new Retailchain();
        r.retailchainid = 10;
        r.name = "Delhaize";
        return r;
    }

    public int retailchainid;
    public String name;
}
