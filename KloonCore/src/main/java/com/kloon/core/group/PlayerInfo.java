package com.kloon.core.group;

import java.util.UUID;

public class PlayerInfo {
    private UUID uuid;
    private String nickname;
    private String rankPrimary;
    private String rankSecondary;
    private String firstLogin;
    private String lastLogin;
    private String lastIP;
    private String email;
    private String password;
    private int coins;
    private int id; // Adicionando o campo id

    public PlayerInfo(UUID uuid, String nickname, String rankPrimary, String rankSecondary, String firstLogin, String lastLogin, String lastIP, int id) { // Atualizando o construtor
        this.uuid = uuid;
        this.nickname = nickname;
        this.rankPrimary = rankPrimary;
        this.rankSecondary = rankSecondary;
        this.firstLogin = firstLogin;
        this.lastLogin = lastLogin;
        this.lastIP = lastIP;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRankPrimary() {
        return rankPrimary;
    }

    public void setRankPrimary(String rankPrimary) {
        this.rankPrimary = rankPrimary;
    }

    public String getRankSecondary() {
        return rankSecondary;
    }

    public void setRankSecondary(String rankSecondary) {
        this.rankSecondary = rankSecondary;
    }

    public String getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(String firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastIP() {
        return lastIP;
    }

    public void setLastIP(String lastIP) {
        this.lastIP = lastIP;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
