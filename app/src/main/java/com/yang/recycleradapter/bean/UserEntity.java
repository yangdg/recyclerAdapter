package com.yang.recycleradapter.bean;

/**
 * Created by Administrator on 2017/10/9.
 */

import java.io.Serializable;

public class UserEntity implements Serializable{
    private static final long serialVersionUID = 8483082023664242231L;
    private String Id;//id
    private String Name;//群组名称
    private String proImg;//头像
    private boolean hasUnderline=true;//绘制view使用,标识是否需要底部线

    public UserEntity(String id, String name, boolean hasUnderline) {
        Id = id;
        Name = name;
        this.hasUnderline = hasUnderline;
    }

    public UserEntity(String id, String name) {
        Id = id;
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProImg() {
        return proImg;
    }

    public void setProImg(String proImg) {
        this.proImg = proImg;
    }

    public boolean isHasUnderline() {
        return hasUnderline;
    }

    public void setHasUnderline(boolean hasUnderline) {
        this.hasUnderline = hasUnderline;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserEntity{");
        sb.append("Id='").append(Id).append('\'');
        sb.append(", Name='").append(Name).append('\'');
        sb.append(", proImg='").append(proImg).append('\'');
        sb.append(", hasUnderline=").append(hasUnderline);
        sb.append('}');
        return sb.toString();
    }
}
