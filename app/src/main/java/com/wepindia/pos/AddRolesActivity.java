package com.wepindia.pos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.WepBaseActivity;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.adapters.UserRolesAdapter;
import com.wepindia.pos.utils.ActionBarUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


public class AddRolesActivity extends WepBaseActivity implements View.OnClickListener {

    // Context object
    Context myContext;

    private EditText editTextAddUser;
    private com.wep.common.app.views.WepButton btnAddRole;
    private ArrayList<String> rolesList;
    private GridView gridViewRoles;
    //ImageView imageViewBack, imageViewHome;
    private UserRolesAdapter rolesAdapter;
    private ArrayAdapter<String> adapterAccess;
    private DatabaseHandler dbHelper ;
    private ArrayList<String> allRoles;
    Cursor crsrRole;
    MessageDialog MsgBox;
    String strUserName = "";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_role);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*TextView tvTitleText = (TextView) findViewById(R.id.tvTitleBarCaption);
        TextView tvTitleUserName = (TextView) findViewById(R.id.tvTitleBarUserName);
        TextView tvTitleDate = (TextView) findViewById(R.id.tvTitleBarDate);
        ActionBarUtils.goBack(this, findViewById(R.id.imgTitleBackIcon));
        ActionBarUtils.goHome(this, findViewById(R.id.imgTitleHomeIcon));
        ActionBarUtils.takeScreenshot(this, findViewById(R.id.imgTitleScreenshotIcon), findViewById(R.id.relativeParent));
        tvTitleText.setText("Add Role");*/
        myContext = this;
        MsgBox = new MessageDialog(myContext);
        strUserName = getIntent().getStringExtra("USER_NAME");

        //tvTitleUserName.setText(strUserName.toUpperCase());
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        com.wep.common.app.ActionBarUtils.setupToolbar(AddRolesActivity.this,toolbar,getSupportActionBar(),"Add Role",strUserName," Date:"+s.toString());
        //tvTitleDate.setText("Date : " + s);

        dbHelper = new DatabaseHandler(AddRolesActivity.this);
        editTextAddUser = (EditText) findViewById(R.id.editTextAddUser);
        btnAddRole = (com.wep.common.app.views.WepButton) findViewById(R.id.btnAddRole);
        btnAddRole.setOnClickListener(this);
        //imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        //imageViewHome = (ImageView) findViewById(R.id.imageViewHome);
        //imageViewHome.setOnClickListener(this);
        //imageViewBack.setOnClickListener(this);

        rolesList = new ArrayList<String>();
        gridViewRoles = (GridView) findViewById(R.id.gridViewRoles);
        gridViewRoles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ArrayList<String> list = rolesAdapter.getItems();
                String roleName = list.get(position);
                //showAccessDialog();
                showDialog(roleName, position);
                //showDialog(roleName);
            }
        });

        try {
            dbHelper.CreateDatabase();
            dbHelper.OpenDatabase();

            //crsrRole = dbHelper.getAllRoles();

            updateGrid();
        } catch (Exception ex) {
            MsgBox.Show("Error", ex.getMessage());
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnAddRole) {
            String txt = editTextAddUser.getText().toString().trim();
            if (txt.equalsIgnoreCase("")) {
                Toast.makeText(AddRolesActivity.this, "Please enter a role", Toast.LENGTH_SHORT).show();
            } else {
                boolean b1 = isContains(txt);
                // Add the role to DB and Update gridView
                if (b1) {
                    Toast.makeText(AddRolesActivity.this, "Role already exist", Toast.LENGTH_SHORT).show();
                } else {
                    long status = dbHelper.addRole(txt);
                    if (status > 0) {
                        editTextAddUser.setText("");
                        updateGrid();
                    }
                }
            }
        }
    }

    public void updateGrid() {

        ArrayList<String> list = dbHelper.getAllRoles();
        if (rolesAdapter != null) {
            rolesAdapter.notifyNewDataAdded(list);
        } else {
            rolesAdapter = new UserRolesAdapter(AddRolesActivity.this, list);
            gridViewRoles.setAdapter(rolesAdapter);
        }
    }

    private void showDialog(final String roleName, final int roleId) {
        final Dialog dialog = new Dialog(AddRolesActivity.this);
        dialog.setContentView(R.layout.custom_access_dialog);
        dialog.setTitle("Choose Access");
        dialog.setCancelable(false);
        dialog.show();
        final ArrayList<String> listsAccess = getAllAccesses();
        final GridView gridViewAccesses = (GridView) dialog.findViewById(R.id.gridViewAccesses);
        adapterAccess = new ArrayAdapter<String>(AddRolesActivity.this, android.R.layout.simple_list_item_multiple_choice, listsAccess);
        gridViewAccesses.setAdapter(adapterAccess);
        gridViewAccesses.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        //gridViewAccesses.setItemChecked(0, true);
        ArrayList<Integer> list = dbHelper.getPermissionsForRole(roleName);
        Iterator<Integer> it = list.iterator();
        int count =0;
        while (it.hasNext()) {
            count++;
            gridViewAccesses.setItemChecked(it.next(), true);
        }
        if(count ==0)
            gridViewAccesses.setItemChecked(0, true);
        Button btnCancelAccess = (Button) dialog.findViewById(R.id.btnCancelAccess);
        btnCancelAccess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        Button btnGrantAccess = (Button) dialog.findViewById(R.id.btnGrantAccess);
        btnGrantAccess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                SparseBooleanArray checkedItems = gridViewAccesses.getCheckedItemPositions();
                int l = dbHelper.deleteAccessesForRole(roleName);
                dbHelper.addAccessesForRole(roleName, listsAccess, checkedItems);
                dialog.dismiss();
            }
        });
        Button btnDeleteRole = (Button) dialog.findViewById(R.id.btnDeleteRole);
        btnDeleteRole.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //dbHelper.deleteRole(roleName);
                dialog.dismiss();
                //updateGrid();
                askForDelete(roleName);
            }
        });
    }

    /*private void showDialog(final String roleName) {
        final Dialog dialog = new Dialog(AddRolesActivity.this);
        dialog.setContentView(R.layout.custom_access_dialog);
        dialog.setTitle("Choose Access");
        dialog.setCancelable(false);
        dialog.show();
        final ArrayList<String> listsAccess = getAllAccesses();
        final GridView gridViewAccesses = (GridView) dialog.findViewById(R.id.gridViewAccesses);
        adapterAccess = new ArrayAdapter<String>(AddRolesActivity.this, android.R.layout.simple_list_item_multiple_choice, listsAccess);
        gridViewAccesses.setAdapter(adapterAccess);
        gridViewAccesses.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        gridViewAccesses.setItemChecked(0, true);
        ArrayList<Integer> list = dbHelper.getPermissionsForRole(roleName);
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            gridViewAccesses.setItemChecked(it.next(), true);
        }
        Button btnCancelAccess = (Button) dialog.findViewById(R.id.btnCancelAccess);
        btnCancelAccess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        Button btnGrantAccess = (Button) dialog.findViewById(R.id.btnGrantAccess);
        btnGrantAccess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                SparseBooleanArray checkedItems = gridViewAccesses.getCheckedItemPositions();
                dbHelper.addAccessesForRole(roleName, listsAccess, checkedItems);
                dialog.dismiss();
            }
        });
    }*/

    public ArrayList<String> getAllAccesses() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(0, "Operator");
        list.add(1, "Masters");
        list.add(2, "Payment & Receipt");
        list.add(3, "Reports");
        list.add(4, "Purchase Order");
        return list;
    }

    public boolean isContains(String str) {
        boolean status = false;
        ArrayList<String> list = dbHelper.getAllRoles();
        for (String s : list) {
            if (str.equalsIgnoreCase(s)) {
                status = true;
                break;
            }
        }
        return status;
    }

    public ArrayList<String> getAllRoles(Cursor crsrRole) {
        ArrayList<String> list = new ArrayList<String>();
        list = new ArrayList<String>();
//        list.add(0, "Manager");
//        list.add(1, "Head Cook");
//        list.add(2, "Waiter");

//		String SELECT_QUERY = "SELECT * FROM " + TBL_USERSROLE;
//		Cursor cursor = dbFNB.rawQuery(SELECT_QUERY, null);
        if (crsrRole != null) {
            //Log.d(TAG,"fetched "+cursor.getCount()+" Items");
            while (crsrRole.moveToNext()){
                String role = crsrRole.getString(crsrRole.getColumnIndex("RoleName"));
                list.add(role);
            }
        }
        return list;
    }

    public void askForDelete(final String roleName)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure want to delete?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                dbHelper.deleteRole(roleName);
                updateGrid();
                dialog.dismiss();

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            //@Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);
            AuthorizationDialog
                    .setTitle("Are you sure you want to exit ?")
                    .setIcon(R.drawable.ic_launcher)
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /*Intent returnIntent =new Intent();
                            setResult(Activity.RESULT_OK,returnIntent);*/
                            dbHelper.CloseDatabase();
                            finish();
                        }
                    })
                    .show();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onHomePressed() {
        ActionBarUtils.navigateHome(this);
    }
}

