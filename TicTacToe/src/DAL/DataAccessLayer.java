package DAL;

import java.sql.*;

public class DataAccessLayer {

    private Connection connection;
    private int playerid;
    private boolean looking = true;
    private boolean won = false;
    private String winner;
    private String playerSymbol;
    private boolean ready;

    public DataAccessLayer() {
        String URL = "jdbc:oracle:thin:@10.172.61.200:1521:orc1";
        String USER = "ZTest";
        String PASS = "123";
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            playerid = getPlayerCount() + 1;
            insertNewPlayer();
            setPlayerSymbol();
            ready = false;
        } catch (SQLException e) {
            close();
        }
    }

    private void insertNewPlayer() {
        String sql = "INSERT INTO ttt_players (symbol, playerid) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int playerCount = getPlayerCount();

            if (playerCount == 0) {
                stmt.setString(1, "X");
                stmt.setInt(2, playerid);
                stmt.executeUpdate();
            } else if (playerCount == 1) {
                stmt.setString(1, "O");
                stmt.setInt(2, playerid);
                stmt.executeUpdate();
                
                startGame();
            } else if (playerCount >= 2) {
                System.out.println("The game is already full. Cannot add more players.");
                close();
                return;
            }
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPlayerCount() {
        String sql = "SELECT COUNT(*) FROM ttt_players";
        try (Statement ps = connection.createStatement()) {
            ResultSet rs = ps.executeQuery(sql);
            rs.next();
            int result = rs.getInt(1);
            ps.close();
            rs.close();
            return result;
        } catch (SQLException e) {
            close();
        }
        return -1;
    }

    private void close() {
        if (connection != null) {
            System.out.println("Game is full.");
            connection = null;
            System.exit(0);
        }
    }

    private void generateNewBoard() {
        deleteBoard();
        String sql = "INSERT INTO ttt_board (x,y,symbol) VALUES (?, ?, ' ')";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, j);
                    stmt.setInt(2, i);
                    stmt.executeUpdate();
                    stmt.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void deleteBoard() {
        String sql = "DELETE FROM ttt_board";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSymbolAt(int x, int y, String symbol) {
        String sql = "UPDATE ttt_board SET symbol = ? WHERE x = ? AND y = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, symbol);
            stmt.setInt(2, x);
            stmt.setInt(3, y);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSymbolAt(int x, int y) {
        String sql = "SELECT symbol FROM ttt_board WHERE x = ? AND y = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, x);
            stmt.setInt(2, y);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            String symbol = rs.getString(1);
            rs.close();
            stmt.close();
            return symbol;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        String sql = "DELETE FROM ttt_players WHERE playerid=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, playerid);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCurrentPlayer() {
        String sql = "SELECT * FROM ttt_queue";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            String currentPlayer = rs.getString(1);
            rs.close();
            stmt.close();
            return currentPlayer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateQueue() {
        String currentPlayer = getCurrentPlayer();
        String sql;
        if (currentPlayer.equals("X"))
            sql = "UPDATE ttt_queue SET current_player = 'O'";
        else
            sql = "UPDATE ttt_queue SET current_player= 'X'";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateQueue(String player) {
        String sql = "UPDATE ttt_queue SET current_player = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, player);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPlayerSymbol() {
        String sql = "SELECT symbol FROM ttt_players WHERE playerid=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, playerid);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            String symbol = rs.getString(1);
            rs.close();
            stmt.close();
            playerSymbol = symbol;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public String getPlayerSymbol() {
        return playerSymbol;
    }

    private void startGame() {
        winner = null;
        generateNewBoard();
        updateQueue("X");
    }

    public boolean lookingForGame() {
        return getReadyPlayers() != 2;
    }

    public int getReadyPlayers() {
        String sql = "SELECT COUNT(*) FROM ttt_players WHERE ready=1";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            stmt.close();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }


    public void checkWinner() {
        for (int i = 0; i < 3; i++) {
            // vertical
            if (!getSymbolAt(i, 0).equals(" ") && getSymbolAt(i, 0).equals(getSymbolAt(i, 1)) && getSymbolAt(i, 1).equals(getSymbolAt(i, 2))) {
                setWinner(getSymbolAt(i, 0));
                won = true;
                System.out.println(winner);
            }

            // horizontal
            if (!getSymbolAt(0, i).equals(" ") && getSymbolAt(0, i).equals(getSymbolAt(1, i)) && getSymbolAt(1, i).equals(getSymbolAt(2, i))) {
                setWinner(getSymbolAt(0, i));
                won = true;
                System.out.println(winner);
            }
        }
        if (!getSymbolAt(0, 0).equals(" ") && getSymbolAt(0, 0).equals(getSymbolAt(1, 1)) && getSymbolAt(1, 1).equals(getSymbolAt(2, 2))) {
            setWinner(getSymbolAt(1, 1));
            won = true;
            System.out.println(winner);
        }
        if (!getSymbolAt(2, 0).equals(" ") && getSymbolAt(2, 0).equals(getSymbolAt(1, 1)) && getSymbolAt(1, 1).equals(getSymbolAt(0, 2))) {
            setWinner(getSymbolAt(1, 1));
            won = true;
            System.out.println(winner);
        }
    }

    public boolean isWon() {
        return won;
    }

    public boolean playerExists() {
        String sql = "SELECT * FROM ttt_players WHERE playerid=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, playerid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                rs.close();
                stmt.close();
                return true;
            }
            rs.close();
            stmt.close();
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        if (playerExists()) {
            String sql = "DELETE FROM ttt_players WHERE playerid=?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, playerid);
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setReady(boolean ready) {
        this.ready = ready;
        String sql = "UPDATE ttt_players SET ready=? WHERE playerid=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int state = ready ? 1 : 0;
            stmt.setInt(1, state);
            stmt.setInt(2, playerid);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isReady() {
        return ready;
    }
}
