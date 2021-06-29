package Datatypes;

import java.io.Serializable;

import java.util.Arrays;
import java.util.List;

public class Tickers implements Serializable {
    public List<Ticker> tickers;

    /*public Tickers(List<Ticker> tickers) {
        this.tickers = tickers;
    }*/
    public Tickers(Ticker[] tickers) {
        this.tickers = Arrays.asList(tickers);
    }
}
