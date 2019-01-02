
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataBaseConnect {

    // Nome do Driver JDBC e Endereco URL da Base de Dados
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public String SERVIDOR;
    static final String NOME_BD = "pd_1819";
    public String URL_BD;
    // Credenciais da Base de Dados
    static final String UTILIZADOR = "root";
    static final String SENHA = "pd-1819";

    /*dataBaseAddr deve ser assim
    * = "localhost:3306/NOME_BD";
    * = "jdbc:mysql://" + SERVIDOR + "/" + NOME_BD;
     */
    public DataBaseConnect(String dataBaseAddr) throws ClassNotFoundException {
        //Registar o Driver JDBC
        SERVIDOR = dataBaseAddr;
        URL_BD = "jdbc:mysql://" + SERVIDOR;
        Class.forName(JDBC_DRIVER);

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
    public String getClientAddr(String file) throws SQLException, Exception {
        Connection conn = null;
        Statement stmt = null;
        String clientAddr = "";
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
                //Recebe uma linha que representa um ficheiro de um cliente
                if (rs.getString("name").compareTo(file) == 0) {
                    clientAddr = rs.getString("client_addr");
                    if (clientAddr.isEmpty()) {
                        //Error
                    }
                }
            }
            rs.close();
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
        try {
            String sql;
            //Abrir a Conexao
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(URL_BD, UTILIZADOR, SENHA);
            //Criar a Query
            sql = "INSERT INTO clients (client_addr,active)" + " VALUES(" + newClient.toStringSQL() + ")";
            for (String str : newClient.getFicheiros()) {
                sql += "INSERT INTO files (client_addr,name)" + "VALUES(" + newClient.getClientAddr() + "," + str + ")";
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
     * getClientFiles
     *
     * @param clientAddr - String da Addr de um cliente que se quer os ficheiros
     * @return files - Lista dos nomes dos ficheiros
     * @throws java.sql.SQLException
     * @throws Exception
     */
    public List<String> getClientFiles(String clientAddr) throws SQLException, Exception {
        Connection conn = null;
        Statement stmt = null;
        List<String> files = new ArrayList<>();
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
                //Recebe uma linha que representa um ficheiro de um cliente
                if (rs.getString("client_addr").compareTo(clientAddr) == 0) {
                    files.add(rs.getString("name"));
                    if (files.isEmpty()) {
                        //Error
                    }
                }
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
     * getClientFiles
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
        List<String>[] details = null;
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
}
