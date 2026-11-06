package com.tuempresa.proyectohibernate.Controller;

import com.tuempresa.proyectohibernate.Dao.LibroDAO;
import com.tuempresa.proyectohibernate.Model.Libro;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class LibroController {

    @FXML
    private TableView<Libro> tblLibros;
    @FXML
    private TableColumn<Libro, Integer> colId;
    @FXML
    private TableColumn<Libro, String> colTitulo;
    @FXML
    private TableColumn<Libro, String> colAutor;
    @FXML
    private TableColumn<Libro, String> colAnio;
    @FXML
    private TableColumn<Libro, BigDecimal> colPrecio;

    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtAutor;
    @FXML
    private TextField txtAnio;
    @FXML
    private TextField txtPrecio;

    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;

    private final LibroDAO libroDAO = new LibroDAO();

    private Long idSeleccionado;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );
        colTitulo.setCellValueFactory(
                new PropertyValueFactory<>("titulo")
        );
        colAutor.setCellValueFactory(
                new PropertyValueFactory<>("autor")
        );
        colAnio.setCellValueFactory(
                new PropertyValueFactory<>("anioPublicacion")
        );
        colPrecio.setCellValueFactory(
                new PropertyValueFactory<>("precio")
        );

        tblLibros.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue != null) {
                        seleccionarLibro(newValue);
                    } else {
                        idSeleccionado = null;
                        actualizarBotones();
                    }
                }
        );
        actualizarBotones();
        cargarLibros();
    }

    private void seleccionarLibro(Libro libro) {
        idSeleccionado = libro.getId();
        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtAnio.setText(String.valueOf(libro.getAnioPublicacion()));
        txtPrecio.setText(String.valueOf(libro.getPrecio()));
        actualizarBotones();
    }

    private void actualizarBotones() {
        boolean sinSeleccion = idSeleccionado == null;
        btnModificar.setDisable(sinSeleccion);
        btnEliminar.setDisable(sinSeleccion);
    }

    private Libro obtenerLibroFormulario() {
        String titulo = txtTitulo.getText();
        String autor = txtAutor.getText();
        int anio = Integer.parseInt(txtAnio.getText());
        BigDecimal precio = new BigDecimal(txtPrecio.getText());
        return new Libro(titulo, autor, anio, precio);
    }

    @FXML
    private void guardarLibro() {
        try {
            Libro libro = obtenerLibroFormulario();
            libroDAO.guardar(libro);
            limpiarFormulario();
            cargarLibros();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void modificarLibro() {
        try {
            Libro libro = obtenerLibroFormulario();
            libro.setId(idSeleccionado);
            boolean modificado = libroDAO.modificar(libro);

            if(!modificado){
                // Alert para indicar que hubo un error a nivel de transaccion
            }
            limpiarFormulario();
            cargarLibros();
            // Alert para indicar que la modificacion fue exitosa

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void eliminarLibro() {
        try {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmacion de eliminacion de Datos");
            confirmacion.setHeaderText("Eliminar Libro");
            confirmacion.setContentText("¿Está seguro que desea eliminar el registro seleccionado?");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if(resultado.isEmpty() || resultado.get() != ButtonType.OK){
                return;
            }

            boolean eliminado = libroDAO.eliminar(idSeleccionado);
            if(!eliminado){
                // Alert
            }
            // Alert para indicar que la operacion fue realizada
            limpiarFormulario();
            cargarLibros();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void cargarLibros() {
        try {
            List<Libro> libros = libroDAO.listar();
            limpiarFormulario();
            tblLibros.setItems(
                    FXCollections.observableArrayList(libros)
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        txtTitulo.clear();
        txtAutor.clear();
        txtAnio.clear();
        txtPrecio.clear();
        tblLibros.getSelectionModel().clearSelection();
        actualizarBotones();
    }
}