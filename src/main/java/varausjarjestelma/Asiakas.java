
package varausjarjestelma;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * @author MarkusM
 */
public class Asiakas {
    private Integer id;
    private String nimi;
    private String puhelinnro;
    private String email;

    public Asiakas() {
    }

    public Asiakas(String nimi, String puhnro, String email) {
        this.nimi = nimi;
        this.puhelinnro = puhnro;
        this.email = email;
    }
    
    public Asiakas(ResultSet rs) throws SQLException {
        this.nimi = rs.getString("nimi");
        this.puhelinnro = rs.getString("puhelinnro");
        this.email = rs.getString("email");
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getPuhnro() {
        return puhelinnro;
    }

    public void setPuhnro(String puhnro) {
        this.puhelinnro = puhnro;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return this.nimi + ": " + this.puhelinnro + ", " + this.email;
    }
}
