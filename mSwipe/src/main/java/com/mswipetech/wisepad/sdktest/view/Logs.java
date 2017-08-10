package com.mswipetech.wisepad.sdktest.view;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by satish on 9/18/13.
 */
public class Logs
{
    public static final boolean IS_DEBUGGING_ON = true;
    public static final String log_filename = "wsv6.txt";
    public static final String log_foldername = "mswipe";
    public static final boolean fileWriteEnabled = false;
    public static final boolean adbWriteEnabled = true;
    public static Logs instance = new Logs();
    private boolean fileWriteCreationFailed = false;
    private boolean disableLogsTemporarly = false;
    
    //BufferedWriter bufWriter = null;
    File file = null;

    public void setDisableLogsTemporarly(boolean isableLogsTemporarly)
    {
        disableLogsTemporarly = isableLogsTemporarly;
    }

    public static void v (final String LOG_TAG, final String msg, boolean writeToFile, boolean writeToADB)
    {
        if (IS_DEBUGGING_ON)
        {
            //final String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            //final String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            //final String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            //final int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
            //Log.v(ApplicationConstants.LOG_TAG, "#" + lineNumber + " " + className + "." + methodName + "() : " + msg);

            if(instance.disableLogsTemporarly)
                return;

            if(fileWriteEnabled && writeToFile)
            {
               instance.writeToFile( LOG_TAG,  msg);
            }

            if(adbWriteEnabled  && writeToADB )
            {
                Log.v(LOG_TAG, msg);
            }
        }
    }

    private void writeToFile(String LOG_TAG, final String msg)
    {
        if(!fileWriteCreationFailed)
        {
                try{
   
                    File fileWriter = getFileWriter();
                    String terminalTime = new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                    BufferedWriter buf = new BufferedWriter(new FileWriter(fileWriter, true));
                    
                    buf.append(terminalTime + " : " + msg + "\n");
                    buf.close();
                }catch(Exception ex){
   
                	ex.printStackTrace();
                }
        }

    }

    private File getFileWriter() throws IOException
    {
        if(file == null) {
            file = createFileWriter();
        }
        return file;
    }

    private File createFileWriter()
    {
    	File filelog = null;

        try
        {
            String root = Environment.getExternalStorageDirectory().toString();

            File myDir = new File(root + "/" + log_foldername);
            myDir.mkdirs();
            
            filelog = new File (myDir, log_filename);
            //if (logfile.exists ()) logfile.delete ();
            filelog.createNewFile();
        } catch(Exception err) {
            fileWriteCreationFailed = true;
            err.printStackTrace();
            // currently return a "dummy" writer so we won't fail on device
            //return new OutputStreamWriter(new ByteArrayOutputStream());
        }
        return filelog;

    }

}
