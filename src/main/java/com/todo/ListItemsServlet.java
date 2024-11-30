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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("/listItems")
public class ListItemsServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ListItemsServlet.class);
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ToDoListDB");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Received request to list all items.");

        EntityManager em = emf.createEntityManager();
        List<ListItem> items;

        try {
            items = em.createQuery("FROM ListItem", ListItem.class).getResultList();
            if (items.isEmpty()) {
                logger.info("No items found in the database.");
            } else {
                logger.info("Retrieved {} item(s) from the database.", items.size());
            }
            req.setAttribute("items", items);
        } catch (Exception e) {
            logger.error("Error retrieving items from the database.", e);
            throw new ServletException("Error retrieving items.", e);
        } finally {
            em.close();
            logger.debug("EntityManager closed after listing items.");
        }

        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
