import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("Conexion exitosa a la base de datos");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage()); 
        }
    }

}
