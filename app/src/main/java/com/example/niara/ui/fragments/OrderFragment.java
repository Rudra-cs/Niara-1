package com.example.niara.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niara.Adapters.CategoryAdapter;
import com.example.niara.Adapters.FoodAdapter;
import com.example.niara.Adapters.OrderFoodInfoAdapter;
import com.example.niara.Api.ApiClient;
import com.example.niara.Api.ApiInterface;
import com.example.niara.Model.Food;
import com.example.niara.Model.OrderInfo;
import com.example.niara.Model.OrderInfoDisplay;
import com.example.niara.R;
import com.example.niara.ui.activities.CustomerFeedback;
import com.example.niara.ui.activities.LoginActivity;
import com.example.niara.ui.activities.MainActivity;
import com.example.niara.ui.activities.SearchActivity;
import com.example.niara.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment implements OrderFoodInfoAdapter.OrderClickListener {
    private ArrayList<JSONObject> orderlistdisplay;
    private ArrayList<JSONObject> infosofOrder;
    private ArrayList<OrderInfo> orderInfolist;

    private RecyclerView rcorders;
    private int i,j;
    private int userid;
    private LinearLayout noorder;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_order, container, false);
        noorder=view.findViewById(R.id.nothingaddedinorder);
        rcorders = view.findViewById(R.id.rc_order);
        rcorders.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.VERTICAL,false));
        loadOrders();
        return view;

    }

    private void loadOrders() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting your Order details");
        progressDialog.show();

        SessionManager sessionManager=new SessionManager(getContext());
        userid=sessionManager.getUserid();

        orderInfolist=new ArrayList<>();
        infosofOrder=new ArrayList<>();
        ApiInterface apiInterface1 = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<OrderInfo>> orderinforesponse = apiInterface1.getOrderinfo();
        orderinforesponse.enqueue(new Callback<ArrayList<OrderInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<OrderInfo>> call, Response<ArrayList<OrderInfo>> response) {
                if (response.isSuccessful() && response.body()!=null){

                    for (i=0;i<response.body().size();i++){
                        JSONObject ob=new JSONObject();
                        if (String.valueOf(userid)==String.valueOf((response.body()).get(i).getUser()) && response.body().get(i).getStatus()!="Delivered"){

                            try {
                                ob.put("status",response.body().get(i).getStatus());
                                ob.put("quantity",response.body().get(i).getQuantity());
                                ob.put("ordered_date",response.body().get(i).getOrdered_date());
                                ob.put("product",response.body().get(i).getProduct());
                                infosofOrder.add(ob);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    if (infosofOrder.isEmpty()){

                        progressDialog.hide();
                        noorder.setVisibility(View.VISIBLE);

                    }
                    else{
                        Log.d("orderlistid", String.valueOf(infosofOrder));
                        orderlistdisplay=new ArrayList<>();
                        for (j=0;j<infosofOrder.size();j++){
                            JSONObject ob=new JSONObject();
                            try {
                                ob.put("status",infosofOrder.get(j).get("status"));
                                ob.put("quantity",infosofOrder.get(j).get("quantity"));
                                ob.put("ordered_date",infosofOrder.get(j).get("ordered_date"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            int idp= 0;
                            try {
                                idp = (int) infosofOrder.get(j).get("product");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                            Call<Food> foodCall=apiInterface.getProductList(idp);
                            foodCall.enqueue(new Callback<Food>() {
                                @Override
                                public void onResponse(Call<Food> call, Response<Food> response1) {
                                    if (response1.isSuccessful()){
                                        progressDialog.hide();
                                        try {
                                            ob.put("title",response1.body().getTitle());
                                            ob.put("prod_img",response1.body().getProduct_image());
                                            orderlistdisplay.add(ob);
                                            Log.d("listoforder", String.valueOf(ob));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if (orderlistdisplay.size()==infosofOrder.size()){
                                            Log.d("listdisplay", String.valueOf(orderlistdisplay));
                                            displayOrderListInRecyclerView(orderlistdisplay);
                                        }
                                    }else {
                                        Log.d("responseoffood", "failed");
                                    }
                                }
                                @Override
                                public void onFailure(Call<Food> call, Throwable t) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<OrderInfo>> call, Throwable t) {

            }
        });
    }

    private void displayOrderListInRecyclerView(ArrayList<JSONObject> orderlistdisplay) {
        List list = new ArrayList(orderlistdisplay);
        if (orderlistdisplay!=null){
            Collections.sort(list,new NameComparator());
            Log.d("sortedlist", String.valueOf(list));
            Collections.reverse(list);
            Log.d("reverse", String.valueOf(list));
            OrderFoodInfoAdapter orderFoodInfoAdapter = new OrderFoodInfoAdapter(getContext(),list);
            rcorders.setAdapter(orderFoodInfoAdapter);
        }



    }

    @Override
    public void onNeedClickListener(int position) {
        startActivity(new Intent(getContext(), CustomerFeedback.class));
    }

    private class NameComparator implements Comparator<JSONObject> {
        @Override
        public int compare(JSONObject o1, JSONObject o2) {
            try {
                return o1.getString("ordered_date").compareTo(o2.getString("ordered_date"));
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;
            }

        }


    }

}