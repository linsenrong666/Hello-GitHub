package com.linsr.dumpling.zhihu.model;

import com.linsr.dumpling.app.model.BasePojo;

import java.util.Arrays;

/**
 * description
 *
 * @author Linsr
 */
public class DailyDetailsPojo extends BasePojo {

    private String body;
    private String image_source;
    private String title;
    private String image;
    private String share_url;
    private String[] js;
    private String ga_prefix;
    private String[] images;
    private String type;
    private String id;
    private String[] css;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String[] getJs() {
        return js;
    }

    public void setJs(String[] js) {
        this.js = js;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

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

    public String[] getCss() {
        return css;
    }

    public void setCss(String[] css) {
        this.css = css;
    }

    @Override
    public String toString() {
        return "DailyDetailsPojo{" +
                "body='" + body + '\'' +
                ", image_source='" + image_source + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", share_url='" + share_url + '\'' +
                ", js=" + Arrays.toString(js) +
                ", ga_prefix='" + ga_prefix + '\'' +
                ", images=" + Arrays.toString(images) +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", css=" + Arrays.toString(css) +
                '}';
    }
}
