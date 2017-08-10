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
import com.wep.common.app.Database.Department;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;


public class FragmentDepartment extends Fragment {

    private static final String TAG = FragmentDepartment.class.getSimpleName();
    Context myContext;
    DatabaseHandler dbDepartment;
    MessageDialog MsgBox;
    EditText txtDeptName;
    TableLayout tblDepartment;
    String strDeptCode = "";
    String strDeptName = "";
    private WepButton btnAddDept,btnEditDept,btnClearDept,btnCloseDept;


    public FragmentDepartment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            dbDepartment = new DatabaseHandler(getActivity());
            dbDepartment.OpenDatabase();
        }catch (Exception e){

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_department, container, false);
        myContext = getActivity();
        MsgBox = new MessageDialog(myContext);
        txtDeptName = (EditText)view.findViewById(R.id.etDeptName);
        tblDepartment = (TableLayout)view.findViewById(R.id.tblDepartments);
        btnAddDept = (WepButton) view.findViewById(R.id.btnAddDept);
        btnAddDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDept();
            }
        });
        btnEditDept = (WepButton) view.findViewById(R.id.btnEditDept);
        btnEditDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDept();
            }
        });
        btnClearDept = (WepButton) view.findViewById(R.id.btnClearDept);
        btnClearDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearDept();
            }
        });
        btnCloseDept = (WepButton) view.findViewById(R.id.btnCloseDept);
        btnCloseDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseDept();
            }
        });
        ResetDept();
        try{
            dbDepartment.CloseDatabase();
            dbDepartment.CreateDatabase();
            dbDepartment.OpenDatabase();
            DisplayDepartment();
        }
        catch(Exception exp){
            Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private void DisplayDepartment(){
        Cursor crsrDepartment;
        crsrDepartment = dbDepartment.getAllDepartments();

        TableRow rowDept = null;
        TextView tvSno, tvDeptCode, tvDeptName;
        ImageButton btnImgDelete;
        int i = 1;
        if(crsrDepartment.moveToFirst()){
            do{
                rowDept = new TableRow(myContext);
                rowDept.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                rowDept.setBackgroundResource(R.drawable.row_background);

                tvSno = new TextView(myContext);
                tvSno.setTextSize(18);
                tvSno.setGravity(1);
                tvSno.setText(String.valueOf(i));
                rowDept.addView(tvSno);

                tvDeptCode = new TextView(myContext);
                tvDeptCode.setTextSize(18);
                tvDeptCode.setText(crsrDepartment.getString(0));
                rowDept.addView(tvDeptCode);

                tvDeptName = new TextView(myContext);
                tvDeptName.setTextSize(18);
                tvDeptName.setText(crsrDepartment.getString(1));
                rowDept.addView(tvDeptName);

                // Delete
                int res = getResources().getIdentifier("delete", "drawable", getActivity().getPackageName());
                btnImgDelete = new ImageButton(myContext);
                btnImgDelete.setImageResource(res);
                btnImgDelete.setLayoutParams(new TableRow.LayoutParams(60, 40));
                btnImgDelete.setOnClickListener(mListener);
                rowDept.addView(btnImgDelete);

                rowDept.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if(String.valueOf(v.getTag()) == "TAG"){
                            TableRow Row = (TableRow) v;
                            TextView DeptCode = (TextView)Row.getChildAt(1);
                            TextView DeptName = (TextView)Row.getChildAt(2);
                            strDeptCode = DeptCode.getText().toString();
                            txtDeptName.setText(DeptName.getText());
                            btnAddDept.setEnabled(false);
                            btnEditDept.setEnabled(true);
                        }
                    }
                });

                rowDept.setTag("TAG");

                tblDepartment.addView(rowDept,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                i++;
            }while(crsrDepartment.moveToNext());
        }
        else{
            Log.d("DisplayDepartment","No Dept found");
        }

    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(final View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                    .setTitle("Delete")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Are you sure you want to Delete this Department. It will delete corresponding Category's and Items also")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            TableRow tr = (TableRow) v.getParent();
                            TextView DeptCode = (TextView) tr.getChildAt(1);
                            long lResult = dbDepartment.DeleteDept(DeptCode.getText().toString());
                            lResult = dbDepartment.DeleteCategByDeptCode(DeptCode.getText().toString());
                            lResult = dbDepartment.DeleteItemByDeptCode(DeptCode.getText().toString());
                            //MsgBox.Show("", "Department Deleted Successfully");
                            Toast.makeText(myContext, "Department Deleted Successfully", Toast.LENGTH_SHORT).show();

                            ClearDepartmentTable();
                            DisplayDepartment();

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };

    private boolean IsDepartmentExists(String DepartmentName){
        boolean isDepartmentExists = false;
        String strDepartment="";
        TextView Department;

        for(int i=1; i<tblDepartment.getChildCount(); i++){

            TableRow Row = (TableRow)tblDepartment.getChildAt(i);

            if(Row.getChildAt(0) != null){
                Department = (TextView) Row.getChildAt(2);

                strDepartment = Department.getText().toString();

                Log.v(TAG, "Department:" + strDepartment.toUpperCase() + " New Department:" + DepartmentName.toUpperCase());

                if(strDepartment.toUpperCase().equalsIgnoreCase(DepartmentName.toUpperCase())){
                    isDepartmentExists = true;
                    break;
                }
            }
        }
        return isDepartmentExists;
    }

    private void InsertDepartment(int iDeptCode,String strDeptName){
        long lRowId;

        Department objDept = new Department(strDeptName,iDeptCode);

        lRowId = dbDepartment.addDepartment(objDept);

        Log.d("Department","Row Id: " + String.valueOf(lRowId));
    }

    private void ClearDepartmentTable(){

        for(int i=1;i<tblDepartment.getChildCount();i++){
            View Row = tblDepartment.getChildAt(i);
            if(Row instanceof TableRow){
                ((TableRow) Row).removeAllViews();
            }
        }
    }

    private void ResetDept(){
        txtDeptName.setText("");
        btnAddDept.setEnabled(true);
        btnEditDept.setEnabled(false);
    }

    public void AddDept(){
        String strDeptName = txtDeptName.getText().toString();
        int iDeptCode;
        if(strDeptName.equalsIgnoreCase("")){
            MsgBox.Show("Warning", "Please fill department name before adding");
        }
        else{
            if(IsDepartmentExists(strDeptName)){
                MsgBox.Show("Warning", "Department already present");
            } else {

                iDeptCode = dbDepartment.getDeptCode();
                Log.d("InsertDepartment","Dept Code: " + String.valueOf(iDeptCode));
                iDeptCode++;
                InsertDepartment(iDeptCode,strDeptName);
                txtDeptName.setText("");
                ClearDepartmentTable();
                DisplayDepartment();
            }
        }
    }

    public void EditDept(){
        strDeptName = txtDeptName.getText().toString();
        Log.d("Department Selection","Code: " + strDeptCode + " Name: " + strDeptName);
        int iResult = dbDepartment.updateDepartment(strDeptCode, strDeptName);
        Log.d("updateDept","Updated Rows: " + String.valueOf(iResult));
        ResetDept();
        if(iResult > 0){
            ClearDepartmentTable();
            DisplayDepartment();
        }
        else{
            MsgBox.Show("Warning", "Update failed");
        }
		/*if(IsDepartmentExists(strDeptName)){
			MsgBox.Show("Warning", "Department already present");
		} else {

		}*/
    }

    public void ClearDept()
    {
        ResetDept();
    }

    public void CloseDept()
    {
        dbDepartment.CloseDatabase();
        getActivity().finish();
    }

}
