
package varausjarjestelma;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * @author MarkusM
 */

public class Varaustiedot {
    private String nimi;
    private String puhelinnro;
    private String email;
    private LocalDateTime alkupvm;
    private LocalDateTime loppupvm;
    private int huoneenNro;
    private String huoneenTyyppi;
    private int paivahinta;

    public Varaustiedot(ResultSet rs) throws SQLException{
        this.nimi = rs.getString("nimi");
        this.puhelinnro = rs.getString("puhelinnro");
        this.email = rs.getString("email");
        this.alkupvm = LocalDateTime.parse(rs.getDate("alkupvm") + " " + "16:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.loppupvm = LocalDateTime.parse(rs.getDate("loppupvm") + " " + "10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.huoneenNro = rs.getInt("numero");
        this.huoneenTyyppi = rs.getString("tyyppi");
        this.paivahinta = rs.getInt("paivahinta");
    }
    
    public int kestoPaivina() {
        LocalDate ldAlkupvm = this.alkupvm.toLocalDate();
        LocalDate ldLoppupvm = this.loppupvm.toLocalDate();
        int kestoPaivina = 0;
        
        while(!ldAlkupvm.equals(ldLoppupvm)) {
            ldAlkupvm = ldAlkupvm.plusDays(1);
            kestoPaivina++;
        }
        
        return kestoPaivina;
    }
    
    public int huoneidenLkm() {
        
        return 1;
    }
    
    public int yhteensa() {
        int yhteensa = this.paivahinta * kestoPaivina();
        return yhteensa;
    }
    
    @Override
    public String toString() {
        return nimi + ": " + puhelinnro + ", " + email + "\n" +
                this.alkupvm + " - " + this.loppupvm + ", Päiviä: " + kestoPaivina() + ", " + huoneidenLkm() + " Huone\n" +
                "Huoneet:\n" +
                "\t" + huoneenTyyppi + ", " + huoneenNro + ", " + paivahinta + "\n" +
                "Yhteensä: " + yhteensa() + " euroa.";
    }
}
