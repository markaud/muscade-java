package com.markaud.muscade.domain;

public class SourceStatistic {
    private String name;
    private Integer itemCount;

    public SourceStatistic(String name, Integer itemCount) {
        this.name = name;
        this.itemCount = itemCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }
}
