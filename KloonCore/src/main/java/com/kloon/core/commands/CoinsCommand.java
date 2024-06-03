package com.kloon.core.commands;

import com.kloon.core.mysql.MySQL;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand implements CommandExecutor {
    private MySQL mysql;

    public CoinsCommand(MySQL mysql) {
        this.mysql = mysql;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            String playerName = args[0];
            int playerCoins = mysql.getCoins(playerName);
            if (playerCoins >= 0) {
                player.sendMessage(ChatColor.GOLD + playerName + " has " + playerCoins + " coins.");
            } else {
                player.sendMessage(ChatColor.RED + "Player not found or an error occurred while retrieving coins.");
            }
            return true;
        }

        if (args.length < 3 || (!args[1].equalsIgnoreCase("set") && !args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("remove"))) {
            player.sendMessage(ChatColor.GOLD + "/coins <player> set <amount> - " + "Sets the amount of coins.");
            player.sendMessage(ChatColor.GOLD + "/coins <player> add <amount> - " + "Add amount of coins.");
            player.sendMessage(ChatColor.GOLD + "/coins <player> remove <amount> - " + "Remove amount of coins.");
            return true;
        }

        String playerName = args[0];
        int coinsAmount;

        try {
            coinsAmount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "The number of coins must be a whole number.");
            return true;
        }

        if (args[1].equalsIgnoreCase("set")) {
            mysql.setCoins(playerName, coinsAmount, player);
        } else if (args[1].equalsIgnoreCase("add")) {
            mysql.addCoins(playerName, coinsAmount, player);
        } else if (args[1].equalsIgnoreCase("remove")) {
            mysql.removeCoins(playerName, coinsAmount, player);
        }

        return true;
    }
}
