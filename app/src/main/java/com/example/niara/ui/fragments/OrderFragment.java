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
public class OrderFragment extends Fragment {
    ArrayList<JSONObject> orderlistdisplay;
    ArrayList<OrderInfo> orderInfolist;

    SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView rcorders;
    private int i,j;
    private int userid=7;

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
        swipeRefreshLayout=view.findViewById(R.id.swiperefreshOrderInfo);



        rcorders = view.findViewById(R.id.rc_order);
        rcorders.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.VERTICAL,false));

        loadOrders();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadOrders();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;


    }

    private void loadOrders() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting your Order details");
        progressDialog.show();

//        SessionManager sessionManager=new SessionManager(getContext());
//        userid=sessionManager.getUserid();

        orderInfolist=new ArrayList<>();
        ApiInterface apiInterface1 = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<OrderInfo>> orderinforesponse = apiInterface1.getOrderinfo();
        orderinforesponse.enqueue(new Callback<ArrayList<OrderInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<OrderInfo>> call, Response<ArrayList<OrderInfo>> response) {
                if (response.isSuccessful()){


                    for (i=0;i<response.body().size();i++){
                        Log.d("iddeb", String.valueOf(userid));
                        Log.d("orerinfo", String.valueOf(orderInfolist));
                        Log.d("orderinfolistswag", String.valueOf((response.body().get(i).getUser())));
                        if (String.valueOf(userid)==String.valueOf((response.body()).get(i).getUser())){
                            Log.d("some", String.valueOf(response.body().get(i).getProduct()));
                            orderInfolist.add(response.body().get(i));
                            Log.d("orderlistid", String.valueOf(orderInfolist.get(i).getProduct()));
//                            idp=orderInfolist.get(i).getProduct();
                        }

                    }
                    Log.d("orderlistid", String.valueOf(orderInfolist));
                    orderlistdisplay=new ArrayList<>();
                    for (j=0;j<orderInfolist.size();j++){
                        JSONObject ob=new JSONObject();
                        try {
                            ob.put("status",orderInfolist.get(j).getStatus());
                            ob.put("quantity",orderInfolist.get(j).getQuantity());
                            ob.put("ordered_date",orderInfolist.get(j).getOrdered_date());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        int idp=orderInfolist.get(j).getProduct();
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
                                    if (orderlistdisplay.size()==orderInfolist.size()){
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

            @Override
            public void onFailure(Call<ArrayList<OrderInfo>> call, Throwable t) {

            }
        });
    }

    private void displayOrderListInRecyclerView(ArrayList<JSONObject> orderlistdisplay) {
//        Log.d("orderlistdisplayswag", String.valueOf(orderlistdisplay));
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
    public class NameComparator implements Comparator<JSONObject> {

        @Override
        public int compare(JSONObject o1, JSONObject o2) {
            try {
                return o1.getString("ordered_date").compareTo(o2.getString("ordered_date"));
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;
            }

        }
        ;

    }


}