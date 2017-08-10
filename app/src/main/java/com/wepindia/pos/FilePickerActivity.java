package com.wepindia.pos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by PriyabratP on 02-02-2017.
 */

public class FilePickerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    private ListView listView;
    public final static String EXTRA_FILE_PATH = "file_path";
    //public final static String EXTRA_SHOW_HIDDEN_FILES = "show_hidden_files";
    public final static String EXTRA_ACCEPTED_FILE_EXTENSIONS = "accepted_file_extensions";
    private final static String DEFAULT_INITIAL_DIRECTORY = "/storage";
    protected File mDirectory;
    protected ArrayList<File> mFiles;
    protected FilePickerListAdapter mAdapter;
    protected boolean mShowHiddenFiles = false;
    protected String[] acceptedFileExtensions;
    private String contentType;
    public static final int FILE_PICKER_CODE = 34890;
    public static final int PICK_IMAGE_CODE = 33890;
    //    static int c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select a File ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.listView);
        contentType = getIntent().getStringExtra("contentType");
        mDirectory = new File(DEFAULT_INITIAL_DIRECTORY);
        // Initialize the ArrayList
        mFiles = new ArrayList<File>();
        // Set the ListAdapter
//        if(c==1)
//        {
//            int k=getIntent().getIntExtra("flag",0);
//            if(k==1)
//            {
//                onBackPressed();
//            }
//        }
        mAdapter = new FilePickerListAdapter(this, mFiles);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        View empty = findViewById(R.id.empty);
        listView.setEmptyView(empty);
        // Initialize the extensions array to allow any file extensions
        acceptedFileExtensions = new String[] {};
        // Get intent extras
        if(getIntent().hasExtra(EXTRA_FILE_PATH)) {
            mDirectory = new File(getIntent().getStringExtra(EXTRA_FILE_PATH));
        }
        /*if(getIntent().hasExtra(EXTRA_SHOW_HIDDEN_FILES)) {
            mShowHiddenFiles = getIntent().getBooleanExtra(EXTRA_SHOW_HIDDEN_FILES, false);
        }*/
        if(getIntent().hasExtra(EXTRA_ACCEPTED_FILE_EXTENSIONS)) {
            ArrayList<String> collection = getIntent().getStringArrayListExtra(EXTRA_ACCEPTED_FILE_EXTENSIONS);
            acceptedFileExtensions = collection.toArray(new String[collection.size()]);
        }
    }
    @Override
    protected void onResume() {
        refreshFilesList();
        super.onResume();
    }

    /**
     * Updates the list view to the current directory
     */
    protected void refreshFilesList() {
        // Clear the files ArrayList
        mFiles.clear();
//        c=1;
        // Set the extension file filter
        ExtensionFilenameFilter filter = new ExtensionFilenameFilter(acceptedFileExtensions);

        // Get the files in the directory
        File[] files = mDirectory.listFiles(filter);
        if(files != null && files.length > 0) {
            for(File f : files) {
                if(f.isHidden() && !mShowHiddenFiles) {
                    // Don't add the file
                    continue;
                }

                // Add the file the ArrayAdapter
                if(!(f.toString().equalsIgnoreCase("/storage/emulated"))){
                    if(isFileExist(f))
                        mFiles.add(f);
                }
            }

            Collections.sort(mFiles, new FileComparator());
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if(mDirectory.getParentFile() != null) {
            // Go to parent directory
            File file = mDirectory.getParentFile();
            System.out.print(mDirectory.getAbsolutePath());

            mDirectory = file;

            refreshFilesList();
            return;
        }
//        c=0;
        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            File newFile = (File)mAdapter.getItem(i);
            if(newFile.isFile())
            {
                @SuppressWarnings("deprecation")
                String strExtension = MimeTypeMap.getFileExtensionFromUrl(newFile.toURL().toString());
                //String strExtension = newFile.getName().substring(newFile.getName().lastIndexOf(".") + 1);
                Log.v("FilePickerActivity", "File Extension:" + strExtension);
                if(contentType.equalsIgnoreCase("image"))
                {
                    if(strExtension.equalsIgnoreCase("png") || strExtension.equalsIgnoreCase("jpg") || strExtension.equalsIgnoreCase("JPEG"))
                    {
                        //Toast.makeText(getApplicationContext(), "Selected file is Correct", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please Select an Image", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else
                {
                    if(strExtension.equalsIgnoreCase("csv") || strExtension.equalsIgnoreCase("CSV"))
                    {
                        //Toast.makeText(getApplicationContext(), "Selected file is Correct", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please Select a CSV file", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Intent extra = new Intent();
                //extra.putExtra(EXTRA_FILE_PATH, newFile.getAbsolutePath() + newFile.getName());
                extra.putExtra(EXTRA_FILE_PATH, newFile.getAbsolutePath());// + newFile.getName());
                setResult(RESULT_OK, extra);
                // Finish the activity
                finish();
            }
            else
            {
                mDirectory = newFile;
                refreshFilesList();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private class FilePickerListAdapter extends ArrayAdapter<File> {

        private List<File> mObjects;

        public FilePickerListAdapter(Context context, List<File> objects) {
            super(context, R.layout.file_picker_list_item, android.R.id.text1, objects);
            mObjects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = null;

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.file_picker_list_item, parent, false);
            } else {
                row = convertView;
            }

            File object = mObjects.get(position);

            ImageView imageView = (ImageView)row.findViewById(R.id.file_picker_image);
            TextView textView = (TextView)row.findViewById(R.id.file_picker_text);
            // Set single line
            textView.setSingleLine(true);
            String fileName = object.getName();
            String title = "";
            if(fileName.contains("UsbDriveA"))
            {
                title = "USB Drive";
            }
            else if(fileName.contains("sdcard0"))
            {
                title = "Internal Storage";
            }
            else if(fileName.contains("extSdCard"))
            {
                title = "SD Card";
            }
            else {
                title = fileName;
            }
            textView.setText(String.valueOf(title));
            if(object.isFile())
            {
                // Show the file icon
                imageView.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
            }
            else
            {
                // Show the folder icon
                imageView.setImageResource(R.drawable.ic_folder_black_24dp);
            }
            return row;
        }
    }

    private class FileComparator implements Comparator<File> {
        public int compare(File f1, File f2) {
            if(f1 == f2) {
                return 0;
            }
            if(f1.isDirectory() && f2.isFile()) {
                // Show directories above files
                return -1;
            }
            if(f1.isFile() && f2.isDirectory()) {
                // Show files below directories
                return 1;
            }
            // Sort the directories alphabetically
            return f1.getName().compareToIgnoreCase(f2.getName());
        }
    }

    private class ExtensionFilenameFilter implements FilenameFilter {
        private String[] mExtensions;

        public ExtensionFilenameFilter(String[] extensions) {
            super();
            mExtensions = extensions;
        }

        public boolean accept(File dir, String filename) {
            if(new File(dir, filename).isDirectory()) {
                // Accept all directory names
                return true;
            }
            if(mExtensions != null && mExtensions.length > 0) {
                for(int i = 0; i < mExtensions.length; i++) {
                    if(filename.endsWith(mExtensions[i])) {
                        // The filename ends with the extension
                        return true;
                    }
                }
                // The filename did not match any of the extensions
                return false;
            }
            // No extensions has been set. Accept all file extensions.
            return true;
        }
    }

    public boolean isFileExist(File file){
        String name = file.getAbsolutePath().toString();
        if(name.contains("UsbDriveB"))
        {
            return false;
        }
        else if(name.contains("UsbDriveC"))
        {
            return false;
        }
        else if(name.contains("UsbDriveD"))
        {
            return false;
        }
        else if(name.contains("UsbDriveE"))
        {
            return false;
        }
        else if(name.contains("UsbDriveF"))
        {
            return false;
        }
        if(file.exists())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}

