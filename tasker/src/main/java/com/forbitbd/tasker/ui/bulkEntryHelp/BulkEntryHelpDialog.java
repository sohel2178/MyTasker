package com.forbitbd.tasker.ui.bulkEntryHelp;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forbitbd.tasker.R;
import com.google.android.material.button.MaterialButton;


public class BulkEntryHelpDialog extends DialogFragment {



    public BulkEntryHelpDialog() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bulk_entry_help_dialog, container, false);
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_bulk_entry_help_dialog, null);
        initView(view);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), com.forbitbd.androidutils.R.style.MyDialog).create();

        alertDialog.setView(view);
        return alertDialog;
    }

    private void initView(View view) {

        MaterialButton btnClose = view.findViewById(R.id.close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}