package com.gwynsoft.revobot.data.players;


import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Landon on 12/18/2016.
 */
public class Hiscores {

    public static final int UNRANKED = -1;

    public enum SkillType {
        OVERALL(0, 23),
        ATTACK(1, 0),
        DEFENCE(2, 6),
        STRENGTH(3, 3),
        HITPOINTS(4, 1),
        RANGED(5, 9),
        PRAYER(6, 12),
        MAGIC(7, 15),
        COOKING(8, 11),
        WOODCUTTING(9, 17),
        FLETCHING(10, 16),
        FISHING(11, 8),
        FIREMAKING(12, 14),
        CRAFTING(13, 13),
        SMITHING(14, 5),
        MINING(15, 2),
        HERBLORE(16, 7),
        AGILITY(17, 4),
        THIEVING(18, 10),
        SLAYER(19, 19),
        FARMING(20, 20),
        RUNECRAFTING(21, 18),
        HUNTER(22, 22),
        CONSTRUCTION(23, 21);
        public final int hiscoreIndex;
        public final int outputIndex;
        SkillType(int hiscoreIndex, int outputIndex) {
            this.hiscoreIndex = hiscoreIndex;
            this.outputIndex = outputIndex;
        }

        @Override
        public String toString() {
            return this.name().charAt(0) + name().substring(1).toLowerCase();
        }

        public static SkillType[] valuesByOutputIndex() {
            SkillType[] values = SkillType.values();
            Arrays.sort(values, Comparator.comparingInt(lhs -> lhs.outputIndex));
            return values;
        }
    }

    public enum ActivityType {
        BHR("Bounty Hunter - Rogue", 24),
        BHH("Bounty Hunter - Hunter", 25),
        LMS("Last Man Standing", 26);
        public final int hiscoreIndex;
        private final String readableName;
        ActivityType(String readableName, int hiscoreIndex) {
            this.hiscoreIndex = hiscoreIndex;
            this.readableName = readableName;
        }

        @Override
        public String toString() {
            return readableName;
        }
    }

    public static class Skill {

        private final int rank;
        private final int level;
        private final long exp;

        public Skill(int rank, int level, long exp) {
            this.rank = rank;
            this.level = level;
            this.exp = exp;
        }

        public Skill() {
            this(UNRANKED,UNRANKED,UNRANKED);
        }
        public int getRank() { return rank; }
        public int getLevel() { return level; }
        public long getExperience() { return exp; }

        public int getVirtualLevel() {
            if(level < 99) return level;
            else {
                return ExperienceTable.getInstance().levelAt(Math.min(200000000, (int)exp));
            }
        }
    }

    public static class ExperienceTable {
        private static ExperienceTable INSTANCE = new ExperienceTable();
        private int[] expTable;
        private ExperienceTable() {
            expTable = new int[126];
            double n;
            for(int level = 1; level <= expTable.length; level++) {
                for(n = 1; n < level; n++) {
                    n += Math.floor(n + 300.0 * Math.pow(2.0, n / 7.0));
                }
                expTable[level - 1] = Math.min(200000000, (int)Math.floor(0.25 * n));
            }
        }

        public static ExperienceTable getInstance() { return INSTANCE; }

        public int experienceAt(int level) {
            return expTable[level - 1];
        }

        public int levelAt(int experience) {
            for(int level = 1; level <= expTable.length; level++) {
                if(experience <= expTable[level - 1])
                    return level;
            }
            return 126;
        }

        public int expToLevel(int experience) {
            if(experience < 0)
                return -1;
            if(experience >= 200000000)
                return 0;
            int currentLevel = levelAt(experience);
            return expTable[Math.min(expTable.length - 1, currentLevel + 1)] - experience;
        }
    }

    public static class Activity {
        private final int rank;
        private final int score;

        public Activity(int rank, int score) {
            this.rank = rank;
            this.score = score;
        }

        public Activity() {
            this(UNRANKED,UNRANKED);
        }

        public int getRank() { return rank; }
        public int getScore() { return score; }
    }
}
