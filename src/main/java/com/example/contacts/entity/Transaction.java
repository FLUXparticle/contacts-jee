package com.example.contacts.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "transactions")
public class Transaction {
  @Id
  @GeneratedValue
  private Long id;
  @ManyToOne
  @JoinColumn(name = "contact_id")
  private Contact contact;
  private Date timestamp;
  private String action;
  @Column(length = 1000)
  private String details;
  // Konstruktoren
  public Transaction() {}
  public Transaction(Contact contact, String action, String details) {
    this.contact = contact;
    this.action = action;
    this.details = details;
    this.timestamp = new Date();
  }

  // Getter und Setter

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Contact getContact() {
    return contact;
  }

  public void setContact(Contact contact) {
    this.contact = contact;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

}
