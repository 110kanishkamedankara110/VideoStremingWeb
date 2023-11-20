package com.phoenix.videoStream.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
public class UserType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_type")
    private String userType;
    @OneToMany(mappedBy = "userType")
    private List<User> users = new LinkedList();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(User user) {
        users.add(user);
    }


}
