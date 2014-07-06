package com.gaoshin.fandroid;

public class Recent {
    private Integer id;
    private RecentType type;
    private Long updated;
    private int rank;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RecentType getType() {
        return type;
    }

    public void setType(RecentType type) {
        this.type = type;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

}
