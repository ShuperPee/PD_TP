
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DataBaseConnect {

    // Nome do Driver JDBC e Endereco URL da Base de Dados
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public String SERVIDOR;
    static final String NOME_BD = "pd_1819";
    public String URL_BD;
    // Credenciais da Base de Dados
    static final String UTILIZADOR = "root";
    static final String SENHA = "gps34-1819";

    /*dataBaseAddr deve ser assim
    * = "localhost:3306/NOME_BD";
    * = "jdbc:mysql://" + SERVIDOR + "/" + NOME_BD;
     */
    public DataBaseConnect(String dataBaseAddr) throws ClassNotFoundException {
        //Registar o Driver JDBC

        Class.forName(JDBC_DRIVER);
        SERVIDOR = dataBaseAddr;
        SERVIDOR = "localhost:3306";
        URL_BD = "jdbc:mysql://" + SERVIDOR + "/" + NOME_BD;

    }

    /**
     * getAllClientsAddr
     *
     * @return ClientsAddrs - uma lista de Addrs dos clientes.
     * @throws java.sql.SQLException
     */
    public List<String> getAllClientsAddr() throws SQLException, Exception {
        Connection conn = null;
        Statement stmt = null;
        List<String> ClientsAddrs = new ArrayList<>();
        try {
            String sql;

            //Abrir a Conexao
            conn = DriverManager.getConnection(URL_BD, UTILIZADOR, SENHA);
            //Criar a query
            sql = "SELECT * FROM clients";
            //Executar a query
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //Extrair informações do resultado da query
            while (rs.next()) {
                //Recebe uma linha que representa um ficheiro de um cliente
                ClientsAddrs.add(rs.getString("client_addr"));
                if (ClientsAddrs.isEmpty()) {
                    //Error
                }
            }
            rs.close();
            return ClientsAddrs;
        } catch (SQLException se) {
            throw new SQLException(se);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                throw new SQLException(se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                throw new SQLException(se);
            }
        }
    }

    /**
     * getClientAddr
     *
     * @param file - identifica o ficheiro.
     * @return clienteAddr - Addr de um cliente que contem o ficheiro
     * @throws java.sql.SQLException
     */
    public String[] getClientAddr(String file) throws SQLException, Exception {
        Connection conn = null;
        Statement stmt = null;
        String clientAddr[] = new String[3];
        try {
            String sql;

            //Abrir a Conexao
            conn = DriverManager.getConnection(URL_BD, UTILIZADOR, SENHA);
            //Criar a query
            sql = "SELECT C.client_addr, C.port_tcp , F.name FROM files F, clients C WHERE C.idclients = F.clients_idclients";
            //Executar a query
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //Extrair informações do resultado da query
            while (rs.next()) {
                //Recebe uma linha que representa um ficheiro de um cliente
                if (rs.getString("name").compareTo(file) == 0) {
                    clientAddr[0] = rs.getString("client_addr");
                    clientAddr[1] = "" + rs.getInt("port_tcp");
                }
            }
            rs.close();
            clientAddr[2] = file;
            return clientAddr;
        } catch (SQLException se) {
            throw new SQLException(se);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                throw new SQLException(se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                throw new SQLException(se);
            }
        }
    }

    /**
     * getClientId
     *
     * @param file - identifica o ficheiro.
     * @return clientId - Id do client
     * @throws java.sql.SQLException
     */
    public int getClientId(String ClientAddr) throws SQLException, Exception {
        Connection conn = null;
        Statement stmt = null;
        int clientId = -1;
        try {
            String sql;

            //Abrir a Conexao
            conn = DriverManager.getConnection(URL_BD, UTILIZADOR, SENHA);
            //Criar a query
            sql = "SELECT * FROM clients";
            //Executar a query
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //Extrair informações do resultado da query
            while (rs.next()) {
                //Recebe uma linha que representa um ficheiro de um cliente
                if (rs.getString("client_addr").compareTo(ClientAddr) == 0) {
                    clientId = rs.getInt("id_clients");
                }
            }
            rs.close();
            return clientId;
        } catch (SQLException se) {
            throw new SQLException(se);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                throw new SQLException(se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                throw new SQLException(se);
            }
        }
    }

    /**
     * getClientFiles
     *
     * @param clientAddr - String da Addr de um cliente que se quer os ficheiros
     * @return files - Lista dos nomes dos ficheiros
     * @throws java.sql.SQLException
     * @throws Exception
     */
    public List<String>[] getClientFiles(String clientAddr) throws SQLException, Exception {
        Connection conn = null;
        Statement stmt = null;
        List<String>[] files = (ArrayList<String>[]) new ArrayList[2];
        files[0] = new ArrayList<>();
        files[1] = new ArrayList<>();
        try {
            String sql;

            //Abrir a Conexao
            conn = DriverManager.getConnection(URL_BD, UTILIZADOR, SENHA);
            //Criar a query
            sql = "SELECT * FROM files";
            //Executar a query
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //Extrair informações do resultado da query
            while (rs.next()) {
                files[0].add(rs.getString("name"));
                files[1].add(rs.getString("size"));
            }
            rs.close();
            return files;
        } catch (SQLException se) {
            throw new SQLException(se);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                throw new SQLException(se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                throw new SQLException(se);
            }
        }
    }

    /**
     * getFiles
     *
     * @return files - Array com duas Listas com nomes e tamanho dos ficheiros
     * @throws java.sql.SQLException
     * @throws Exception
     */
    public List<String>[] getFiles() throws SQLException, Exception {
        Connection conn = null;
        Statement stmt = null;
        List<String>[] files = (ArrayList<String>[]) new ArrayList[2];
        files[0] = new ArrayList<>();
        files[1] = new ArrayList<>();
        try {
            String sql;

            //Abrir a Conexao
            conn = DriverManager.getConnection(URL_BD, UTILIZADOR, SENHA);
            //Criar a query
            sql = "SELECT name, size FROM files";
            //Executar a query
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //Extrair informações do resultado da query
            while (rs.next()) {
                //Recebe uma linha que representa um ficheiro de um cliente
                files[0].add(rs.getString("name"));
                files[1].add(rs.getString("size"));
            }

            rs.close();
            return files;
        } catch (SQLException se) {
            throw new SQLException(se);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                throw new SQLException(se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                throw new SQLException(se);
            }
        }
    }

    /**
     * getClientsDetails
     *
     * @return files - Lista com username e password
     * @throws java.sql.SQLException
     * @throws Exception
     */
    public List<String>[] getClientsDetails() throws SQLException, Exception {
        Connection conn = null;
        Statement stmt = null;
        List<String>[] details = (ArrayList<String>[]) new ArrayList[2];
        details[0] = new ArrayList<>();
        details[1] = new ArrayList<>();
        try {
            String sql;

            //Abrir a Conexao
            conn = DriverManager.getConnection(URL_BD, UTILIZADOR, SENHA);
            //Criar a query
            sql = "SELECT username, password FROM clients";
            //Executar a query
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //Extrair informações do resultado da query
            while (rs.next()) {
                //Recebe uma linha que representa um ficheiro de um cliente
                if (!rs.getBoolean("islogged")) {
                    details[0].add(rs.getString("username"));
                    details[1].add(rs.getString("password"));
                }
            }

            rs.close();
            return details;
        } catch (SQLException se) {
            throw new SQLException(se);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                throw new SQLException(se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                throw new SQLException(se);
            }
        }
    }

    /**
     * getDownloads
     *
     * @return files - Lista downloads(up e down) de um client
     * @throws java.sql.SQLException
     * @throws Exception
     */
    public List<Download> getDownloads(String ClientAddr) throws SQLException, Exception {
        Connection conn = null;
        Statement stmt = null;
        List<Download> downloads = new ArrayList<>();
        try {
            String sql;

            //Abrir a Conexao
            conn = DriverManager.getConnection(URL_BD, UTILIZADOR, SENHA);
            //Criar a query
            sql = "SELECT * FROM DOWNLOADS";
            //Executar a query
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //Extrair informações do resultado da query
            while (rs.next()) {
                //Recebe uma linha que representa um download
                if (rs.getString("client_up").equals(ClientAddr) || rs.getString("client_down").equals(ClientAddr)) {
                    Calendar time = GregorianCalendar.getInstance();
                    time.setTime(rs.getDate("date"));
                    downloads.add(new Download(rs.getString("filename"), rs.getString("client_up"), rs.getString("client_down"), time));
                }
            }

            rs.close();
            return downloads;
        } catch (SQLException se) {
            throw new SQLException(se);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                throw new SQLException(se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                throw new SQLException(se);
            }
        }
    }

    /**
     * addClient
     *
     * @param newClient - um cliente a Adicionar
     * @return sucesso da operação
     * @throws java.sql.SQLException
     * @throws Exception
     */
    public boolean addClient(InitClient newClient) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        int clientId = -1;
        try {
            String sql;
            //Abrir a Conexao
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(URL_BD, UTILIZADOR, SENHA);
            //Criar a Query
            sql = "INSERT INTO clients (client_addr,active)" + " VALUES(" + newClient.toStringSQL() + ")";

            clientId = this.getClientId(newClient.getClientAddr());
            if (clientId == -1) {
                return false;
            }
            List<String>[] ficheiros = newClient.getFicheiros();
            int i = 0;
            for (String name : ficheiros[0]) {
                sql += "INSERT INTO files (clients_idclients,name,size)" + "VALUES(" + clientId + "," + newClient.getClientAddr() + "," + name + "," + ficheiros[1].get(i) + ")";
                i++;
            }
            sql += ";";
            stmt = conn.prepareStatement(sql);
            //Substitui o "?" pelo estado
            //stmt.setInt(1, tarefa.getEstado());
            stmt.executeUpdate();
            return true;
        } catch (SQLException se) {
            throw new SQLException(se);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                throw new SQLException(se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                throw new SQLException(se);
            }
        }
    }

    /**
     * addDownload
     *
     * @param download - um download a adicionar
     * @return sucesso da operação
     * @throws java.sql.SQLException
     * @throws Exception
     */
    public boolean addDownload(Download download) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        int clientId = -1;
        int FileId = -1;
        try {
            String sql;
            //Abrir a Conexao
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(URL_BD, UTILIZADOR, SENHA);

            clientId = this.getClientId(download.getClient_up());
            if (clientId == -1) {
                return false;
            }
            //Criar a Query
            sql = "INSERT INTO DOWNLOADS (files_clients_idclients,name,client_up,client_down,date)"
                    + "VALUES(" + clientId + "," + download.getFile() + "," + download.getClient_up() + ","
                    + download.getClient_down() + ",?);";
            stmt = conn.prepareStatement(sql);
            //Substitui o "?" pela data
            stmt.setDate(1, new Date(download.getTime().getTimeInMillis()));
            stmt.executeUpdate();
            return true;
        } catch (SQLException se) {
            throw new SQLException(se);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                throw new SQLException(se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                throw new SQLException(se);
            }
        }
    }

    /**
     * resetClientUDP
     *
     * @param clientAddr - String da Addr de um cliente que se quer os ficheiros
     * @throws java.sql.SQLException
     * @throws Exception
     */
    public void resetClientUDP(String clientAddr) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            String sql;
            //Abrir a Conexao
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(URL_BD, UTILIZADOR, SENHA);
            //Criar a Query
            sql = "UPDATE clients SET counter_udp = 3 WHERE client_addr = ?";
            stmt = conn.prepareStatement(sql);

            //Substitui o "?" pelo ClienteAddress
            stmt.setString(1, clientAddr);
            stmt.executeUpdate();
            return;
        } catch (SQLException se) {
            throw new SQLException(se);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                throw new SQLException(se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                throw new SQLException(se);
            }
        }
    }

    /**
     * badClientUDP
     *
     * @param clientAddr - String da Addr de um cliente que se quer os ficheiros
     * @throws java.sql.SQLException
     * @throws Exception
     */
    public int badClientUDP(String clientAddr) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        Statement stmtGet = null;
        int counter_udp = 0;
        try {
            String sql;
            //Abrir a Conexao
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(URL_BD, UTILIZADOR, SENHA);
            sql = "SELECT * FROM clients";
            //Executar a query
            stmtGet = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (rs.getString("client_addr").compareTo(clientAddr) == 0) {
                    counter_udp = rs.getInt("counter_udp");
                }
                break;
            }
            counter_udp--;
            //Criar a Query
            sql = "UPDATE clients SET counter_udp = ? WHERE client_addr = ?";
            stmt = conn.prepareStatement(sql);

            //Substitui o "?" pelo counter_udp
            stmt.setInt(1, counter_udp);
            stmt.executeUpdate();
            return counter_udp;
        } catch (SQLException se) {
            throw new SQLException(se);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                throw new SQLException(se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                throw new SQLException(se);
            }
        }
    }

    /**
     * removeClient
     *
     * @param clientAddr - String da Addr de um cliente que se quer os ficheiros
     * @throws java.sql.SQLException
     * @throws Exception
     */
    public void removeClient(String clientAddr) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            String sql;
            //Abrir a Conexao
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(URL_BD, UTILIZADOR, SENHA);

            //Criar a Query
            sql = "DELETE FROM clients WHERE client_addr = ?";
            stmt = conn.prepareStatement(sql);
            //Substitui o "?" pelo clientAddr
            stmt.setString(1, clientAddr);
            stmt.executeUpdate();
            return;
        } catch (SQLException se) {
            throw new SQLException(se);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                throw new SQLException(se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                throw new SQLException(se);
            }
        }
    }

}
