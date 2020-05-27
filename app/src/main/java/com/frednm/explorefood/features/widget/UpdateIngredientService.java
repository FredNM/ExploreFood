package com.frednm.explorefood.features.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class UpdateIngredientService extends IntentService {

    public static String INGREDIENTS_LIST = "INGREDIENTS_LIST";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UpdateIngredientService() {
        super("UpdateIngredientService");
    }

    public static void startBakingService(Context context, ArrayList<String> ingredientsList) {
        Intent intent = new Intent(context, UpdateIngredientService.class);
        intent.putExtra(INGREDIENTS_LIST, ingredientsList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<String> ingredientsList = intent.getExtras().getStringArrayList(INGREDIENTS_LIST);
            handleActionUpdateBakingWidgets(ingredientsList);
        }
    }

    private void handleActionUpdateBakingWidgets(ArrayList<String> ingredientsList) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.putExtra(INGREDIENTS_LIST, ingredientsList);
        sendBroadcast(intent);
    }
}
