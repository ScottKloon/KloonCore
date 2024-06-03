package com.kloon.core.group;

import com.kloon.core.api.TagAPI;
import com.kloon.core.mysql.MySQL;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class GroupListener implements Listener {

    private final MySQL mysql;

    public GroupListener(MySQL mysql) {
        this.mysql = mysql;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String playerName = player.getName();
        String playerIP = player.getAddress().getAddress().getHostAddress();

        if (!mysql.isPlayerRegistered(playerUUID)) {
            mysql.registerPlayer(playerUUID, playerName, playerIP);
            return;
        }
        if (!mysql.playerExists(playerName)) {
            mysql.registerPlayerOnBank(playerName);
        }

        mysql.updateLastLoginAndIP(playerUUID, playerIP);

    }


    @EventHandler
    public void setTag(PlayerJoinEvent event){
        Player player = event.getPlayer();
        PlayerGroup playerGroup = mysql.getPrimaryRank(player.getUniqueId());

        if(playerGroup.Igual(PlayerGroup.MEMBER)){
            String tag = playerGroup.getColor() + "";
            TagAPI.setTag(player, tag);
        }else if(playerGroup.MaiorOuIgual(PlayerGroup.CHAMPION)){
            String tag = playerGroup.getTag() + " ";
            TagAPI.setTag(player, tag);
        }


    }
}
