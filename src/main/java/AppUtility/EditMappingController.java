package AppUtility;

import AppUtility.Db.*;
import AppUtility.Mapping.Conditional;
import AppUtility.Mapping.DataType;
import AppUtility.Mapping.MapProperty;
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
    private final JFXListView<FormField> formFieldListView = new JFXListView<>();

    private Form form;
    private List<FormField> formFields = new ArrayList<>();
    private List<CensusHeader> censusHeaders = new ArrayList<>();
    private HashMap<String, String> mappings = new HashMap<>();
    private HashMap<String, String> formProperties = new HashMap<>();

    @FXML
    public void initialize() {
//        this.form = DataModel.getSelectedForm();
        this.formFields = form.getFormFields();
        this.censusHeaders = form.getCensusHeaders();
        this.mappings = form.getMappingsAsMap();
        this.formProperties = form.getFormPropertiesAsMap();

        setupScene();
    }

    @FXML
    public void initModel() {

    }

    private void setupScene() {
        formFieldListView.getItems().addAll(formFields);

        Label instructions = new Label("Match fields from the Carrier Form (left) to the proper Census Data (right).");
        instructions.getStyleClass().add("lbl-default");

        JFXTextField filterField = new JFXTextField();
        filterField.setLabelFloat(true);
        filterField.setPromptText("Filter...");

        filterField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                double height = formFieldListView.getHeight();
                formFieldListView.getItems().clear();
                if (newValue.equals("")) {
                    formFieldListView.getItems().addAll(formFields);
                } else {
                    for (FormField field : formFields) {
                        if (field.getFieldName().toLowerCase().contains(newValue.toLowerCase())) {
                            formFieldListView.getItems().add(field);
                        }
                    }
                }
                formFieldListView.setPrefHeight(height);
            }
        });

        mapping.getChildren().add(instructions);
        mapping.getChildren().add(filterField);
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
        comboBox.setDisable(false);

        settings.getChildren().clear();
        settings.getChildren().add(createContainer(comboBox));

        //Data Type combo box
        JFXComboBox<DataType> dataTypeComboBox = new JFXComboBox<>();
        dataTypeComboBox.getItems().addAll(DataType.values());
        dataTypeComboBox.setPromptText("Data Type");
        settings.getChildren().add(createContainer(dataTypeComboBox));

        //Dependent options
        JFXCheckBox isDependentField = new JFXCheckBox("Dependent Field");
        isDependentField.getStyleClass().add("checkbox-default");

        JFXComboBox<String> dependent = new JFXComboBox<>();
        addDependentSelections(dependent);

        HBox dependentOptionsWrapper = new HBox();
        dependentOptionsWrapper.setSpacing(20);
        dependentOptionsWrapper.setAlignment(Pos.BOTTOM_CENTER);
        dependentOptionsWrapper.getChildren().addAll(isDependentField, dependent);

        settings.getChildren().add(dependentOptionsWrapper);

        //Data type settings
        VBox dataTypeSettings = new VBox();
        dataTypeSettings.setFillWidth(true);
        dataTypeSettings.setSpacing(15);
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
                comboBox.setDisable(false);

                switch (newValue) {
                    case TEXT:
                        saveButton.setOnAction(event -> {
                            saveMapping(comboBox);
                            Mapping mapping = getSelectedMapping();
                            if (mapping != null) {
                                List<MappingProperty> mappingPropertyList = mapping.getMappingProperties();
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATA_TYPE, DataType.TEXT.name());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.IS_DEPENDENT_FIELD, isDependentField.selectedProperty().getValue().toString());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DEPENDENT, dependent.getSelectionModel().getSelectedItem());
                            }
                        });
                        break;
                    case SSN:
                        JFXCheckBox isSplit = new JFXCheckBox("Split SSN");
                        JFXCheckBox removeDashes = new JFXCheckBox("Remove Dashes");
                        JFXComboBox<String> ssnSection = new JFXComboBox<>();
                        isSplit.getStyleClass().add("checkbox-default");
                        removeDashes.getStyleClass().add("checkbox-default");
                        dataTypeSettings.getChildren().addAll(isSplit, removeDashes);
                        isSplit.selectedProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                if (newValue) {
                                    ssnSection.getItems().clear();
                                    ssnSection.getItems().addAll("Beginning", "Middle", "End");
                                    ssnSection.setPromptText("Select Section...");
                                    dataTypeSettings.getChildren().add(ssnSection);
                                    removeDashes.selectedProperty().setValue(false);
                                } else {
                                    dataTypeSettings.getChildren().remove(ssnSection);
                                }
                            }
                        });

                        removeDashes.selectedProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                if (newValue) {
                                    isSplit.selectedProperty().setValue(false);
                                }
                            }
                        });

                        Mapping ssnMapping = getSelectedMapping();
                        if (ssnMapping != null) {
                            HashMap<String, String> mappingProperties = ssnMapping.getMappingPropertiesAsMap();
                            isSplit.setSelected(Boolean.valueOf(mappingProperties.get(MapProperty.SSN_SPLIT.toString())));
                            if (isSplit.isSelected()) {
                                ssnSection.getSelectionModel().select(mappingProperties.get(MapProperty.SSN_SECTION.toString()));
                            }
                            removeDashes.setSelected(Boolean.valueOf(mappingProperties.get(MapProperty.SSN_UNDECORATED.toString())));
                        }

                        saveButton.setOnAction(event -> {
                            saveMapping(comboBox);
                            Mapping mapping = getSelectedMapping();
                            if (mapping != null) {
                                List<MappingProperty> mappingPropertyList = mapping.getMappingProperties();

                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.SSN_SPLIT, isSplit.selectedProperty().getValue().toString());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.SSN_SECTION, ssnSection.getSelectionModel().getSelectedItem());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.SSN_UNDECORATED, removeDashes.selectedProperty().getValue().toString());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATA_TYPE, DataType.SSN.name());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.IS_DEPENDENT_FIELD, isDependentField.selectedProperty().getValue().toString());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DEPENDENT, dependent.getSelectionModel().getSelectedItem());
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

                        Mapping dateMapping = getSelectedMapping();
                        if (dateMapping != null) {
                            HashMap<String, String> dateMappingProperties = dateMapping.getMappingPropertiesAsMap();
                            if (dateMappingProperties.get(MapProperty.DATE_SPLIT.toString()) != null) {
                                splitDate.selectedProperty().setValue(Boolean.valueOf(dateMappingProperties.get(MapProperty.DATE_SPLIT.toString())));
                            }
                            if (dateMappingProperties.get(MapProperty.DATE_SECTION.toString()) != null) {
                                dateSection.getSelectionModel().select(dateMappingProperties.get(MapProperty.DATE_SECTION.toString()));
                            }
                        }

                        saveButton.setOnAction(event -> {
                            saveMapping(comboBox);
                            Mapping mapping = getSelectedMapping();
                            if (mapping != null) {
                                List<MappingProperty> mappingPropertyList = mapping.getMappingProperties();

                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATE_SPLIT, splitDate.selectedProperty().getValue().toString());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATE_SECTION, dateSection.getSelectionModel().getSelectedItem());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATA_TYPE, DataType.DATE.name());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.IS_DEPENDENT_FIELD, isDependentField.selectedProperty().getValue().toString());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DEPENDENT, dependent.getSelectionModel().getSelectedItem());
                            }
                        });
                        break;
                    case PHONE:
                        JFXCheckBox splitPhone = new JFXCheckBox("Split Phone Number");
                        JFXComboBox<String> phonePart = new JFXComboBox<>();

                        dataTypeSettings.getChildren().add(splitPhone);

                        splitPhone.getStyleClass().add("checkbox-default");

                        splitPhone.selectedProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                if (newValue) {
                                    phonePart.getItems().clear();
                                    phonePart.getItems().addAll("Area Code", "Prefix", "Suffix");
                                    phonePart.setPromptText("Select Phone Number Part...");

                                    dataTypeSettings.getChildren().add(phonePart);
                                } else {
                                    dataTypeSettings.getChildren().remove(phonePart);
                                }
                            }
                        });

                        Mapping phoneMapping = getSelectedMapping();
                        if (phoneMapping != null) {
                            HashMap<String, String> phoneMappings = phoneMapping.getMappingPropertiesAsMap();
                            if (phoneMappings.get(MapProperty.PHONE_SPLIT.toString()) != null) {
                                splitPhone.setSelected(Boolean.valueOf(phoneMappings.get(MapProperty.PHONE_SPLIT.toString())));
                            }
                            if (phoneMappings.get(MapProperty.PHONE_SECTION.toString()) != null) {
                                phonePart.getSelectionModel().select(phoneMappings.get(MapProperty.PHONE_SECTION.toString()));
                            }
                        }

                        saveButton.setOnAction(event -> {
                            saveMapping(comboBox);
                            Mapping mapping = getSelectedMapping();
                            if (mapping != null) {
                                List<MappingProperty> mappingPropertyList = mapping.getMappingProperties();

                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.PHONE_SPLIT, splitPhone.selectedProperty().getValue().toString());
                                if (splitPhone.selectedProperty().getValue()) {
                                    saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.PHONE_SECTION, phonePart.getSelectionModel().getSelectedItem());
                                }
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATA_TYPE, DataType.PHONE.name());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.IS_DEPENDENT_FIELD, isDependentField.selectedProperty().getValue().toString());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DEPENDENT, dependent.getSelectionModel().getSelectedItem());
                            }
                        });

                        break;
                    case RADIO:
                        List<RadioCondition> radioConditionList = new ArrayList<>();

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

                        Mapping radioMapping = getSelectedMapping();
                        if (radioMapping != null) {
                            radioConditionList.addAll(radioMapping.getRadioConditions());
                            for (RadioCondition radioCondition : radioConditionList) {
                                addCondition(radioCondition, radioConditions, comboBox);
                            }
                        }

                        VBox radioWrapper = new VBox();
                        radioWrapper.setSpacing(25);
                        radioWrapper.getChildren().addAll(radioLabel, radioConditions, radioButtonBar);

                        radioScrollPane.setContent(radioWrapper);
                        radioScrollPane.getStyleClass().add("scroll-pane-default");

                        RadioCondition blankRadioCondition = new RadioCondition();
                        addCondition(blankRadioCondition, radioConditions, comboBox);
                        radioConditionList.add(blankRadioCondition);

                        addAnother.setOnAction(event -> {
                            RadioCondition newRadioCondition = new RadioCondition();
                            addCondition(newRadioCondition, radioConditions, comboBox);
                            radioConditionList.add(newRadioCondition);
                        });

                        JFXCheckBox hasDefaultValue = new JFXCheckBox("Default Value");
                        hasDefaultValue.getStyleClass().add("checkbox-default");
                        JFXTextField defaultValue = new JFXTextField();
                        defaultValue.setPromptText("Enter a Default Value");

                        if (radioMapping != null) {
                            HashMap<String, String> mappingPropertyMap = radioMapping.getMappingPropertiesAsMap();
                            hasDefaultValue.setSelected(Boolean.valueOf(mappingPropertyMap.get(MapProperty.HAS_DEFAULT.toString())));
                            defaultValue.setText(mappingPropertyMap.get(MapProperty.DEFAULT_VALUE.toString()));
                        }

                        HBox defaultValueWrapper = new HBox();
                        defaultValueWrapper.getChildren().addAll(hasDefaultValue, defaultValue);
                        defaultValueWrapper.setSpacing(20);
                        defaultValueWrapper.setAlignment(Pos.BOTTOM_CENTER);

                        Label defaultNotice = new Label("Default Value will be overriden by the below conditions");
                        defaultNotice.getStyleClass().add("lbl-default");

                        dataTypeSettings.getChildren().add(defaultValueWrapper);
                        dataTypeSettings.getChildren().add(defaultNotice);
                        dataTypeSettings.getChildren().add(radioScrollPane);

                        saveButton.setOnAction(event -> {
                            saveMapping(comboBox);
                            Mapping mapping = getSelectedMapping();
                            if (mapping != null) {
                                for (RadioCondition radioCondition : radioConditionList) {
                                    mapping.addRadioCondition(radioCondition);
                                }
                                List<MappingProperty> mappingPropertyList = mapping.getMappingProperties();
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.HAS_DEFAULT, hasDefaultValue.selectedProperty().getValue().toString());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DEFAULT_VALUE, defaultValue.getText());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATA_TYPE, DataType.RADIO.name());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.IS_DEPENDENT_FIELD, isDependentField.selectedProperty().getValue().toString());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DEPENDENT, dependent.getSelectionModel().getSelectedItem());
                            }
                        });

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

                        JFXCheckBox alwaysChecked = new JFXCheckBox("Always Checked (Overrides below condition)");
                        alwaysChecked.getStyleClass().add("checkbox-default");

                        dataTypeSettings.getChildren().add(alwaysChecked);
                        dataTypeSettings.getChildren().add(chboxWrapper);

                        Mapping savedMapping = getSelectedMapping();
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

                            alwaysChecked.setSelected(Boolean.valueOf(checkboxProperties.get(MapProperty.ALWAYS_CHECKED.toString())));
                        }

                        saveButton.setOnAction(event -> {
                            saveMapping(comboBox);
                            Mapping mapping = getSelectedMapping();
                            if (mapping != null) {
                                List<MappingProperty> mappingPropertyList = mapping.getMappingProperties();
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.CONDITIONAL, conditionals.getSelectionModel().getSelectedItem().name());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.TEXT_VALUE, value.textProperty().getValue());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.ALWAYS_CHECKED, alwaysChecked.selectedProperty().getValue().toString());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATA_TYPE, DataType.CHECKBOX.name());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.IS_DEPENDENT_FIELD, isDependentField.selectedProperty().getValue().toString());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DEPENDENT, dependent.getSelectionModel().getSelectedItem());
                            }
                        });
                        break;
                    case INITIAL:
                        Label initialLabel = new Label("Only the first letter of this field will be used.");
                        initialLabel.getStyleClass().add("lbl-default");

                        dataTypeSettings.getChildren().add(initialLabel);

                        saveButton.setOnAction(event -> {
                            saveMapping(comboBox);
                            Mapping mapping = getSelectedMapping();
                            if (mapping != null) {
                                List<MappingProperty> mappingPropertyList = mapping.getMappingProperties();
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATA_TYPE, DataType.INITIAL.name());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.IS_DEPENDENT_FIELD, isDependentField.selectedProperty().getValue().toString());
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DEPENDENT, dependent.getSelectionModel().getSelectedItem());
                            }
                        });
                        break;
                    case GROUP_NAME:
                        comboBox.getSelectionModel().clearSelection();
                        comboBox.setDisable(true);

                        saveButton.setOnAction(event -> {
                            saveMapping();
                            Mapping mapping = getSelectedMapping();
                            if (mapping != null) {
                                List<MappingProperty> mappingPropertyList = mapping.getMappingProperties();
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATA_TYPE, DataType.GROUP_NAME.name());
                            }
                        });
                        break;
                    case GROUP_NUMBER:
                        comboBox.getSelectionModel().clearSelection();
                        comboBox.setDisable(true);

                        saveButton.setOnAction(event -> {
                            saveMapping();
                            Mapping mapping = getSelectedMapping();
                            if (mapping != null) {
                                List<MappingProperty> mappingPropertyList = mapping.getMappingProperties();
                                saveOrUpdateMappingProperty(mapping, mappingPropertyList, MapProperty.DATA_TYPE, DataType.GROUP_NUMBER.name());
                            }
                        });
                        break;
                    default: //do nothing
                }
            }
        });

        Mapping savedMapping = getSelectedMapping();
        if (savedMapping != null) {
            HashMap<String, String> mappingPropertiesMap = savedMapping.getMappingPropertiesAsMap();

            String dataType = mappingPropertiesMap.get(MapProperty.DATA_TYPE.toString());

            if (Boolean.valueOf(formProperties.get(FormProperties.HAS_CHILDREN.toString())) || Boolean.valueOf(formProperties.get(FormProperties.HAS_SPOUSE.toString()))) {
                isDependentField.setSelected(Boolean.valueOf(mappingPropertiesMap.get(MapProperty.IS_DEPENDENT_FIELD.toString())));
                dependent.getSelectionModel().select(mappingPropertiesMap.get(MapProperty.DEPENDENT.toString()));
            }

            if (dataType != null) {
                dataTypeComboBox.getSelectionModel().select(DataType.valueOf(dataType));
            }
        }
    }

    private void addDependentSelections(JFXComboBox<String> dependent) {
        if (Boolean.valueOf(formProperties.get(FormProperties.HAS_SPOUSE.toString()))) {
            dependent.getItems().add("Spouse");
        }

        if (Boolean.valueOf(formProperties.get(FormProperties.HAS_CHILDREN.toString()))) {
            int numberOfChildren = Integer.valueOf(formProperties.get(FormProperties.CHILDREN_COUNT.toString()));

            for (int i = 1; i <= numberOfChildren; i++) {
                dependent.getItems().add("Child " + i);
            }
        }
    }

    private Mapping getSelectedMapping() {
        return new Mapping().findByFormFieldId(formFieldListView.getSelectionModel().getSelectedItem().getId());
    }

    private void addCondition(RadioCondition radioCondition, VBox radioConditions, JFXComboBox<CensusHeader> comboBox) {
        //When {Census Header} {Conditional} {Value}, {Form Field} is {Value}
        Label conditionLabel = new Label("When " + getSelectedHeader(comboBox) + " ");
        JFXComboBox<Conditional> conditionals = new JFXComboBox<>(); //Hey, Listen
        conditionals.getItems().addAll(Conditional.values());
        JFXTextField censusValue = new JFXTextField(); //Hey, Listen
        Label conditionLabelContinued = new Label(", " + formFieldListView.getSelectionModel().getSelectedItem().getFieldName() + " is ");
        JFXTextField formValue = new JFXTextField(); //Hey, Listen

        conditionals.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Conditional>() {
            @Override
            public void changed(ObservableValue<? extends Conditional> observable, Conditional oldValue, Conditional newValue) {
                radioCondition.setConditional(newValue);
            }
        });

        censusValue.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                radioCondition.setCensusValue(censusValue.getText());
            }
        });

        formValue.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                radioCondition.setFormValue(formValue.getText());
            }
        });

        HBox conditionWrapper = new HBox();
        conditionWrapper.setSpacing(10);
        conditionWrapper.setAlignment(Pos.BOTTOM_CENTER);

        if (radioCondition.hasId()) {
            JFXButton deleteButton = new JFXButton("-");
            deleteButton.setButtonType(JFXButton.ButtonType.RAISED);
            deleteButton.getStyleClass().add("btn-default");
            deleteButton.setOnAction(event -> {
                radioCondition.delete();
//                radioConditions.getChildren().remove(conditionWrapper);
                populateSettingsPane(formFieldListView.getSelectionModel().getSelectedItem()); //A little arcane
            });
            conditionWrapper.getChildren().add(deleteButton);

//            Conditional conditional = radioCondition.getConditional();

            if (radioCondition.getConditional() != null) {
                conditionals.getSelectionModel().select(radioCondition.getConditional());
            }
            censusValue.setText(radioCondition.getCensusValue());
            formValue.setText(radioCondition.getFormValue());
        }

        conditionWrapper.getChildren().addAll(conditionLabel, conditionals, censusValue, conditionLabelContinued, formValue);

        radioConditions.getChildren().add(conditionWrapper);
    }

    private String getSelectedHeader(JFXComboBox<CensusHeader> comboBox) {
        return comboBox.getSelectionModel().getSelectedItem().getHeader();
    }

    private void saveMapping(JFXComboBox<CensusHeader> comboBox) {
        Mapping mapping = getSelectedMapping();
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

    private void saveMapping() {
        Mapping mapping = getSelectedMapping();
        if (mapping != null) {
            if (mapping.getCensusHeaderId() == 0) {
                CensusHeader dummyHeader = new CensusHeader("No Header", form.getId());
                dummyHeader.save();
                mapping.setCensusHeaderId(dummyHeader.getId());
            }
            mapping.save();
            Snackbar.show(wrapper, "Mapping updated.");
        } else {
            mapping = new Mapping();
            mapping.setFormId(form.getId());
            CensusHeader dummyHeader = new CensusHeader("No Header", form.getId());
            dummyHeader.save();
            mapping.setCensusHeaderId(dummyHeader.getId());
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
