package common.solr;

import org.apache.solr.client.solrj.embedded.JettySolrRunner;

public class EmbeddedJettySolr {
	public synchronized static JettySolrRunner startJettySolr(String home, int port) throws Exception {
		System.setProperty("solr.solr.home", home);
		JettySolrRunner runner = new JettySolrRunner("/", port);
		runner.start();
		return runner;
	}
}
