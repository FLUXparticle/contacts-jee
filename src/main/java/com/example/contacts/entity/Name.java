package com.example.contacts.entity;

import jakarta.persistence.*;

import java.io.*;

@Entity
@Table(name = "names")
public class Name implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    // Konstruktoren
    public Name() {
        // empty
    }

    public Name(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Name{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}