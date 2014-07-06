package common.solr;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.util.reflection.ReflectionUtil;

public class SolrTypeManager {
	private static Logger logger = LoggerFactory.getLogger(SolrTypeManager.class);

	private static SolrTypeManager instance = null;
	public static SolrTypeManager getInstance() {
		if(instance == null) {
            instance = new SolrTypeManager();
        }
		return instance;
	}

	private ArrayList<SolrType> list = new ArrayList<SolrType>();

	private SolrTypeManager() {
		list.add(new SolrType("i", "integer"    , Integer.class, false));
		list.add(new SolrType("l", "long"   , Long.class, false));
		list.add(new SolrType("f", "float"  , Float.class, false));
		list.add(new SolrType("d", "double" , Double.class, false));
		list.add(new SolrType("i", "integer"    , Enum.class, false));

		list.add(new SolrType("si", "sint"   , Integer.class, true));
		list.add(new SolrType("sl", "slong"  , Long.class, true));
		list.add(new SolrType("sf", "sfloat" , Float.class, true));
		list.add(new SolrType("sd", "sdouble", Double.class, true));
		list.add(new SolrType("si", "sint"   , Enum.class, true));

		list.add(new SolrType("s", "string" , String.class));
		list.add(new SolrType("t", "text"   , String.class));
		list.add(new SolrType("b", "boolean", Boolean.class));
		list.add(new SolrType("dt", "date"   , Date.class));
	}

	public ArrayList<SolrType> getSolrTypeList() {
		return list;
	}

	public String getDynamicFieldList() {
		StringBuffer sb = new StringBuffer();
		for(SolrType solrType : list) {
			for (int indexed = 0; indexed < 2; indexed++) {
				for (int stored = 0; stored < 2; stored++) {
					for (int multiValued = 0; multiValued < 2; multiValued++) {
						sb.append(solrType.getDynamicField(indexed==0, stored==0, multiValued==0));
						sb.append("\n");
					}
				}
			}
		}
		return sb.toString();
	}

	public SolrType getSolrType(String primitive, boolean sortableOrText) {
		if(primitive.equalsIgnoreCase("string")) {
            return getSolrType(primitive, false, sortableOrText);
        } else {
            return getSolrType(primitive, sortableOrText, true);
        }
	}

	public SolrType getSolrType(String primitive, boolean sortable, boolean text) {
		for(SolrType solrType : list) {
			String simpleName = solrType.getClazz().getSimpleName();
			boolean isString = primitive.equalsIgnoreCase("string");
			if(isString) {
				if(text) {
					if(solrType.getTypeName().equals("text")) {
                        return solrType;
                    }
				}
				else {
					if(solrType.getTypeName().equals("string")) {
                        return solrType;
                    }
				}
			}
			else {
				if(solrType.getSortable() == null) {
					if(simpleName.equalsIgnoreCase(primitive)) {
                        return solrType;
                    }
				}
				else {
					if(simpleName.equalsIgnoreCase(primitive) && (solrType.getSortable() == sortable)) {
                        return solrType;
                    }
				}
			}
		}
		return null;
	}

	public SolrType getSolrType(Field field) {
		SolrField anno = field.getAnnotation(SolrField.class);
        if(anno == null) {
            return null;
        }
        Class<?> fieldType = field.getType();
		if(fieldType.equals(java.util.List.class)) {
			try {
                fieldType = ReflectionUtil.getFieldGenericType(field);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
		}

		if(fieldType.getEnumConstants() != null) {
			fieldType = fieldType.getSuperclass();
		}

		String simpleName = fieldType.getSimpleName();
		if(simpleName.equals("String")) {
			boolean text = anno.text();
			return getSolrType(simpleName, text);
		}
		else {
			boolean sortable = anno.sortable();
			return getSolrType(simpleName, sortable);
		}
	}

	public SolrType getSolrType(Class clazz, String fieldName) {
		try {
			return getSolrType(clazz.getDeclaredField(fieldName));
		} catch (Exception e) {
			return null;
		}
	}

	public Field getJavaFieldBySoldFieldName(Class clazz, String solrFieldName) {
		if(clazz.equals(Object.class)) {
            return null;
        }
		try {
			Field[] fields = clazz.getDeclaredFields();
			for(Field field : fields) {
				if(solrFieldName.startsWith(field.getName()+"_") || solrFieldName.equals(getSolrFieldName(field))) {
                    return field;
                }
			}
			try {
				Field sameNameField = clazz.getDeclaredField(solrFieldName);
				if(sameNameField != null) {
                    return sameNameField;
                }
			} catch (Exception e) {
			}
		} catch (Exception e) {
		}
		return getJavaFieldBySoldFieldName(clazz.getSuperclass(), solrFieldName);
	}

	public Method getGetterBySoldFieldName(Class clazz, String solrFieldName) {
		Field field = getJavaFieldBySoldFieldName(clazz, solrFieldName);
		if(field == null) {
            return null;
        }
		String name = field.getName();
		name = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
		try {
			return clazz.getMethod(name, null);
		} catch (Exception e) {
			return null;
		}
	}

	public Method getSetterBySoldFieldName(Class clazz, String solrFieldName) {
		Field field = getJavaFieldBySoldFieldName(clazz, solrFieldName);
		if(field == null) {
            return null;
        }
		String name = field.getName();
		name = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
		try {
			return clazz.getMethod(name, field.getType());
		} catch (Exception e) {
			return null;
		}
	}

	public String getSolrFieldName(Class clazz, String fieldName) {
		if(clazz.equals(Object.class)) {
            return null;
        }
		String solrFieldName = null;
		try {
			solrFieldName = getSolrFieldName(clazz.getDeclaredField(fieldName));
		} catch (Exception e) {
		}
		if(solrFieldName == null) {
            solrFieldName = getSolrFieldName(clazz.getSuperclass(), fieldName);
        }
		return solrFieldName;
	}

	public String getSolrFieldName(Field field) {
		SolrType solrType = getSolrType(field);
		if(solrType == null) {
            return null;
        }
		return getSolrFieldName(solrType, field);
	}

	public String getSolrFieldName(SolrType solrType, Field field) {
		SolrField anno = field.getAnnotation(SolrField.class);
		boolean indexed = anno.indexed();
		boolean stored = anno.stored();
		boolean multi = field.getType().equals(java.util.List.class);
		return field.getName() + solrType.getDynamicFieldName(indexed, stored, multi);
	}

	public SolrInputDocument getSolrInputDocument(Object obj, String instanceId) {
		SolrInputDocument doc = new SolrInputDocument();
		addToSolrInputDocument(obj, obj.getClass(), doc, instanceId);
		return doc;
	}

	public HashMap<String, Field> getSolrFieldList(Class clazz) {
		HashMap<String, Field> bag = new HashMap<String, Field>();
		getSolrFieldList(clazz, bag);
		return bag;
	}

	private void getSolrFieldList(Class clazz, HashMap<String, Field> bag) {
		if(clazz.equals(Object.class)) {
            return;
        }

		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			SolrType solrType = getSolrType(field);
			if(solrType == null) {
                continue;
            }
			String solrFieldName = getSolrFieldName(solrType, field);
			bag.put(solrFieldName, field);
		}

		getSolrFieldList(clazz.getSuperclass(), bag);
	}

	public void addToSolrInputDocument(Object obj, Class clazz, SolrInputDocument doc, String instanceId) {
		if(clazz.equals(Object.class)) {
            return;
        }
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			SolrType solrType = getSolrType(field);
			if(solrType == null) {
                continue;
            }
			String solrFieldName = getSolrFieldName(solrType, field);
			Method getter = null;
            try {
                getter = ReflectionUtil.getGetter(clazz, field.getName());
            } catch (Exception e1) {
                throw new RuntimeException(e1);
            }
			Object fieldValue = null;
			if(getter != null) {
				try {
					fieldValue = getter.invoke(obj, null);
				} catch (Exception e) {
				}
			}
			else {
				field.setAccessible(true);
				try {
					fieldValue = field.get(obj);
				} catch (Exception e) {
				}
			}
			if(fieldValue==null) {
                continue;
            }
			if(solrFieldName.equals("id_s_i_s_nm")) {
                fieldValue = getSolrId(instanceId, (String)fieldValue);
            }
			if(fieldValue instanceof Enum) {
				fieldValue = new Integer(((Enum)fieldValue).ordinal());
			}
			doc.addField(solrFieldName, fieldValue);
		}
		addToSolrInputDocument(obj, clazz.getSuperclass(), doc, instanceId);
	}

	public static String getSolrId(String instanceId, String id) {
		if(id.indexOf(' ') != -1) {
			StringBuffer sb = new StringBuffer();
			String[] idList = id.split(" ");
			for (int i = 0; i < idList.length; i++) {
				if((idList[i] == null) || (idList[i].length() == 0)) {
                    continue;
                }
				sb.append(instanceId).append("_").append(idList[i]);
				if(i != idList.length - 1) {
                    sb.append(" ");
                }
			}
			return sb.toString();
		}
		else if (id.length() == 0) {
            return instanceId;
        } else if (id.startsWith(instanceId)) {
            return id;
        } else {
            return instanceId + "_" + id;
        }
	}
	public static String getIdFromSolrId(String id) {
		if(id == null) {
            return null;
        }
		int pos = id.indexOf("_");
		if(pos == -1) {
            return "";
        }
		return id.substring(pos+1);
	}

	public <T> ResultList<T> getBeanList(QueryResponse resp, Class<T> type) {
		ResultList<T> list = new ResultList<T>();
		list.setStart(resp.getResults().getStart());
		list.setTotal(resp.getResults().getNumFound());
		SolrDocumentList result = resp.getResults();
		for(SolrDocument doc : result) {
			list.add(getBean(doc, type));
		}
		return list;
	}

	public <T> T getBean(SolrDocument doc, Class<T> type) {
		T bean = null;
		try {
			bean = type.newInstance();
			for (String solrFieldName : doc.getFieldNames()) {
				Object fieldValue = doc.getFieldValue(solrFieldName);
				if(fieldValue == null) {
                    continue;
                }

				if(solrFieldName.equals("id_s_i_s_nm")) {
					fieldValue = getIdFromSolrId(fieldValue.toString());
				}

				Field field = getJavaFieldBySoldFieldName(type, solrFieldName);
				if(field == null) {
                    continue;
                }

				Class<?> fieldType = field.getType();
				if(fieldType.equals(List.class)) {
					fieldValue = doc.getFieldValues(solrFieldName);
				}
				if(fieldType.getEnumConstants() != null) {
					Object[] enumConstants = fieldType.getEnumConstants();
					for (Object obj : enumConstants) {
						if(((Enum)obj).ordinal() == ((Integer)fieldValue).intValue()) {
							fieldValue = obj;
							break;
						}
					}
				}

				Method setter = getSetterBySoldFieldName(type, solrFieldName);
				if(setter != null) {
					try {
						setter.invoke(bean, fieldValue);
					} catch (Exception e) {
						logger.error(e.getMessage() + " " + setter.getName());
					}
				}
				else {
					if(field == null) {
                        continue;
                    }
					field.setAccessible(true);
					try {
						field.set(bean, fieldValue);
					} catch (Exception e) {
						logger.error(e.getMessage() + " " + field.getName());
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
		return bean;
	}

	public static void main(String[] args) {
		System.out.println(SolrTypeManager.getInstance().getDynamicFieldList());
	}
}
