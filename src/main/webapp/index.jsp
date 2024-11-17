<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.todo.entity.ListItem" %>
<%@ page import="jakarta.persistence.EntityManager" %>
<%@ page import="jakarta.persistence.EntityManagerFactory" %>
<%@ page import="jakarta.persistence.Persistence" %>
<!DOCTYPE html>
<html>
<head>
    <title>Justin's To-Do List</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="container">
    <h1>To Do List</h1>

    <%
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ToDoListDB");
        EntityManager em = emf.createEntityManager();

        List<ListItem> items = em.createQuery("SELECT i FROM ListItem i", ListItem.class).getResultList();

        em.close();
        emf.close();
    %>

    <% if (items == null || items.isEmpty()) { %>
    <p>No tasks found. <a href="addItem.jsp" class="add-button">Add your first item</a></p>
    <% } else { %>

    <table>
        <thead>
        <tr>
            <th>#</th>
            <th>Task</th>
        </tr>
        </thead>
        <tbody>
        <% int index = 1; %>
        <% for (ListItem item : items) { %>
        <tr>
            <td><%= index++ %></td>
            <td><%= item.getTask() %></td>
        </tr>
        <% } %>
        </tbody>
    </table>

    <a href="addItem.jsp" class="add-button">Add New Task</a>
    <a href="deleteItem.jsp" class="add-button" style="background-color: #dc3545;">Delete Task</a>
    <% } %>
</div>
</body>
</html>
