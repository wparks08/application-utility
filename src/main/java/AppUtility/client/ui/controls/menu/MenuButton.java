package AppUtility.client.ui.controls.menu;

import com.jfoenix.controls.JFXButton;
import javafx.scene.paint.Paint;

public class MenuButton extends JFXButton {
    private final String MENU_BUTTON_STYLE = "-fx-background-color: #424242; -fx-background-radius: 0; -fx-text-fill: #EEEEEE;";
    private final String STYLE_CLASS = "dashboard-menu-button";
    private final String TEXT_FILL = "WHITE";
    private final double PREF_HEIGHT = 50.0;
    private final double PREF_WIDTH = 256.0;

    public MenuButton() {
        super();
        initialize();
    }

    public MenuButton(String text) {
        super(text);
    }

    private void initialize() {
        this.setStyle(MENU_BUTTON_STYLE);
        this.getStyleClass().add(STYLE_CLASS);
        this.setTextFill(Paint.valueOf(TEXT_FILL));
        this.setPrefHeight(PREF_HEIGHT);
        this.setPrefWidth(PREF_WIDTH);
    }
}
