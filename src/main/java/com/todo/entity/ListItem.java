package com.todo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ListItem", schema = "ToDoListDB")
public class ListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "task", nullable = false)
    private String task;

    public ListItem(String task) {
        this.task = task;
    }

    public ListItem() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "ListItem{id=" + id + ", task='" + task + '\'' + '}';
    }
}
