package com.frednm.explorefood.data.repository.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


/**
 * Provides Connectivity updates as {@link LiveData}
 */
public class ConnectivityLiveData extends LiveData<Boolean> {

    private final ConnectivityManager connectivityManager;

    private final NetworkCallback networkCallback = new NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            postValue(true);
        } // postValue s'applique à notre LiveDaata, c'est ce que renverra ConnectivityLiveData ! Mais on utilise postValue() car on est susceptible
        // de le faire plusieurs fois comme on le voit aussi en bas, et seul la dernière valeur envoyée sera postée, donc la plus récente.

        @Override
        public void onLost(Network network) {
            postValue(false);
        }
    };

    public ConnectivityLiveData(Context context) {
        connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void onActive() { // This method is executed when the number of active observers goes from 0 to 1
    // Donc dès que l'observer est actif, directement on exécute hasConnectivityCheck pour envoyer l'état actuel de la connexion
    // Ensuite, on "enregistre" le connectivyManager, et en fait la méthode de CallBack là va s'exécuter chaque fois qu'il y'aura
    // un changement de la connectivité ! Et comme on le voit, elle prend en argument notre networkCallback, et va donc exécuter sa
    // méthode onAvailable si Network available, ou onLost si network perdu ! Et comme on le voit, dedans on ne va faire qu'envoyer
    // l'état actuel de la connexion !!!
    // NB : En réalité, je pense que ceci ne va jamais être exécuté ! Notre LiveData n'est pas observé au niveau du Repository, vu que
    // pour être un observeur, il faut avoir un cycle de Vie ! Ceci aurait nécessité de créer une classe "Observer" étendant LifeCycle,
    // dans laquelle on allait dire considère que tu es actif quand par exemple une méthode du Repository est exécutée, sinon tu es inactif.
    // Mais bon c'est bcp d'extensions ! Finalement comme on le voit dans NetworkBoundResource, on appelle la méthode getValue() pour avoir
    // la valeur du LiveData, et ci-dessous on l'a redéfini de manière à exécuter le hasConnectivityCheck() lorsqu'elle est appelée.
    // TODO : A la limite, ceci sera à faire plus tard en Kotlin, avec Flowable et non LiveData ! Car Flowable lui aussi est réactif, sauf
    // TODO : qu'il ne nécessite pas d'observeur ayant un cycle de vie. Il est plus approprié de l'utiliser au niveau de la couche data (repository
    // TODO : model, etc), et le LiveData plutôt au niveau de la View&ViewModel.

        super.onActive();

        postValue(hasConnectivityCheck());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback);
        }
    }

    @Override
    protected void onInactive() { // Donc quand on n'a plus d'observeur actif, "désactive" la méthode de callback !!!
        super.onInactive();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    private boolean hasConnectivityCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // For newer SDk version, SDK Min 23
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ;
            }
        } else {  // For older SDK version
            try {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
                    Log.i("update_statut", "Network is available : true");
                    return true;
                }
            } catch (Exception e) {
                Log.i("update_statut", "" + e.getMessage());
            }
        }
        return false;
    }

    @Nullable
    @Override
    public Boolean getValue() {
        Boolean value = super.getValue();
        return (value != null)? null : hasConnectivityCheck(); // Je ne capte pas pourquoi cette logique inversée ...
    }
}

