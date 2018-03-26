package com.lnsel.webservicecall;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    EditText etn_username,etn_password;
    Button btn_login;
    String TAG="MainActivity";
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = new ProgressDialog(this);
        progress.setMessage("loading...");
        progress.setCanceledOnTouchOutside(false);

        etn_username=(EditText)findViewById(R.id.etn_username);
        etn_password=(EditText)findViewById(R.id.etn_password);
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=etn_username.getText().toString();
                String password=etn_password.getText().toString();
                login(username,password);

            }
        });

    }

    public void login(final String username,final String password){

        progress.show();

        String url="http://61.16.131.206/erp_srcc/api/student/login";

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        //String str_response = response;
                        System.out.println("response : "+response);

                        try {
                            JSONObject jsonObj = new JSONObject(response);

                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");

                            if(status.equals("success")){

                                JSONObject record=jsonObj.getJSONObject("record");

                                String userId = record.getString("id");
                                String userName = record.getString("username");
                                String userType = record.getString("user_type");
                                String statusInner = record.getString("status");
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();

                            }else{
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progress.dismiss();
                Toast.makeText(getApplicationContext(),"Server not Responding, Please check your Internet Connection", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("user_type", "3");

                Log.d("username", username);
                Log.d("password", password);
                Log.d("user_type", "3");

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
