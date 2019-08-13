
package varausjarjestelma;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/*
 * @author MarkusM
 */
@Component
public class HuoneDao implements Dao<Huone, Integer> {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    VarausDao varausDao;
    
    @Override
    public void create(Huone huone) throws SQLException {
        jdbcTemplate.update("INSERT INTO Huone(numero, tyyppi, paivahinta) "
                + "VALUES(?, ?, ?)", huone.getNumero(), huone.getTyyppi(),
                huone.getPaivahinta());
    }

    @Override
    public Huone read(Integer numero) throws SQLException {
        Huone huone = jdbcTemplate.queryForObject("SELECT * FROM Huone "
                + "WHERE numero = ?", new BeanPropertyRowMapper<> (Huone.class), numero);
        return huone;
    }

    @Override
    public Huone update(Huone huone) throws SQLException {
        jdbcTemplate.update("UPDATE Huone SET numero = ?, tyyppi = ?, paivahinta = ? "
                + "WHERE numero = ?", huone.getNumero(), huone.getTyyppi(), 
                huone.getPaivahinta(), huone.getNumero());
        return huone;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        jdbcTemplate.update("DELETE FROM Huone WHERE numero = ?", key);
    }

    @Override
    public List<Huone> list() throws SQLException {
        List<Huone> huoneet = jdbcTemplate.query("SELECT * FROM Huone", (rs, rowNum) ->
        new Huone(rs.getInt("numero"), rs.getString("tyyppi"), rs.getInt("paivahinta")));
        
        return huoneet;
    }
    
    public List<Huone> listKyseisenVarauksenHuoneet(int varausid) throws SQLException {
        List<Huone> huoneet = jdbcTemplate.query("SELECT numero, tyyppi, paivahinta FROM Huone "
                + "JOIN Huonevaraus ON Huonevaraus.huonenumero = Huone.numero "
                + "JOIN Varaus ON Varaus.id = Huonevaraus.varausid "
                + "WHERE varausid = ?", (rs, rowNum) -> new Huone(rs.getInt("numero"), 
                        rs.getString("tyyppi"), rs.getInt("paivahinta")), varausid);
        
        return huoneet;
    }
    
    public List<Huone> haeHuoneita(LocalDateTime haluttuAlku, LocalDateTime haluttuLoppu, String haluttuTyyppi, String maxHinta) throws SQLException, Exception {
        List<Varaus> varaukset = varausDao.list();
        int varaustenLkm = varaukset.size();
        List<Integer> varattujenHuoneidenNrot = new ArrayList<>();

        for (int i = 0; i < varaustenLkm; i++) {
            // Lisätään alkuun 16 ja loppuun 10 tuntia, koska kannasta tullessa tunnit nollilla.
            // Ja yksi minuutti, jotta ei tarvita equals ehtoa ehtolauseeseen
            LocalDateTime alku = varaukset.get(i).getAlkupvm().plusHours(16).plusMinutes(1);
            LocalDateTime loppu = varaukset.get(i).getLoppupvm().plusHours(10).plusMinutes(1);

            if((haluttuAlku.isBefore(alku) && haluttuLoppu.isAfter(alku) && (haluttuLoppu.isBefore(loppu) || haluttuLoppu.isAfter(loppu))) ||
                (haluttuAlku.isAfter(alku) && haluttuAlku.isBefore(loppu))) {
                varattujenHuoneidenNrot.add(varaukset.get(i).getHuonenro());
            }
        }
        
        List<Integer> vapaidenHuoneidenNrot = poistetaanVaratutHuoneetListasta(varattujenHuoneidenNrot);

        if(vapaidenHuoneidenNrot.isEmpty()) {
            System.out.println("Yhtään huonetta ei ole vapaana.");
            return new ArrayList<>();
        } else {
            // Luetaan vapaiden huoneiden numeroiden listan perusteella kannasta kaikki vapaat
            // Huoneet listaan.
            List<Huone> vapaatHuoneet = new ArrayList<>();
            vapaidenHuoneidenNrot.forEach(numero -> {
                try {
                    Huone huone = read(numero);
                    String tyyppi = huone.getTyyppi();
                    int hinta = huone.getPaivahinta();
                    // Tarkistetaan rajausehdot
                    if(haluttuTyyppi.equals("") && maxHinta.equals("")) {
                        vapaatHuoneet.add(huone);
                    } else if(haluttuTyyppi.equalsIgnoreCase(tyyppi) && maxHinta.equals("")) {
                        vapaatHuoneet.add(huone);
                    } else if(haluttuTyyppi.equals("") && hinta <= Integer.valueOf(maxHinta)) {
                        vapaatHuoneet.add(huone);
                    } else if(haluttuTyyppi.equalsIgnoreCase(tyyppi) && hinta <= Integer.valueOf(maxHinta)) {
                        vapaatHuoneet.add(huone);
                    } 
                } catch (SQLException ex) {
                    Logger.getLogger(HuoneDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            if(vapaatHuoneet.isEmpty()) {
                System.out.println("Yhtään huonetta ei ole vapaana.");
                return new ArrayList<>();
            } else {
                List<Huone> vapaatHuoneetHalvimmastaKalleimpaan = vapaatHuoneet.stream()
                    .sorted()
                    .collect(Collectors.toCollection(ArrayList::new));
                return vapaatHuoneetHalvimmastaKalleimpaan;
            }
        }  
    }
    
    public List<Integer> poistetaanVaratutHuoneetListasta(List<Integer> varattujenHuoneidenNrot) throws Exception {
        // Kaikki Huoneet kannasta listaan.
        List<Huone> kaikkiHuoneet = list();
        // Kaikkien Huoneiden numerot listaan.
        List<Integer> kaikkienHuoneidenNrot = new ArrayList<>();
        kaikkiHuoneet.forEach((huone) -> {
            kaikkienHuoneidenNrot.add(huone.getNumero());
        });
        // Poistetaan kaikkien huoneiden numeroiden listasta varattujen huoneiden numerot.
        varattujenHuoneidenNrot.forEach((numero -> {
            if(kaikkienHuoneidenNrot.contains(numero)) {
                kaikkienHuoneidenNrot.remove(numero);
            }
        }));
        
        return kaikkienHuoneidenNrot;
    }

}
