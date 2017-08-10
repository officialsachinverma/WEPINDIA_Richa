package com.wepindia.pos.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.Kitchen;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;

public class FragmentKitchen extends Fragment {

    Context myContext;
    DatabaseHandler dbKitchen ;
    MessageDialog MsgBox;
    EditText txtKitchenName;
    WepButton btnAddKitchen, btnEditKitchen,btnClearKitchen,btnCloseKitchen;
    TableLayout tblKitchen;
    String strKitchenCode = "";
    String strKitchenName = "";


    public FragmentKitchen() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            dbKitchen = new DatabaseHandler(getActivity());
            dbKitchen.OpenDatabase();
        }catch (Exception e){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_kitchen, container, false);
        myContext = getActivity();
        MsgBox = new MessageDialog(myContext);
        txtKitchenName = (EditText)view.findViewById(R.id.etKitchenName);
        tblKitchen = (TableLayout)view.findViewById(R.id.tblKitchen);
        btnAddKitchen = (WepButton) view.findViewById(R.id.btnAddKitchen);
        btnAddKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddKitchen();
            }
        });
        btnEditKitchen = (WepButton) view.findViewById(R.id.btnEditKitchen);
        btnEditKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCateg();
            }
        });
        btnClearKitchen = (WepButton) view.findViewById(R.id.btnClearKitchen);
        btnClearKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearKitchen();
            }
        });
        btnCloseKitchen = (WepButton) view.findViewById(R.id.btnCloseKitchen);
        btnCloseKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseKitchen();
            }
        });
        ResetKitchen();
        try{
            dbKitchen.CloseDatabase();
            dbKitchen.CreateDatabase();
            dbKitchen.OpenDatabase();
            DisplayKitchen();
        }
        catch(Exception exp){
            Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private void DisplayKitchen(){
        Cursor crsrKitchen;
        crsrKitchen = dbKitchen.getAllKitchen();

        TableRow rowKitchen = null;
        TextView tvSno, tvKitchenCode, tvKitchenName;
        ImageButton btnImgDelete;
        int i = 1;
        if(crsrKitchen.moveToFirst()){
            do{
                rowKitchen = new TableRow(myContext);
                rowKitchen.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                rowKitchen.setBackgroundResource(R.drawable.row_background);

                tvSno = new TextView(myContext);
                tvSno.setTextSize(18);
                tvSno.setGravity(1);
                tvSno.setText(String.valueOf(i));
                rowKitchen.addView(tvSno);

                tvKitchenCode = new TextView(myContext);
                tvKitchenCode.setTextSize(18);
                tvKitchenCode.setText(crsrKitchen.getString(0));
                rowKitchen.addView(tvKitchenCode);

                tvKitchenName = new TextView(myContext);
                tvKitchenName.setTextSize(18);
                tvKitchenName.setText(crsrKitchen.getString(1));
                rowKitchen.addView(tvKitchenName);

                // Delete
                int res = getResources().getIdentifier("delete", "drawable", getActivity().getPackageName());
                btnImgDelete = new ImageButton(myContext);
                btnImgDelete.setImageResource(res);
                btnImgDelete.setLayoutParams(new TableRow.LayoutParams(60, 40));
                btnImgDelete.setOnClickListener(mListener);
                rowKitchen.addView(btnImgDelete);

                rowKitchen.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if(String.valueOf(v.getTag()) == "TAG"){
                            TableRow Row = (TableRow) v;
                            TextView CategCode = (TextView)Row.getChildAt(1);
                            TextView CategName = (TextView)Row.getChildAt(2);
                            strKitchenCode = CategCode.getText().toString();
                            txtKitchenName.setText(CategName.getText());
                            btnAddKitchen.setEnabled(false);
                            btnEditKitchen.setEnabled(true);
                        }
                    }
                });

                rowKitchen.setTag("TAG");

                tblKitchen.addView(rowKitchen,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                i++;
            }while(crsrKitchen.moveToNext());
        }
        else{
            Log.d("DisplayCategory","No Dept found");
        }

    }

    private View.OnClickListener mListener = new View.OnClickListener() {

        public void onClick(final View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                    .setTitle("Delete")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Are you sure you want to Delete this Kitchen")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            TableRow tr = (TableRow) v.getParent();
                            TextView KitchenCode = (TextView) tr.getChildAt(1);
                            long lResult = dbKitchen.DeleteKitchen(KitchenCode.getText().toString());
                            //MsgBox.Show("", "Kitchen Deleted Successfully");
                            Toast.makeText(myContext, "Kitchen Deleted Successfully", Toast.LENGTH_SHORT).show();

                            ClearKitchenTable();
                            DisplayKitchen();

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };

    private boolean IsKitchenExists(String KitchenName){
        boolean isKitchenExists = false;
        String strKitchen="";
        TextView Kitchen;

        for(int i=1; i<tblKitchen.getChildCount(); i++){

            TableRow Row = (TableRow)tblKitchen.getChildAt(i);

            if(Row.getChildAt(0) != null){
                Kitchen = (TextView) Row.getChildAt(2);

                strKitchen = Kitchen.getText().toString();

                Log.v("KitchenActivity", "Kitchen:" + strKitchen.toUpperCase() + " New Kitchen:" + KitchenName.toUpperCase());

                if(strKitchen.toUpperCase().equalsIgnoreCase(KitchenName.toUpperCase())){
                    isKitchenExists = true;
                    break;
                }
            }
        }
        return isKitchenExists;
    }

    private void InsertKitchen(int iKitchenCode,String strKitchenName){
        long lRowId;

        Kitchen objKitchen = new Kitchen(strKitchenName,iKitchenCode);

        lRowId = dbKitchen.addKitchen(objKitchen);

        Log.d("Kitchen","Row Id: " + String.valueOf(lRowId));
    }

    private void ClearKitchenTable(){
        for(int i=1;i<tblKitchen.getChildCount();i++){
            View Row = tblKitchen.getChildAt(i);
            if(Row instanceof TableRow){
                ((TableRow) Row).removeAllViews();
            }
        }
    }

    private void ResetKitchen(){
        txtKitchenName.setText("");
        btnAddKitchen.setEnabled(true);
        btnEditKitchen.setEnabled(false);
    }

    public void AddKitchen(){
        String strKitchenName = txtKitchenName.getText().toString();
        int iKitchenCode;
        if(strKitchenName.equalsIgnoreCase("")){
            MsgBox.Show("Warning", "Please Enter Kitchen Name");
        }
        else{
            if(IsKitchenExists(strKitchenName)){
                MsgBox.Show("Warning", "Kitchen already present");
            } else {
                iKitchenCode = dbKitchen.getKitchenCode();
                iKitchenCode++;
                InsertKitchen(iKitchenCode,strKitchenName);
                txtKitchenName.setText("");
                ClearKitchenTable();
                DisplayKitchen();
            }
        }
    }

    public void EditCateg(){
        strKitchenName = txtKitchenName.getText().toString();
        Log.d("Kitchen Selection","Code: " + strKitchenCode + " Name: " + strKitchenName);
        int iResult = dbKitchen.updateKitchen(strKitchenCode, strKitchenName);
        Log.d("updateKitchen","Updated Rows: " + String.valueOf(iResult));
        ResetKitchen();
        if(iResult > 0){
            ClearKitchenTable();
            DisplayKitchen();
        }
        else{
            MsgBox.Show("Warning", "Update failed");
        }
		/*if(IsKitchenExists(strKitchenName)){
			MsgBox.Show("Warning", "Kitchen already present");
		} else {

		}*/
    }

    public void ClearKitchen(){
        ResetKitchen();
    }

    public void CloseKitchen(){

        dbKitchen.CloseDatabase();
        getActivity().finish();
    }
}
