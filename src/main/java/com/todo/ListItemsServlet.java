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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/listItems")
public class ListItemsServlet extends HttpServlet {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ToDoListDB");
    private static final Logger logger = Logger.getLogger(ListItemsServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("ListItemsServlet accessed to fetch all items.");

        EntityManager em = emf.createEntityManager();
        List<ListItem> items;

        try {
            items = em.createQuery("FROM ListItem", ListItem.class).getResultList();
            logger.info("Retrieved " + items.size() + " items from the database.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving items.", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to retrieve items.");
            return;
        } finally {
            em.close();
        }

        // Store the items in the request scope to make them available in the JSP
        req.setAttribute("items", items);

        // Forward the request to the JSP
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
