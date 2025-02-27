/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskmanagerjavafx;

/**
 *
 * @author user113
 */
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeParseException;

public class TaskManager {
    private List<Task> tasks;
    private static final String FILE_NAME = "tasks.dat";

    public TaskManager() {
        this.tasks = new ArrayList<>();
        loadTasks();
    }

    public Task findTaskByTitle(String title) {
        for (Task task : tasks) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                return task;
            }
        }
        return null;
    }
        
    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
    }

    public void removeTask(String title) {
        tasks.removeIf(task -> task.getTitle().equalsIgnoreCase(title));
        saveTasks();
    }
    
    public void editTask(String title, String newDescription) {
    Task task = findTaskByTitle(title);
    if (task != null) {
        task.setDescription(newDescription);
        saveTasks();
    }
}
    
    public void editTask(String title, String description, String deadline, String priority) {
    Task task = findTaskByTitle(title); // Поиск задачи по заголовку

    if (task != null) {
        task.setDescription(description);
        task.setPriority(priority);

        // Проверка на пустое значение дедлайна
        if (deadline != null && !deadline.isEmpty()) {
            try {
                task.setDeadline(LocalDate.parse(deadline)); // Преобразуем строку в LocalDate
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Using current deadline.");
                // В случае ошибки можно оставить старую дату или сделать что-то другое
        }
            saveTasks();
            } else {
            System.out.println("Task not found.");
        }
    }
}
    public void markTaskAsCompleted(String title) {
        for (Task task : tasks) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                task.setCompleted(true);
                saveTasks();
                break;
            }
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }

    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            tasks = (List<Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            tasks = new ArrayList<>();
        }
    }

    public void exportTasksToCSV(String fileName) {
        try (PrintWriter writer = new PrintWriter(new File(fileName))) {
            writer.println("Title,Description,Deadline,Priority,Completed");
            for (Task task : tasks) {
                writer.println(task.getTitle() + "," + task.getDescription() + "," + task.getDeadline() + "," + task.getPriority() + "," + task.isCompleted());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void importTasksFromCSV(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            reader.readLine(); // Пропускаем заголовок
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                tasks.add(new Task(parts[0], parts[1], parts[2], parts[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkDeadlines() {
        LocalDate now = LocalDate.now();
        for (Task task : tasks) {
            LocalDate deadline = task.getDeadline();
            if (deadline.isBefore(now.plusDays(3))) {
                System.out.println("Task '" + task.getTitle() + "' is due soon!");
            }
        }
    }
}
