<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="jakarta.persistence.EntityManager" %>
<%@ page import="jakarta.persistence.EntityManagerFactory" %>
<%@ page import="jakarta.persistence.Persistence" %>
<%@ page import="com.todo.entity.ListItem" %>
<!DOCTYPE html>
<html>
<head>
    <title>Delete To-Do Item</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="container">
    <h1>Delete To-Do Item</h1>
    <div class="form-group">
        <form action="deleteItem.jsp" method="get">
            <label for="itemId">Select Item to Delete:</label>
            <select id="itemId" name="id" required>
                <%
                    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ToDoListDB");
                    EntityManager em = emf.createEntityManager();
                    List<ListItem> items = em.createQuery("SELECT i FROM ListItem i", ListItem.class).getResultList();
                    em.close();
                    emf.close();

                    for (ListItem item : items) {
                %>
                <option value="<%= item.getId() %>"><%= item.getTask() %></option>
                <%
                    }
                %>
            </select>
            <button type="submit" class="submit-button">Delete Item</button>
        </form>
    </div>
    <%
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            try {
                Long id = Long.parseLong(idParam);
                emf = Persistence.createEntityManagerFactory("ToDoListDB");
                em = emf.createEntityManager();

                ListItem itemToDelete = em.find(ListItem.class, id);

                if (itemToDelete != null) {
                    em.getTransaction().begin();
                    em.remove(itemToDelete);
                    em.getTransaction().commit();
    %>
    <p>Task with ID <%= id %> was successfully deleted.</p>
    <%
    } else {
    %>
    <p>Item not found. Please try again.</p>
    <%
        }
        em.close();
        emf.close();
    } catch (NumberFormatException e) {
    %>
    <p>Invalid ID format. Please try again.</p>
    <%
            }
        }
    %>
    <a href="index.jsp" class="back-link">Back to To-Do List</a>
</div>
</body>
</html>
