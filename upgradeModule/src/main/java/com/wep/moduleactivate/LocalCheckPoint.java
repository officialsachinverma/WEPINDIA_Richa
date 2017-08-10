package com.wep.moduleactivate;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.razorpay.Checkout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PriyabratP on 16-03-2017.
 */

public class LocalCheckPoint {

    private static final String TAG = LocalCheckPoint.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private LocalCheckPoint checkPoint;
    private Activity activity;
    private String tagName;
    private Dialog dialog;

    public LocalCheckPoint(Activity activity) {
        this.activity = activity;
        sharedPreferences =
                /*this.activity.getSharedPreferences(TAG, Context.MODE_PRIVATE)*/
                PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public boolean isActivated(String tagName) {
        return sharedPreferences.getBoolean(tagName, false);
    }

    public void setActivate(String tagName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(tagName, true);
        editor.commit();
    }

    public void setDeActivate(String tagName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(tagName, false);
        editor.commit();
    }

    public void check(String tagName) {
        this.tagName = tagName;
        if (getCheckPoint().isActivated(tagName)) {
            showAlert("Already Activated");
        } else {
            showAlertOptions(tagName);
        }
    }

    private void showAlertOptions(final String tagName) {
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.layout_alert_options);
        //dialog.setCancelable(false);
        dialog.setTitle("Add a Payment Method");

        TextView textPay = (TextView) dialog.findViewById(R.id.textPay);
        TextView textReedem = (TextView) dialog.findViewById(R.id.textReedem);

        final View viewRel = dialog.findViewById(R.id.persentRelLayout);
        final EditText textCoupon = (EditText) dialog.findViewById(R.id.editTextCoupon);
        Button btnCouponApply = (Button) dialog.findViewById(R.id.btnCouponApply);

        final View viewLin = dialog.findViewById(R.id.editBoxAll);
        final EditText editTextEmail = (EditText) dialog.findViewById(R.id.editTextEmail);
        final EditText editTextMobile = (EditText) dialog.findViewById(R.id.editTextMobile);
        Button btnPay = (Button) dialog.findViewById(R.id.btnPay);

        final TextView textViewValidator = (TextView) dialog.findViewById(R.id.textViewValidator);

        textPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewRel.setVisibility(View.GONE);
                if(viewLin.getVisibility() == View.VISIBLE)
                {
                    viewLin.setVisibility(View.GONE);
                }
                else
                {
                    viewLin.setVisibility(View.VISIBLE);
                }
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = editTextEmail.getText().toString().trim();
                final String mobile = editTextMobile.getText().toString().trim();
                if(email.equalsIgnoreCase("") || mobile.equalsIgnoreCase(""))
                {
                    textViewValidator.setText("Fill Required Fields");
                }
                else
                {
                    final ProgressDialog progressDialog = new ProgressDialog(activity);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();
                    RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
                    StringRequest request = new StringRequest(Request.Method.GET, activity.getString(R.string.url_price_api), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.getBoolean("success")) {
                                    double price = jsonObject.getDouble("price");
                                    startPayment(activity, (int) price,email,mobile);
                                }else {
                                    textViewValidator.setText("Problem Fetching Data");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }finally {

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            textViewValidator.setText(""+error.getMessage());
                            progressDialog.dismiss();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String,String> map = new HashMap<String, String>();
                            map.put("mac",Util.getMacAddress(activity));
                            map.put("tag",tagName);
                            return map;
                        }
                    };
                    queue.add(request);
                }
            }
        });

        textCoupon.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        textCoupon.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        textReedem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewLin.setVisibility(View.GONE);
                if(viewRel.getVisibility() == View.VISIBLE)
                {
                    viewRel.setVisibility(View.GONE);
                }
                else
                {
                    viewRel.setVisibility(View.VISIBLE);
                }
            }
        });

        btnCouponApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(activity);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
                RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
                StringRequest request = new StringRequest(Request.Method.GET, activity.getString(R.string.url_auth_api), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        setActivate(tagName);
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textViewValidator.setText(""+error.getMessage());
                        progressDialog.dismiss();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> map = new HashMap<String, String>();
                        map.put("mac",Util.getMacAddress(activity));
                        map.put("id",textCoupon.getText().toString().trim());
                        return map;
                    }
                };
                queue.add(request);
            }
        });
        dialog.show();
    }

    public LocalCheckPoint getCheckPoint() {
        if (checkPoint == null)
            checkPoint = new LocalCheckPoint(this.activity);
        return checkPoint;
    }

    public void showAlert(String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this.activity).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void startPayment(Activity activity,int amount,String email,String contact) {
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", activity.getString(R.string.company_name));
            options.put("description", activity.getString(R.string.payment_desc));
            options.put("image", activity.getString(R.string.brand_logo_url));
            options.put("currency", activity.getString(R.string.currency));
            options.put("amount", amount);
            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", contact);
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void setPaymentSuccess(String msg) {
        sendPaymentConfirmation("success");
    }

    public void setPaymentFailed(int status, String msg) {
        sendPaymentConfirmation("fail");
        Toast.makeText(activity, "Payment Failed", Toast.LENGTH_SHORT).show();
    }

    public void sendPaymentConfirmation(final String status){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Confirming...");
        progressDialog.show();
        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.GET, activity.getString(R.string.url_confirm_api), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("success"))
                    {
                        setActivate(tagName);
                        if(dialog!=null)
                            dialog.dismiss();
                        progressDialog.dismiss();
                    }
                    else
                    {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("mac",Util.getMacAddress(activity));
                map.put("tag",tagName);
                map.put("status",status);
                return map;
            }
        };
        queue.add(request);
    }
}