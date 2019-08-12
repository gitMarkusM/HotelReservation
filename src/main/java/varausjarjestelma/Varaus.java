
package varausjarjestelma;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @author MarkusM
 */
public class Varaus {
    private int id;
    private LocalDateTime alkupvm;
    private LocalDateTime loppupvm;
    private int asiakasId;
    private int huonenro;
    private Asiakas asiakas;
    private Huone huone;
    private Map<Integer, List<Huone>> huonevaraukset;
//    private int varaustenLkm;

    public Varaus() {
    }
    
    public Varaus(LocalDateTime alkupvm, LocalDateTime loppupvm, int asiakasId) {
        this.alkupvm = alkupvm;
        this.loppupvm = loppupvm;
        this.asiakasId = asiakasId;
        this.huonevaraukset = new HashMap<>();
    }
    
    public Varaus(ResultSet rs) throws SQLException{
        this.id = rs.getInt(("id"));
        this.asiakasId = rs.getInt("asiakasid");
        this.alkupvm = LocalDateTime.parse(rs.getDate("alkupvm") + " " + "16:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.loppupvm = LocalDateTime.parse(rs.getDate("loppupvm") + " " + "10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.huonenro = rs.getInt("numero");
    }
    
    public Varaus(ResultSet rs, boolean b) throws SQLException{
        this.asiakas = new Asiakas(rs.getString("nimi"), rs.getString("puhelinnro"), rs.getString("email"));
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

    public int getHuonenro() {
        return this.huonenro;
    }

    public void setHuonenro(int huonenro) {
        this.huonenro = huonenro;
    }

    public Map<Integer, List<Huone>> getHuonevaraukset() {
        return huonevaraukset;
    }

    public void setHuonevaraukset(Map<Integer, List<Huone>> huonevaraukset) {
        this.huonevaraukset = huonevaraukset;
    }
    
    public Map<Integer, List<Huone>> luoHuonevaraus(int varausid, int varattavienLkm, List<Huone> vapaatHuoneet) {
        this.huonevaraukset.put(varausid, new ArrayList<>());
        
        for (int i = 0; i < varattavienLkm; i++) {
            if(this.huonevaraukset.containsKey(varausid)) {
                this.huonevaraukset.get(varausid).add(vapaatHuoneet.get(i));
            }
        }
        
        return this.huonevaraukset;
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
        int yhteensa = this.huone.getPaivahinta() * kestoPaivina();
        return yhteensa;
    }
    
    @Override
    public String toString() {
        return this.alkupvm + " - " + this.loppupvm + ", Päiviä: " + kestoPaivina() + 
                ", " + huoneidenLkm() + " Huone\n" + "Yhteensä: " + yhteensa() + " euroa.";
    }
    
}
