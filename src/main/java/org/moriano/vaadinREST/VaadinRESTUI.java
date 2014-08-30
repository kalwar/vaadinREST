package org.moriano.vaadinREST;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

@Theme("mytheme")
@SuppressWarnings("serial")
public class VaadinRESTUI extends UI {

    private TextField urlField = new TextField("Url");
    private TextArea responseArea = new TextArea();
    private ComboBox httpMethods = new ComboBox("Method");
    private Button button = new Button("Send!");
    private final VerticalLayout layout = new VerticalLayout();
    private HttpRequester httpRequester = new HttpRequester();


    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = VaadinRESTUI.class, widgetset = "org.moriano.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        this.configureLayout();
        this.configureComponents();
        this.addListeners();
    }

    /**
     * Set up the layout
     */
    private void configureLayout() {
        this.layout.setMargin(true);
        this.setContent(layout);
        this.layout.addComponent(this.urlField);
        this.layout.addComponent(this.httpMethods);
        this.layout.addComponent(this.button);
        this.layout.addComponent(this.responseArea);
    }

    /**
     * Configure some components, normally populating them
     */
    private void configureComponents() {
        this.responseArea.setRows(40);
        this.responseArea.setColumns(130);
        this.httpMethods.removeAllItems();
        this.httpMethods.addItem(HttpMethod.GET.getMethod());
        this.httpMethods.addItem(HttpMethod.POST.getMethod());
        this.httpMethods.addItem(HttpMethod.PUT.getMethod());
        this.httpMethods.addItem(HttpMethod.DELETE.getMethod());

        this.urlField.setValue("http://www.google.com");
    }

    /**
     * Well, we need our client to do something, don't we? :)
     */
    private void addListeners() {
        this.button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                responseArea.setValue("");
                responseArea.setValue("Sending to " + urlField.getValue() + "\n Using " + httpMethods.getValue() + "\n========================================\n");
                String response = httpRequester.call(urlField.getValue(), httpMethods.getValue().toString(), null);
                responseArea.setValue(responseArea.getValue() + "\n" + response);
            }
        });
    }
}