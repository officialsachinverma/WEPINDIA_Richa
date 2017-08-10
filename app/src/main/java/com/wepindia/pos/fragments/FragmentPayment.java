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
import com.wep.common.app.Database.Description;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;

public class FragmentPayment extends Fragment {

    Context myContext;
    DatabaseHandler dbDescription ;
    MessageDialog MsgBox;
    EditText txtDescriptionText;
    TableLayout tblDescription;
    String strDescriptionId = "";
    String strDescriptionText = "";
    private WepButton btnAddDescription,btnEditDescription,btnClearDescription,btnCloseDescription;


    public FragmentPayment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            dbDescription = new DatabaseHandler(getActivity());
            dbDescription.OpenDatabase();
        }catch (Exception e){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_payment, container, false);
        myContext = getActivity();
        MsgBox = new MessageDialog(myContext);
        txtDescriptionText = (EditText)view.findViewById(R.id.etDescriptionText);
        tblDescription = (TableLayout)view.findViewById(R.id.tblDescription);
        btnAddDescription = (WepButton) view.findViewById(R.id.btnAddDescription);
        btnAddDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDescription();
            }
        });
        btnEditDescription = (WepButton) view.findViewById(R.id.btnEditDescription);
        btnEditDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDescription();
            }
        });
        btnClearDescription = (WepButton) view.findViewById(R.id.btnClearDescription);
        btnClearDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearDescription();
            }
        });
        btnCloseDescription = (WepButton) view.findViewById(R.id.btnCloseDescription);
        btnCloseDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseDescription();
            }
        });
        ResetDescription();

        try{
            dbDescription.CloseDatabase();
            dbDescription.CreateDatabase();
            dbDescription.OpenDatabase();
            DisplayDescription();
        }
        catch(Exception exp){
            Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private void DisplayDescription(){
        Cursor crsrDescription;
        crsrDescription = dbDescription.getAllDescription();

        TableRow rowDescription = null;
        TextView tvSno, tvDescriptionId, tvDescriptionText;
        ImageButton btnImgDelete;
        int i = 1;
        if(crsrDescription.moveToFirst()){
            do{
                rowDescription = new TableRow(myContext);
                rowDescription.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                rowDescription.setBackgroundResource(R.drawable.row_background);

                tvSno = new TextView(myContext);
                tvSno.setTextSize(18);
                tvSno.setText(String.valueOf(i));
                tvSno.setGravity(1);
                rowDescription.addView(tvSno);

                tvDescriptionId = new TextView(myContext);
                tvDescriptionId.setTextSize(18);
                tvDescriptionId.setText(crsrDescription.getString(0));
                rowDescription.addView(tvDescriptionId);

                tvDescriptionText = new TextView(myContext);
                tvDescriptionText.setTextSize(18);
                tvDescriptionText.setText(crsrDescription.getString(1));
                rowDescription.addView(tvDescriptionText);

                // Delete
                int res = getResources().getIdentifier("delete", "drawable", getActivity().getPackageName());
                btnImgDelete = new ImageButton(myContext);
                btnImgDelete.setImageResource(res);
                btnImgDelete.setLayoutParams(new TableRow.LayoutParams(60, 40));
                btnImgDelete.setOnClickListener(mListener);
                rowDescription.addView(btnImgDelete);

                rowDescription.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if(String.valueOf(v.getTag()) == "TAG"){
                            TableRow Row = (TableRow) v;
                            TextView DescriptionId = (TextView)Row.getChildAt(1);
                            TextView DescriptionText = (TextView)Row.getChildAt(2);
                            strDescriptionId = DescriptionId.getText().toString();
                            txtDescriptionText.setText(DescriptionText.getText());
                            btnAddDescription.setEnabled(false);
                            btnEditDescription.setEnabled(true);
                        }
                    }
                });

                rowDescription.setTag("TAG");

                tblDescription.addView(rowDescription,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                i++;
            }while(crsrDescription.moveToNext());
        }
        else{
            Log.d("DisplayDescription","No Description found");
        }

    }

    private View.OnClickListener mListener = new View.OnClickListener() {

        public void onClick(final View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                    .setTitle("Delete")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Are you sure you want to Delete this Payment Description")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            TableRow tr = (TableRow) v.getParent();
                            TextView DescriptionId = (TextView) tr.getChildAt(1);
                            long lResult = dbDescription.DeleteDescription(DescriptionId.getText().toString());
                            //MsgBox.Show("", "Payment Description Deleted Successfully");
                            Toast.makeText(myContext, "Payment Description Deleted Successfully", Toast.LENGTH_SHORT).show();

                            ClearDescriptionTable();
                            DisplayDescription();

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

    private boolean IsDescriptionExists(String DescriptionName){
        boolean isDescriptionExists = false;
        String strDescription="";
        TextView Description;

        for(int i=1; i<tblDescription.getChildCount(); i++){

            TableRow Row = (TableRow)tblDescription.getChildAt(i);

            if(Row.getChildAt(0) != null){
                Description = (TextView) Row.getChildAt(2);

                strDescription = Description.getText().toString();

                Log.v("DescriptionActivity", "Department:" + strDescription.toUpperCase() + " New Department:" + DescriptionName.toUpperCase());

                if(strDescription.toUpperCase().equalsIgnoreCase(DescriptionName.toUpperCase())){
                    isDescriptionExists = true;
                    break;
                }
            }
        }
        return isDescriptionExists;
    }

    private void InsertDescription(int iDescriptionId,String strDescriptionText){
        long lRowId;

        Description objDescription = new Description(strDescriptionText,iDescriptionId);

        lRowId = dbDescription.addDescription(objDescription);

        Log.d("Description","Row Id: " + String.valueOf(lRowId));
    }

    private void ClearDescriptionTable(){
        for(int i=1;i<tblDescription.getChildCount();i++){
            View Row = tblDescription.getChildAt(i);
            if(Row instanceof TableRow){
                ((TableRow) Row).removeAllViews();
            }
        }
    }

    private void ResetDescription(){
        txtDescriptionText.setText("");
        btnAddDescription.setEnabled(true);
        btnEditDescription.setEnabled(false);
    }

    public void AddDescription(){
        String strDescriptionText = txtDescriptionText.getText().toString();
        int iDescriptionId;
        if(strDescriptionText.equalsIgnoreCase("")){
            MsgBox.Show("Warning", "Please enter description text");
        }
        else{
            if(IsDescriptionExists(strDescriptionText)){
                MsgBox.Show("Warning", "Description already present");
            } else {

                iDescriptionId = dbDescription.getDescriptionId();
                iDescriptionId++;
                InsertDescription(iDescriptionId,strDescriptionText);
                txtDescriptionText.setText("");
                ClearDescriptionTable();
                DisplayDescription();
            }
        }
    }

    public void EditDescription(){
        strDescriptionText = txtDescriptionText.getText().toString();
        Log.d("Category Selection","Code: " + strDescriptionId + " Name: " + strDescriptionText);
        int iResult = dbDescription.updateDescription(strDescriptionId, strDescriptionText);
        Log.d("updateCategory","Updated Rows: " + String.valueOf(iResult));
        ResetDescription();
        if(iResult > 0){
            ClearDescriptionTable();
            DisplayDescription();
        }
        else{
            MsgBox.Show("Warning", "Update failed");
        }
		/*if(IsDescriptionExists(strDescriptionText)){
			MsgBox.Show("Warning", "Department already present");
		} else {

		}*/
    }

    public void ClearDescription(){
        ResetDescription();
    }

    public void CloseDescription(){

        dbDescription.CloseDatabase();
        getActivity().finish();
    }

}
