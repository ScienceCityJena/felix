package org.apache.felix.cm.plugins;

import static org.junit.Assert.assertEquals;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Test;
import org.osgi.service.cm.ConfigurationPlugin;

public class EnvConfigutationPluginTest {

	private static String ENV_KEY = "felix.test.cm.env";
	private static String REPLACEMENT = "works!";

	private static String ENV_PLACEHOLDER_DEF = "$ENV{" + ENV_KEY + "}";

	private static String ENV_PLACEHOLDER_CUST = "#ENV." + ENV_KEY;
	private static String ENV_PLACEHOLDER_PATTERN = "\\#(ENV)\\.(.*)";
	private static String ENV_PLACEHOLDER_GROUP = "2";

	@Test
	public void testEnvDefault() throws Exception {

		ConfigurationPlugin cp = new EnvConfigutationPlugin();

		System.setProperty(ENV_KEY, REPLACEMENT);

		Dictionary<String, Object> dict = new Hashtable<>();

		dict.put(ENV_KEY, ENV_PLACEHOLDER_DEF);
		cp.modifyConfiguration(null, dict);

		assertEquals(REPLACEMENT, dict.get(ENV_KEY));
	}

	@Test
	public void testEnvCostom() throws Exception {

		ConfigurationPlugin cp = new EnvConfigutationPlugin();

		System.setProperty(ENV_KEY, REPLACEMENT);
		System.setProperty(EnvConfigutationPlugin.PATTERN, ENV_PLACEHOLDER_PATTERN);
		System.setProperty(EnvConfigutationPlugin.PATTERN_GROUP, ENV_PLACEHOLDER_GROUP);
		

		Dictionary<String, Object> dict = new Hashtable<>();

		dict.put(ENV_KEY, ENV_PLACEHOLDER_CUST);
		cp.modifyConfiguration(null, dict);

		assertEquals(REPLACEMENT, dict.get(ENV_KEY));
	}
}
