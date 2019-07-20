package varausjarjestelma;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VarausjarjestelmaSovellus implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(VarausjarjestelmaSovellus.class);
//        alustaTietokanta();
    }

    @Autowired
    Tekstikayttoliittyma tekstikayttoliittyma;

    @Override
    public void run(String... args) throws Exception {
        Scanner lukija = new Scanner(System.in);
        tekstikayttoliittyma.kaynnista(lukija);
    }
    
    private static void alustaTietokanta() {
        try(Connection conn = DriverManager.getConnection("jdbc:h2:./hotelliketju", "sa", "")) {
            conn.prepareStatement("DROP TABLE Huone, Asiakas, Varaus, Lisavaruste, Huonevaraus, Lisavarustevaraus IF EXISTS;").executeUpdate();
            conn.prepareStatement("CREATE TABLE Huone(numero Integer Primary key, tyyppi VarChar(25), paivahinta Integer);").executeUpdate();
            conn.prepareStatement("CREATE TABLE Asiakas(id Integer auto_increment Primary key, nimi VarChar(30), puhelinnro VarChar(15), email VarChar(35));").executeUpdate();
            conn.prepareStatement("CREATE TABLE Varaus(id Integer auto_increment Primary key, asiakasid Integer, huonenumero Integer, alkupvm Date,loppupvm Date DEFAULT '2019-07-04', Foreign key(asiakasid) References Asiakas(id));").executeUpdate();
            conn.prepareStatement("CREATE TABLE Lisavaruste(id Integer auto_increment Primary key, varausid Integer, nimi VarChar(30), paivahinta Integer)").executeUpdate();
            conn.prepareStatement("CREATE TABLE Huonevaraus(huoneNumero Integer, varausid Integer, Foreign key(huonenumero) References Huone(numero), Foreign key(varausid) References Varaus(id))").executeUpdate();
            conn.prepareStatement("CREATE TABLE Lisavarustevaraus(lisavarusteid Integer, varausid Integer, Foreign key(lisavarusteid) References Lisavaruste(id), Foreign key(varausid) References Varaus(id))").executeUpdate();
            
            conn.prepareStatement("INSERT INTO Huone(numero, tyyppi, paivahinta) VALUES(101, 'Standard', 100)").executeUpdate();
            conn.prepareStatement("INSERT INTO Huone(numero, tyyppi, paivahinta) VALUES(102, 'Standard', 100)").executeUpdate();
            conn.prepareStatement("INSERT INTO Huone(numero, tyyppi, paivahinta) VALUES(103, 'Standard', 100)").executeUpdate();
            conn.prepareStatement("INSERT INTO Huone(numero, tyyppi, paivahinta) VALUES(201, 'Commodore', 150)").executeUpdate();
            conn.prepareStatement("INSERT INTO Huone(numero, tyyppi, paivahinta) VALUES(202, 'Commodore', 150)").executeUpdate();
            conn.prepareStatement("INSERT INTO Huone(numero, tyyppi, paivahinta) VALUES(203, 'Commodore', 150)").executeUpdate();
            conn.prepareStatement("INSERT INTO Huone(numero, tyyppi, paivahinta) VALUES(301, 'Excelsior', 200)").executeUpdate();
            conn.prepareStatement("INSERT INTO Huone(numero, tyyppi, paivahinta) VALUES(302, 'Excelsior', 200)").executeUpdate();
            conn.prepareStatement("INSERT INTO Huone(numero, tyyppi, paivahinta) VALUES(303, 'Excelsior', 200)").executeUpdate();
            
//            conn.prepareStatement("INSERT INTO Asiakas(nimi, puhelinnro, email) VALUES()").executeUpdate();
//            conn.prepareStatement("INSERT INTO Asiakas(nimi, puhelinnro, email) VALUES()").executeUpdate();
//            conn.prepareStatement("INSERT INTO Asiakas(nimi, puhelinnro, email) VALUES()").executeUpdate();
//            conn.prepareStatement("INSERT INTO Asiakas(nimi, puhelinnro, email) VALUES()").executeUpdate();
//            
//            conn.prepareStatement("INSERT INTO Varaus() VALUES()").executeUpdate();
//            conn.prepareStatement("INSERT INTO Varaus() VALUES()").executeUpdate();
//            conn.prepareStatement("INSERT INTO Varaus() VALUES()").executeUpdate();
//            conn.prepareStatement("INSERT INTO Varaus() VALUES()").executeUpdate();
//            conn.prepareStatement("INSERT INTO Varaus() VALUES()").executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(VarausjarjestelmaSovellus.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
