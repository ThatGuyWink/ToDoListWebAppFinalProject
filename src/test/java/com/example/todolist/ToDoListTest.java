package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TodoListTest {

    @Test
    void testAddItem() {
        TodoList todoList = new TodoList();
        todoList.addItem("Buy groceries");
        assertEquals(1, todoList.getItems().size());
        assertTrue(todoList.getItems().contains("Buy groceries"));
    }

    @Test
    void testRemoveItem() {
        TodoList todoList = new TodoList();
        todoList.addItem("Buy groceries");
        todoList.removeItem("Buy groceries");
        assertEquals(0, todoList.getItems().size());
        assertFalse(todoList.getItems().contains("Buy groceries"));
    }

    @Test
    void testGetItems() {
        TodoList todoList = new TodoList();
        todoList.addItem("Buy groceries");
        todoList.addItem("Do laundry");
        assertEquals(2, todoList.getItems().size());
        assertTrue(todoList.getItems().contains("Buy groceries"));
        assertTrue(todoList.getItems().contains("Do laundry"));
    }
}
