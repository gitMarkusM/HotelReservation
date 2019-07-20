
package varausjarjestelma;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/*
 * @author MarkusM
 */

public class HuoneVaraus {
    private List<Integer> huonenrot;
    private int varausId;

    public HuoneVaraus() {
    }

    public HuoneVaraus(List<Integer> huonenrot, int varausId) {
        this.huonenrot = huonenrot;
        this.varausId = varausId;
    }
    
    public HuoneVaraus(ResultSet rs) throws SQLException {
        this.huonenrot = new ArrayList<>();
        this.varausId = rs.getInt("id");
    }

    public List<Integer> getHuonenrot() {
        return huonenrot;
    }
    
    public int getHuonenro(int i) {
        return this.huonenrot.get(i);
    }

    public void setHuonenrot(List<Integer> huonenrot) {
        this.huonenrot = huonenrot;
    }

    public int getVarausId() {
        return varausId;
    }

    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }
    
}
