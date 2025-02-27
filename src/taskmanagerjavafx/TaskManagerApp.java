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
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.time.LocalDate;

public class TaskManagerApp extends Application {
    private TaskManager taskManager = new TaskManager();
    private ObservableList<String> taskList = FXCollections.observableArrayList();

    private ComboBox<String> filterComboBox = new ComboBox<>();
    private ComboBox<String> sortComboBox = new ComboBox<>();
    private TextField searchField = new TextField();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ListView<String> listView = new ListView<>(taskList);

        TextField titleField = new TextField();
        titleField.setPromptText("Task Title");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        TextField deadlineField = new TextField();
        deadlineField.setPromptText("Deadline (YYYY-MM-DD)");

        ComboBox<String> priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().addAll("Low", "Medium", "High");
        priorityComboBox.setValue("Medium");

        Button addButton = new Button("Add Task");
        addButton.setOnAction(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String deadline = deadlineField.getText();
            String priority = priorityComboBox.getValue();

            if (!title.isEmpty()) {
                taskManager.addTask(new Task(title, description, deadline, priority));
                updateTaskList();
                clearFields(titleField, descriptionField, deadlineField, priorityComboBox);
            }
        });

        Button removeButton = new Button("Remove Task");
        removeButton.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String taskTitle = selected.substring(4, selected.indexOf("(")).trim();
                taskManager.removeTask(taskTitle);
                updateTaskList();
            }
        });

        Button editButton = new Button("Edit Task");
        editButton.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String taskTitle = selected.substring(4, selected.indexOf("(")).trim();
                String newDescription = descriptionField.getText();
                String newDeadline = deadlineField.getText();
                String newPriority = priorityComboBox.getValue();
                
                LocalDate newDeadlineDate = newDeadline.isEmpty() ? null : LocalDate.parse(newDeadline); 
                
                taskManager.editTask(taskTitle, descriptionField.getText(), deadlineField.getText(), priorityComboBox.getValue());
                updateTaskList();
                clearFields(titleField, descriptionField, deadlineField, priorityComboBox);
            }
        });

        Button completeButton = new Button("Mark as Completed");
        completeButton.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String taskTitle = selected.substring(4, selected.indexOf("(")).trim();
                taskManager.markTaskAsCompleted(taskTitle);
                updateTaskList();
            }
        });

        // Фильтрация
        filterComboBox.getItems().addAll("All", "Completed", "Incomplete");
        filterComboBox.setValue("All");
        filterComboBox.setOnAction(e -> updateTaskList());

        // Сортировка
        sortComboBox.getItems().addAll("Priority", "Deadline", "Status");
        sortComboBox.setValue("Priority");
        sortComboBox.setOnAction(e -> updateTaskList());

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleField, descriptionField, deadlineField, priorityComboBox,
                addButton, removeButton, editButton, completeButton, filterComboBox, sortComboBox, searchField, listView);

        addButton.setMinWidth(150);
        editButton.setMinWidth(150);
        removeButton.setMinWidth(150);
        
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setTitle("Task Manager");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Инициализация списка
        updateTaskList();
    }
    
    private void updateTaskList() {
    // Получаем текущие задачи
    List<Task> tasks = taskManager.getTasks();

    // Фильтруем задачи
    String filterValue = filterComboBox.getValue();
    if (filterValue.equals("Completed")) {
        tasks = tasks.stream().filter(Task::isCompleted).collect(Collectors.toList());
    } else if (filterValue.equals("Incomplete")) {
        tasks = tasks.stream().filter(task -> !task.isCompleted()).collect(Collectors.toList());
    }

    // Сортируем задачи
    String sortValue = sortComboBox.getValue();
    if (sortValue.equals("Priority")) {
        tasks.sort(Comparator.comparing(Task::getPriority));
    } else if (sortValue.equals("Deadline")) {
        tasks.sort(Comparator.comparing(Task::getDeadline));
    } else if (sortValue.equals("Status")) {
        tasks.sort(Comparator.comparing(Task::isCompleted));
    }

    // Обновляем отображение задач
    taskList.clear();
    for (Task task : tasks) {
        taskList.add(task.toString());
    }
}

    private void clearFields(TextField titleField, TextField descriptionField, TextField deadlineField, ComboBox<String> priorityComboBox) {
        titleField.clear();
        descriptionField.clear();
        deadlineField.clear();
        priorityComboBox.setValue("Medium");
    }
}