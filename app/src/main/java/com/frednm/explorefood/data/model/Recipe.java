package com.frednm.explorefood.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Entity(primaryKeys = {"label","imageUrl"}) // Composite primary key, having name + image (image url)
// Vu qu'il n'y a pas d'id unique de chaque recette sur l'API, j'était obligé de créer une clé ici, et j'ai choisi le tandem nom + url de l'image !
// Une possibilité aurait été de créer moi mm la clé unique ici, assignant automatiquement à chaque fois un nbre à la recette, mais le danger c'est
// que comme je prends les infos en ligne, rien ne me garantit que la mm recette ne reviendra pas 2 fois, la première fois je l'aurai numérotée
// 11 et la 2e fois 143 par exemple, créant ainsi des doublons ! Là le meilleur choix de clé unique, c'est vraiment d'utiliser les infos qui viennent
// de la requête en ligne, et là j'ai pris 2 colonnes.
public class Recipe {

    @SerializedName("uri")
    @Expose
    private String uri;

    @NonNull
    @SerializedName("label")
    @Expose
    private String label; // recipe Title

    @NonNull
    @SerializedName("image")
    @Expose
    private String imageUrl;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("url")
    @Expose
    private String recipeUrl; // Where you will find instructions about recipe

    @SerializedName("ingredientLines")
    @Expose
    private List<String> ingredients;

//    @SerializedName("time")
//    @Expose
//    private String time;

//    @SerializedName("instructions")
//    @Expose
//    private String instructions;

    // only for DB
    @ColumnInfo(defaultValue = "0")
    private Boolean isUserFavorite;
    @ColumnInfo(defaultValue = "0")
    private Boolean isInBasket;
    @ColumnInfo(defaultValue = "0")
    private Boolean isOnlineRecipe;

    public Recipe(String uri, @NotNull String label, @NotNull String imageUrl, String source, String recipeUrl, List<String> ingredients, Boolean isUserFavorite, Boolean isInBasket, Boolean isOnlineRecipe) {
        this.uri = uri;
        this.label = label;
        this.imageUrl = imageUrl;
        this.source = source;
        this.recipeUrl = recipeUrl;
        this.ingredients = ingredients;
        this.isUserFavorite = isUserFavorite;
        this.isInBasket = isInBasket;
        this.isOnlineRecipe = isOnlineRecipe;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Boolean getUserFavorite() {
        return isUserFavorite;
    }

    public void setUserFavorite(Boolean userFavorite) {
        isUserFavorite = userFavorite;
    }

    public Boolean getInBasket() {
        return isInBasket;
    }

    public void setInBasket(Boolean inBasket) {
        isInBasket = inBasket;
    }

    public Boolean getOnlineRecipe() {
        return isOnlineRecipe;
    }

    public void setOnlineRecipe(Boolean onlineResult) {
        isOnlineRecipe = onlineResult;
    }
}
