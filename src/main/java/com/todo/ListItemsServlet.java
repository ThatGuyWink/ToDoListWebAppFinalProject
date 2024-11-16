package com.todo;

import com.todo.entity.ListItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.IOException;
import java.util.List;

@WebServlet("/listItems")
public class ListItemsServlet extends HttpServlet {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ToDoListDB");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManager em = emf.createEntityManager();
        List<ListItem> items;

        try {
            items = em.createQuery("FROM ListItem", ListItem.class).getResultList();
        } finally {
            em.close();
        }

        // Store the items in the request scope to make them available in the JSP
        req.setAttribute("items", items);


        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
