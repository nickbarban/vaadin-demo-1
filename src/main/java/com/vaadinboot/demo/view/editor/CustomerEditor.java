package com.vaadinboot.demo.view.editor;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadinboot.demo.domain.Customer;
import com.vaadinboot.demo.repository.CustomerRepository;

/**
 * @author Nick Barban.
 */
@SpringComponent
@UIScope
public class CustomerEditor extends VerticalLayout implements KeyNotifier {

    private final CustomerRepository repository;

    private Customer customer;

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");

    Button saveButton = new Button("Save", VaadinIcon.CHECK.create());
    Button cancelButton = new Button("Cancel");
    Button deleteButton = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton, deleteButton);

    Binder<Customer> binder = new Binder<>(Customer.class);
    private ChangeHandler changeHandler;

    public CustomerEditor(CustomerRepository repository) {

        this.repository = repository;

        add(firstName, lastName, actions);

        binder.bindInstanceFields(this);

        setSpacing(true);

        saveButton.getElement().getThemeList().add("primary");
        deleteButton.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        saveButton.addClickListener(e -> save());
        deleteButton.addClickListener(e -> delete());
        cancelButton.addClickListener(e -> editCustomer(customer));
        setVisible(false);
    }

    public void editCustomer(Customer c) {
        if (c == null) {
            setVisible(false);
            return;
        }

        final boolean persisted = c.getId() != null;
        if (persisted) {
            customer = repository.findById(c.getId()).get();
        } else {
            customer = c;
        }

        cancelButton.setVisible(persisted);

        binder.setBean(customer);
        setVisible(true);

        firstName.focus();
    }

    void delete() {
        repository.delete(customer);
        changeHandler.onChange();
    }

    void save() {
        repository.save(customer);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}
