package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class auto {
    private  SimpleStringProperty nazwa;
    private SimpleIntegerProperty lata;
//    private String nazwa;
//    private Integer lata;

    public String getNazwa() {
        return nazwa.get();
    }

    public SimpleStringProperty nazwaProperty() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa.set(nazwa);
    }

    public int getLata() {
        return lata.get();
    }

    public SimpleIntegerProperty lataProperty() {
        return lata;
    }

    public void setLata(int lata) {
        this.lata.set(lata);
    }

    public auto(String nazwa, Integer lata) {
        this.nazwa = new SimpleStringProperty(nazwa);
        this.lata = new SimpleIntegerProperty(lata);


    }
}
