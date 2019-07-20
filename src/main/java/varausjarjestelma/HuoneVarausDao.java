
package varausjarjestelma;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/*
 * @author MarkusM
 */
@Component
public class HuoneVarausDao implements Dao<HuoneVaraus, Integer> {
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    public void create(HuoneVaraus huoneVaraus) throws SQLException {
//        jdbcTemplate.update("INSERT INTO HuoneVaraus(huonenumero, varausid )"
//                + "VALUES(?,?)", huoneVaraus.getHuonenro(0), huoneVaraus.getVarausId());
    }
    
    public void createFromList(HuoneVaraus huoneVaraus, int lkm) throws SQLException {
        for (int i = 0; i < lkm; i++) {
            jdbcTemplate.update("INSERT INTO HuoneVaraus(huonenumero, varausid )"
                + "VALUES(?,?)", huoneVaraus.getHuonenro(i), huoneVaraus.getVarausId());
        }    
    }

    @Override
    public HuoneVaraus read(Integer varausid) throws SQLException {
        HuoneVaraus huoneVaraus = jdbcTemplate.queryForObject("SELECT * FROM Huonevaraus "
                + "WHERE varausid = ?", new BeanPropertyRowMapper<> (HuoneVaraus.class), varausid);
        return huoneVaraus;
    }

    @Override
    public HuoneVaraus update(HuoneVaraus huoneVaraus) throws SQLException {
//        jdbcTemplate.update("UPDATE Huonevaraus SET huone_numero = ?,"
//                + "varaus_id = ?", huoneVaraus.getHuonenro(), huoneVaraus.getVarausId());
        return huoneVaraus;
    }

    @Override
    public void delete(Integer huonenumero) throws SQLException {
        jdbcTemplate.update("DELETE FROM Huonevaraus WHERE huonenumero = ?", huonenumero);
    }

    @Override
    public List<HuoneVaraus> list() throws SQLException {
//        List<HuoneVaraus> huonevaraukset = jdbcTemplate.query("SELECT huonenumero, varausid "
//                + "FROM Huonevaraus", (rs, rowNum) -> new HuoneVaraus(rs.getInt("huonenumero"),
//                rs.getInt("varausid")));
//        return huonevaraukset;
        return null;
    }

}
