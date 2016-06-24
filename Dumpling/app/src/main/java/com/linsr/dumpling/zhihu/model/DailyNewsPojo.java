package com.linsr.dumpling.zhihu.model;

import com.linsr.dumpling.app.model.BasePojo;

import java.util.Arrays;
import java.util.List;

/**
 * description
 *
 * @author Linsr
 */
public class DailyNewsPojo extends BasePojo {

    private int date;

    private List<StoriesPojo> stories;

    private TopStories[] top_stories;

    public int getDate() {

        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public List<StoriesPojo> getStories() {
        return stories;
    }

    public void setStories(List<StoriesPojo> stories) {
        this.stories = stories;
    }

    public TopStories[] getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(TopStories[] top_stories) {
        this.top_stories = top_stories;
    }

    class TopStories {
        private String images;
        private String type;
        private String id;
        private String title;
        private String ga_prefix;

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
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
            return "TopStories{" +
                    "images='" + images + '\'' +
                    ", type='" + type + '\'' +
                    ", ID='" + id + '\'' +
                    ", TITLE='" + title + '\'' +
                    ", ga_prefix='" + ga_prefix + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DailyNewsPojo{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                ", top_stories=" + Arrays.toString(top_stories) +
                '}';
    }
}
