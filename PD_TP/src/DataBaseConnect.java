
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
    static final String SERVIDOR = "localhost:3306";
    static final String NOME_BD = "pd_1819";
    static final String URL_BD = "jdbc:mysql://" + SERVIDOR + "/" + NOME_BD;
    // Credenciais da Base de Dados
    static final String UTILIZADOR = "root";
    static final String SENHA = "pd-1819";

    public DataBaseConnect() throws ClassNotFoundException {
        //Registar o Driver JDBC
        Class.forName(JDBC_DRIVER);
    }

    /**
     * obterTarefas - Se tiver historico(1), devolve todas as tarefas. - Senão
     * tiver historico(0), devolve só as tarefas entregues.
     *
     * @param file - identifica o ficheiro.
     * @return uma lista de tarefas.
     * @throws java.sql.SQLException
     */
    public static String getClientAddr(String file) throws SQLException, Exception {
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
    public static boolean addClient(InitClient newClient) throws SQLException, Exception {
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

    public static List<String> getClientFiles(String clientAddr) throws SQLException, Exception {
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
}
