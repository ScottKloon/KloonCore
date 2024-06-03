package com.kloon.core.mysql;

import com.kloon.core.group.PlayerGroup;
import com.kloon.core.group.PlayerInfo;
import org.bukkit.entity.Player;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class MySQL {

    private final String host, database, username, password;
    private final int port;
    private Connection connection;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public MySQL(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }
    public void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            synchronized (this) {
                if (connection != null && !connection.isClosed()) {
                    return;
                }
                Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    public void createTableIfNotExists() {
        String createPlayerTableSQL = "CREATE TABLE IF NOT EXISTS player (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "UUID VARCHAR(36) UNIQUE," +
                "nickname VARCHAR(255)," +
                "rank_primary VARCHAR(255), " +
                "rank_secondary VARCHAR(255), " +
                "first_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "last_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "last_ip VARCHAR(255)" +
                ")";

        String createPlayerBankTableSQL = "CREATE TABLE IF NOT EXISTS player_bank (" +
                "nickname VARCHAR(16) PRIMARY KEY," +
                "cash INT," +
                "coins INT" +
                ")";

        try {
            Connection connection = getConnection();
            try (PreparedStatement playerStatement = connection.prepareStatement(createPlayerTableSQL);
                 PreparedStatement playerBankStatement = connection.prepareStatement(createPlayerBankTableSQL)) {
                playerStatement.executeUpdate();
                playerBankStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setCash(String playerName, int cashAmount, Player player) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE player_bank SET cash = ? WHERE nickname = ?");
            statement.setInt(1, cashAmount);
            statement.setString(2, playerName);
            statement.executeUpdate();
            statement.close();
            player.sendMessage("Cash amount for " + playerName + " set to " + cashAmount);
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("An error occurred while setting the cash amount for " + playerName);
        }
    }
    public void addCash(String playerName, int cashAmount, Player player) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE player_bank SET cash = cash + ? WHERE nickname = ?");
            statement.setInt(1, cashAmount);
            statement.setString(2, playerName);
            statement.executeUpdate();
            statement.close();
            player.sendMessage(cashAmount + " cash adicionadas para " + playerName);
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("Ocorreu um erro ao adicionar cash para " + playerName);
        }
    }
    public void removeCash(String playerName, int cashAmount, Player player) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE player_bank SET cash = GREATEST(cash - ?, 0) WHERE nickname = ?");
            statement.setInt(1, cashAmount);
            statement.setString(2, playerName);
            statement.executeUpdate();
            statement.close();
            player.sendMessage(cashAmount + " cash removidas de " + playerName);
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("Ocorreu um erro ao remover cash de " + playerName);
        }
    }
    public int getCash(String playerName) {
        int cash = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT cash FROM player_bank WHERE nickname = ?");
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                cash = resultSet.getInt("cash");
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cash;
    }
    public void setCoins(String playerName, int coinsAmount, Player player) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE player_bank SET coins = ? WHERE nickname = ?");
            statement.setInt(1, coinsAmount);
            statement.setString(2, playerName);
            statement.executeUpdate();
            statement.close();
            player.sendMessage("Coin amount for " + playerName + " set to " + coinsAmount);
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("An error occurred while setting the coin amount for " + playerName);
        }
    }
    public void addCoins(String playerName, int coinsAmount, Player player) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE player_bank SET coins = coins + ? WHERE nickname = ?");
            statement.setInt(1, coinsAmount);
            statement.setString(2, playerName);
            statement.executeUpdate();
            statement.close();
            player.sendMessage(coinsAmount + " moedas adicionadas para " + playerName);
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("Ocorreu um erro ao adicionar moedas para " + playerName);
        }
    }
    public void removeCoins(String playerName, int coinsAmount, Player player) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE player_bank SET coins = GREATEST(coins - ?, 0) WHERE nickname = ?");
            statement.setInt(1, coinsAmount);
            statement.setString(2, playerName);
            statement.executeUpdate();
            statement.close();
            player.sendMessage(coinsAmount + " moedas removidas de " + playerName);
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("Ocorreu um erro ao remover moedas de " + playerName);
        }
    }
    public int getCoins(String playerName) {
        int coins = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT coins FROM player_bank WHERE nickname = ?");
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                coins = resultSet.getInt("coins");
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coins;
    }
    public void registerPlayer(UUID playerUUID, String nickname, String lastIP) {

        String insertSQL = "INSERT INTO player (uuid, nickname, rank_primary, rank_secondary, first_login, last_login, last_ip) " +
                "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, playerUUID.toString());
            statement.setString(2, nickname);
            statement.setString(3, "MEMBER");
            statement.setString(4, ""); // Assuming rank_secondary can be an empty string
            statement.setString(5, lastIP);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void setPrimaryRank(UUID playerUUID, PlayerGroup rank) {
        String updateSQL = "UPDATE player SET rank_primary = ? WHERE uuid = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, rank.name());
            statement.setString(2, playerUUID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setSecondaryRank(UUID playerUUID, PlayerGroup rank) {
        String updateSQL = "UPDATE player SET rank_secondary = ? WHERE uuid = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, rank.name());
            statement.setString(2, playerUUID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void removeSecondaryRank(UUID playerUUID) {
        String updateSQL = "UPDATE player SET rank_secondary = '' WHERE uuid = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, playerUUID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public PlayerGroup getPrimaryRank(UUID playerUUID) {
        String query = "SELECT rank_primary FROM player WHERE uuid = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String rankName = resultSet.getString("rank_primary");
                    return PlayerGroup.valueOf(rankName.toUpperCase());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public PlayerGroup getSecondaryRank(UUID playerUUID) {
        String query = "SELECT rank_secondary FROM player WHERE uuid = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String rankName = resultSet.getString("rank_secondary");
                    return PlayerGroup.valueOf(rankName.toUpperCase());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFirstLogin(UUID playerUUID) {
        String query = "SELECT first_login FROM player WHERE uuid = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Timestamp firstLogin = resultSet.getTimestamp("first_login");
                    return dateFormat.format(firstLogin);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getLastLogin(UUID playerUUID) {
        String query = "SELECT last_login FROM player WHERE uuid = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Timestamp lastLogin = resultSet.getTimestamp("last_login");
                    return dateFormat.format(lastLogin);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getLastIP(UUID playerUUID) {
        String query = "SELECT last_ip FROM player WHERE uuid = ?";


        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, playerUUID.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("last_ip");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public UUID getUUID(String nickname) {
        String query = "SELECT uuid FROM player WHERE nickname = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nickname);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return UUID.fromString(resultSet.getString("uuid"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public PlayerInfo getPlayerNickname(String nickname) {
        String query = "SELECT * FROM player WHERE nickname = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nickname);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                    int id = resultSet.getInt("id");
                    String rankPrimary = resultSet.getString("rank_primary");
                    String rankSecondary = resultSet.getString("rank_secondary");
                    Timestamp firstLogin = resultSet.getTimestamp("first_login");
                    Timestamp lastLogin = resultSet.getTimestamp("last_login");
                    String lastIP = resultSet.getString("last_ip");

                    return new PlayerInfo(uuid, nickname, rankPrimary, rankSecondary, dateFormat.format(firstLogin), dateFormat.format(lastLogin), lastIP, id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean isPlayerRegistered(UUID playerUUID) {
        String query = "SELECT COUNT(*) FROM player WHERE uuid = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void updateLastLoginAndIP(UUID playerUUID, String lastIP) {
        String updateSQL = "UPDATE player SET last_login = CURRENT_TIMESTAMP, last_ip = ? WHERE uuid = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, lastIP);
            statement.setString(2, playerUUID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean playerExists(String playerName) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT nickname FROM player_bank WHERE nickname = ?");
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();
            boolean exists = resultSet.next();
            statement.close();
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void registerPlayerOnBank(String playerName) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO player_bank (nickname, cash, coins) VALUES (?, ?, ?)");
            statement.setString(1, playerName);
            statement.setInt(2, 0); // Cash
            statement.setInt(3, 0); // Coins
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
