package com.example.examendi2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private Label welcomeText11;
    @FXML
    private TextField txtNombre;
    @FXML
    private RadioButton radioHombre;
    @FXML
    private ToggleGroup radioSexo;
    @FXML
    private RadioButton radioMujer;
    @FXML
    private Label welcomeText1;
    @FXML
    private TextField txtPeso;
    @FXML
    private Label welcomeText12;
    @FXML
    private TextField txtEdad;
    @FXML
    private Label welcomeText12111;
    @FXML
    private TextField txtTalla;
    @FXML
    private Label welcomeText121111;
    @FXML
    private ComboBox comboActividad;
    @FXML
    private Label welcomeText1211111;
    @FXML
    private TextArea txtObservaciones;
    @FXML
    private Button btnCalcular;
    @FXML
    private Label labelInfo;
    @FXML
    private Button btnDescargar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> actividad = FXCollections.observableArrayList();
        actividad.addAll("Sedentario", "Moderado", "Activo", "Muy Activo");

        comboActividad.setItems(actividad);
        comboActividad.getSelectionModel().selectFirst();
    }

    @FXML
    public void calcular(ActionEvent actionEvent) {
            if(!txtNombre.getText().isEmpty() && !txtPeso.getText().isEmpty() &&
            !txtTalla.getText().isEmpty() && !txtEdad.getText().isEmpty()) {
                String nombre = txtNombre.getText();
                Double peso = Double.valueOf(txtPeso.getText());
                Double talla = Double.valueOf(txtTalla.getText());
                Integer edad = Integer.valueOf(txtEdad.getText());

                Double formulaGer;
                if (radioHombre.isSelected()) {
                    formulaGer = 66.473 + 13.751 * peso + 5.0033 * talla - 6.755 * edad;
                } else {
                    formulaGer = 655.0955 + 9.463 * peso + 1.8496 * talla - 4.6756 * edad;
                }

                Long formulaGerRedondeado = Math.round(formulaGer);

                Double formulaGet = 0.0;
                if (radioHombre.isSelected() && comboActividad.getValue().equals("Sedentario")) {
                    formulaGet = formulaGer * 1.3;
                } else if (radioHombre.isSelected() && comboActividad.getValue().equals("Moderado")) {
                    formulaGet = formulaGer * 1.6;
                } else if (radioHombre.isSelected() && comboActividad.getValue().equals("Activo")) {
                    formulaGet = formulaGer * 1.7;
                } else if (radioHombre.isSelected() && comboActividad.getValue().equals("Muy Activo")) {
                    formulaGet = formulaGer * 2.1;
                } else if (radioMujer.isSelected() && comboActividad.getValue().equals("Sedentario")) {
                    formulaGet = formulaGer * 1.3;
                } else if (radioMujer.isSelected() && comboActividad.getValue().equals("Moderado")) {
                    formulaGet = formulaGer * 1.5;
                } else if (radioMujer.isSelected() && comboActividad.getValue().equals("Activo")) {
                    formulaGet = formulaGer * 1.6;
                } else if (radioMujer.isSelected() && comboActividad.getValue().equals("Muy Activo")) {
                    formulaGet = formulaGer * 1.9;
                }
                Long formulaGetRedondeado = Math.round(formulaGet);

                labelInfo.setText("El cliente " + nombre + " tiene un GER de " + formulaGerRedondeado + " y un GET de " + formulaGetRedondeado);
            }else{
                labelInfo.setText("Campo/s incompleto/s");
            }
    }

    @FXML
    public void descargarPdf(ActionEvent actionEvent) {
        Connection c = null;
        try {
            c = DriverManager.getConnection("jdbc:mysql://localhost/examenDI2","root","");
            HashMap hm = new HashMap<>();
            var jasperPrint = JasperFillManager.fillReport("Clientes.jasper",hm,c);

            var visor = new JRViewer(jasperPrint);

            JRViewer viewer = new JRViewer(jasperPrint);

            JFrame frame = new JFrame("Listado de Alumnos");
            frame.getContentPane().add(viewer);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.pack();
            frame.setVisible(true);

            System.out.print("Done!");

            JRPdfExporter exp = new JRPdfExporter();
            exp.setExporterInput(new SimpleExporterInput(jasperPrint));
            exp.setExporterOutput(new SimpleOutputStreamExporterOutput("alumnos.pdf"));
            exp.setConfiguration(new SimplePdfExporterConfiguration());
            exp.exportReport();

            System.out.print("Done!");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}