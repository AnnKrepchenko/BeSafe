package com.krepchenko.besafe.ui.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.krepchenko.besafe.R;


public class LoadingDialog extends DialogFragment {

    public static LoadingDialog newInstance() {
        return new LoadingDialog();
    }

    public static LoadingDialog start(FragmentManager manager, String dialogTag) {
        LoadingDialog dialog = LoadingDialog.newInstance();
        dialog.setCancelable(false);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(dialog, dialogTag);
        transaction.commitAllowingStateLoss();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View baseView = inflater.inflate(R.layout.dialog_loading, container, false);
        return baseView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

}
