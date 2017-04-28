import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestJvmVersion {

	private static int RAND_BOUND = 200;

    private String randomVersionString(Random r, boolean hasUpdate) {
        if (hasUpdate)
            return String.format("%d.%d.%d_%d", r.nextInt(RAND_BOUND), r.nextInt(RAND_BOUND), r.nextInt(RAND_BOUND), r.nextInt(RAND_BOUND));
        else
            return String.format("%d.%d.%d", r.nextInt(RAND_BOUND), r.nextInt(RAND_BOUND), r.nextInt(RAND_BOUND));
    }

    private void assertEqualOrGreater(String a, String b) {
        JvmVersion vA = new JvmVersion(a);
        JvmVersion vB = new JvmVersion(b);
        assertTrue(vA.isEqualOrGreater(vB));
        if (vA.compareTo(vB) != 0)
            assertFalse(vB.isEqualOrGreater(vA));
    }

    private void assertParseInvalid(String s) {
        try {
            JvmVersion v = new JvmVersion(s);
            fail("s should be an invalid string but is parsed to \"" + v.toString() + "\"");
        } catch(Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
            assertEquals("Invalid version string \"" + s + "\"", ex.getMessage());
        }
    }

    private void testParse(Random r, boolean hasUpdate) {
        for (int i = 0; i < 100; i++) {
            String s = randomVersionString(r, hasUpdate);
            assertEquals(s + (hasUpdate ? "" : "_0"), new JvmVersion(s).toString());
        }
    }

	@Test
	public void testParse() {
		Random r = new Random(1); //fixed seed
        //format x.y.z_u
        testParse(r, true);
        //format x.y.z (no update number)
        testParse(r, false);
	}

	@Test
    public void parseWithIdentifier() {
        assertEquals("12.2.3_45", new JvmVersion("12.2.3_45-rc2").toString());
        assertEquals("4.2.1_0", new JvmVersion("4.2.1-1ea").toString());
    }

	@Test
    public void testParseBadString() {
        assertParseInvalid("");
        assertParseInvalid("a");
        assertParseInvalid("1,2");
        assertParseInvalid("1.8.0.40");
        assertParseInvalid(".._");
    }

    private void testEquals(Random r, boolean hasUpdate) {
        for (int i = 0; i < 100; i++) {
            String s = randomVersionString(r, hasUpdate);
            assertEqualOrGreater(s, s);
        }
    }

    @Test
    public void testEquals() {
        Random r = new Random(2);
        testEquals(r, true);
        testEquals(r, false);
    }

    @Test
    public void testEqualOrGreater() {
	    assertEqualOrGreater("2.0.0_0", "1.0.0_0");
        assertEqualOrGreater("2.14.32_15", "1.18.40_16");
	    assertEqualOrGreater("1.2.0_0", "1.1.0_0");
        assertEqualOrGreater("1.2.1_0", "1.1.2_0");
        assertEqualOrGreater("2.2.3_0", "2.2.1_0");
        assertEqualOrGreater("3.3.3_5-ea", "3.3.2_10-rc2");
        assertEqualOrGreater("1.1.1_5", "1.1.1_2");
        //from example at http://www.oracle.com/technetwork/java/javase/versioning-naming-139433.html
        assertEqualOrGreater("1.3.1_01", "1.3.1");
        assertEqualOrGreater("1.3.1", "1.3.0_01");
        assertEqualOrGreater( "1.3.0_01", "1.3.0");
    }

    @Test
    public void testRunningJvm() {
        String realVersionBkp = System.getProperty("java.version");
        //pretend we are on a JVM with some random version number
        System.setProperty("java.version", "2.4.0_121");
        assertTrue(JvmVersion.runningJvmSupports("1.8.0_40"));
        assertTrue(JvmVersion.runningJvmSupports("1.7.2"));
        assertTrue(JvmVersion.runningJvmSupports("1.8.0"));
        assertTrue(JvmVersion.runningJvmSupports("1.6.0_0"));
        assertTrue(JvmVersion.runningJvmSupports("1.6.5_0"));
        assertTrue(JvmVersion.runningJvmSupports("1.8.0_121"));
        assertTrue(JvmVersion.runningJvmSupports("2.4.0_121"));
        assertFalse(JvmVersion.runningJvmSupports("2.4.0_122"));
        assertFalse(JvmVersion.runningJvmSupports("2.4.1_121"));
        assertFalse(JvmVersion.runningJvmSupports("2.5.0_121"));
        assertFalse(JvmVersion.runningJvmSupports("3.4.0_121"));
        //restore real version
        //(not needed because these properties are not persistent; this is here for better maintainability of the code)
        //(e.g. adding other test which need the real version)
        System.setProperty("java.version", realVersionBkp);
    }

}
