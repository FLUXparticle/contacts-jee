package com.example.contacts.controller;

import com.example.contacts.entity.*;
import com.example.contacts.service.*;
import jakarta.faces.view.*;
import jakarta.inject.*;

import java.io.*;
import java.util.*;

@Named
@ViewScoped
public class TransactionBean implements Serializable {

    @Inject
    private ContactService contactService;

    private Long contactId;

    private List<Transaction> transactions;

    public void loadTransactions() {
        System.out.println("contactId = " + contactId);
        if (contactId != null) {
            transactions = contactService.getTransactionsByContactId(contactId);
            System.out.println("transactions = " + transactions);
        }
    }

    // Getter und Setter
    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        System.out.println("TransactionBean.setContactId");
        this.contactId = contactId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

}
