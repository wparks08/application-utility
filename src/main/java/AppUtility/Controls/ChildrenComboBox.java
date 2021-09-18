package AppUtility.Controls;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ChildrenComboBox extends JFXComboBox<Integer> {
    private static final int MAX_CHILDREN = 15;
    private static final String PROMPT_TEXT = "Number of Children per Form";

    public ChildrenComboBox() {
        super();
        addIncrementsToItems();
        setPromptText(PROMPT_TEXT);
        setDisable(true);
    }

    private void addIncrementsToItems() {
        for (int i = 1; i <= MAX_CHILDREN; i++) {
            this.getItems().add(i);
        }
    }

    public void bindCheckboxToDisableProperty(JFXCheckBox checkBox) {
        ChildrenComboBox childrenComboBox = this;
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    childrenComboBox.setDisable(false);
                } else {
                    childrenComboBox.setDisable(true);
                }
            }
        });
    }
}
