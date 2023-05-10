package com.myjavafx.movieclient;

import com.myjavafx.movieclient.view.LaunchView;
import com.myjavafx.movieclient.view.LoginView;
import com.myjavafx.movieclient.view.ManagerView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication
public class MovieClientApplication extends AbstractJavaFxApplicationSupport {

    public static Stage myStage;
    public static void main(String[] args) {

        launch(MovieClientApplication.class, LoginView.class,new LaunchView(), args);
        //SpringApplication.run(MovieClientApplication.class, args);
    }

    public MovieClientApplication() {
        super();
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        myStage = stage;
        stage.centerOnScreen();
//        double width = 600;
//        double height = 400;
//
//        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
//        stage.setX((screenBounds.getWidth() - width) / 2);
//        stage.setY((screenBounds.getHeight() - height) / 2);

        //final Scene scene = new Scene( new Group(), width, height);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    public void beforeInitialView(Stage stage, ConfigurableApplicationContext ctx) {
        super.beforeInitialView(stage, ctx);
        stage.initStyle(StageStyle.UNDECORATED);
    }

    @Override
    public void beforeShowingSplash(Stage splashStage) {
        super.beforeShowingSplash(splashStage);
    }

    @Override
    public Collection<Image> loadDefaultIcons() {
        return Arrays.asList(new Image(this.getClass().getClassLoader().getResource("images/icons/icon.png").toExternalForm()));
    }
}
