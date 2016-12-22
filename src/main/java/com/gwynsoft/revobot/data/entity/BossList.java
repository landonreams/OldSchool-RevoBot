package com.gwynsoft.revobot.data.entity;

import com.gwynsoft.revobot.utils.TextUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Landon on 12/22/2016.
 */
public enum BossList {
    INSTANCE;

    private final HashMap<String,Monster> bosses = new HashMap<>();
    private final HashSet<Monster> bossSet = new HashSet<>();

    BossList() {
        try {
            JSONArray bossesJson = new JSONArray(TextUtil.getRawText("config/bosses.json"));
            if(bossesJson != null) {
                for(int i = 0; i < bossesJson.length(); i++) {
                    JSONObject boss = bossesJson.getJSONObject(i);
                    try {
                        Monster monster = new Monster(boss);
                        bosses.put(monster.getName().toLowerCase(), monster);
                        for(String alias : monster.getAliases()) {
                            bosses.put(alias.toLowerCase(), monster);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        } catch (Exception e) {

        }
        bossSet.addAll(bosses.values());
    }

    public Set<Monster> getBosses() {
        return bossSet;
    }

    public Monster getBoss(String name) {
        if(bosses.containsKey(name.toLowerCase())) {
            return bosses.get(name.toLowerCase());
        } else return null;
    }

}
