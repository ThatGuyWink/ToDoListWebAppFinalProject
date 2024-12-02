package com.todo;

import com.todo.entity.ListItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.*;

public class Main {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ToDoListDB");
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    static {
        try {
            setupLogger();
        } catch (Exception e) {
            System.err.println("Failed to set up logger: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        logger.info("To Do List application started.");

        while (running) {
            System.out.println("\n--- To Do List Application ---");
            System.out.println("1. Add an Item");
            System.out.println("2. Delete an Item");
            System.out.println("3. View All Items");
            System.out.println("4. Exit");

            System.out.print("Please choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1 -> {
                    logger.info("Selected option 1: Add an Item.");
                    addItem(scanner);
                }
                case 2 -> {
                    logger.info("Selected option 2: Delete an Item.");
                    deleteItem(scanner);
                }
                case 3 -> {
                    logger.info("Selected option 3: View All Items.");
                    viewItems();
                }
                case 4 -> {
                    logger.info("Selected option 4: Exit.");
                    running = false;
                    emf.close();
                    System.out.println("Exiting program.");
                }
                default -> {
                    logger.warning("Invalid choice entered: " + choice);
                    System.out.println("Invalid choice, please try again.");
                }
            }
        }

        logger.info("To Do List application stopped.");
    }

    private static void addItem(Scanner scanner) {
        System.out.print("Enter the task description: ");
        String task = scanner.nextLine();

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            ListItem newItem = new ListItem(task);  // Pass the task to the constructor
            em.persist(newItem);
            tx.commit();
            logger.info("Item added successfully: " + newItem);
            System.out.println("Item added: " + newItem);
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            logger.log(Level.SEVERE, "Error adding item: " + task, e);
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void deleteItem(Scanner scanner) {
        System.out.print("Enter the ID of the item to delete: ");
        Long id = scanner.nextLong();

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            ListItem item = em.find(ListItem.class, id);
            if (item != null) {
                em.remove(item);
                tx.commit();
                logger.info("Item deleted successfully: " + item);
                System.out.println("Item deleted: " + item);
            } else {
                logger.warning("Item not found with ID: " + id);
                System.out.println("Item not found with ID: " + id);
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            logger.log(Level.SEVERE, "Error deleting item with ID: " + id, e);
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void viewItems() {
        EntityManager em = emf.createEntityManager();
        List<ListItem> items;

        try {
            items = em.createQuery("from ListItem", ListItem.class).getResultList();

            if (items.isEmpty()) {
                logger.info("No items found in the list.");
                System.out.println("No items found.");
            } else {
                for (ListItem item : items) {
                    System.out.println("ID: " + item.getId() + ", Task: " + item.getTask());
                }
                logger.info("Displayed " + items.size() + " items.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving items.", e);
        } finally {
            em.close();
        }
    }

    private static void setupLogger() throws IOException {
        // Ensure the logs directory exists
        String logsDirectory = "logs";
        java.nio.file.Path logsPath = java.nio.file.Paths.get(logsDirectory);
        if (!java.nio.file.Files.exists(logsPath)) {
            java.nio.file.Files.createDirectory(logsPath);
        }

        // Create a FileHandler to write logs to a file in the logs directory
        String logFilePath = logsDirectory + "/todo_list.log";
        Handler fileHandler = new FileHandler(logFilePath, true); // Append mode
        fileHandler.setFormatter(new SimpleFormatter());

        // Configure the root logger to use this file handler
        Logger rootLogger = Logger.getLogger("");
        rootLogger.addHandler(fileHandler);
        rootLogger.setLevel(Level.ALL);

        logger.info("Logger initialized. Writing logs to " + logFilePath);
    }
}
