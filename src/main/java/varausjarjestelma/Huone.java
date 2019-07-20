
package varausjarjestelma;

/*
 * @author MarkusM
 */
public class Huone implements Comparable<Huone> {
    private int numero;
    private String tyyppi;
    private int paivahinta;

    public Huone() {
    }

    public Huone(int numero, String tyyppi, int paivahinta) {
        this.numero = numero;
        this.tyyppi = tyyppi;
        this.paivahinta = paivahinta;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTyyppi() {
        return tyyppi;
    }

    public void setTyyppi(String tyyppi) {
        this.tyyppi = tyyppi;
    }

    public int getPaivahinta() {
        return paivahinta;
    }

    public void setPaivahinta(int paivahinta) {
        this.paivahinta = paivahinta;
    }
    
    @Override
    public String toString() {
        return this.getTyyppi() + ", " + this.getNumero() + ", " + this.paivahinta + " euroa";
    }

    @Override
    public int compareTo(Huone huone) {
        return this.paivahinta - huone.getPaivahinta();
    }
}
