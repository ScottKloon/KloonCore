package com.kloon.core.commands;

import com.kloon.core.group.PlayerGroup;
import com.kloon.core.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GroupCommand implements CommandExecutor {

    private final MySQL mysql;

    public GroupCommand(MySQL mysql) {
        this.mysql = mysql;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser executado por um jogador.");
            return true;
        }

        Player player = (Player) sender;
        PlayerGroup playerRank = mysql.getPrimaryRank(player.getUniqueId());

        if (playerRank.MaiorOuIgual(PlayerGroup.MANAGER)) {
            if (args.length < 3 || (!args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("set") && !args[1].equalsIgnoreCase("remove"))) {
                sender.sendMessage(ChatColor.GREEN + "Sistema de grupos:");
                sender.sendMessage(ChatColor.GREEN + "/group <jogador> set <grupo>");
                sender.sendMessage(ChatColor.GREEN + "/group <jogador> add <grupo>");
                sender.sendMessage(ChatColor.GREEN + "/group <jogador> remove <grupo>");
                return true;
            }

            String playerName = args[0];
            String action = args[1].toLowerCase();
            String rankName = args[2];
            Player target = Bukkit.getPlayer(playerName);

            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Jogador não encontrado ou offline.");
                return true;
            }

            UUID playerUUID = target.getUniqueId();
            PlayerGroup newRank;

            try {
                newRank = PlayerGroup.valueOf(rankName.toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + "Grupo inválido.");
                return true;
            }

            switch (action) {
                case "set":
                    mysql.setPrimaryRank(playerUUID, newRank);
                    sender.sendMessage(ChatColor.GREEN + "Grupo primário de " + playerName + " atualizado para " + rankName + ".");
                    break;
                case "add":
                    mysql.setSecondaryRank(playerUUID, newRank);
                    sender.sendMessage(ChatColor.GREEN + "Grupo secundário de " + playerName + " atualizado para " + rankName + ".");
                    break;
                case "remove":
                    mysql.removeSecondaryRank(playerUUID);
                    sender.sendMessage(ChatColor.GREEN + "Grupo secundário de " + playerName + " removido.");
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Ação inválida.");
                    break;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Você não tem permissão para executar este comando.");
        }

        return true;
    }
}
