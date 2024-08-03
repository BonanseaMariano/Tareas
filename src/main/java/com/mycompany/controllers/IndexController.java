package com.mycompany.controllers;

import com.mycompany.models.Tarea;
import com.mycompany.services.TareaServicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class IndexController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private TareaServicio tareaServicio;

    @javafx.fxml.FXML
    private TableColumn<Tarea, String> nombreTareaColumna;

    @javafx.fxml.FXML
    private TableColumn<Tarea, String> estatusTareaColumna;

    @javafx.fxml.FXML
    private TableColumn<Tarea, String> responsableTareaColumna;

    @javafx.fxml.FXML
    private TableColumn<Tarea, Integer> idTareaColumna;

    @javafx.fxml.FXML
    private TableView<Tarea> tareaTabla;

    private final ObservableList<Tarea> tareaList = FXCollections.observableArrayList();

    @javafx.fxml.FXML
    private TextField responsableTareaTexto;

    @javafx.fxml.FXML
    private TextField estatusTareaTexto;

    @javafx.fxml.FXML
    private TextField nombreTareaTexto;

    private Integer idTareaInterno;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tareaTabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        configurarColumnas();
        listarTareas();
    }

    private void configurarColumnas() {
        idTareaColumna.setCellValueFactory(new PropertyValueFactory<>("idTarea"));
        nombreTareaColumna.setCellValueFactory(new PropertyValueFactory<>("nombreTarea"));
        responsableTareaColumna.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        estatusTareaColumna.setCellValueFactory(new PropertyValueFactory<>("estatus"));
    }

    private void listarTareas() {
        logger.info("Listando tareas");
        tareaList.clear();
        tareaList.addAll(tareaServicio.listarTareas());
        tareaTabla.setItems(tareaList);
    }

    @javafx.fxml.FXML
    public void agregarTarea() {
        if (nombreTareaTexto.getText().isEmpty() || responsableTareaTexto.getText().isEmpty() || estatusTareaTexto.getText().isEmpty()) {
            mostrarMensaje("Error", "Todos los campos son obligatorios", Alert.AlertType.ERROR);
            return;
        }

        Tarea tarea = new Tarea();
        recolectarDatosFormulario(tarea);
        tarea.setIdTarea(null);
        tareaServicio.guardarTarea(tarea);
        mostrarMensaje("Informacion", "Tarea guardada con exito", Alert.AlertType.INFORMATION);
        limpiarFormulario();
        listarTareas();
    }

    private void mostrarMensaje(String titulo, String mensaje, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void recolectarDatosFormulario(Tarea tarea) {
        if (idTareaInterno != null) {
            tarea.setIdTarea(idTareaInterno);
        }
        tarea.setNombreTarea(nombreTareaTexto.getText());
        tarea.setResponsable(responsableTareaTexto.getText());
        tarea.setEstatus(estatusTareaTexto.getText());
    }

    @javafx.fxml.FXML
    public void limpiarFormulario() {
        idTareaInterno = null;
        nombreTareaTexto.clear();
        responsableTareaTexto.clear();
        estatusTareaTexto.clear();
    }

    @javafx.fxml.FXML
    public void cargarTareaFormulario() {
        Tarea tarea = tareaTabla.getSelectionModel().getSelectedItem();
        if (tarea != null) {
            idTareaInterno = tarea.getIdTarea();
            nombreTareaTexto.setText(tarea.getNombreTarea());
            responsableTareaTexto.setText(tarea.getResponsable());
            estatusTareaTexto.setText(tarea.getEstatus());
        }
    }

    @javafx.fxml.FXML
    public void modificarTarea() {
        if (idTareaInterno == null) {
            mostrarMensaje("Informacion", "Debe seleccionar una tarea", Alert.AlertType.INFORMATION);
            return;
        }

        if (nombreTareaTexto.getText().isEmpty() || responsableTareaTexto.getText().isEmpty() || estatusTareaTexto.getText().isEmpty()) {
            mostrarMensaje("Error", "Todos los campos son obligatorios", Alert.AlertType.ERROR);
            return;
        }

        Tarea tarea = new Tarea();
        recolectarDatosFormulario(tarea);
        tareaServicio.guardarTarea(tarea);
        mostrarMensaje("Informacion", "Tarea modificada con exito", Alert.AlertType.INFORMATION);
        limpiarFormulario();
        listarTareas();
    }

    @javafx.fxml.FXML
    public void eliminarTarea() {
        Tarea tarea = tareaTabla.getSelectionModel().getSelectedItem();
        if (tarea != null) {
            logger.info("Eliminando tarea: {}", tarea);
            tareaServicio.eliminarTarea(tarea);
            mostrarMensaje("Informacion", "Tarea " + tarea.getIdTarea() + " eliminada con exito", Alert.AlertType.INFORMATION);
            limpiarFormulario();
            listarTareas();
        } else {
            mostrarMensaje("Error", "Debe seleccionar una tarea", Alert.AlertType.ERROR);
        }
    }
}
