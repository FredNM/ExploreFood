package com.frednm.explorefood.features.recipe.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Instead of creating Custom Data Binding on TextView, use the usual android:text, and call methods of this class to do
 * the text modifications needed.
 * See https://medium.com/androiddevelopers/data-binding-lessons-learnt-4fd16576b719
 */

public class TextCreator {

    public String showIngredients(List<String> ingredients) {
        if (ingredients != null) {
        List<String> paragraph = new ArrayList<>();
        for(int i=0;i<ingredients.size();i++){
            String sentence = "   -  " + ingredients.get(i);
            paragraph.add(sentence);
        }
        return TextUtils.join("\n", paragraph);
        } else {
            return null;
        }
    }
}


