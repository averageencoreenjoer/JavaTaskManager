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
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Task implements Serializable {
    private String title;
    private String description;
    private LocalDate deadline;
    private String priority;
    private boolean isCompleted;

    public Task(String title, String description, String deadline, String priority) {
        this.title = title;
        this.description = description;
        setDeadline(deadline); // используем метод для обработки
        this.priority = priority;
        this.isCompleted = false;
    }

    // Геттеры
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDeadline() { return deadline; }
    public String getPriority() { return priority; }
    public boolean isCompleted() { return isCompleted; }

    // Сеттеры
    public void setDescription(String description) { this.description = description; }
    public void setTitle(String title) { this.title = title; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
   

    // Обновленный метод для установки дедлайна с обработкой ошибок
    public void setDeadline(String deadline) {
        if (deadline != null && !deadline.trim().isEmpty()) {
            try {
                this.deadline = LocalDate.parse(deadline); // пытаемся преобразовать в LocalDate
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter a valid date in the format YYYY-MM-DD.");
            }
        } else {
            this.deadline = null; // если строка пустая, оставляем null
        }
    }

    public void setPriority(String priority) { this.priority = priority; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    @Override
    public String toString() {
        return "[" + (isCompleted ? "✔" : "✘") + "] " + title + " (Priority: " + priority + ", Deadline: " + deadline + ", Decription: " + description + ")";
    }
}