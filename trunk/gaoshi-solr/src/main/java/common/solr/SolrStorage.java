package common.solr;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

public class SolrStorage <T extends SolrObject> {
	protected SolrServer server;
	private Class<T> beanClass;

	public SolrStorage(SolrServer server) {
		this.server = server;

		Class clazz = this.getClass();
		ParameterizedType s = (ParameterizedType)clazz.getGenericSuperclass();
		beanClass = (Class<T>) s.getActualTypeArguments()[0];
	}

	public SolrStorage(SolrServer server, String className) throws ClassNotFoundException{
		this.server = server;
		beanClass = (Class<T>) Class.forName(className);
	}

	private String getInstanceId() {
		return beanClass.getSimpleName();
	}

	private static CommonsHttpSolrServer getSolrAccessPoint(String solrUrl) throws SolrServerException, IOException {
		int defaultTimeout = 15000;
		CommonsHttpSolrServer server = new CommonsHttpSolrServer(solrUrl);
		server.setSoTimeout(defaultTimeout);
		server.setConnectionManagerTimeout(defaultTimeout);
		server.setConnectionTimeout(defaultTimeout);
		server.setDefaultMaxConnectionsPerHost(100);
		server.setMaxTotalConnections(100);
		server.setFollowRedirects(false);
		server.setAllowCompression(true);
		server.setMaxRetries(0);
		return server;
	}

	private Class<T> getBeanClass() {
		return beanClass;
	}

	public T get(String id) throws SolrServerException {
		SolrQuery query = new SolrQuery();
		String idSolrFieldName = SolrTypeManager.getInstance().getSolrFieldName(getBeanClass(), "id");
		query.setQuery( idSolrFieldName + ":" + SolrTypeManager.getSolrId(getInstanceId(), id));
		query.setRows(1);
		QueryResponse resp = server.query( query );
		ResultList<T> list = SolrTypeManager.getInstance().getBeanList(resp, getBeanClass());
		if(list.size() == 0) {
            return null;
        }
		return list.get(0);
	}

	public ResultList<T> get(ArrayList<String> idList) throws SolrServerException {
		if(idList.size() == 0) {
            return new ResultList<T>();
        }

		StringBuffer sb = new StringBuffer();
		sb.append('(');
		for(String id : idList) {
			sb.append(SolrTypeManager.getSolrId(getInstanceId(), id)).append(' ');
		}
		sb.append(')');

		SolrQuery query = new SolrQuery();
		String idSolrFieldName = SolrTypeManager.getInstance().getSolrFieldName(getBeanClass(), "id");
		query.setQuery( idSolrFieldName + ":" + sb.toString());
		query.setStart(0);
		query.setRows(idList.size());
		QueryResponse resp = server.query( query );
		ResultList<T> list = SolrTypeManager.getInstance().getBeanList(resp, getBeanClass());
		return list;
	}

	public ArrayList<String> getNonExistIdList(ArrayList<String> idList) throws SolrServerException {
		ArrayList<T> objList = get(idList);
		ArrayList<String> nonExistList = new ArrayList<String>();
		nonExistList.addAll(idList);
		for(T obj : objList) {
			nonExistList.remove(obj.getId());
		}
		return nonExistList;
	}

	public ResultList<T> search(String field, String value) throws SolrServerException {
		return search(field, value, 0, 25);
	}

	public ResultList<T> search(String field, ArrayList<String> value) throws SolrServerException {
		return search(field, value, 0, 25);
	}

	public SolrQuery getQuery() {
		return getQuery(new ArrayList<KeyValuePair>(), 0, 25);
	}

	public SolrQuery getQuery(int start, int size) {
		return getQuery(new ArrayList<KeyValuePair>(), start, size);
	}

	public SolrQuery getQuery(String field, String value) {
		return getQuery(field, value, 0, 25);
	}

	public SolrQuery getQuery(String field, String value, int start, int size) {
		ArrayList<KeyValuePair> paras = new ArrayList<KeyValuePair>();
		paras.add(new KeyValuePair(field, value));
		return getQuery(paras, start, size);
	}

	public SolrQuery setSortField(SolrQuery query, String field, ORDER order) {
		String solrFieldName = SolrTypeManager.getInstance().getSolrFieldName(getBeanClass(), field);
		query.setSortField(solrFieldName, order);
		return query;
	}

	public ResultList<T> search(ArrayList<KeyValuePair> paras) throws SolrServerException {
		return search(paras, 0, 25);
	}

    public SolrQuery getQuery(ArrayList<KeyValuePair> paras) {
        return getQuery(paras, 0, 100);
    }

	public SolrQuery getQuery(ArrayList<KeyValuePair> paras, int start, int size) {
		StringBuffer sb = new StringBuffer();

		String id = null;
		for(KeyValuePair kv : paras) {
			String key = kv.getKey();
			String value = kv.getValue();
			if((value == null) || (value.length()==0)) {
                continue;
            }
			if(key.equals("id")) {
				id = value;
				if(id.indexOf(' ') == -1) {
                    sb.append(" +id_s_i_s_nm:").append(SolrTypeManager.getSolrId(getInstanceId(), id)).append("");
                } else {
                    sb.append(" +id_s_i_s_nm:(").append(SolrTypeManager.getSolrId(getInstanceId(), id)).append(")");
                }
			}
			else {
				String solrFieldName = SolrTypeManager.getInstance().getSolrFieldName(getBeanClass(), key);
				sb.append(" +").append(solrFieldName).append(":(").append(value).append(")");
			}
		}

		if(paras.size() == 0) {
			sb.append(" +id_s_i_s_nm:(").append(getInstanceId()).append("*)");
		}

		SolrQuery query = new SolrQuery();
		query.setStart(start);
		query.setRows(size);
		query.setQuery(sb.toString());
		query.addField("score");

		return query;
	}

	public ResultList<T> search(ArrayList<KeyValuePair> paras, int start, int size) throws SolrServerException {
		SolrQuery query = getQuery(paras, start, size);
		QueryResponse resp = server.query( query );
		ResultList<T> list = SolrTypeManager.getInstance().getBeanList(resp, getBeanClass());
		return list;
	}

	public ResultList<T> search(SolrQuery query) throws Exception {
		QueryResponse resp = server.query( query );
		ResultList<T> list = SolrTypeManager.getInstance().getBeanList(resp, getBeanClass());
		return list;
	}

	public ResultList<T> search(String field, String value, int start, int size) throws SolrServerException {
		ArrayList<KeyValuePair> paras = new ArrayList<KeyValuePair>();
		paras.add(new KeyValuePair(field, value));
		return search(paras, start, size);
	}

	public ResultList<T> search(String field, ArrayList<String> value, int start, int size) throws SolrServerException {
		ArrayList<KeyValuePair> paras = new ArrayList<KeyValuePair>();
		for(String s : value) {
            paras.add(new KeyValuePair(field, s));
        }
		return search(paras, start, size);
	}

	public void save(T solrObj) throws Exception {
		solrObj.setTimestamp(new Date());
		server.add(SolrTypeManager.getInstance().getSolrInputDocument(solrObj, getInstanceId()));
	}

	public void saveAndCommit(T solrObj) throws Exception {
		save(solrObj);
		server.commit();
	}

	public void delete(String id) throws Exception {
		if(id.startsWith(getInstanceId() + "_")) {
            server.deleteById(id);
        } else {
            server.deleteById(SolrTypeManager.getSolrId(getInstanceId(), id));
        }
	}

    public void deleteByQuery(String query) throws Exception {
        server.deleteByQuery(query);
    }

    public void deleteAll() throws Exception {
        server.deleteByQuery("*:*");
    }

	public void commit() throws SolrServerException, IOException {
			server.commit();
	}
}
