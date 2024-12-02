package com.todo;

import com.todo.entity.ListItem;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "addItemServlet", value = "/addItem")
public class AddItemServlet extends HttpServlet {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ToDoListDB");
    private static final Logger logger = Logger.getLogger(AddItemServlet.class.getName());

    public AddItemServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String task = req.getParameter("task");
        logger.info("Received request to add item: " + task);

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            ListItem newItem = new ListItem(task);
            em.persist(newItem);
            tx.commit();
            logger.info("Successfully added item: " + newItem);
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            logger.log(Level.SEVERE, "Error adding item: " + task, e);
        } finally {
            em.close();
        }

        resp.sendRedirect("index.jsp");
    }
}
