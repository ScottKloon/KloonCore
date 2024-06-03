package com.kloon.core;

import com.kloon.core.commands.CoinsCommand;
import com.kloon.core.commands.GroupCommand;
import com.kloon.core.group.GroupListener;
import com.kloon.core.mysql.MySQL;
import com.kloon.core.commands.CashCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class KloonCore extends JavaPlugin {

    public MySQL mysql;
    @Override
    public void onEnable() {
        openMySQL();
        getCommand("group").setExecutor(new GroupCommand(mysql));
        getCommand("coins").setExecutor(new CoinsCommand(mysql));
        getCommand("cash").setExecutor(new CashCommand(mysql));
        getServer().getPluginManager().registerEvents(new GroupListener(mysql), this);

    }

    @Override
    public void onDisable() {
        closeMySQL();
    }
    public void openMySQL() {
        // Configure sua conexão MySQL
        String host = "localhost";
        int port = 3306;
        String database = "kloon";
        String username = "root";
        String password = "";

        mysql = new MySQL(host, port, database, username, password);
        try {
            mysql.connect();
            mysql.createTableIfNotExists();
            getLogger().info("Tabela 'player' criada/verificada com sucesso.");
        } catch (Exception e) {
            getLogger().severe("Erro ao conectar ao MySQL ou criar/verificar a tabela: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void closeMySQL() {
        if (mysql != null) {
            try {
                mysql.disconnect();
                getLogger().info("Desconectado do MySQL com sucesso.");
            } catch (Exception e) {
                getLogger().severe("Erro ao desconectar do MySQL: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
