package com.todo;

import com.todo.entity.ListItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public static void main(String[] args) {
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

            switch (choice) {
                case 1 -> addItem(scanner);
                case 2 -> deleteItem(scanner);
                case 3 -> viewItems();
                case 4 -> {
                    running = false;
                    emf.close();
                    System.out.println("Exiting program.");
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
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
            System.out.println("Item added: " + newItem);
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
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
                System.out.println("Item deleted: " + item);
            } else {
                System.out.println("Item not found with ID: " + id);
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void viewItems() {
        EntityManager em = emf.createEntityManager();
        List<ListItem> items = em.createQuery("from ListItem", ListItem.class).getResultList();

        if (items.isEmpty()) {
            System.out.println("No items found.");
        } else {
            for (ListItem item : items) {
                System.out.println("ID: " + item.getId() + ", Task: " + item.getTask());
            }
        }

        em.close();
    }
}
