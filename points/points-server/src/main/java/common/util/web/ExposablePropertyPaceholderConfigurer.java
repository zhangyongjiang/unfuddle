package common.util.web;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.gaoshin.points.server.bean.Configuration;

public class ExposablePropertyPaceholderConfigurer extends
		PropertyPlaceholderConfigurer {
	private Map<String, String> resolvedProps;

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		resolvedProps = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			resolvedProps.put(
					keyStr, parseStringValue(props.getProperty(keyStr), props,
							new HashSet()));
		}
	}

	public Configuration getConfiguration(String key, Object def) {
		String value = resolvedProps.get(key);
		if(value == null) {
			value = def.toString();
		}
		Configuration conf = new Configuration(key, value);
		return conf;
	}
}
