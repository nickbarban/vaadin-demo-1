package com.vaadinboot.demo.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.Autocapitalize;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.fluttercode.datafactory.impl.DataFactory;

import java.util.stream.IntStream;

/**
 * @author Nick Barban.
 */
@Route("game")
public class GameView extends VerticalLayout {

    private static final int GAME_COMPLEXITY = 5;
    final String secretWord;


    public GameView() {
        secretWord = generateSecretWord();

        System.out.println(secretWord);

        Label label = new Label("Make a word");
        HorizontalLayout makeWord = new HorizontalLayout();
        IntStream.range(0, GAME_COMPLEXITY).forEach(i -> {
            TextField field = new TextField();
            field.setWidth("50px");
            field.setMaxLength(1);
            field.setMinLength(1);
            field.setAutocapitalize(Autocapitalize.CHARACTERS);
            field.getStyle().set("text-align", "center");
            field.getStyle().set("text-transform", "uppercase");
            field.setPattern("[a-z]");
            makeWord.add(field);
        });
        makeWord.add(new Button("Check"));
        makeWord.setSpacing(true);
        add(label, makeWord);
        this.setAlignItems(Alignment.CENTER);
        this.setAlignSelf(Alignment.CENTER);

    }

    private String generateSecretWord() {
        String randomWord = "";

        while (hasRepeatableChars(randomWord)) {
            randomWord = new DataFactory().getRandomWord(GAME_COMPLEXITY);
        }

        return randomWord;
    }

    private static boolean hasRepeatableChars(String str) {
        if (StringUtils.isEmpty(str)) {
            return true;
        }

        boolean[] charSet = new boolean[256];

        for (int i = 0; i < str.length(); i++) {
            int val = str.charAt(i);

            if (charSet[val]) {
                return false;
            }

            charSet[val] = true;
        }

        return true;
    }
}
