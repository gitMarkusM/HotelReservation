
package varausjarjestelma;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    
    @Override
    public void create(Varaus varaus) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        + "alkupvm, loppupvm FROM Varaus ", (rs, rowNum) ->
        new Varaus(rs));
            
        for(Varaus varaus: varaukset) {
            varaus.setHuoneet(huoneDao.huoneetToListByVarausid(varaus.getId()));
        }
        
        return varaukset;
    }
    
    public List<Varaus> listVarauksetByAsiakasID(Integer asiakasID) throws SQLException {
        List<Varaus> varaukset = jdbcTemplate.query("SELECT id, asiakasid, alkupvm, "
                + "loppupvm FROM Varaus WHERE asiakasid = ?", (rs, rowNum) -> 
                        new Varaus(rs), asiakasID);
        for(Varaus varaus: varaukset) {
            varaus.setHuoneet(huoneDao.huoneetToListByVarausid(varaus.getId()));
        }
        
        return varaukset;
    }
    
    public void luoHuonevaraus(Map<Integer, List<Huone>> huonevaraukset) throws SQLException {
        Integer key = 0;
        for (Iterator<Integer> it = huonevaraukset.keySet().iterator(); it.hasNext();) {
            key = it.next();
        }
        List<Huone> huoneet = huonevaraukset.get(key);
        for (int i = 0; i < huoneet.size(); i++) {
            jdbcTemplate.update("INSERT INTO Huonevaraus(varausid, huonenumero) VALUES(?, ?)",
                    key, huoneet.get(i).getNumero());
        }  
    }
    
////    public Map<Integer, List<Huone>> mapHuonevaraukset() {
////        Map<Integer, List<Huone>> huonevaraukset = new HashMap<>();
////        List<String> huonevarauksetList = jdbcTemplate.query("SELECT * FROM Huonevaraus",
////                (rs, rowNum) -> rs.getString("varausid") + " " + rs.getString("huonenumero"));
////        
////        huonevarauksetList.forEach(mj -> {
////            String palat[] = mj.split(" ");
////            int varausid = Integer.valueOf(palat[0]);
////            int huonenumero = Integer.valueOf(palat[1]);
////            Huone huone = new Huone();
////            try {
////                huone = huoneDao.read(huonenumero);
////            } catch (SQLException ex) {
////                Logger.getLogger(VarausDao.class.getName()).log(Level.SEVERE, null, ex);
////            }
////            huonevaraukset.putIfAbsent(varausid, new ArrayList<>());
////            huonevaraukset.get(varausid).add(huone);
////        });
////        
////        return huonevaraukset;
////    }
     
    public Date LocalDateTimeToSqlDate(LocalDateTime ldt) {     
        LocalDate ld = ldt.toLocalDate();
        Date date = Date.valueOf(ld);
        return date;
    }
    
}
