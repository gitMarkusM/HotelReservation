package varausjarjestelma;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class Tekstikayttoliittyma {
    
    @Autowired
    HuoneDao huoneDao;
    
    @Autowired
    VarausDao varausDao;
    
    @Autowired
    AsiakasDao asiakasDao;
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    public void kaynnista(Scanner lukija) throws SQLException, Exception {
        while (true) {
            System.out.println("Komennot: ");
            System.out.println(" x - lopeta");
            System.out.println(" 1 - lisaa huone");
            System.out.println(" 2 - listaa huoneet");
            System.out.println(" 3 - hae huoneita");
            System.out.println(" 4 - lisaa varaus");
            System.out.println(" 5 - listaa varaukset");
            System.out.println(" 6 - tilastoja");
            System.out.println("");

            String komento = lukija.nextLine();
            if (komento.equals("x")) {
                break;
            }
            if (komento.equals("1")) {
                lisaaHuone(lukija);
            } else if (komento.equals("2")) {
                listaaHuoneet();
            } else if (komento.equals("3")) {
                haeHuoneita(lukija);
            } else if (komento.equals("4")) {
                lisaaVaraus(lukija);
            } else if (komento.equals("5")) {
                listaaVaraukset();
            } else if (komento.equals("6")) {
                tilastoja(lukija);
            }
        }
    }

    private void lisaaHuone(Scanner s) throws SQLException {
        System.out.println("Lisätään huone.");
        System.out.println("");

        System.out.println("Minkä tyyppinen huone on?");
        String tyyppi = s.nextLine();
        System.out.println("Mikä huoneen numeroksi asetetaan?");
        int numero = Integer.valueOf(s.nextLine());
        System.out.println("Kuinka monta euroa huone maksaa yöltä?");
        int hinta = Integer.valueOf(s.nextLine());
        
        Huone huone = new Huone(numero, tyyppi, hinta);
        huoneDao.create(huone);
    }

    private void listaaHuoneet() throws SQLException {
        System.out.println("Listataan huoneet...");
        System.out.println("");
        
        List<Huone> huoneet = huoneDao.list();
        huoneet.forEach((h) -> {
            System.out.println(h);
        });
        System.out.println("");
    }

    private void haeHuoneita(Scanner s) throws SQLException, Exception{
        System.out.println("Haetaan huoneita");
        System.out.println("");

        System.out.println("Milloin varaus alkaisi (yyyy-MM-dd)?");;
        LocalDateTime alku = LocalDateTime.parse(s.nextLine() + " " + "16:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.println("Milloin varaus loppuisi (yyyy-MM-dd)?");
        LocalDateTime loppu = LocalDateTime.parse(s.nextLine() + " " + "10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.println("Minkä tyyppinen huone? (tyhjä = ei rajausta)");
        String tyyppi = s.nextLine();
        System.out.println("Minkä hintainen korkeintaan? (tyhjä = ei rajausta)");
        String maksimihinta = s.nextLine();
        
        System.out.println("Vapaat huoneet: ");
        System.out.println("****************************************"); 
            tulostaHuoneet(huoneDao.haeHuoneita(alku, loppu, tyyppi, maksimihinta));
        System.out.println("****************************************");
    }

    private void lisaaVaraus(Scanner s) throws SQLException, Exception {
        System.out.println("Haetaan huoneita.");
        System.out.println("");
        
        System.out.println("Milloin varaus alkaisi (yyyy-MM-dd)?");
        LocalDateTime alku = LocalDateTime.parse(s.nextLine() + " " + "16:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.println("Milloin varaus loppuisi (yyyy-MM-dd)?");
        LocalDateTime loppu = LocalDateTime.parse(s.nextLine() + " " + "10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        
        System.out.println("Minkä tyyppinen huone? (tyhjä = ei rajausta)");
        String haluttuTyyppi = s.nextLine();

        System.out.println("Minkä hintainen korkeintaan? (tyhjä = ei rajausta)");
        String maksimihinta = s.nextLine();    
        
        // Mitä ilman et tule toimeen -kyselyt tulisi tähän kohtaan.
        
        // Näytetään vapaat huoneet ja kysytään varattavien lukumäärä. Varataan halvimmat.
        List<Huone> vapaatHuoneet = huoneDao.haeHuoneita(alku, loppu, haluttuTyyppi, maksimihinta);
        System.out.println("Vapaat huoneet:");
        System.out.println("****************************************");
            tulostaHuoneet(vapaatHuoneet);
        System.out.println("****************************************\n");
        
        if(!vapaatHuoneet.isEmpty()) {
            int huoneidenLkm = 0;
            while(true) {
                System.out.print("Kuinka monta huonetta haluat varata? ");
                huoneidenLkm = Integer.valueOf(s.nextLine());
                if(huoneidenLkm > vapaatHuoneet.size()) {
                    System.out.println("Huoneita ei ole vapaana riittävästi.");
                    System.out.println("Kokeile uudelleen.");
                } else {
                    break;
                }
            }

            // Asiakkaan tiedot. Jos on jo, ei tehdä uutta samannimistä.
            System.out.println("Syötä varaajan nimi:");
            String nimi = s.nextLine();
            
            List<Asiakas> asiakkaat = asiakasDao.list();
            int loydettyID = 0;
            for(Asiakas asiakas: asiakkaat) {
                if(asiakas.getNimi().equalsIgnoreCase(nimi)) {
                    loydettyID = asiakas.getId();
                }
            } 
            
            Varaus varaus = new Varaus(alku, loppu, 0);
            if(loydettyID == 0) {
                System.out.println("Syötä varaajan puhelinnumero:");
                String puhelinnro = s.nextLine();
                System.out.println("Syötä varaajan sähköpostiosoite:");
                String email = s.nextLine();
                int asiakasID = asiakasDao.createAndReturnKey(new Asiakas(nimi,puhelinnro,email));
                varaus.setAsiakasId(asiakasID);
            } else if(loydettyID > 0) {
                varaus.setAsiakasId(loydettyID);
            }
            
            int varauksenID = varausDao.createAndReturnKey(varaus);
            
            varausDao.luoHuonevaraus(varaus.lisaaHuonevaraus(varauksenID, huoneidenLkm, vapaatHuoneet));
        }
    }

    private void listaaVaraukset() throws SQLException{
        List<Varaus> varaukset = varausDao.list();
        
        varaukset.forEach(varaus -> {
            try {
                System.out.println(asiakasDao.read(varaus.getAsiakasId()));
            } catch (SQLException ex) {
                Logger.getLogger(Tekstikayttoliittyma.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(varaus);
        });

    }

    private static void tilastoja(Scanner lukija) {
        System.out.println("Mitä tilastoja tulostetaan?");
        System.out.println("");

        // tilastoja pyydettäessä käyttäjältä kysytään tilasto
        System.out.println(" 1 - Parhaat asiakkaat");
        System.out.println(" 2 - Varausprosentti huoneittain");
        System.out.println(" 3 - Varausprosentti huonetyypeittäin");

        System.out.println("Syötä komento: ");
        int komento = Integer.valueOf(lukija.nextLine());

        switch (komento) {
            case 1:
                parhaatAsiakkaat();
                break;
            case 2:
                varausprosenttiHuoneittain(lukija);
                break;
            case 3:
                varausprosenttiHuonetyypeittain(lukija);
                break;
            default:
                break;
        }
    }

    private static void parhaatAsiakkaat() {
        System.out.println("Tulostetaan parhaat asiakkaat");
        System.out.println("");

        
        
//        System.out.println("Anssi Asiakas, anssi@asiakas.net, +358441231234, 1323 euroa");
//        System.out.println("Essi Esimerkki, essi@esimerkki.net, +358443214321, 229 euroa");
    }

    private static void varausprosenttiHuoneittain(Scanner lukija) {
        System.out.println("Tulostetaan varausprosentti huoneittain");
        System.out.println("");

        System.out.println("Mistä lähtien tarkastellaan?");
        LocalDateTime alku = LocalDateTime.parse(lukija.nextLine() + "-01 " + "16:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.println("Mihin asti tarkastellaan?");
        LocalDateTime loppu = LocalDateTime.parse(lukija.nextLine() + "-01 " + "10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        // alla esimerkkitulostus
        System.out.println("Tulostetaan varausprosentti huoneittain");
        System.out.println("Excelsior, 604, 119 euroa, 0.0%");
        System.out.println("Excelsior, 605, 119 euroa, 0.0%");
        System.out.println("Superior, 705, 159 euroa, 22.8%");
        System.out.println("Commodore, 128, 229 euroa, 62.8%");
    }

    private static void varausprosenttiHuonetyypeittain(Scanner lukija) {
        System.out.println("Tulostetaan varausprosentti huonetyypeittäin");
        System.out.println("");

        System.out.println("Mistä lähtien tarkastellaan?");
        LocalDateTime alku = LocalDateTime.parse(lukija.nextLine() + "-01 " + "16:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.println("Mihin asti tarkastellaan?");
        LocalDateTime loppu = LocalDateTime.parse(lukija.nextLine() + "-01 " + "10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        // alla esimerkkitulostus
        System.out.println("Tulostetaan varausprosentti huonetyypeittän");
        System.out.println("Excelsior, 0.0%");
        System.out.println("Superior, 22.8%");
        System.out.println("Commodore, 62.8%");
    }

    public static void tulostaHuoneet(List<Huone> vapaatHuoneet) {
        vapaatHuoneet.forEach(huone -> {
            System.out.println(huone);
        });
    }
    
//    public static void tulostaVaraustiedot(List<Varaus> varaustiedot) {
//        System.out.println("******************************************************");
//        varaustiedot.forEach(varaustieto -> {
//            System.out.println(varaustieto);
//            System.out.println("******************************************************");
//        });
//    }
    
}