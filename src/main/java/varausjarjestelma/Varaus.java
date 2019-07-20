
package varausjarjestelma;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * @author MarkusM
 */
public class Varaus {
    private int id;
    private LocalDateTime alkupvm;
    private LocalDateTime loppupvm;
    private int asiakasId; //Integer

    public Varaus() {
    }
    
    public Varaus(LocalDateTime alkupvm, LocalDateTime loppupvm, int asiakasId) {
        this.alkupvm = alkupvm;
        this.loppupvm = loppupvm;
        this.asiakasId = asiakasId;
    }
    
    public Varaus(ResultSet rs) throws SQLException{
        this.id = rs.getInt(("id"));
        this.asiakasId = rs.getInt("asiakasid");
        this.alkupvm = LocalDateTime.parse(rs.getDate("alkupvm") + " " + "16:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.loppupvm = LocalDateTime.parse(rs.getDate("loppupvm") + " " + "10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getAlkupvm() {
        return alkupvm;
    }

    public void setAlkupvm(LocalDateTime alkupvm) {
        this.alkupvm = alkupvm;
    }

    public LocalDateTime getLoppupvm() {
        return loppupvm;
    }

    public void setLoppupvm(LocalDateTime loppupvm) {
        this.loppupvm = loppupvm;
    }

    public Integer getAsiakasId() {
        return asiakasId;
    }
    
    public void setAsiakasId(Integer asiakasId) {
        this.asiakasId = asiakasId;
    }
    
//    public int laskeKestoPaivina(LocalDateTime alkupvm, LocalDateTime loppupvm) {
//        LocalDate ldAlkupvm = alkupvm.toLocalDate();
//        LocalDate ldLoppupvm = loppupvm.toLocalDate();
//        int kestoPaivina = 0;
//        
//        while(!ldAlkupvm.equals(ldLoppupvm)) {
//            ldAlkupvm = ldAlkupvm.plusDays(1);
//            kestoPaivina++;
//        }
//        
//        return kestoPaivina;
//    }
//    
//    @Override
//    public String toString() {
//        return alkupvm + ", " + loppupvm + ", " + laskeKestoPaivina(alkupvm, loppupvm) + 
//                " päivää, 1 huone.";
//    }
    
}
