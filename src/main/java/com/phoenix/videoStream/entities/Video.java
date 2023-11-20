package com.phoenix.videoStream.entities;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Video extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(mappedBy = "video")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<History> histories = new LinkedList();

    public User getUser() {
        return user;
    }

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(History history) {
        histories.add(history);
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    private User user;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(length = 100000)
    private String description;
    private String thumbnail;
    private String video;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
