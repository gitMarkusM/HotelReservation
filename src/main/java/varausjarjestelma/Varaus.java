
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
    private int huonenro;
    private Asiakas asiakas;
    private Huone huone;

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
        this.huonenro = rs.getInt("numero");
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

    public int getHuonenro() {
        return huonenro;
    }

    public void setHuonenro(int huonenro) {
        this.huonenro = huonenro;
    }
    
}
