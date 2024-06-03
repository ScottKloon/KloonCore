package com.kloon.core.group;

import org.bukkit.ChatColor;

public enum PlayerGroup {

    MASTER(ChatColor.GOLD, "Master", "[Master]",13),
    MANAGER(ChatColor.DARK_RED, "Gerente", "[Gerente]",12),
    ADMIN(ChatColor.RED, "Admin", "[Admin]",11),
    MODERATOR(ChatColor.DARK_GREEN, "Moderador", "[Moderador]",10),
    HELPER(ChatColor.YELLOW, "Ajudante", "[Ajudante]",9),
    BUILDER(ChatColor.GREEN, "Construtor", "[Construtor]",8),
    YOUTUBER(ChatColor.RED, "YouTuber", "[YouTuber]",7),
    EMPEROR(ChatColor.AQUA, "Imperador", "[Imperador]",6),
    SUPREME(ChatColor.DARK_RED, "Supremo", "[Supremo]",5),
    LEGENDARY(ChatColor.DARK_GREEN, "Lendário", "[Lendário]",4),
    HERO(ChatColor.DARK_PURPLE, "Herói", "[Herói]",3),
    CHAMPION(ChatColor.DARK_AQUA, "Campeão", "[Campeão]",2),
    MEMBER(ChatColor.GRAY, "Membro", "Membro",1);


    private final ChatColor color;
    private final String name;
    private final String tag;
    private final int order;

    PlayerGroup(ChatColor color, String name, String tag, int order) {
        this.color = color;
        this.name = name;
        this.tag = tag;
        this.order = order;
    }

    public ChatColor getColor() {
        return color;
    }
    public String getTag() {if (color != null) {return color + tag;} else {return tag;}}
    public String setTag(){return tag;}

    public String getName() {return name;}
    public int getOrder() {return order;}

    public boolean MaiorOuIgual(PlayerGroup otherRank) {return this.order >= otherRank.getOrder();}
    public boolean MenorOuIgual(PlayerGroup otherRank) {return this.order <= otherRank.getOrder();}
    public boolean MaiorQue(PlayerGroup otherRank) {return this.order > otherRank.getOrder();}
    public boolean MenorQue(PlayerGroup otherRank) {return this.order < otherRank.getOrder();}
    public boolean Igual(PlayerGroup otherRank) {
        return this == otherRank;
    }

}
