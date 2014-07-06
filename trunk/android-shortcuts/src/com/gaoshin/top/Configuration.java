package com.gaoshin.top;

import com.gaoshin.sorma.annotation.Column;
import com.gaoshin.sorma.annotation.Ddl;
import com.gaoshin.sorma.annotation.Table;

@Table(
    create = {
            "CREATE TABLE IF NOT EXISTS Configuration (_id INTEGER PRIMARY KEY autoincrement, ckey VARCHAR(255), cvalue TEXT)",
            "create index IF NOT EXISTS keyindex on Configuration(ckey)"
    },
        autoId = true,
    keyColumn = "_id",
    ddls = {
                @Ddl(
                        yyyyMMddHHmmss = "20110412000000",
                    value="CREATE TABLE IF NOT EXISTS Configuration (_id INTEGER PRIMARY KEY autoincrement, ckey VARCHAR(255), cvalue TEXT)"
                ),
                @Ddl(yyyyMMddHHmmss = "20110412000000",
                    value="create index IF NOT EXISTS keyindex on Configuration(ckey)"
                )
    }
)
public class Configuration {
    @Column(name="_id")
	private Integer id;

    @Column(name = "ckey")
	private String key;

    @Column(name = "cvalue")
	private String value;

    public Configuration() {
    }

    public Configuration(String key, Object obj) {
        this.key = key;
        this.value = obj.toString();
    }

    public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

    public void setValue(String value) {
        this.value = value;
    }

    public void setValue(Object value) {
        this.value = value.toString();
    }

    public int getIntValue(int def) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return def;
        }
    }

    public int getIntValue() {
        return Integer.parseInt(value);
    }

    public long getLongValue(long def) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return def;
        }
    }

    public long getLongValue() {
        return Long.parseLong(value);
    }

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

    public boolean getBoolean() {
        return value != null && (value.equals("1") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes"));
    }
}
