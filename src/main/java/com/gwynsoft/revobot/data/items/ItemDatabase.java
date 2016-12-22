package com.gwynsoft.revobot.data.items;

import com.gwynsoft.revobot.utils.TextUtil;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;

/**
 * Created by Landon on 12/22/2016.
 */
public enum ItemDatabase {
    INSTANCE;

    private Connection conn;

    private static final String DB_PREFIX = "jdbc:sqlite:";
    private static final String DB_FILEPATH = "db/items.db";
    private static final String DB_LOC = DB_PREFIX + DB_FILEPATH;

    private static final String RSBUDDY_GE_LOOKUP = "https://api.rsbuddy.com/grandExchange?a=guidePrice&i=%d";

    private HashMap<String,RSItem> itemHashMap = new HashMap<>();

    public RSItem getItem(String itemName) {
        if(itemHashMap.containsKey(itemName)) {
            RSItem item = itemHashMap.get(itemName);
            if(item.isTradeable()) {
                item.setGePrice(getPriceGuideFromId(item.getId()));
            }
            return item;
        } else {
            try {
                conn = DriverManager.getConnection(DB_LOC);
                PreparedStatement stmt = conn.prepareStatement("select * from item where name like ?");
                stmt.setString(1, itemName);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return parseResultSet(rs);
                } else {
                    stmt.setString(1, "%" + itemName + "%");
                    rs = stmt.executeQuery();
                    if(rs.next()) {
                        return parseResultSet(rs);
                    } else {
                        return null;
                    }
                }
            } catch (Exception e) {
                return null;
            }
        }
    }

    private RSItem parseResultSet(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        int id = rs.getInt("id");
        int value = rs.getInt("value");
        boolean tradeable = rs.getBoolean("tradeable");
        int gePrice = -1;
        if (tradeable) {
            gePrice = getPriceGuideFromId(id);
        }

        RSItem result = new RSItem(name, id, value, gePrice, tradeable);

        itemHashMap.put(result.getName(), result);

        return result;
    }

    public int getPriceGuideFromId(int id) {
        try {
            JSONObject result = new JSONObject(TextUtil.getURLToText(String.format(RSBUDDY_GE_LOOKUP, id)));
            return result.getInt("overall");
        } catch (IOException e) {
            return -1;
        }
    }
}
