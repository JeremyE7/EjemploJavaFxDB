/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testdb;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.util.Optional;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, Integer> idColumn;
    @FXML
    private TableColumn<Task, String> nameColumn;
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    @FXML
    private TableColumn<Task, String> dueDateColumn;
    @FXML
    private TableColumn<Task, String> priorityColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;
    @FXML
    private PieChart taskChart;

    private ObservableList<Task> taskList;

    public void initialize() {
        DB.initializeDatabase();
        taskList = FXCollections.observableArrayList(DB.getAllTasks());

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        taskTable.setItems(taskList);

        updateChart();
    }

    @FXML
    private void handleAddTask(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewDialog.fxml"));
            DialogPane dialogPane = loader.load();
            ViewDialogController controller = loader.getController();

            Stage dialogStage = new Stage();
            controller.setDialogStage(dialogStage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Añadir Tarea");
            dialogStage.setResizable(false); // Hacer que la ventana no sea redimensionable

            Scene scene = new Scene(dialogPane); // Usamos directamente el DialogPane como raíz de la escena

            dialogStage.setScene(scene);
            dialogStage.showAndWait();
            System.out.println(controller.getTask());
            DB.addTask(controller.getTask());
            taskList.add(controller.getTask());
            updateChart();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditTask(ActionEvent event) {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            TextInputDialog dialog = new TextInputDialog(selectedTask.getName());
            dialog.setTitle("Editar Tarea");
            dialog.setHeaderText("Editar Nombre de la Tarea");
            dialog.setContentText("Nombre:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                selectedTask.setName(name);
                DB.updateTask(selectedTask);
            });

            updateChart();
        }
    }

    @FXML
    private void handleDeleteTask(ActionEvent event) {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskList.remove(selectedTask);
            DB.deleteTask(selectedTask.getId());
            updateChart();
        }
    }

    private void updateChart() {
        int completed = 0;
        int inProgress = 0;
        int pending = 0;

        for (Task task : taskList) {
            switch (task.getStatus()) {
                case "Completada":
                    completed++;
                    break;
                case "En Progreso":
                    inProgress++;
                    break;
                case "Pendiente":
                    pending++;
                    break;
            }
        }

        taskChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Completadas", completed),
                new PieChart.Data("En Progreso", inProgress),
                new PieChart.Data("Pendientes", pending)
        ));
    }

    private void checkReminders() {
        for (Task task : taskList) {
            // Implementar lógica de recordatorio
            if (task.getStatus().equals("Pendiente") && task.getDueDate().equals("2024-12-31")) { // Ejemplo de recordatorio
                System.out.println("Recordatorio: " + task.getName() + " vence pronto.");
            }
        }
    }
}
