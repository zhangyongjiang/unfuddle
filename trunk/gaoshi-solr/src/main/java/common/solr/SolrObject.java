package common.solr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class SolrObject {
	@SolrField(text=false)
	protected String id;

	private Float score;
	private String uuid;

	@SolrField(sortable=true)
	private Date timestamp = new Date();

	@SolrField
	private int version;

	public void setScore(Float score) {
		this.score = score;
	}

	public Float getScore() {
		return score;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Date getTimestamp() {
		if(timestamp == null) {
            timestamp = new Date();
        }
		return timestamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static String append(String... strings){
		StringBuffer sb = new StringBuffer();
		List<String> list = appendList(strings);
		for(String s : list) {
            sb.append(s).append(' ');
        }

		return sb.substring(0, sb.length()-1);
	}

	public static String removeDuplicates(String... strings){
		List<String> list = appendList(strings);
		HashSet<String> hash = new HashSet<String>();
		for(String s : list) {
			for(String word : s.split(" ")){
				if((word == null) || (word.length() == 0)) {
                    if(hash.contains(word)) {
                        continue;
                    }
                }
				hash.add(word);
			}
		}
		Object[] words = hash.toArray();
		Arrays.sort(words);
		StringBuffer sb = new StringBuffer();
		for(Object s : words) {
			sb.append(s).append(' ');
		}

		return sb.substring(0, sb.length()-1);
	}

	public static List<String> appendList(String... strings){
		List<String> result = new ArrayList<String>();
		for(String str : strings) {
			if((str == null) || (str.length() == 0) || result.contains(str)) {
                continue;
            }
			result.add(str);
		}

		return result;
	}

	public static String getOne(String... strings){
		for(String str : strings) {
			if((str != null) || (str.length() != 0)) {
                return str;
            }
		}

		return null;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUuid() {
		return uuid;
	}

	public String getStrTimestamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
		return sdf.format(timestamp);
	}

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }
}
