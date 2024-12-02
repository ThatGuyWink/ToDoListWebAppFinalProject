package com.example;

import java.util.ArrayList;
import java.util.List;

public class TodoList {
    private List<String> items; // A list to store todo items

    // Constructor
    public TodoList() {
        this.items = new ArrayList<>();
    }

    // Method to add an item
    public void addItem(String item) {
        items.add(item); // Add the item to the list
    }

    // Method to get all items
    public List<String> getItems() {
        return items; // Return the list of items
    }

    // Method to remove an item
    public void removeItem(String item) {
        items.remove(item); // Remove the item from the list
    }
}
