package be.vdab;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
/*
Autonumber kolommen.
Voorbeeld: een soort toevoegen
*/
public class Vb10_1 {
    private static final String URL = "jdbc:mysql://localhost/tuincentrum?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String INSERT_SOORT = "insert into soorten(naam) values (?)";
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Naam: ");
            String naam = scanner.nextLine();
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    // Je vermeldt RETURN_GENERATED_KEYS waar je het insert statement specifieert;
                    PreparedStatement statement = connection.prepareStatement(INSERT_SOORT, Statement.RETURN_GENERATED_KEYS)) {
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                connection.setAutoCommit(false);
                statement.setString(1, naam);
                // Voert het insert statement uit.
                statement.executeUpdate();
                /*
                De method getGeneratedKeys geeft een ResultSet met de autonumber waarde.
                Je vermeldt ook deze ResultSet binnen de ronde haakjes van een try blok.
                Zo wordt hij automatisch gesloten na gebruik.
                */
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    // Plaatst je op de eerste ResultSet rij met de next method.
                    resultSet.next();
                    /*
                    Leest de inhoud van de eerste kolom. 
                    Je spreekt de kolom aan met zijn volgnummer omdat ze geen naam heeft.
                    De kolom inhoud is de autonumber waarde.
                    */
                    System.out.println(resultSet.getLong(1));
                    connection.commit();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
