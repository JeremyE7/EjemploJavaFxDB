/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package testdb;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class ViewDialogController implements Initializable {

    @FXML
    private TextField txtFieldNombre;
    @FXML
    private TextField txtFieldDesc;
    @FXML
    private DatePicker dPickFecha;
    @FXML
    private ComboBox<String> cBoxPrior;
    @FXML
    private ComboBox<String> cBoxEstado;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;

    private Stage dialogStage; // La Stage del diálogo
    private Task task;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configura el ComboBox de Prioridad
        ObservableList<String> prioridades = FXCollections.observableArrayList(
                "Alta", "Media", "Baja"
        );
        cBoxPrior.setItems(prioridades);

        // Configura el ComboBox de Estado
        ObservableList<String> estados = FXCollections.observableArrayList(
                "Pendiente", "En Proceso", "Completada"
        );
        cBoxEstado.setItems(estados);
        
        cBoxPrior.setValue("Alta"); // Establece "Alta" como valor por defecto para el ComboBox de Prioridad
        cBoxEstado.setValue("Pendiente");
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void closeModal(ActionEvent event) {
        // Cierra la ventana de diálogo
        dialogStage.close();
    }

    @FXML
    private void addTask(ActionEvent event) {
        String nombre = txtFieldNombre.getText();
        String descripcion = txtFieldDesc.getText();
        String estado = cBoxEstado.getValue();
        String prioridad = cBoxPrior.getValue();
        String date = dPickFecha.getValue().toString();
        
        this.task = new Task(nombre, descripcion, date, prioridad, estado);
        dialogStage.close();
    }

}
