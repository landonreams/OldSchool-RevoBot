package com.gwynsoft.revobot.data.entity;

import com.gwynsoft.revobot.data.items.RatedItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Landon on 12/22/2016.
 */
public class Monster {
    private String name;
    private String[] aliases;
    private String wikiPage;
    private ArrayList<RatedItem> drops, relatedItems;

    public Monster(JSONObject json) {
        try {
            this.name = json.getString("name");
            this.wikiPage = json.getString("url");

            JSONArray jAliases = json.getJSONArray("aliases");
            JSONArray jDrops = json.getJSONArray("drops");

            if(jAliases == null) {
                aliases = new String[] { };
            } else {
                aliases = new String[jAliases.length()];
                for(int i = 0; i < jAliases.length(); i++) {
                    aliases[i] = jAliases.getString(i);
                }
            }

            if(jDrops == null) {
                drops = null;
            } else {
                drops = new ArrayList<>();
                relatedItems = new ArrayList<>();
                for(int i = 0; i < jDrops.length(); i++) {
                    JSONObject item = jDrops.getJSONObject(i);
                    double rate = Math.round(10 * (1 / item.getDouble("rate"))) / 10.0;
                    RatedItem rItem = new RatedItem(item.getString("item"), rate);
                    if(rate < 0) {
                        relatedItems.add(rItem);
                    } else {
                        drops.add(rItem);
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public String getName() { return name; }
    public String[] getAliases() { return aliases; }
    public String getWikiPage() { return wikiPage; }
    public RatedItem[] getDrops() { return drops == null ? new RatedItem[] { } : drops.toArray(new RatedItem[]{}); }
    public RatedItem[] getRelatedItems() { return relatedItems == null ? new RatedItem[] { } : relatedItems.toArray(new RatedItem[]{}); }
}
