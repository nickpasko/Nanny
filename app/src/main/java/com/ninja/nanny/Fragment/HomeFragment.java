package com.ninja.nanny.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ninja.nanny.Custom.CustomFragment;
import com.ninja.nanny.MainActivity;
import com.ninja.nanny.R;
import com.ninja.nanny.Utils.Common;
import com.ninja.nanny.Utils.Constant;

public class HomeFragment extends CustomFragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    LayoutInflater mInflater;
    View mView;
    MainActivity mContext;
    TextView tvLeftThisWeek, tvLeftThisMonth, tvUpcomingPayments, tvSpentForWish, tvShowDebit, tvLabelDebit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = (MainActivity)getActivity();
        mInflater = inflater;

        if(!Common.getInstance().isActiveBankExist()) {
            Toast.makeText(mContext, "You should set active bank info", Toast.LENGTH_SHORT).show();

            FragmentTransaction transaction = mContext.getSupportFragmentManager()
                    .beginTransaction();

            AddBankFragment f2 = new AddBankFragment();
            String title2 = Constant.FRAGMENT_ADD_BANK;


            transaction.add(R.id.content_frame, f2, title2).addToBackStack(title2).commit();
//            transaction.add(R.id.content_frame, f2, title2).addToBackStack(title2).commit();
        }

        setUI();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presentData();
    }

    void presentData() {
        tvLeftThisWeek.setText(Common.getInstance().leftOnThisWeek() + " $");
        tvLeftThisMonth.setText(Common.getInstance().leftOnThisMonth() + " $");
        tvUpcomingPayments.setText(Common.getInstance().upcomingPayments() + " $");
        tvSpentForWish.setText(Common.getInstance().sumOfWishesForMonth() + " $");
        tvShowDebit.setText("");
        tvLabelDebit.setVisibility(View.VISIBLE);


    }

    void setUI() {
        mView.findViewById(R.id.btnAdvice).setOnClickListener(this);
        mView.findViewById(R.id.btnMenu).setOnClickListener(this);

        tvLeftThisWeek = (TextView)mView.findViewById(R.id.tvLeftThisWeek);
        tvLeftThisMonth = (TextView)mView.findViewById(R.id.tvLeftThisMonth);
        tvUpcomingPayments = (TextView)mView.findViewById(R.id.tvUpcomingPayment);
        tvSpentForWish = (TextView)mView.findViewById(R.id.tvSpentForWish);
        tvShowDebit = (TextView)mView.findViewById(R.id.tvMoneyDebit);
        tvLabelDebit = (TextView)mView.findViewById(R.id.tvLabelDebitCard);

        mView.findViewById(R.id.rlytDebitCard).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                tvShowDebit.setText(Common.getInstance().balanceOfActiveBank() + " $");
                tvLabelDebit.setVisibility(View.INVISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvShowDebit.setText("");
                        tvLabelDebit.setVisibility(View.VISIBLE);
                    }
                }, 3000);

                return false;
            }
        });

        presentData();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnAdvice:
                AdviceFragment f = new AdviceFragment();
                String title = Constant.FRAGMENT_ADVICE;

                FragmentTransaction transaction = mContext.getSupportFragmentManager()
                        .beginTransaction();
                transaction.add(R.id.content_frame, f, title).addToBackStack(title).commit();
                break;
            case R.id.btnMenu:
                mContext.toggleMenu();
                break;
            default:
        }
    }


}