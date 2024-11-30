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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(name = "addItemServlet", value = "/addItem")
public class AddItemServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AddItemServlet.class);
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ToDoListDB");

    public AddItemServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String task = req.getParameter("task");
        logger.info("Received request to add new item with task description: {}", task);

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            ListItem newItem = new ListItem(task);
            em.persist(newItem);
            tx.commit();
            logger.info("Successfully added new item: {}", newItem);
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
                logger.warn("Transaction rolled back due to an error.");
            }
            logger.error("Error adding new item with task description: {}", task, e);
        } finally {
            em.close();
            logger.debug("EntityManager closed after adding an item.");
        }

        resp.sendRedirect("index.jsp");
    }
}

