package com.vaadinboot.demo.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadinboot.demo.domain.Customer;
import com.vaadinboot.demo.repository.CustomerRepository;
import com.vaadinboot.demo.view.editor.CustomerEditor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Nick Barban.
 */
@Route
public class MainView extends VerticalLayout {

    private final CustomerRepository repository;

    private final CustomerEditor editor;

    private final Button addNewButton;

    final Grid<Customer> grid;

    final TextField filter;

    public MainView(CustomerRepository repository, CustomerEditor editor) {
        this.repository = repository;
        this.editor = editor;
        this.grid = new Grid<>(Customer.class);
        this.filter = new TextField();
        this.addNewButton = new Button("New customer", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewButton);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("id", "firstName", "lastName");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Filter by last name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> editor.editCustomer(e.getValue()));

        addNewButton.addClickListener(event -> editor.editCustomer(new Customer("", "")));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCustomers(filter.getValue());
        });

        listCustomers(null);
    }

    void listCustomers(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(repository.findAll());
        } else {
            grid.setItems(repository.findByLastNameStartsWithIgnoreCase(filterText));
        }
    }
}
