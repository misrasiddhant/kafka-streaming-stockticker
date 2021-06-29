package Datatypes;

import java.io.Serializable;

public class Ticker implements Serializable {
    public String name;
    public float price;

    public Ticker(String name, float price){
        this.name=name;
        this.price=price;
        System.out.println("created");
    }
}
