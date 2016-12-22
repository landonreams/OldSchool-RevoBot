package com.gwynsoft.revobot.data.players;

import com.gwynsoft.revobot.utils.TextUtil;
import net.dv8tion.jda.core.utils.IOUtil;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.EnumMap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Landon on 12/18/2016.
 */
public class Player {

    public enum PrintingFormat {
        SIMPLE, DETAILED;
    }

    private static final String BACKUP_HISCORES_LINK = "http://services.runescape.com/m=hiscore_oldschool%s/a=50/hiscorepersonal.ws?user1=%s";

    private static final String SIMPLE_FORMAT = "config/fmt_simple.txt";

    private static final String SIMPLE_VIRTUAL_FORMAT = "config/fmt_simple_virtual.txt";

    private static final String DETAILED_FORMAT = "config/fmt_detailed.txt";

    private static final int NUMBER_OF_ENTRIES = 27;
    private static final int ITEMS_PER_ENTRY = 3;

    private static final int S_RANK = 0;
    private static final int S_LEVEL = S_RANK + 1;
    private static final int S_EXP = S_RANK + 2;

    private static final int A_RANK = 0;
    private static final int A_SCORE = A_RANK + 1;

    private final EnumMap<Hiscores.SkillType, Hiscores.Skill> skills = new EnumMap<Hiscores.SkillType, Hiscores.Skill>(Hiscores.SkillType.class);
    private final EnumMap<Hiscores.ActivityType, Hiscores.Activity> activities = new EnumMap<Hiscores.ActivityType, Hiscores.Activity>(Hiscores.ActivityType.class);

    private String name;
    private GameMode gameMode;

    public static Player lookup(String name, GameMode gameMode) {
        try {
            URL url = gameMode.getUrl(name);
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String contents = new String(IOUtil.readFully(in));
            long[][] results = rawDataToInts(contents);
            return new Player(name, gameMode, results);
        } catch (Exception e) {
            return null;
        }
    }

    private static long[][] rawDataToInts(String rawData) {
        long[][] results = new long[NUMBER_OF_ENTRIES][ITEMS_PER_ENTRY];

        String[] entries = rawData.split("\\s");
        String[] items;
        for(int i = 0; i < results.length; i++) {
            try {
                items = entries[i].split(",");
                for (int j = 0; j < results[i].length; j++) {
                    try {
                        results[i][j] = Long.parseLong(items[j]);
                    } catch (Exception e) {
                        results[i][j] = -1;
                    }
                }
            } catch (Exception e) {
                results[i] = new long[]{-1, -1, -1};
            }
        }
        return results;
    }

    private Player(String name, GameMode gameMode, long[][] results) {
        this.name = StringUtils.capitalize(name);
        this.gameMode = gameMode;

        for (Hiscores.SkillType t : Hiscores.SkillType.values()) {
            try {
                long[] entry = results[t.hiscoreIndex];
                skills.put(t, new Hiscores.Skill((int)entry[S_RANK], (int)entry[S_LEVEL], entry[S_EXP]));
            } catch (Exception e) {
                skills.put(t, new Hiscores.Skill());
            }
        }

        for (Hiscores.ActivityType t : Hiscores.ActivityType.values()) {
            try {
                long[] entry = results[t.hiscoreIndex];
                activities.put(t, new Hiscores.Activity((int)entry[A_RANK], (int)entry[A_SCORE]));
            } catch (Exception e) {
                activities.put(t, new Hiscores.Activity());
            }
        }
    }

    public String toDisplay(PrintingFormat pFormat, boolean bVirtual) {
        switch(pFormat) {
            case SIMPLE:
                if(bVirtual)
                    return toSimpleVirtualDisplay();
                else
                    return toSimpleDisplay();
            case DETAILED: return toDetailedDisplay(bVirtual);
            default: return null;
        }
    }

    private String toDetailedDisplay(boolean bVirtual) {
        try {
            String fmt = "```" + TextUtil.getRawText(DETAILED_FORMAT).replace("\r\n", "%n") + "```";

            StringBuilder params = new StringBuilder();

            String title = bVirtual ? "Virtual hiscores for " + name : "Hiscores for " + name;

            params.append(StringUtils.center(title, 44)).append("|")
                    .append(StringUtils.center(gameMode.toString() + " Mode", 44)).append("|");

            for(Hiscores.SkillType t : Hiscores.SkillType.values()) {
                params.append(t.toString()).append("|");
                Hiscores.Skill s = skills.get(t);
                if(s.getRank() < 0) {
                    params.append("N/A| - |      -      |");
                } else {
                    params.append(String.format("%,d", s.getRank())).append("|");

                    long level = -1;
                    if (bVirtual) {
                        if (t == Hiscores.SkillType.OVERALL) {
                            level = getVirtualTotal();
                        } else {
                            level = s.getVirtualLevel();
                        }
                    } else {
                        level = s.getLevel();
                    }

                    params.append(String.format("%,d", level)).append("|");
                    params.append(String.format("%,d", s.getExperience())).append("|");
                }
            }

            for(Hiscores.ActivityType t : Hiscores.ActivityType.values()) {
                params.append(t.toString()).append("|");
                Hiscores.Activity a = activities.get(t);
                if(a.getRank() < 0) {
                    params.append("  N/A|   -   |");
                } else {
                    params.append(String.format("%,d", activities.get(t).getRank())).append("|");
                    params.append(String.format("%,d", activities.get(t).getScore())).append("|");
                }
            }
            return String.format(fmt, params.toString().split("\\|"));
        } catch (Exception e) {
            e.printStackTrace();
            return backupString();
        }
    }

    private String toSimpleVirtualDisplay() {
        try {
            String fmt = "```" + TextUtil.getRawText(SIMPLE_VIRTUAL_FORMAT).replace("\r\n", "%n") + "```";
            StringBuilder params = new StringBuilder();
            params.append(StringUtils.center(name, 12)).append("|");
            params.append(StringUtils.center(gameMode.shortName() + " Mode", 14)).append("|");
            params.append(Integer.toString(skills.get(Hiscores.SkillType.OVERALL).getLevel())).append("|");
            params.append(Long.toString(getVirtualTotal())).append("|");
            for (Hiscores.SkillType t : Hiscores.SkillType.valuesByOutputIndex()) {
                params.append(Integer.toString(skills.get(t).getVirtualLevel())).append("|");
            }

            return String.format(fmt, params.toString().split("\\|"));
        } catch (Exception e) {
            e.printStackTrace();;
            return backupString();
        }
    }

    private String toSimpleDisplay() {
        try {
            String fmt = "```" + TextUtil.getRawText(SIMPLE_FORMAT).replace("\r\n", "%n") + "```";
            String[] levels = Arrays.stream(Hiscores.SkillType.valuesByOutputIndex())
                    .map(skill -> Integer.toString(skills.get(skill).getLevel()))
                    .toArray(String[]::new);

            return String.format(fmt,
                    (String[]) ArrayUtils.addAll(
                            new String[] {
                                    StringUtils.center(name, 12),
                                    StringUtils.center("(" + gameMode.shortName() + ")", 10)
                            }
                            , levels));

        } catch (Exception e) {
            return backupString();
        }
    }

    private String backupString() {
        return String.format("Something went wrong loading those hsicores,%njust let an admin know.%nI'll give you a link to your hiscores in the meantime!%n%n%s",
                String.format(BACKUP_HISCORES_LINK, gameMode.getUrlStub(), name.replace(" ", "%20")));
    }

    private long getVirtualTotal() {
        long total = 0;
        for(Hiscores.SkillType t : Hiscores.SkillType.values()) {
            if(t != Hiscores.SkillType.OVERALL) {
                total += skills.get(t).getVirtualLevel();
            }
        }
        return total;
    }

}

