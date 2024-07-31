package io.keikai.tutorial.database;

import java.sql.*;
import java.util.*;

public class MyDataService {

    private static final String URL = "jdbc:mysql://localhost:3306/keikai_tutorial";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public MyDataService() {
        createTableIfNotExists();
        if (queryAll().isEmpty()) {
            initializeData();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS trades ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "type VARCHAR(255),"
                + "salesperson VARCHAR(255),"
                + "sale INT)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeData() {
        save(new Trade("Beverages", "Suyama", 5122));
        save(new Trade("Meat", "Davolio", 450));
        save(new Trade("Produce", "Buchanan", 6328));
        save(new Trade("Tools", "Suyama", 230));
        save(new Trade("Vegetables", "John", 947));
    }

    public void save(Map<Integer, Trade> changedMap) {
        String sql = "UPDATE trades SET type = ?, salesperson = ?, sale = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Trade trade : changedMap.values()) {
                pstmt.setString(1, trade.getType());
                pstmt.setString(2, trade.getSalesPerson());
                pstmt.setInt(3, trade.getSale());
                pstmt.setInt(4, trade.getId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(Trade trade) {
        String sql = "INSERT INTO trades (type, salesperson, sale) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, trade.getType());
            pstmt.setString(2, trade.getSalesPerson());
            pstmt.setInt(3, trade.getSale());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trade.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Trade> queryAll() {
        Map<Integer, Trade> result = new TreeMap<>();
        String sql = "SELECT * FROM trades";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Trade trade = new Trade(rs.getInt("id"));
                trade.setType(rs.getString("type"));
                trade.setSalesPerson(rs.getString("salesperson"));
                trade.setSale(rs.getInt("sale"));
                result.put(trade.getId(), trade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}