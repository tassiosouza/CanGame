package com.funfactory.cangamemake.presenter;

import com.funfactory.cangamemake.util.DialogUtil.DialogCallback;

import android.app.Activity;
import android.content.Intent;

public interface IGenericActivity {

    /**
     * Exibe toast com a mensagem passada
     */
    public void toastMessage(int resId);
    
    /**
     * Exibe toast com a mensagem passada
     */
    public void toastMessage(String message);

    /**
     * Retorna instancia da activity.
     */
    public Activity getContext();

    /**
     * Encerra activity corrente.
     */
    public void finalizarActivity();

    /**
     * Executa intent passada.
     */
    void executarIntent(Intent intent);
    
    /**
     * Exibe dialog com atributos recebido.
     */
    void confirmarAcao(int titulo, int mensagem, DialogCallback callback);

}
