package org.apache.felix.cm.plugins;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.felix.cm.impl.Log;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationPlugin;
import org.osgi.service.log.LogService;

public class EnvConfigutationPlugin implements ConfigurationPlugin {

	private static String DEFAULT_PATTERN = "\\$ENV\\{(.*)\\}";
	private static String DEFAULT_GROUP = "1";
	public static String SWITCH = "felix.cm.plugin.env.switch";
	public static String SWITCH_ACTIVE = "1";
	public static String RANKING = "felix.cm.plugin.env.ranking";
	public static String PATTERN = "felix.cm.plugin.env.pattern";
	public static String PATTERN_GROUP = "felix.cm.plugin.env.pattern.group";

	@Override
	public void modifyConfiguration(ServiceReference<?> reference, Dictionary<String, Object> properties) {

		if (properties == null) {
			Log.logger.log(LogService.LOG_ERROR, "Configuration dictionary is null.", new Object[] {});
			return;
		}

		String spattern = System.getProperty(PATTERN, DEFAULT_PATTERN);
		Pattern pattern = Pattern.compile(spattern);
		String sgroup = System.getProperty(PATTERN_GROUP, DEFAULT_GROUP);
		int group = Integer.valueOf(sgroup);

		final Enumeration<String> e = properties.keys();
		while (e.hasMoreElements()) {
			final String k = e.nextElement();
			final String v = properties.get(k).toString();

			Matcher matcher = pattern.matcher(v);
			if (!matcher.matches()) {

				continue;
			}

			final String envVar = matcher.group(group);
			final String resolvedValue = System.getProperty(envVar);

			if (resolvedValue == null) {
				Log.logger.log(LogService.LOG_WARNING, "Environment variable '{0}' not set.", new Object[] { envVar });
				continue;
			}

			properties.put(k, resolvedValue);
		}

	}

}
