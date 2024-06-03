package com.kloon.core.commands;

import com.kloon.core.mysql.MySQL;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;

public class CashCommand implements CommandExecutor {
    private MySQL bankMySQL;
    private Connection connection;

    public CashCommand(MySQL bankMySQL) {
        this.bankMySQL = bankMySQL;
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
            int playerCash = bankMySQL.getCash(playerName);
            if (playerCash >= 0) {
                player.sendMessage(ChatColor.DARK_GREEN + playerName + " has $" + playerCash + " cash.");
            } else {
                player.sendMessage(ChatColor.RED + "Player not found or an error occurred while retrieving coins.");
            }
            return true;
        }

        if (args.length < 3 || (!args[1].equalsIgnoreCase("set") && !args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("remove"))) {
            player.sendMessage(ChatColor.DARK_GREEN + "/cash <player> set <amount> - " + "Sets the amount of cash.");
            player.sendMessage(ChatColor.DARK_GREEN + "/cash <player> add <amount> - " + "Add amount of cash.");
            player.sendMessage(ChatColor.DARK_GREEN + "/cash <player> remove <amount> - " + "Remove amount of cash.");
            return true;
        }

        String playerName = args[0];
        int cashAmount;

        try {
            cashAmount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "The number of coins must be a whole number.");
            return true;
        }

        if (args[1].equalsIgnoreCase("set")) {
            bankMySQL.setCash(playerName, cashAmount, player);
        } else if (args[1].equalsIgnoreCase("add")) {
            bankMySQL.addCash(playerName, cashAmount, player);
        } else if (args[1].equalsIgnoreCase("remove")) {
            bankMySQL.removeCash(playerName, cashAmount, player);
        }

        return true;
    }


}
