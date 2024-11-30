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

@WebServlet(name = "deleteItemServlet", value = "/deleteItem")
public class DeleteItemServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DeleteItemServlet.class);
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ToDoListDB");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        logger.info("Received request to delete item with ID: {}", idParam);

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            int id = Integer.parseInt(idParam);
            tx.begin();
            ListItem item = em.find(ListItem.class, id);
            if (item != null) {
                em.remove(item);
                tx.commit();
                logger.info("Successfully deleted item: {}", item);
            } else {
                logger.warn("Item not found with ID: {}", id);
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
                logger.warn("Transaction rolled back due to an error.");
            }
            logger.error("Error deleting item with ID: {}", idParam, e);
        } finally {
            em.close();
            logger.debug("EntityManager closed after deleting an item.");
        }

        resp.sendRedirect("index.jsp");
    }
}
