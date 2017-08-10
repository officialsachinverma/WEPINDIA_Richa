package com.wepindia.printers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.Bitmap.CreateBitmap;
import com.Bitmap.FormatAndroid;
import com.bt.BluetoothChatService;
import com.bt.Connectivity;
import com.bt.ReadFromBt;
import com.bt.SendToWrite;
import com.wep.common.app.print.Payment;
import com.wep.common.app.print.PrintKotBillItem;
import com.wepindia.printers.sohamsa.DeviceListActivity;
import com.wepindia.printers.sohamsa.PrinterSohamsaActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PriyabratP on 27-12-2016.
 */

public abstract class WepPrinterBaseActivity extends HeyDeyBaseActivity {

}
