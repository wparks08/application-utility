package AppUtility.ui;

import AppUtility.Dependent;
import AppUtility.Employee;
import AppUtility.db.Carrier;
import AppUtility.db.Form;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRow extends HBox {

    private Employee employee;

    private JFXCheckBox isEnrollment = new JFXCheckBox("Enrollment");
    private JFXCheckBox isChange = new JFXCheckBox("Change");
    private JFXComboBox<Carrier> carriers = new JFXComboBox<>();
    private JFXComboBox<Form> forms = new JFXComboBox<>();

    private List<Carrier> carrierList = new ArrayList<>();

    public EmployeeRow(Employee employee) {
        super();
        this.setSpacing(20);
        this.setPadding(new Insets(15));

        this.employee = employee;
        this.setAlignment(Pos.CENTER_LEFT);

        addCheckboxes();
        addName();
        addDependentCount();
        addCarrierComboBox();
        addFormComboBox();
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public EmployeeRow(Employee employee, List<Carrier> carrierList) {
        super();
        this.carrierList.addAll(carrierList);
        this.setSpacing(20);
        this.setPadding(new Insets(15));

        this.employee = employee;
        this.setAlignment(Pos.CENTER_LEFT);

        addCheckboxes();
        addName();
        addDependentCount();
        addCarrierComboBox();
        addFormComboBox();
    }

    public void setIsEnrollment(Boolean isEnrollment) {
        this.isEnrollment.setSelected(isEnrollment);
    }

    public Boolean getIsEnrollment() { return this.isEnrollment.selectedProperty().get(); }

    public void setIsChange(Boolean isChange) {
        this.isChange.setSelected(isChange);
    }

    public Boolean getIsChange() { return this.isChange.selectedProperty().get(); }

    public Form getSelectedForm() {
        return this.forms.getSelectionModel().getSelectedItem();
    }

    private void addCheckboxes() {
        isEnrollment.getStyleClass().add("checkbox-default");
        isChange.getStyleClass().add("checkbox-default");

        VBox checkboxWrapper = new VBox();
        checkboxWrapper.setSpacing(10);
        checkboxWrapper.setPadding(new Insets(5));
        checkboxWrapper.getChildren().addAll(isEnrollment, isChange);

        this.getChildren().add(checkboxWrapper);
    }

    private void addName() {
        Label name = new Label(employee.getInfo("First Name") + " " + employee.getInfo("Last Name"));
        name.setPrefWidth(150);

        this.getChildren().add(name);
    }

    private void addDependentCount() {
        int childCount = 0;
        int spouseCount = 0;

        List<Dependent> dependentList = employee.getDependents();

        for (Dependent dependent : dependentList) {
            if (dependent.getInfo("Relationship").equals("Spouse")) {
                spouseCount++;
            }
            if (dependent.getInfo("Relationship").equals("Child")) {
                childCount++;
            }
        }

        VBox dependentCountWrapper = new VBox();
        dependentCountWrapper.setSpacing(10);
        dependentCountWrapper.setPadding(new Insets(5));

        Label spouseLabel = new Label("Spouse: " + spouseCount);
        Label childLabel = new Label("Children: " + childCount);

        dependentCountWrapper.getChildren().addAll(spouseLabel, childLabel);
        this.getChildren().add(dependentCountWrapper);
    }

    private void addCarrierComboBox() {
        carriers.setPromptText("Select Carrier");
        carriers.getItems().addAll(carrierList);

        this.getChildren().add(carriers);
    }

    private void addFormComboBox() {
        forms.setPromptText("Select Form");
        carriers.getSelectionModel().selectedItemProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Carrier selectedCarrier = carriers.getSelectionModel().getSelectedItem();
                forms.getItems().addAll((List<Form>) selectedCarrier.getChildren(Form.class));
            }
        });

        this.getChildren().add(forms);
    }
}
