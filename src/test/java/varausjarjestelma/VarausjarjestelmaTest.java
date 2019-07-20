package varausjarjestelma;

import org.junit.BeforeClass;
import org.junit.Test;

public class VarausjarjestelmaTest {

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("spring.jpa.properties.org.hibernate.flushMode", "ALWAYS");
        System.setProperty("org.hibernate.flushMode", "ALWAYS");
    }

    @Test
    public void noTests() {
        // projektia varten ei ole automaattisia testejä
    }
}
