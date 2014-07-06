/**
 * 
 */
package common.solr;

public class SolrType {
	private String typeName;
	private String fieldName;
	private Class clazz;
	private Boolean sortable;

	public SolrType(String fieldName, String typeName, Class clazz) {
		this(fieldName, typeName, clazz, null);
	}
	
	public SolrType(String fieldName, String typeName, Class clazz, Boolean sortable) {
		this.typeName = typeName;
		this.fieldName = fieldName;
		this.clazz = clazz;
		this.sortable = sortable;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public void setSortable(Boolean sortable) {
		this.sortable = sortable;
	}

	public Boolean getSortable() {
		return sortable;
	}
	
	public String getDynamicFieldName(boolean indexed, boolean stored, boolean multiValued) {
		StringBuffer sb = new StringBuffer();
		sb.append('_')
			.append(fieldName)
			.append("_").append(indexed ? "i" : "ni")
			.append("_").append(stored ? "s" : "ns")
			.append("_").append(multiValued ? "m" : "nm");
		return sb.toString();
	}
	
	public String getDynamicField(boolean indexed, boolean stored, boolean multiValued) {
		StringBuffer sb = new StringBuffer();
		sb.append("<dynamicField name=\"*")
			.append(getDynamicFieldName(indexed, stored, multiValued))
			.append("\"  type=\"")
			.append(typeName).append("\"    indexed=\"")
			.append(indexed).append("\"  stored=\"")
			.append(stored).append("\" multiValued=\"")
			.append(multiValued).append("\"/>");
		return sb.toString();
	}
}