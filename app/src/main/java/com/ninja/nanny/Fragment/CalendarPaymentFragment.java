package com.ninja.nanny.Fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ninja.nanny.Custom.CustomFragment;
import com.ninja.nanny.MainActivity;
import com.ninja.nanny.Model.Payment;
import com.ninja.nanny.R;
import com.ninja.nanny.Utils.Common;
import com.ninja.nanny.Utils.Constant;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class CalendarPaymentFragment extends CustomFragment implements CompoundButton.OnCheckedChangeListener {


    public CalendarPaymentFragment() {
        // Required empty public constructor
    }

    LayoutInflater mInflater;
    View mView;
    MainActivity mContext;
    TextView tvDay, tvYear;
    CaldroidFragment caldroidFragment;
    ToggleButton tbNotPaidBill, tbPaidBill, tbNotPaidSaving, tbPaidSaving;
    HashMap<Date, Drawable> hashMapBackgroundDrawble;
    HashMap<Date, Integer> hashMapTextColor;
    HashMap<String, Integer> hashMapConverter;
    final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    List<Date> listWishDate;
    List<Payment> listCurrentPayment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_calendar_payment, container, false);
        mContext = (MainActivity)getActivity();
        mInflater = inflater;

        setUI();

        return mView;
    }

    boolean isNewDate(Date date){
        int dateYear = date.getYear();
        int dateMonth = date.getMonth();
        int dateDay = date.getDate();

        for(int i = 0; i < listWishDate.size(); i ++) {
            Date dateItem = listWishDate.get(i);

            int dateItemYear = dateItem.getYear();
            int dateItemMonth = dateItem.getMonth();
            int dateItemDay = dateItem.getDate();

            if((dateYear == dateItemYear) && (dateMonth == dateItemMonth) && (dateDay == dateItemDay)) return false;
        }

        return true;
    }

    private void displayPayments() {
        hashMapBackgroundDrawble.clear();
        hashMapTextColor.clear();
        hashMapConverter.clear();

        listWishDate = new ArrayList<>();

        listCurrentPayment = Common.getInstance().getCurrentPayments();

        Calendar cal = Calendar.getInstance();
        int nSalaryDate = Common.getInstance().nSalaryDate;

        boolean isNotPaidBill = tbNotPaidBill.isChecked();
        boolean isPaidBill = tbPaidBill.isChecked();
        boolean isNotPaidSaving = tbNotPaidSaving.isChecked();
        boolean isPaidSaving = tbPaidSaving.isChecked();

        for(int i = 0; i < listCurrentPayment.size(); i ++) {
            Payment payment = listCurrentPayment.get(i);

            int nPaymentMode = payment.getPaymentMode();
            int nPaidStatus = payment.getPaidStatus();
            int nDateOfMonth = payment.getDateOfMonth();
            Drawable drawable = null;
            boolean flag = false;

            if (nPaymentMode > 1 && nPaidStatus == 0 && isNotPaidBill) {
                drawable = getResources().getDrawable(R.drawable.circle_view_blue_outside);
                flag = true;
            } else if (nPaymentMode > 1 && nPaidStatus == 1 && isPaidBill) {
                drawable = getResources().getDrawable(R.drawable.circle_view_green_outside);
                flag = true;
            } else if (nPaymentMode < 2 && nPaidStatus == 0 && isNotPaidSaving) {
                drawable = getResources().getDrawable(R.drawable.circle_view_yellow_outside);
                flag = true;
            } else if (nPaymentMode < 2 && nPaidStatus == 1 && isPaidSaving) {
                drawable = getResources().getDrawable(R.drawable.circle_view_orange_outside);
                flag = true;
            }

            if(!flag) continue;

            long timestampPayment = payment.getPaymentTimstampInCurrentPeriod();

            cal.setTimeInMillis(timestampPayment);

            Date date = cal.getTime();

            hashMapBackgroundDrawble.put(date, drawable);
            hashMapTextColor.put(date, R.color.white);
            hashMapConverter.put(formatter.format(date), i);
            listWishDate.add(date);
        }



//        for(int i = 0; i < 12; i ++) {
//            cal = Calendar.getInstance();
//            cal.set(Calendar.DAY_OF_MONTH, nSalaryDate);
//            cal.add(Calendar.MONTH, i);
//            Date date = cal.getTime();
//            Drawable drawable = getResources().getDrawable(R.drawable.circle_border_blue);
//
//            if(isNewDate(date)) {
//                hashMapBackgroundDrawble.put(date, drawable);
//                hashMapTextColor.put(date, R.color.caldroid_light_red);
//            }
//
//            if(i > 0) {
//                cal.add(Calendar.MONTH, - 2 * i);
//                date = cal.getTime();
//                drawable = getResources().getDrawable(R.drawable.circle_border_blue);
//
//                if(isNewDate(date)) {
//                    hashMapBackgroundDrawble.put(date, drawable);
//                    hashMapTextColor.put(date, R.color.caldroid_light_red);
//                }
//            }
//        }

        long timestampCurrentPeriodStart = Common.getInstance().getTimestampCurrentPeriodStart();
        long timestampCurrentPeriodEnd = Common.getInstance().getTimestampCurrentPeriodEnd();

        cal.setTimeInMillis(timestampCurrentPeriodStart);

        Date dateCurrentPeriodStart = cal.getTime();

        if(isNewDate(dateCurrentPeriodStart)) {
            Drawable drawable = getResources().getDrawable(R.drawable.circle_border_blue);
            hashMapBackgroundDrawble.put(dateCurrentPeriodStart, drawable);
            hashMapTextColor.put(dateCurrentPeriodStart, R.color.caldroid_light_red);
        }


        if (caldroidFragment != null) {
            caldroidFragment.setBackgroundDrawableForDates(hashMapBackgroundDrawble);
            caldroidFragment.setTextColorForDates(hashMapTextColor);
            caldroidFragment.refreshView();
        }

        caldroidFragment.setMinDate(dateCurrentPeriodStart);

        cal.setTimeInMillis(timestampCurrentPeriodEnd);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        caldroidFragment.setMaxDate(cal.getTime());
    }

    @Override
    public void onResume() {
        super.onResume();
        displayPayments();
    }

    void initCalendar() {
        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        caldroidFragment = new CaldroidFragment();

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();

        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

        caldroidFragment.setArguments(args);

        // Attach to the activity
        FragmentTransaction t = mContext.getSupportFragmentManager().beginTransaction();
        t.replace(R.id.lyCalendar, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                String strDate = formatter.format(date);

                if(hashMapConverter.containsKey(strDate)) {
                    int nSelected = hashMapConverter.get(formatter.format(date));
                    EditPaymentFragment f = new EditPaymentFragment();
                    String title = Constant.FRAGMENT_EDIT_PAYMENT;
                    f.paymentSelected = listCurrentPayment.get(nSelected);

                    FragmentTransaction transaction = mContext.getSupportFragmentManager()
                            .beginTransaction();
                    transaction.add(R.id.content_frame, f, title).addToBackStack(title).commit();
                }
            }

            @Override
            public void onChangeMonth(int month, int year) {
            }

            @Override
            public void onLongClickDate(Date date, View view) {

            }

            @Override
            public void onCaldroidViewCreated() {

            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

        hashMapBackgroundDrawble = new HashMap<Date, Drawable>();
        hashMapTextColor = new HashMap<Date, Integer>();
        hashMapConverter = new HashMap<String, Integer>();
    }

    void setUI() {
        mView.findViewById(R.id.btnBack).setOnClickListener(this);
        mView.findViewById(R.id.rlyCover).setOnClickListener(this);
        mView.findViewById(R.id.btnAdd).setOnClickListener(this);

        tvDay = (TextView)mView.findViewById(R.id.tvDay);
        tvYear = (TextView)mView.findViewById(R.id.tvYear);

        SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
        tvYear.setText(dfYear.format(new Date()));

        SimpleDateFormat dfDate = new SimpleDateFormat("EEE, MMM d");
        tvDay.setText(dfDate.format(new Date()));

        tbNotPaidBill = (ToggleButton)mView.findViewById(R.id.tbtnNotPaidBill);
        tbPaidBill = (ToggleButton)mView.findViewById(R.id.tbtnPaidBill);
        tbNotPaidSaving = (ToggleButton)mView.findViewById(R.id.tbtnNotPaidSaving);
        tbPaidSaving = (ToggleButton)mView.findViewById(R.id.tbtnPaidSaving);

        tbNotPaidBill.setChecked(true);
        tbPaidBill.setChecked(true);
        tbNotPaidSaving.setChecked(true);
        tbPaidSaving.setChecked(true);

        tbNotPaidBill.setOnCheckedChangeListener(this);
        tbPaidBill.setOnCheckedChangeListener(this);
        tbNotPaidSaving.setOnCheckedChangeListener(this);
        tbPaidSaving.setOnCheckedChangeListener(this);

        initCalendar();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                mContext.getSupportFragmentManager().popBackStackImmediate();
                break;
            case R.id.rlyCover:
                break;
            case R.id.btnAdd:
                NewPaymentFragment f = new NewPaymentFragment();
                String title = Constant.FRAGMENT_NEW_PAYMENT;

                FragmentTransaction transaction = mContext.getSupportFragmentManager()
                        .beginTransaction();
                transaction.add(R.id.content_frame, f, title).addToBackStack(title).commit();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        displayPayments();
    }
}
