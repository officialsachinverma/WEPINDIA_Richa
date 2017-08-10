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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.Category;
import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;

import java.util.List;


public class FragmentCategory extends Fragment {

    Context myContext;
    DatabaseHandler dbCategory;
    MessageDialog MsgBox;
    EditText txtCategName;
    WepButton btnAddCateg, btnEditCateg,btnClearCateg,btnCloseCateg;
    TableLayout tblCategory;
    Spinner spinner;
    String strCategCode = "";
    String strCategName = "";
    int iDeptCode = 0;
    private List<String> labels;


    public FragmentCategory() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            dbCategory = new DatabaseHandler(getActivity());
            dbCategory.OpenDatabase();
        }catch (Exception e){

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_category, container, false);
        myContext = getActivity();
        dbCategory.CreateDatabase();
        MsgBox = new MessageDialog(myContext);
        spinner = (Spinner) view.findViewById(R.id.spnrDeptName);
        txtCategName = (EditText)view.findViewById(R.id.etCategName);
        tblCategory = (TableLayout)view.findViewById(R.id.tblCategory);
        btnAddCateg = (WepButton) view.findViewById(R.id.btnAddCateg);
        btnAddCateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCateg();
            }
        });
        btnEditCateg = (WepButton) view.findViewById(R.id.btnEditCateg);
        btnEditCateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCateg();
            }
        });
        btnClearCateg = (WepButton) view.findViewById(R.id.btnClearCateg);
        btnClearCateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearCateg();
            }
        });
        btnCloseCateg = (WepButton) view.findViewById(R.id.btnCloseCateg);
        btnCloseCateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseCateg();
            }
        });

        ResetCateg();

        try{
            dbCategory.CloseDatabase();
            dbCategory.CreateDatabase();
            dbCategory.OpenDatabase();

            DisplayCategory();
            loadSpinnerData();
        }
        catch(Exception exp){
            Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_LONG).show();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSpinnerData();
        ClearCategoryTable();
        DisplayCategory();
    }

    @SuppressWarnings("deprecation")
    private void DisplayCategory(){
        Cursor crsrCategory;
        crsrCategory = dbCategory.getAllCategorywithDeptName();

        TableRow rowCateg = null;
        TextView tvSno, tvCategCode, tvCategName, tvDeptCode;
        ImageButton btnImgDelete;
        int i = 1;
        if(crsrCategory.moveToFirst()){
            do{
                rowCateg = new TableRow(myContext);
                rowCateg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                rowCateg.setBackgroundResource(R.drawable.row_background);

                tvSno = new TextView(myContext);
                tvSno.setTextSize(18);
                tvSno.setGravity(1);
                tvSno.setText(String.valueOf(i));
                rowCateg.addView(tvSno);

                tvCategCode = new TextView(myContext);
                tvCategCode.setTextSize(18);
                tvCategCode.setText(crsrCategory.getString(crsrCategory.getColumnIndex("CategCode")));
                rowCateg.addView(tvCategCode);

                tvCategName = new TextView(myContext);
                tvCategName.setTextSize(18);
                tvCategName.setText(crsrCategory.getString(crsrCategory.getColumnIndex("CategName")));
                rowCateg.addView(tvCategName);

                tvDeptCode = new TextView(myContext);
                tvDeptCode.setTextSize(18);
                tvDeptCode.setText(crsrCategory.getString(crsrCategory.getColumnIndex("DeptName")));
                rowCateg.addView(tvDeptCode);

                // Delete
                int res = getResources().getIdentifier("delete", "drawable", getActivity().getPackageName());
                btnImgDelete = new ImageButton(myContext);
                btnImgDelete.setImageResource(res);
                btnImgDelete.setLayoutParams(new TableRow.LayoutParams(60, 40));
                btnImgDelete.setOnClickListener(mListener);
                rowCateg.addView(btnImgDelete);

                rowCateg.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if(String.valueOf(v.getTag()) == "TAG"){
                            TableRow Row = (TableRow) v;
                            TextView CategCode = (TextView)Row.getChildAt(1);
                            TextView CategName = (TextView)Row.getChildAt(2);
                            strCategCode = CategCode.getText().toString();
                            TextView DeptName = (TextView)Row.getChildAt(3);
                            String deptName = DeptName.getText().toString();// dbCategory.getDepartmentIdById(strCategCode);
                            int id = getIndex(deptName+"");
                            spinner.setSelection(id);
                            txtCategName.setText(CategName.getText());
                            btnAddCateg.setEnabled(false);
                            btnEditCateg.setEnabled(true);
                        }
                    }
                });

                rowCateg.setTag("TAG");

                tblCategory.addView(rowCateg,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                i++;
            }while(crsrCategory.moveToNext());
        }
        else{
            Log.d("DisplayCategory","No Categ found");
        }
    }

    public int getIndex(String item)
    {
        for (int i = 0; i < labels.size(); i++)
        {
            String auction = labels.get(i);
            if (item.equals(auction))
            {
                return i;
            }
        }
        return -1;
    }

    private View.OnClickListener mListener = new View.OnClickListener() {

        public void onClick(final View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                    .setTitle("Delete")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Are you sure you want to Delete this Category. It will delete corresponding Items linkage also")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            TableRow tr = (TableRow) v.getParent();
                            TextView CategCode = (TextView) tr.getChildAt(1);
                            long lResult = dbCategory.DeleteCateg(CategCode.getText().toString());
                            lResult = dbCategory.DeleteItemByCategCode(CategCode.getText().toString());
                            //MsgBox.Show("", "Category Deleted Successfully");
                            Toast.makeText(myContext, "Category Deleted Successfully", Toast.LENGTH_SHORT).show();

                            ClearCategoryTable();
                            DisplayCategory();

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

    private boolean IsCategoryExists(String CategoryName){
        boolean isCategoryExists = false;
        String strCategory="";
        TextView Category;

        for(int i=1; i<tblCategory.getChildCount(); i++){

            TableRow Row = (TableRow)tblCategory.getChildAt(i);

            if(Row.getChildAt(0) != null){
                Category = (TextView) Row.getChildAt(2);

                strCategory = Category.getText().toString();

                Log.v("CategoryActivity", "Category:" + strCategory.toUpperCase() + " New Category:" + CategoryName.toUpperCase());

                if(strCategory.toUpperCase().equalsIgnoreCase(CategoryName.toUpperCase())){
                    isCategoryExists = true;
                    break;
                }
            }
        }
        return isCategoryExists;
    }

    private void InsertCategory(int iCategCode,String strCategName, int iDeptCode){
        long lRowId;

        Category objCateg = new Category(strCategName,iCategCode, iDeptCode);

        lRowId = dbCategory.addCategory(objCateg);

        Log.d("Category","Row Id: " + String.valueOf(lRowId));
    }

    private void ClearCategoryTable(){
        for(int i=1;i<tblCategory.getChildCount();i++){
            View Row = tblCategory.getChildAt(i);
            if(Row instanceof TableRow){
                ((TableRow) Row).removeAllViews();
            }
        }
    }

    private void ResetCateg(){
        txtCategName.setText("");
        btnAddCateg.setEnabled(true);
        btnEditCateg.setEnabled(false);
        loadSpinnerData();
    }

    public void AddCateg(){
        String strCategName = txtCategName.getText().toString();
        int iCategCode, iDeptCode;
        //iDeptCode = spinner.getSelectedItemPosition() + 1;
        iDeptCode =  dbCategory.getDepartmentIdByName(labels.get(spinner.getSelectedItemPosition()));
        if(strCategName.equalsIgnoreCase(""))
        {
            MsgBox.Show("Warning", "Please Enter Category before adding");
        }else if (spinner.getSelectedItem().toString().equalsIgnoreCase("Select"))
        {
            MsgBox.Show("Warning", "Please Select Department before adding");
        }
        else
        {
            if(IsCategoryExists(strCategName))
            {
                MsgBox.Show("Warning", "Category already present");
            }
            else
            {

                iCategCode = dbCategory.getCategCode();
                iCategCode++;
                InsertCategory(iCategCode,strCategName, iDeptCode);

                ResetCateg();
                ClearCategoryTable();
                DisplayCategory();
            }
        }
    }

    public void EditCateg(){
        strCategName = txtCategName.getText().toString();
        iDeptCode =  dbCategory.getDepartmentIdByName(labels.get(spinner.getSelectedItemPosition()));
        Log.d("Category Selection","Code: " + strCategCode + " Name: " + strCategName);

        int iResult = dbCategory.updateCategory(strCategCode, strCategName, iDeptCode);
        Log.d("updateCategory", "Updated Rows: " + String.valueOf(iResult));
        ResetCateg();
        if (iResult > 0) {
            ClearCategoryTable();
            DisplayCategory();
        } else {
            MsgBox.Show("Warning", "Update failed");
        }
		/*if (IsCategoryExists(strCategName)) {
			MsgBox.Show("Warning", "Category already present");
		} else {

		}*/
    }

    public void ClearCateg(){
        ResetCateg();
    }

    public void CloseCateg(){
        dbCategory.CloseDatabase();
        getActivity().finish();
    }

    private void loadSpinnerData() {
        labels = dbCategory.getAllDeptforCateg();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
}
