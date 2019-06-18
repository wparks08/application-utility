package AppUtility;

import AppUtility.db.*;
import AppUtility.mapping.Conditional;
import AppUtility.mapping.DataType;
import AppUtility.mapping.MapProperty;
import com.jfoenix.controls.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.jfoenix.controls.JFXButton.ButtonType.RAISED;

public class EditMappingController {
    @FXML
    private HBox wrapper;
    @FXML
    private VBox mapping;
    @FXML
    private VBox settings;
    @FXML
    private JFXListView<FormField> formFieldListView;

    private DataModel model;
    private Form form;
    private List<FormField> formFields = new ArrayList<>();
    private List<CensusHeader> censusHeaders = new ArrayList<>();
    private HashMap<String, String> mappings = new HashMap<>();

    @FXML
    public void initialize() {
        formFieldListView = new JFXListView<>();
    }

    @FXML
    public void initModel(DataModel model) {
        this.model = model;
        this.form = model.getSelectedForm();
        this.formFields = form.getFormFields();
        this.censusHeaders = form.getCensusHeaders();
        this.mappings = form.getMappingsAsMap();

        setupScene();
    }

    private void setupScene() {
        formFieldListView.getItems().addAll(formFields);

        Label instructions = new Label("Match fields from the Carrier Form (left) to the proper Census Data (right).");
        instructions.getStyleClass().add("lbl-default");

        mapping.getChildren().add(instructions);
        mapping.getChildren().add(createContainer("list-view-container", formFieldListView));

        formFieldListView.getSelectionModel().getSelectedItems().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                populateSettingsPane(formFieldListView.getSelectionModel().getSelectedItem());
            }
        });

        JFXComboBox<CensusHeader> comboBox = new JFXComboBox<>();
        comboBox.getItems().addAll(censusHeaders);
        comboBox.setPromptText("Census Header Selection");
        comboBox.setDisable(true);
        settings.getChildren().clear();
        settings.getChildren().add(createContainer(comboBox));
    }

    private void populateSettingsPane(FormField selectedItem) {
        //Census Header combo box
        JFXComboBox<CensusHeader> comboBox = new JFXComboBox<>();
        comboBox.getItems().addAll(censusHeaders);
        if (mappings.containsKey(selectedItem.getFieldName())) {
            String mappedHeaderString = mappings.get(selectedItem.getFieldName());
            CensusHeader mappedHeader = null;
            for (CensusHeader censusHeader : censusHeaders) {
                if (censusHeader.getHeader().equals(mappedHeaderString)) {
                    mappedHeader = censusHeader;
                    break;
                }
            }
            comboBox.getSelectionModel().select(mappedHeader);
        }

        comboBox.setPromptText("Census Header Selection");

        settings.getChildren().clear();
        settings.getChildren().add(createContainer(comboBox));

        //Data Type combo box
        JFXComboBox<DataType> dataTypeComboBox = new JFXComboBox<>();
        dataTypeComboBox.getItems().addAll(DataType.values());
        dataTypeComboBox.setPromptText("Data Type");
        settings.getChildren().add(createContainer(dataTypeComboBox));
        VBox dataTypeSettings = new VBox();
        dataTypeSettings.setFillWidth(true);
        dataTypeSettings.setSpacing(10);
        dataTypeSettings.setAlignment(Pos.CENTER);
        settings.getChildren().add(createContainer(dataTypeSettings));

        //Save Button
        JFXButton saveButton = new JFXButton("Save");
        saveButton.setButtonType(RAISED);
        saveButton.getStyleClass().add("btn-success");
        //Default Save action
        saveButton.setOnAction(event -> saveMapping(comboBox));

        settings.getChildren().add(createContainer(saveButton));

        //Data Type Settings
        dataTypeComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DataType>() {
            @Override
            public void changed(ObservableValue<? extends DataType> observable, DataType oldValue, DataType newValue) {
                dataTypeSettings.getChildren().clear();
                switch (newValue) {
                    case TEXT:
                        saveButton.setOnAction(event -> {
                            saveMapping(comboBox);
                            Mapping mapping = new Mapping().findByFormFieldId(formFieldListView.getSelectionModel().getSelectedItem().getId());
                            if (mapping != null) {
                                List<MappingProperty> mappingPropertyList = mapping.getMappingProperties();
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATA_TYPE, DataType.TEXT.name());
                            }
                        });
                        break;
                    case SSN:
                        JFXCheckBox isSplit = new JFXCheckBox("Split SSN");
                        JFXComboBox<String> ssnSection = new JFXComboBox<>();
                        isSplit.getStyleClass().add("checkbox-default");
                        dataTypeSettings.getChildren().add(isSplit);
                        isSplit.selectedProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                if (newValue) {
                                    ssnSection.getItems().clear();
                                    ssnSection.getItems().addAll("Beginning", "Middle", "End");
                                    ssnSection.setPromptText("Select Section...");
                                    dataTypeSettings.getChildren().add(ssnSection);
                                } else {
                                    dataTypeSettings.getChildren().remove(ssnSection);
                                }
                            }
                        });

                        Mapping ssnMapping = new Mapping().findByFormFieldId(formFieldListView.getSelectionModel().getSelectedItem().getId());
                        if (ssnMapping != null) {
                            HashMap<String, String> mappingProperties = ssnMapping.getMappingPropertiesAsMap();
                            isSplit.setSelected(Boolean.valueOf(mappingProperties.get(MapProperty.SSN_SPLIT.toString())));
                            if (isSplit.isSelected()) {
                                ssnSection.getSelectionModel().select(mappingProperties.get(MapProperty.SSN_SECTION.toString()));
                            }
                        }

                        saveButton.setOnAction(event -> {
                            saveMapping(comboBox);
                            Mapping mapping = new Mapping().findByFormFieldId(formFieldListView.getSelectionModel().getSelectedItem().getId());
                            if (mapping != null) {
                                HashMap<String, String> mappingProperties = mapping.getMappingPropertiesAsMap();
                                List<MappingProperty> mappingPropertyList = mapping.getMappingProperties();

                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.SSN_SPLIT, isSplit.selectedProperty().getValue().toString());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.SSN_SECTION, ssnSection.getSelectionModel().getSelectedItem());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATA_TYPE, DataType.SSN.name());
                            }
                        });
                        break;
                    case DATE:
                        JFXCheckBox splitDate = new JFXCheckBox("Partial Date");
                        JFXComboBox<String> dateSection = new JFXComboBox<>();

                        splitDate.getStyleClass().add("checkbox-default");

                        dataTypeSettings.getChildren().add(splitDate);

                        splitDate.selectedProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                if (newValue) {
                                    dateSection.getItems().clear();
                                    dateSection.getItems().addAll("Month", "Day", "Year");
                                    dateSection.setPromptText("Select part...");
                                    dataTypeSettings.getChildren().add(dateSection);
                                } else {
                                    dataTypeSettings.getChildren().remove(dateSection);
                                }
                            }
                        });

                        Mapping dateMapping = new Mapping().findByFormFieldId(formFieldListView.getSelectionModel().getSelectedItem().getId());
                        if (dateMapping != null) {
                            HashMap<String, String> dateMappingProperties = dateMapping.getMappingPropertiesAsMap();
                            if (dateMappingProperties.get(MapProperty.DATE_SPLIT.toString()) != null) {
                                dateSection.getSelectionModel().select(dateMappingProperties.get(MapProperty.DATE_SPLIT.toString()));
                            }
                        }

                        saveButton.setOnAction(event -> {
                            saveMapping(comboBox);
                            Mapping mapping = new Mapping().findByFormFieldId(formFieldListView.getSelectionModel().getSelectedItem().getId());
                            if (mapping != null) {
                                HashMap<String, String> mappingProperties = mapping.getMappingPropertiesAsMap();
                                List<MappingProperty> mappingPropertyList = mapping.getMappingProperties();

                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATE_SPLIT, splitDate.selectedProperty().getValue().toString());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATE_SECTION, dateSection.getSelectionModel().getSelectedItem());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATA_TYPE, DataType.DATE.name());

                            }
                        });
                        break;
                    case PLAN:
                        //maybe not?
                        break;
                    case RADIO:
                        Label radioLabel = new Label("Conditions");
                        ScrollPane radioScrollPane = new ScrollPane();
                        radioScrollPane.setFitToHeight(true);
                        radioScrollPane.setFitToWidth(true);

                        VBox radioConditions = new VBox();
                        radioConditions.setSpacing(20);
                        ButtonBar radioButtonBar = new ButtonBar();
                        JFXButton addAnother = new JFXButton("Add +");
                        addAnother.setButtonType(RAISED);
                        addAnother.getStyleClass().add("btn-success");
                        radioButtonBar.getButtons().add(addAnother);

                        VBox radioWrapper = new VBox();
                        radioWrapper.setSpacing(25);
                        radioWrapper.getChildren().addAll(radioLabel, radioConditions, radioButtonBar);

                        radioScrollPane.setContent(radioWrapper);
                        radioScrollPane.getStyleClass().add("scroll-pane-default");

                        addCondition(radioConditions, comboBox);

                        addAnother.setOnAction(event -> {
                            addCondition(radioConditions, comboBox);
                        });

                        dataTypeSettings.getChildren().add(radioScrollPane);

                        break;
                    case CHECKBOX:
                        //Checked if {selected census header} {Conditional} {Text input}
                        String selectedHeader = getSelectedHeader(comboBox);
                        Label chboxPrompt = new Label("Checked if " + selectedHeader + " ");
                        chboxPrompt.getStyleClass().add("lbl-default");

                        comboBox.getSelectionModel().selectedItemProperty().addListener(new InvalidationListener() {
                            @Override
                            public void invalidated(Observable observable) {
                                chboxPrompt.setText("Checked if " + getSelectedHeader(comboBox) + " ");
                            }
                        });

                        JFXComboBox<Conditional> conditionals = new JFXComboBox<>();
                        conditionals.getItems().addAll(Conditional.values());
                        conditionals.setPromptText("Condition...");

                        JFXTextField value = new JFXTextField();
                        value.getStyleClass().add("text-input");
                        value.setPromptText("Value...");

                        HBox chboxWrapper = new HBox();
                        chboxWrapper.setFillHeight(true);
                        chboxWrapper.setAlignment(Pos.BOTTOM_CENTER);
                        chboxWrapper.setSpacing(10);
                        chboxWrapper.getChildren().addAll(chboxPrompt, conditionals, value);

                        dataTypeSettings.getChildren().add(chboxWrapper);

                        Mapping savedMapping = new Mapping().findByFormFieldId(formFieldListView.getSelectionModel().getSelectedItem().getId());
                        if (savedMapping != null) {
                            HashMap<String, String> checkboxProperties = savedMapping.getMappingPropertiesAsMap();

                            if (checkboxProperties.containsKey(MapProperty.CONDITIONAL.toString())) {
                                Conditional conditional = Conditional.valueOf(checkboxProperties.get(MapProperty.CONDITIONAL.toString()));
                                conditionals.getSelectionModel().select(conditional);
                            }

                            if (checkboxProperties.containsKey(MapProperty.TEXT_VALUE.toString())) {
                                String textValue = checkboxProperties.get(MapProperty.TEXT_VALUE.toString());
                                value.setText(textValue);
                            }
                        }

                        saveButton.setOnAction(event -> {
                            saveMapping(comboBox);
                            Mapping mapping = new Mapping().findByFormFieldId(formFieldListView.getSelectionModel().getSelectedItem().getId());
                            if (mapping != null) {
                                List<MappingProperty> mappingPropertyList = mapping.getMappingProperties();
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.CONDITIONAL, conditionals.getSelectionModel().getSelectedItem().name());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.TEXT_VALUE, value.textProperty().getValue());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATA_TYPE, DataType.CHECKBOX.name());
                            }
                        });
                        break;
                    default: //do nothing
                }
            }
        });

        Mapping savedMapping = new Mapping().findByFormFieldId(formFieldListView.getSelectionModel().getSelectedItem().getId());
        if (savedMapping != null) {
            String dataType = savedMapping.getMappingPropertiesAsMap().get(MapProperty.DATA_TYPE.toString());
            if (dataType != null) {
                dataTypeComboBox.getSelectionModel().select(DataType.valueOf(dataType));
            }
        }
    }

    private void addCondition(VBox radioConditions, JFXComboBox<CensusHeader> comboBox) {
        //When {Census Header} {Conditional} {Value}, {Form Field} is {Value}
        Label conditionLabel = new Label("When " + getSelectedHeader(comboBox) + " ");
        JFXComboBox<Conditional> conditionals = new JFXComboBox<>();
        conditionals.getItems().addAll(Conditional.values());
        JFXTextField censusValue = new JFXTextField();
        Label conditionLabelContinued = new Label(", " + formFieldListView.getSelectionModel().getSelectedItem().getFieldName() + " is ");
        JFXTextField formValue = new JFXTextField();

        HBox conditionWrapper = new HBox();
        conditionWrapper.setSpacing(10);
        conditionWrapper.setAlignment(Pos.BOTTOM_CENTER);
        conditionWrapper.getChildren().addAll(conditionLabel, conditionals, censusValue, conditionLabelContinued, formValue);

        radioConditions.getChildren().add(conditionWrapper);
    }

    private String getSelectedHeader(JFXComboBox<CensusHeader> comboBox) {
        return comboBox.getSelectionModel().getSelectedItem().getHeader();
    }

    private void saveMapping(JFXComboBox<CensusHeader> comboBox) {
        Mapping mapping = new Mapping().findByFormFieldId(formFieldListView.getSelectionModel().getSelectedItem().getId());
        if (mapping != null) {
            mapping.setCensusHeaderId(comboBox.getSelectionModel().getSelectedItem().getId());
            mapping.save();
            Snackbar.show(wrapper, "Mapping updated.");
        } else {
            mapping = new Mapping();
            mapping.setFormId(form.getId());
            mapping.setCensusHeaderId(comboBox.getSelectionModel().getSelectedItem().getId());
            mapping.setFormFieldId(formFieldListView.getSelectionModel().getSelectedItem().getId());
            mapping.save();
            Snackbar.show(wrapper, "Mapping saved.");
        }

        refreshMappings();
    }

    private void refreshMappings() {
        this.mappings = form.getMappingsAsMap();
    }

    private StackPane createContainer(Node... children) {
        StackPane stackPane = new StackPane(children);
        stackPane.setPadding(new Insets(20));

        return stackPane;
    }

    private StackPane createContainer(String styleClass, Node... children) {
        StackPane stackPane = new StackPane(children);
        stackPane.getStyleClass().add(styleClass);
        stackPane.setPadding(new Insets(20));

        return stackPane;
    }

    private void saveOrUpdateMappingProperty(Mapping mapping, List<MappingProperty> mappingProperties, MapProperty mapProperty, String value) {
        Iterator<MappingProperty> mappingPropertyIterator = mappingProperties.iterator();
        boolean exists = false;

        while (mappingPropertyIterator.hasNext()) {
            MappingProperty current = mappingPropertyIterator.next();
            if (current.getProperty().equals(mapProperty.toString())) {
                current.setValue(value);
                current.save();
                exists = true;
                break;
            }
        }

        if (!exists) {
            mapping.addMappingProperty(mapProperty.toString(), value);
        }
    }
}
