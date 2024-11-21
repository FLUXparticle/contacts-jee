package com.example.contacts.entity;

import jakarta.persistence.*;

import java.io.*;

@Entity
@Table(name = "contacts")
public class Contact implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String email;
    private String address;

    // Konstruktoren
    public Contact() {
        // empty
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
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

	public String getEmail() {
		return email;
	}

    public void setEmail(String email) {
        this.email = email;
    }

	public String getAddress() {
		return address;
	}

    public void setAddress(String address) {
        this.address = address;
    }

}
