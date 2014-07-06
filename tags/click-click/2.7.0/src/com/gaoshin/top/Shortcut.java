package com.gaoshin.top;

import android.content.Context;
import android.content.Intent;

import com.gaoshin.sorma.annotation.Column;
import com.gaoshin.sorma.annotation.Ddl;
import com.gaoshin.sorma.annotation.Table;

@Table(
        name = "shortcut",
        autoId = true,
        create = {
        "create table if not exists shortcut (" +
                " _id integer PRIMARY KEY autoincrement" +
                ", pkg varchar(255)" +
                ", activity varchar(255)" +
                ", lastAccessTime bigint" +
                ", times integer" +
                ", type varchar(32)" +
                ", label varchar(32)" +
                ", uri varchar(1024)" +
                ", data varchar(1024)" +
                ", groupId integer" +
                ", icon integer" +
                ", sequence integer" +
                ")"
        },
        ddls = {
                @Ddl(
                        yyyyMMddHHmmss = "20110424000000",
                        value = "alter table shortcut add column label varchar(32)"
                    ),
                @Ddl(
                        yyyyMMddHHmmss = "20110623000000",
                        value = "alter table shortcut add column sequence bigint"
                    ),
                @Ddl(
                        yyyyMMddHHmmss = "20110623000000",
                        value = "update shortcut set sequence=_id"
                    )
        },
        keyColumn = "_id")
public class Shortcut {
    @Column(name = "_id")
    private Integer id;

    @Column
    private String pkg;

    @Column
    private String activity;

    @Column
    private String uri;

    @Column
    private String data;

    @Column
    private Long lastAccessTime;

    @Column
    private int times;

    @Column
    private String label;

    @Column
    private ShortcutType type;

    @Column
    private Integer groupId;

    @Column
    private Integer icon;

    @Column
    private Integer sequence;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void setType(ShortcutType type) {
        this.type = type;
    }

    public ShortcutType getType() {
        return type;
    }

    public boolean isActivity(String pkgName, String activityName) {
        return pkgName.equals(this.pkg) && activityName.equals(this.activity);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o.getClass().equals(Shortcut.class)))
            return false;
        Shortcut app = (Shortcut) o;
        return pkg.equals(app.getPkg()) && activity.equals(app.getActivity()) && type.equals(app.getType());
    }

    public void exec(Context context) {
        if (ShortcutType.Launch.equals(type)) {
            if (ShortcutListActivity.class.getName().equals(activity)) {
                Intent intent = new Intent(context, ShortcutGroupActivity.class);
                intent.putExtra("groupId", groupId);
                intent.putExtra("fromListActivity", false);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                ApplicationUtil.runApp(context, this);
            }
        } else if (ShortcutType.Info.equals(type)) {
            ApplicationUtil.showAppInfo(context, pkg);
        } else if (ShortcutType.Kill.equals(type)) {
            ApplicationUtil.kill(context, pkg);
        }
    }

    public String toString() {
        return pkg + "," + activity + "," + type;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getSequence() {
        return sequence;
    }
}
