
package varausjarjestelma;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

/*
 * @author MarkusM
 */
@Component
public class VarausDao implements Dao<Varaus, Integer> { 
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    AsiakasDao asiakasDao;
    
    @Autowired
    HuoneDao huoneDao;
    
    @Autowired
    HuoneVarausDao huoneVarausDao;
    
    @Override
    public void create(Varaus varaus) throws SQLException {
//        jdbcTemplate.update("INSERT INTO Varaus(alkupvm, loppupvm, asiakas_id) "
//                + "VALUES(?,?,?,?)", varaus.getAlkupvm(), 
//                varaus.getLoppupvm(), varaus.getAsiakasId());
    }

    @Override
    public Varaus read(Integer key) throws SQLException {
        Varaus varaus = jdbcTemplate.queryForObject("SELECT * FROM Varaus "
                + "WHERE id = ?", 
                new BeanPropertyRowMapper<> (Varaus.class), key);
        return varaus;
    }

    @Override
    public Varaus update(Varaus varaus) throws SQLException {
        jdbcTemplate.update("UPDATE Varaus SET alkupvm = ?, loppupvm = ? "
                + "WHERE asiakasid = ?",
                varaus.getAlkupvm(), varaus.getLoppupvm(), varaus.getAsiakasId());
        return varaus;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        jdbcTemplate.update("DELETE FROM Varaus WHERE id = ?", key);
    }

    @Override
    public List<Varaus> list() throws SQLException {
        List<Varaus> varaukset = jdbcTemplate.query("SELECT id, asiakasid, "
                + "alkupvm, loppupvm FROM Varaus", (rs, rowNum) ->
                new Varaus(rs));  
        return varaukset;
    }
    
    public Integer createAndReturnKey(Varaus varaus) throws SQLException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO Varaus(alkupvm, loppupvm, asiakasid) VALUES(?,?,?)", 
                    Statement.RETURN_GENERATED_KEYS);  
            stmt.setDate(1, LocalDateTimeToSqlDate(varaus.getAlkupvm()));
            stmt.setDate(2, LocalDateTimeToSqlDate(varaus.getLoppupvm()));
            stmt.setInt(3, varaus.getAsiakasId());
            return stmt;
        }, keyHolder);
        
        int id = keyHolder.getKey().intValue();

        return id;
    }
    
    public List<Varaustiedot> listaaVaraustiedot() throws SQLException {
        List<Varaustiedot> varaustiedot = jdbcTemplate.query("SELECT "
                + "Asiakas.nimi, Asiakas.puhelinnro, Asiakas.email, Varaus.alkupvm, "
                + "Varaus.loppupvm, Huone.numero, Huone.tyyppi, Huone.paivahinta "
                + "FROM Varaus JOIN Asiakas ON Asiakas.id = Varaus.asiakasid JOIN "
                + "Huonevaraus ON Huonevaraus.varausid = Varaus.id JOIN Huone ON "
                + "Huone.numero = Huonevaraus.huonenumero", (rs, rowNum) -> 
                new Varaustiedot(rs));
        
        return varaustiedot;
    }
    
//    public void listaaVaraustiedot() throws SQLException {
//        List<Varaus> varaukset = list();
//        List<Asiakas> asiakkaat = asiakasDao.list(); // hashmap?
//        List<HuoneVaraus> huonevaraukset = huoneVarausDao.list();
//        
//        for (int i = 0; i < varaukset.size(); i++) {
//            for(Asiakas asiakas: asiakkaat) {
//                if(asiakas.getId() == varaukset.get(i).getAsiakasId()) {
//                    System.out.print(asiakas + ", ");
//                    System.out.println(varaukset.get(i) + " Huoneet:");
//                    
//                    for(HuoneVaraus hv: huonevaraukset) {
//                        if(hv.getVarausId() == varaukset.get(i).getId()) {
//                            int huoneNro = hv.getHuonenrot();
//                            Huone huone = huoneDao.read(huoneNro);
//                            System.out.println(huone);
//                            System.out.println("Yhteensä: ");
//                        }
//                    }   
//                }
//            }
//        }
//    }
    
    public Date LocalDateTimeToSqlDate(LocalDateTime ldt) {     
        LocalDate ld = ldt.toLocalDate();
        Date date = Date.valueOf(ld);
        return date;
    }

}
