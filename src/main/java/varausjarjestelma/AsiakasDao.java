
package varausjarjestelma;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
public class AsiakasDao implements Dao<Asiakas, Integer> {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void create(Asiakas object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Asiakas read(Integer key) throws SQLException {
        Asiakas asiakas = jdbcTemplate.queryForObject("SELECT * FROM Asiakas "
                + "WHERE id = ?", new BeanPropertyRowMapper<> (Asiakas.class), key);
        return asiakas;
    }

    @Override
    public Asiakas update(Asiakas asiakas) throws SQLException {
        jdbcTemplate.update("UPDATE Asiakas SET nimi = ?, puhelinnro = ?, email = ?"
                + "WHERE id = ?",
                asiakas.getNimi(), asiakas.getPuhnro(), asiakas.getEmail(), asiakas.getId());
        return asiakas;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        jdbcTemplate.update("DELETE FROM Asiakas WHERE id = ?", key);
    }

    @Override
    public List<Asiakas> list() throws SQLException {
        List<Asiakas> asiakkaat = jdbcTemplate.query("SELECT id, nimi, puhelinnro, email "
                + "FROM Asiakas", (rs, rowNum) -> new Asiakas(rs));
        return asiakkaat;
    }
    
    public Integer createAndReturnKey(Asiakas asiakas) throws SQLException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO Asiakas(nimi, puhelinnro, email) VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, asiakas.getNimi());
                stmt.setString(2, asiakas.getPuhnro());
                stmt.setString(3, asiakas.getEmail());
                return stmt;
        }, keyHolder);
        
        int id = keyHolder.getKey().intValue();
        return id;
    }

}
