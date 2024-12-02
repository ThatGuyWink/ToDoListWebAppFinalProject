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

@WebServlet(name = "deleteItemServlet", value = "/deleteItem")
public class DeleteItemServlet extends HttpServlet {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ToDoListDB");
    private static final Logger logger = Logger.getLogger(DeleteItemServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        logger.info("Received request to delete item with ID: " + idParam);

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            int id = Integer.parseInt(idParam);
            tx.begin();
            ListItem item = em.find(ListItem.class, id);
            if (item != null) {
                em.remove(item);
                tx.commit();
                logger.info("Successfully deleted item with ID: " + id);
            } else {
                logger.warning("Item with ID: " + id + " not found.");
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            logger.log(Level.SEVERE, "Error deleting item with ID: " + idParam, e);
        } finally {
            em.close();
        }

        // Redirect back to the main list page
        resp.sendRedirect("index.jsp");
    }
}
