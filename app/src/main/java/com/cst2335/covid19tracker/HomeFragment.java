package com.cst2335.covid19tracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class HomeFragment extends Fragment {
private static final String STATS_Url = "https://api.covid19api.com/summary";

    //context for fragments
    Context context;
    //UI Views
    private ProgressBar progressBar;
    private TextView totalCasesTv, newCasesTv, totalDeathsTv, newDeathsTv, totalRecoveredTv, newRecoveredTv;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //init UI Views
        progressBar = view.findViewById(R.id.progressBar);
        totalCasesTv = view.findViewById(R.id.totalCasesTv);
        newCasesTv = view.findViewById(R.id.newCasesTv);
        totalDeathsTv = view.findViewById(R.id.totalDeathsTv);
        newDeathsTv = view.findViewById(R.id.newDeathsTv);
        totalRecoveredTv = view.findViewById(R.id.totalRecoveredTv);
        newRecoveredTv = view.findViewById(R.id.newRecoveredTv);


        progressBar.setVisibility(View.GONE);

        loadHomeData();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        loadHomeData();
    }


    private void loadHomeData(){
        //show progress
        progressBar.setVisibility(View.VISIBLE);

        //JSON String request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, STATS_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //response recevied, handle response
                handleResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // some error occured, hide progress, show error message
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context,""+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        //add request to queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void handleResponse(String response) {
        try{
            //since we know, our response is in JoSON object so convert it to object
            JSONObject jsonObject = new JSONObject(response);

            JSONObject globalJo = jsonObject.getJSONObject("Global");

            // get data from it
            String newConfirmed = globalJo.getString("NewConfirmed");
            String totalConfirmed = globalJo.getString("TotalConfirmed");
            String newDeaths = globalJo.getString("NewDeaths");
            String totalDeaths = globalJo.getString("TotalDeaths");
            String newRecovered = globalJo.getString("NewRecovered");
            String totalRecovered = globalJo.getString("TotalRecovered");

            //set data
            totalCasesTv.setText(totalConfirmed);
            newCasesTv.setText(newConfirmed);
            totalDeathsTv.setText(totalDeaths);
            newDeathsTv.setText(newDeaths);
            totalRecoveredTv.setText(totalRecovered);
            newRecoveredTv.setText(newRecovered);

                //hide progress
            progressBar.setVisibility(View.GONE);
        }
        catch (Exception e){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}