package be.tabtabstudio.veganapp.model.entities;

public class Supermarket {
    public static Supermarket getMock() {
        Supermarket s = new Supermarket();
        s.placeid = "ChIJ7WClVotQw0cRj9AHiVnCH5E";
        s.name = "AD Delhaize Assebroek";
        s.address = "Baron Ruzettelaan 298, Brugge";
        s.retailchain = Retailchain.getMock();
        return s;
    }
    public String placeid;
    public String name;
    public String address;
    public Retailchain retailchain;
}