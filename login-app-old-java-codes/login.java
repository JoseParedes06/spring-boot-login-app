import com.mysql.cj.x.protobuf.MysqlxPrepare;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.Scanner;

public class login {

    private Scanner scanner = new Scanner(System.in);

    // ─── CRUD ──── (Create, Read, Update and Delete)
    private void crear(String usuario, String password) {
        // SQL Query
        String sql = ("INSERT INTO login_app.usuarios (USERNAME, PASSWORD) VALUES (?, ?)");
        // first check if the user already exists, if so return message that already exists
        if (existeUsuario(usuario)   ) {
            System.out.println("El usuario ya existe");
        } else {
            try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement createUser = connection.prepareStatement(sql)) {
                createUser.setString(1, usuario);
                createUser.setString(2, password);
                createUser.executeUpdate();
                System.out.println("Cuenta creada exitosamente");
            } catch (SQLException e) {
                System.out.println("Error al crear la cuenta" + e.getMessage());
            }
        }
    }
    private void actualizar(String usuarioActual, String passActual) {
        System.out.println("Que deseas Actualizar?");
        System.out.println("1: Nombre de usuario");
        System.out.println("2: Contraseña");
        System.out.println("3: Ambos");
        String opcion = scanner.nextLine().trim();
        if (opcion.equals("1")) {
            String userSql = "UPDATE usuarios SET USERNAME = ? WHERE USERNAME = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement updateUser = connection.prepareStatement(userSql)) {
                System.out.println("Nuevo nombre de usuario: ");
                String nuevoUsuario = scanner.nextLine();
                updateUser.setString(1, nuevoUsuario);
                updateUser.setString(2, usuarioActual);
                int rows = updateUser.executeUpdate();
                if (rows > 0) {
                    System.out.println("Usuario actualizado exitosamente");
                    sesionIniciada(nuevoUsuario, passActual);
                } else {
                    System.out.println("No se pudo actualizar");
                }
            } catch (SQLException e) {
                System.out.println("Error al actualizar el nombre de usuario" + e.getMessage());
            }
        }else if (opcion.equals("2")) {
            String passSql = "UPDATE usuarios SET PASSWORD = ? WHERE USERNAME = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement updatePass = connection.prepareStatement(passSql)) {
                System.out.println("Nueva contraseña: ");
                String nuevaPass = scanner.nextLine();
                updatePass.setString(1, nuevaPass);
                updatePass.setString(2,usuarioActual);
                int rows = updatePass.executeUpdate();
                if (rows > 0) {
                    System.out.println("Contraseña actualizada exitosamente");
                    sesionIniciada(usuarioActual, nuevaPass);
                } else {
                    System.out.println("No se pudo actualizar");
                }
            }catch (SQLException e) {
                System.out.println("Error al actualizar la contraseña" + e.getMessage());
            }
        } else {
            String bothSql = "UPDATE usuarios SET USERNAME = ?, PASSWORD = ? WHERE USERNAME = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement both = connection.prepareStatement(bothSql)) {
                System.out.println("Nuevo nombre de usuario: ");
                String nuevoUsuario = scanner.nextLine();
                System.out.println("Nueva contraseña: ");
                String nuevaPass = scanner.nextLine();
                both.setString(1, nuevoUsuario);
                both.setString(2, nuevaPass);
                both.setString(3, usuarioActual);
                int rows = both.executeUpdate();
                if (rows > 0) {
                    System.out.println("Cuenta actualizada exitosamente");
                    sesionIniciada(nuevoUsuario, nuevaPass);
                } else {
                    System.out.println("No se pudo actualizar");
                }
            }catch (SQLException e) {
                System.out.println("Error al actualizar tu cuenta" + e.getMessage());
            }
        }
    }
    private void borrar(String borrarUsuario, String borrarPass) {
        String sql = "DELETE FROM usuarios WHERE USERNAME = ? AND PASSWORD = ?";
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement borrarUser = connection.prepareStatement(sql)) {
            borrarUser.setString(1,borrarUsuario);
            borrarUser.setString(2,borrarPass);
            int rows = borrarUser.executeUpdate();
            if (rows > 0) {
                System.out.println("Cuenta borrada exitosamente");
            } else {
                System.out.println("Usuario o contraseña incorrectos");
            }
        } catch (SQLException e) {
            System.out.println("Error al borrar la cuenta" + e.getMessage());
        }
    }
    // SESIÓN
    private void iniciarSesion(String usuarioRecibido, String passRecibida) {
        String sql = "SELECT * FROM usuarios WHERE USERNAME = ? AND PASSWORD = ?";
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement iniciar = connection.prepareStatement(sql)) {
            iniciar.setString(1, usuarioRecibido);
            iniciar.setString(2, passRecibida);
            ResultSet rs = iniciar.executeQuery();
            if (rs.next()) {
                System.out.println("Bienvenido: " + usuarioRecibido + "!");
                sesionIniciada(usuarioRecibido, passRecibida);
            } else {
                System.out.println("Usuario o contraseña incorrectos");
            }
        }catch (SQLException e) {
            System.out.println("Error al iniciar sesion" + e.getMessage());
        }
    }
    //  HELPERS
    private boolean existeUsuario(String usuario) {
        String sql = "SELECT USERNAME FROM usuarios WHERE USERNAME = ?";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement checkUser = connection.prepareStatement(sql)) {

                checkUser.setString(1, usuario);
                ResultSet rs = checkUser.executeQuery();

                return rs.next();
            } catch (SQLException e) {
            System.out.println("Error al verificar si el usuario existe" + e.getMessage());
        }
        return false;
    }
    private boolean hayUsuarios() {
        String sql = "SELECT COUNT(*) FROM usuarios";
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement checkUser = connection.prepareStatement(sql)) {
            ResultSet rs = checkUser.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.out.println("Error al verificar si hay usuarios" + e.getMessage());
        }
        return false;
    }
    private void verUsuarios() {
        String sql = "SELECT * FROM usuarios";
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement ver = connection.prepareStatement(sql)) {
            ResultSet rs = ver.executeQuery();
            if (!rs.next()) {
                System.out.println("No hay usuarios");
            } else {
                int count = 0;
                do {
                    count++;
                    System.out.println("Usuario" + count + ": " + rs.getString("USERNAME"));
                } while (rs.next());
                System.out.println("Hay: " + count + " Total de usuarios: ");
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar si hay usuarios" + e.getMessage());
        }
    }
    // SESION INCIADA
    private void sesionIniciada(String usuario, String pass) {
        boolean enSesion = true;
        while (enSesion) {
            System.out.println("Hola! has Inciado sesion, como esto es un app de prueba, solo puedes elegir entre borrar tu cuenta o cerrar sesion");
            System.out.println("1: Cerrar sesion");
            System.out.println("2: Borrar cuenta");
            System.out.println("3: Actualizar usuario/contraseña");
            System.out.println("Porfavor elije una opcion (1-3)");
            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1":
                    System.out.println("Cerrando sesion...");
                    enSesion = false;
                    start();
                    break;
                case "2":
                    System.out.println("Hola! Vamos a borrar tu cuenta!");
                    System.out.println("Porfavor ingresa tu nombre y contraseña para poder borrar tu cuenta:");
                    System.out.println("Nombre de usuario: ");
                    String borrarUsuario = scanner.nextLine();
                    System.out.println("Contraseña: ");
                    String borrarPass = scanner.nextLine();

                    if (!borrarUsuario.equals(usuario)) {
                        System.out.println("Usuario incorrecto");
                        break;
                    } else {
                        borrar(borrarUsuario, borrarPass);
                        if (!existeUsuario(borrarUsuario)) {
                            enSesion = false;
                        }
                    }
                    break;
                case "3":
                    actualizar(usuario, pass);
                    enSesion = false;
                    break;
                default:
                    System.out.println("Opcion invalida: Elije desde el 1 al 2");
            }
        }
    }
    //  MENÚ PRINCIPAL
    private void start() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n===== MENÚ =====");
            System.out.println("1: Registrarse");
            System.out.println("2: Iniciar sesión");
            System.out.println("3: Cerrar sesión");
            System.out.println("4: Borrar cuenta");
            System.out.println("5: Ver lista de usuarios");
            System.out.println("0: Salir");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    System.out.println("Nombre de usuario: ");
                    String newUsuario = scanner.nextLine();
                    System.out.println("Contraseña: ");
                    String newPassword = scanner.nextLine();
                    crear(newUsuario, newPassword);
                    break;
                case "2":
                    System.out.println("Nombre de usuario: ");
                    String usuarioRecibido = scanner.nextLine();
                    System.out.println("Contraseña: ");
                    String passRecibida = scanner.nextLine();
                    iniciarSesion(usuarioRecibido, passRecibida);
                    break;
                case "3":
                    System.out.println("Session cerrada exitosamente!");
                    break;
                case "4":
                    if (!hayUsuarios()) {
                        System.out.println("No hay cuentas.");
                    } else {
                        System.out.println("Nombre de usuario: ");
                        String borrarUsuario = scanner.nextLine();
                        System.out.println("Contraseña: ");
                        String borrarPass = scanner.nextLine();
                        borrar(borrarUsuario, borrarPass);
                    }
                    break;
                case "5":
                    verUsuarios();
                    break;
                case "0":
                    System.out.println("¡Hasta luego!");
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida (0-5).");
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("Estoy en main!!");
        login app = new login();
        app.start();
    }
}
