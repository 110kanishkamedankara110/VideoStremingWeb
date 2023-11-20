package com.phoenix.videoStream.dto;

import javax.servlet.http.Part;
import java.io.InputStream;

public class UpdateDto {
    private String title;
    private String description;
    private int id;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
