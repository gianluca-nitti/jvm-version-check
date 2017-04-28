import java.util.regex.Pattern;
import java.util.regex.Matcher;

class JvmVersion implements Comparable<JvmVersion> {

	/* format: major.minor.patch_update (ex. 1.8.0_40)*/
	private int major;
	private int minor;
	private int patch;
	private int update;

	public JvmVersion(String s) {
		Pattern p = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)(?:_(\\d+))?(?:-(?:\\w|\\d)*)?");
		Matcher m = p.matcher(s);
		if (!m.matches()) throw new IllegalArgumentException("Invalid version string \"" + s + "\"");
		this.major = Integer.parseInt(m.group(1));
		this.minor = Integer.parseInt(m.group(2));
		this.patch = Integer.parseInt(m.group(3));
		if (m.group(4)!= null && m.group(4).length() != 0)
			this.update = Integer.parseInt(m.group(4));
		else
			this.update = 0;
	}

	public int compareTo(JvmVersion other) {
		if (major != other.major) return major - other.major;
		if (minor != other.minor) return minor - other.minor;
		if (patch != other.patch) return patch - other.patch;
		return update - other.update;
	}

	public boolean isEqualOrGreater(JvmVersion other) {
		return compareTo(other) >= 0;
	}

	public String toString() {
		return String.format("%d.%d.%d_%d", major, minor, patch, update);
	}

	public static JvmVersion getRunningVersion() {
		return new JvmVersion(System.getProperty("java.version"));
	}

	public static boolean runningJvmSupports(JvmVersion target) {
		return getRunningVersion().isEqualOrGreater(target);
	}

	public static boolean runningJvmSupports(String target) {
		return runningJvmSupports(new JvmVersion(target));
	}

	public static void main(String[] args) {
		System.exit(runningJvmSupports(args[0]) ? 0 : 1);
	}
}
