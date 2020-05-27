package com.frednm.explorefood.data.local;

import androidx.room.TypeConverter;

import com.frednm.explorefood.data.model.Location;
import com.frednm.explorefood.data.model.Rating;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;


public class Converters {

    private static Gson gson = new Gson();

    // 1- For List<String>
    @TypeConverter
    public static List<String> stringToStringList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String stringListToString(List<String> someObjects) {
        return gson.toJson(someObjects);
    }

    // 2- For Location
    @TypeConverter
    public static Location stringToLocation(String data) {
        Type listType = new TypeToken<Location>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String locationToString(Location someObjects) {
        return gson.toJson(someObjects);
    }

    // 3- For Rating
    @TypeConverter
    public static Rating stringToRating(String data) {
        Type listType = new TypeToken<Rating>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String ratingToString(Rating someObjects) {
        return gson.toJson(someObjects);
    }
}
