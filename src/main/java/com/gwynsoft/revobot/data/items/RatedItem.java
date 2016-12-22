package com.gwynsoft.revobot.data.items;

/**
 * Created by Landon on 12/22/2016.
 */
public class RatedItem {
    private double dropRate;
    private RSItem item;

    public RatedItem(String name, double dropRate) {
       item = ItemDatabase.INSTANCE.getItem(name);
       this.dropRate = dropRate;
    }

    public boolean valid() { return item != null; }

    public RSItem item() { return item; }

    public double getDropRate() { return dropRate; }

    @Override
    public String toString() {
        if (item != null) {
            String sRate = null;
            if(dropRate == (long)dropRate) {
                sRate = String.format("(1/%d)", (long)dropRate);
            } else {
                sRate = String.format("(~1/%.2f)", dropRate);
            }
            if (item.isTradeable()) {
                item.setGePrice(ItemDatabase.INSTANCE.getPriceGuideFromId(item.getId()));
                int price = item.getGePrice();
                if(price <= 0)
                    return String.format("**%s** %s - Inactive on GE", item.getName(), sRate, item.getGePrice());
                else
                    return String.format("**%s** %s - %,d gp", item.getName(), sRate, item.getGePrice());
            } else {
                return String.format("**%s** %s", item.getName(), sRate);
            }
        } else {
            return "Item not found.";
        }
    }
}
