package com.linsr.dumpling.zhihu.model;

import com.linsr.dumpling.app.model.BasePojo;

import java.util.Arrays;

/**
 * description
 *
 * @author Linsr
 */
public class StoriesPojo extends BasePojo {

    private String[] images;
    private String type;
    private String id;
    private String title;
    private String ga_prefix;

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    @Override
    public String toString() {
        return "StoriesPojo{" +
                "images=" + Arrays.toString(images) +
                ", type='" + type + '\'' +
                ", ID='" + id + '\'' +
                ", TITLE='" + title + '\'' +
                ", ga_prefix='" + ga_prefix + '\'' +
                '}';
    }
}
