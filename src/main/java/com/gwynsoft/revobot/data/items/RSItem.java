package com.gwynsoft.revobot.data.items;

/**
 * Created by Landon on 12/19/2016.
 */
public class RSItem {
    protected String name;
    protected int id;
    protected int value;
    protected int gePrice;
    protected boolean tradeable;

    public RSItem(String name, int id, int value, int gePrice, boolean tradeable) {
        this.name = name;
        this.id = id;
        this.value = value;
        this.gePrice = gePrice;
        this.tradeable = tradeable;
    }

    public void setGePrice(int price) { gePrice = price; }
    public String getName() { return name; }
    public int getId() { return id; }
    public int getHighAlch() { return (int)(0.6 * (double)value); }
    public int getValue() { return value; }
    public int getGePrice() { return gePrice; }
    public boolean isTradeable() { return tradeable; }

    @Override
    public String toString() {
        if(tradeable)
            if(gePrice == 0)
                return String.format("**%s** - Inactive on GE", name);
            else
                return String.format("**%s** - %,d gp", name, gePrice);
        else
            return String.format("**%s**");
    }
}
