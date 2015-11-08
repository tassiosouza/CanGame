package com.funfactory.cangamemake.view.adapter;

import android.text.InputFilter;
import android.text.Spanned;

public class FiltroNome implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        for (int i = start; i < end; i++) {
            if (!Character.isLetter(source.charAt(i)) && !Character.isSpace(source.charAt(i))) {
                return "";
            }
        }
        return null;
    }

}
