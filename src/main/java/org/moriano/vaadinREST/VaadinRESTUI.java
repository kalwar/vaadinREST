package org.moriano.vaadinREST;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.moriano.vaadinREST.model.FullHttpResponse;
import org.moriano.vaadinREST.util.HttpMethod;
import org.moriano.vaadinREST.util.HttpRequester;

import java.util.Map;

@Theme("mytheme")
@SuppressWarnings("serial")
public class VaadinRESTUI extends UI {

    private TextField urlField = new TextField();
    private TextArea responseArea = new TextArea();
    private ComboBox httpMethods = new ComboBox();
    private Button button = new Button("Send!");
    private final VerticalLayout baseLayout = new VerticalLayout();
    private final VerticalLayout layout = new VerticalLayout();
    private final HorizontalLayout topLayout = new HorizontalLayout();
    private HttpRequester httpRequester = new HttpRequester();
    private Table responseHeadersTable = new Table();


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
        this.baseLayout.setMargin(true);
        this.setContent(baseLayout);
        this.baseLayout.addComponent(this.topLayout);
        this.topLayout.addComponent(this.urlField);
        this.topLayout.addComponent(this.httpMethods);
        this.topLayout.addComponent(this.button);
        this.layout.addComponent(responseHeadersTable);
        this.layout.addComponent(this.responseArea);
        this.baseLayout.addComponent(topLayout);
        this.baseLayout.addComponent(layout);
    }

    /**
     * Configure some components, normally populating them
     */
    private void configureComponents() {
        this.responseArea.setRows(40);
        this.responseArea.setColumns(130);
        this.urlField.setColumns(40);
        this.httpMethods.addItem(HttpMethod.GET.getMethod());
        this.httpMethods.addItem(HttpMethod.POST.getMethod());
        this.httpMethods.addItem(HttpMethod.PUT.getMethod());
        this.httpMethods.addItem(HttpMethod.DELETE.getMethod());
        this.httpMethods.setValue(HttpMethod.GET.getMethod());
        this.responseHeadersTable.addContainerProperty("Name", String.class, null);
        this.responseHeadersTable.addContainerProperty("Value", String.class, null);

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
                FullHttpResponse response = httpRequester.call(urlField.getValue(), httpMethods.getValue().toString(), null);


                StringBuilder buffer = new StringBuilder(responseArea.getValue());
                buffer.append("RESPONSE CODE IS ").append(response.getResponseCode()).append("\n========================================\n");
                Map<String, String> headers = response.getHeaders();
                responseHeadersTable.removeAllItems();
                for(Map.Entry<String, String> entry : headers.entrySet()) {
                    responseHeadersTable.addItem(new Object[]{entry.getKey(), entry.getValue()}, entry.getKey());

                }

                buffer.append("\n========================================\nRESPONSE\n").append(response.getResponse());



                responseArea.setValue(buffer.toString());
            }
        });
    }
}