package com.example.contacts.controller;

import jakarta.el.*;
import jakarta.enterprise.context.*;
import jakarta.faces.annotation.*;
import jakarta.faces.application.*;
import jakarta.faces.component.*;
import jakarta.faces.component.behavior.*;
import jakarta.faces.component.html.*;
import jakarta.faces.context.*;
import jakarta.faces.view.facelets.*;

import java.io.*;
import java.util.*;

@View("/hello-java.xhtml")
@ApplicationScoped
public class HelloJavaFacelet extends Facelet {

    @Override
    public void apply(FacesContext context, UIComponent parent) throws IOException {
        if (findRoot(parent) != null) {
            return;
        }

        Application application = context.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();

        HtmlPanelGroup root = new HtmlPanelGroup();
        root.setId("helloJavaRoot");
        root.setLayout("block");
        parent.getChildren().add(root);

        HtmlPanelGroup resultPanel = new HtmlPanelGroup();
        resultPanel.setId("resultPanel");
        resultPanel.setLayout("block");
        root.getChildren().add(resultPanel);

        HtmlOutputText helloPrefix = new HtmlOutputText();
        helloPrefix.setValue("Hello, ");
        resultPanel.getChildren().add(helloPrefix);

        HtmlOutputText helloName = new HtmlOutputText();
        helloName.setValueExpression("value", valueExpression(
                expressionFactory,
                context,
                "#{helloBean.name}",
                String.class
        ));
        resultPanel.getChildren().add(helloName);

        HtmlOutputText helloSuffix = new HtmlOutputText();
        helloSuffix.setValue("!");
        resultPanel.getChildren().add(helloSuffix);

        HtmlPanelGroup namesPanel = new HtmlPanelGroup();
        namesPanel.setLayout("block");
        namesPanel.setValueExpression("rendered", valueExpression(
                expressionFactory,
                context,
                "#{not empty helloBean.names}",
                Boolean.class
        ));
        resultPanel.getChildren().add(namesPanel);

        HtmlDataTable namesTable = new HtmlDataTable();
        namesTable.setId("namesTable");
        namesTable.setValueExpression("value", valueExpression(
                expressionFactory,
                context,
                "#{helloBean.names}",
                Object.class
        ));
        namesTable.setVar("nameEntity");
        namesPanel.getChildren().add(namesTable);

        UIColumn namesColumn = new UIColumn();
        namesTable.getChildren().add(namesColumn);

        HtmlOutputText nameOutput = new HtmlOutputText();
        nameOutput.setValueExpression("value", valueExpression(
                expressionFactory,
                context,
                "#{nameEntity.name}",
                String.class
        ));
        namesColumn.getChildren().add(nameOutput);

        HtmlPanelGroup emptyPanel = new HtmlPanelGroup();
        emptyPanel.setLayout("block");
        emptyPanel.setValueExpression("rendered", valueExpression(
                expressionFactory,
                context,
                "#{empty helloBean.names}",
                Boolean.class
        ));
        resultPanel.getChildren().add(emptyPanel);

        HtmlOutputText emptyMessage = new HtmlOutputText();
        emptyMessage.setValue("No names have been added yet.");
        emptyPanel.getChildren().add(emptyMessage);

        HtmlForm form = new HtmlForm();
        form.setId("nameForm");
        root.getChildren().add(form);

        HtmlOutputLabel label = new HtmlOutputLabel();
        label.setFor("name");
        label.setValue("Name:");
        form.getChildren().add(label);

        form.getChildren().add(spacer());

        HtmlInputText input = new HtmlInputText();
        input.setId("name");
        input.setRequired(true);
        input.setValueExpression("value", valueExpression(
                expressionFactory,
                context,
                "#{helloBean.name}",
                String.class
        ));
        form.getChildren().add(input);

        form.getChildren().add(spacer());

        HtmlCommandButton button = new HtmlCommandButton();
        button.setValue("Update");
        button.setActionExpression(methodExpression(
                expressionFactory,
                context,
                "#{helloBean.submit}"
        ));
        addAjax(application, button);
        form.getChildren().add(button);
    }

    private static UIComponent findRoot(UIComponent parent) {
        for (UIComponent child : parent.getChildren()) {
            if ("helloJavaRoot".equals(child.getId())) {
                return child;
            }
        }
        return null;
    }

    private static void addAjax(Application application, HtmlCommandButton button) {
        AjaxBehavior ajaxBehavior = (AjaxBehavior) application.createBehavior(AjaxBehavior.BEHAVIOR_ID);
        ajaxBehavior.setExecute(List.of("nameForm"));
        ajaxBehavior.setRender(List.of("resultPanel"));
        ((ClientBehaviorHolder) button).addClientBehavior("action", ajaxBehavior);
    }

    private static ValueExpression valueExpression(
            ExpressionFactory expressionFactory,
            FacesContext context,
            String expression,
            Class<?> type
    ) {
        return expressionFactory.createValueExpression(context.getELContext(), expression, type);
    }

    private static MethodExpression methodExpression(
            ExpressionFactory expressionFactory,
            FacesContext context,
            String expression
    ) {
        return expressionFactory.createMethodExpression(context.getELContext(), expression, null, new Class<?>[0]);
    }

    private static HtmlOutputText spacer() {
        HtmlOutputText spacer = new HtmlOutputText();
        spacer.setValue(" ");
        return spacer;
    }
}
