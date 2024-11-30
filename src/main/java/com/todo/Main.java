package com.todo;

import com.todo.entity.ListItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ToDoListDB");

    public static void main(String[] args) {
        MDC.put("application", "ToDoListApp"); // Add application-wide context
        logger.info("Starting Justin's To-Do List application...");
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Justin's To Do List Application ---");
            System.out.println("1. Add an Item");
            System.out.println("2. Delete an Item");
            System.out.println("3. View All Items");
            System.out.println("4. Exit");

            System.out.print("Please choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            MDC.put("menuChoice", String.valueOf(choice)); // Add context for menu choice
            switch (choice) {
                case 1 -> addItem(scanner);
                case 2 -> deleteItem(scanner);
                case 3 -> viewItems();
                case 4 -> {
                    running = false;
                    emf.close();
                    logger.info("Exiting the application and closing EntityManagerFactory.");
                    System.out.println("Exiting program.");
                }
                default -> {
                    logger.warn("Invalid choice entered: {}", choice);
                    System.out.println("Invalid choice, please try again.");
                }
            }
            MDC.remove("menuChoice"); // Clear context for menu choice
        }
        MDC.clear(); // Clear all context at the end
    }

    private static void addItem(Scanner scanner) {
        System.out.print("Enter the task description: ");
        String task = scanner.nextLine();

        MDC.put("action", "addItem"); // Add action context
        MDC.put("taskDescription", task); // Add task description context
        logger.info("User chose to add an item with description: {}", task);

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            ListItem newItem = new ListItem(task);  // Pass the task to the constructor
            em.persist(newItem);
            tx.commit();
            logger.info("Item successfully added: {}", newItem);
            System.out.println("Item added: " + newItem);
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
                logger.warn("Transaction rolled back due to an error.");
            }
            logger.error("Error adding item: {}", task, e);
        } finally {
            em.close();
            logger.debug("EntityManager closed after adding an item.");
        }

        MDC.remove("action");
        MDC.remove("taskDescription");
    }

    private static void deleteItem(Scanner scanner) {
        System.out.print("Enter the ID of the item to delete: ");
        Long id = scanner.nextLong();

        MDC.put("action", "deleteItem"); // Add action context
        MDC.put("itemId", String.valueOf(id)); // Add item ID context
        logger.info("User chose to delete an item with ID: {}", id);

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            ListItem item = em.find(ListItem.class, id);
            if (item != null) {
                em.remove(item);
                tx.commit();
                logger.info("Item successfully deleted: {}", item);
                System.out.println("Item deleted: " + item);
            } else {
                logger.warn("Item not found with ID: {}", id);
                System.out.println("Item not found with ID: " + id);
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
                logger.warn("Transaction rolled back due to an error.");
            }
            logger.error("Error deleting item with ID: {}", id, e);
        } finally {
            em.close();
            logger.debug("EntityManager closed after deleting an item.");
        }

        MDC.remove("action");
        MDC.remove("itemId");
    }

    private static void viewItems() {
        MDC.put("action", "viewItems"); // Add action context
        logger.info("User chose to view all items.");

        EntityManager em = emf.createEntityManager();

        try {
            List<ListItem> items = em.createQuery("from ListItem", ListItem.class).getResultList();
            if (items.isEmpty()) {
                logger.info("No items found in the database.");
                System.out.println("No items found.");
            } else {
                logger.info("Retrieved {} item(s) from the database.", items.size());
                for (ListItem item : items) {
                    System.out.println("ID: " + item.getId() + ", Task: " + item.getTask());
                }
            }
        } catch (Exception e) {
            logger.error("Error retrieving items from the database.", e);
        } finally {
            em.close();
            logger.debug("EntityManager closed after viewing items.");
        }

        MDC.remove("action");
    }
}
