package com.myjavafx.movieclient.view;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@FXMLView(value = "/fxml/Login.fxml")
public class LoginView extends AbstractFxmlView {
    public LoginView() {
        super();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        super.setApplicationContext(applicationContext);
    }

    @Override
    public Parent getView() {
        return super.getView();
    }

    @Override
    public void getView(Consumer<Parent> consumer) {
        super.getView(consumer);
    }

    @Override
    public Node getViewWithoutRootContainer() {
        return super.getViewWithoutRootContainer();
    }

    @Override
    public Object getPresenter() {
        return super.getPresenter();
    }

    @Override
    public void getPresenter(Consumer<Object> presenterConsumer) {
        super.getPresenter(presenterConsumer);
    }

    @Override
    public Optional<ResourceBundle> getResourceBundle() {
        return super.getResourceBundle();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
