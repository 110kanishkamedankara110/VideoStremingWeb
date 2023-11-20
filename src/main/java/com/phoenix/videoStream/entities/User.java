package com.phoenix.videoStream.entities;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    private String password;
    @OneToMany(mappedBy = "user")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<History> histories = new LinkedList();

    public List<History> getHistory() {
        return histories;
    }

    public void setHistory(History history) {
        histories.add(history);
    }

    public void setVideo(List<Video> video) {
        this.video = video;
    }

    @ManyToOne
    private UserType userType;
    @OneToMany(mappedBy = "user")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Video> video = new LinkedList();

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<Video> getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video.add(video);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
