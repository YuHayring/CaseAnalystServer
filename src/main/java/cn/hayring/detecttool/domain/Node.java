package cn.hayring.detecttool.domain;

import com.google.gson.annotations.SerializedName;

public abstract class Node {
    public abstract Long getId();

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public abstract String getName();

    protected Integer level;

    protected String info;


    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLabel() {
        return this.getClass().getSimpleName();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public static final String PERSON = "Person";

    public static final String EVENT = "Event";

    public static final String THING = "Thing";

    public static final String PLACE = "Place";
}
