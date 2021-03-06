package com.ninja.nanny.Fragment;


import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ninja.nanny.Custom.CustomFragment;
import com.ninja.nanny.MainActivity;
import com.ninja.nanny.Model.UsedAmount;
import com.ninja.nanny.Preference.UserPreference;
import com.ninja.nanny.R;
import com.ninja.nanny.Utils.Common;
import com.ninja.nanny.Utils.Constant;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;


public class SettingFragment extends CustomFragment implements DiscreteSeekBar.OnProgressChangeListener {


    public SettingFragment() {
        // Required empty public constructor
    }

    LayoutInflater mInflater;
    View mView;
    MainActivity mContext;
    TextView tvToleranceDays, tvTolerancePercent;
    EditText etMinimalAmountPerDay, etSalaryDate, etMonthlyIncome, etUsedSalary;
    DiscreteSeekBar seekBarDays, seekBarPercent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_setting, container, false);
        mContext = (MainActivity)getActivity();
        mInflater = inflater;

        setUI();

        return mView;
    }

    void setUI() {

        mView.findViewById(R.id.btnMenu).setOnClickListener(this);
        mView.findViewById(R.id.btnSave).setOnClickListener(this);

        etMinimalAmountPerDay = (EditText)mView.findViewById(R.id.etMinimalAmountPerDay);
        etSalaryDate = (EditText)mView.findViewById(R.id.etSalaryDate);
        etMonthlyIncome = (EditText)mView.findViewById(R.id.etMonthlyIncome);
        etUsedSalary = (EditText)mView.findViewById(R.id.etUsedSalary);

        seekBarDays = (DiscreteSeekBar)mView.findViewById(R.id.seekbarDays);
        seekBarPercent = (DiscreteSeekBar)mView.findViewById(R.id.seekbarPercent);

        seekBarDays.setOnProgressChangeListener(this);
        seekBarPercent.setOnProgressChangeListener(this);

        tvToleranceDays = (TextView)mView.findViewById(R.id.tvToleranceDays);
        tvTolerancePercent = (TextView)mView.findViewById(R.id.tvTolerancePercent);

        if(Common.getInstance().timestampInitConfig > 0) {
            long timestampCurrentPeriodEnd = Common.getInstance().getTimestampCurrentPeriodEnd();
            UsedAmount usedAmount = Common.getInstance().dbHelper.getUsedAmount(timestampCurrentPeriodEnd);

            etMinimalAmountPerDay.setText(Common.getInstance().nMinimalDayAmount + "");
            etSalaryDate.setText(Common.getInstance().nSalaryDate + "");
            etMonthlyIncome.setText(Common.getInstance().nMonthlyIncome + "");
            seekBarDays.setProgress(Common.getInstance().nToleranceDays);
            seekBarPercent.setProgress(Common.getInstance().nTolerancePercents);
            tvToleranceDays.setText(Common.getInstance().nToleranceDays + " DAYS");
            tvTolerancePercent.setText(Common.getInstance().nTolerancePercents + " %");
            etUsedSalary.setText(usedAmount.getUsedAmount() + "");


        }

        etMinimalAmountPerDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    String strContent = etMinimalAmountPerDay.getText().toString();

                    if(strContent.length() == 0) {
                        etMinimalAmountPerDay.setText("0");
                    }
                }
            }
        });

        etSalaryDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    String strContent = etSalaryDate.getText().toString();

                    if(strContent.length() == 0) {
                        etSalaryDate.setText("15");
                    }
                }
            }
        });

        etUsedSalary.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    String strContent = etUsedSalary.getText().toString();

                    if(strContent.length() == 0) {
                        etUsedSalary.setText("0");
                    }
                }
            }
        });

        etUsedSalary.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);

                    // NOTE: In the author's example, he uses an identifier
                    // called searchBar. If setting this code on your EditText
                    // then use v.getWindowToken() as a reference to your
                    // EditText is passed into this callback as a TextView

                    in.hideSoftInputFromWindow(etUsedSalary
                                    .getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    etUsedSalary.clearFocus();
                    // Must return true here to consume event
                    return true;

                }
                return false;
            }
        });

        mView.findViewById(R.id.lyContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etMinimalAmountPerDay.getWindowToken(), 0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etMinimalAmountPerDay.getWindowToken(), 0);

        switch (v.getId()) {
            case R.id.btnMenu:
                if(Common.getInstance().timestampInitConfig == 0) {
                    Toast.makeText(mContext, "Please update the initial setting before", Toast.LENGTH_SHORT).show();
                    break;
                }
                mContext.toggleMenu();
                break;

            case R.id.btnSave:
                saveSettingInfo();
                break;
        }
    }

    void saveSettingInfo() {
        String strMinimalAmountPerDay = etMinimalAmountPerDay.getText().toString();
        String strSalaryDate = etSalaryDate.getText().toString();
        String strMonthlyIncome = etMonthlyIncome.getText().toString();
        String strUsedSalary = etUsedSalary.getText().toString();

        if(strMinimalAmountPerDay.length() < 1) {
            etMinimalAmountPerDay.setError(Html.fromHtml("<font color='red'>please input the minial amount per day</font>"));
            return;
        }

        if(strSalaryDate.length() < 1) {
            etSalaryDate.setError(Html.fromHtml("<font color='red'>please input the salary date</font>"));
            return;
        }

        if(strMonthlyIncome.length() < 1) {
            etMonthlyIncome.setError(Html.fromHtml("<font color='red'>please input the monthly income</font>"));
            return;
        }

        if(strUsedSalary.length() < 1) {
            etUsedSalary.setError(Html.fromHtml("<font color='red'>please input the used salary</font>"));
            return;
        }

        int nMinimalAmountPerDay = Integer.valueOf(strMinimalAmountPerDay);
        int nSalaryDate = Integer.valueOf(strSalaryDate);
        int nMonthlyIncome = Integer.valueOf(strMonthlyIncome);
        int nUsedSalary = Integer.valueOf(strUsedSalary);
        int nToleranceDays = seekBarDays.getProgress();
        int nTolerancePercent = seekBarPercent.getProgress();

        if(nSalaryDate == 0 || nSalaryDate > 31) {
            etSalaryDate.setError(Html.fromHtml("<font color='red'>please input the valid value between 1 and 31 for salary date</font>"));
            return;
        }

        if(nMonthlyIncome == 0) {
            etMonthlyIncome.setError(Html.fromHtml("<font color='red'>please input monthly income</font>"));
            return;
        }

        if(nMinimalAmountPerDay > nMonthlyIncome / 30) {
            etMinimalAmountPerDay.setError(Html.fromHtml("<font color='red'>minimal amount per day should be lower than one 30th of monthly income</font>"));
            return;
        }

        UserPreference.getInstance().putSharedPreference(Constant.PREF_KEY_MINIMAL_AMOUNT_PER_DAY, nMinimalAmountPerDay);
        UserPreference.getInstance().putSharedPreference(Constant.PREF_KEY_SALARY_DATE, nSalaryDate);
        UserPreference.getInstance().putSharedPreference(Constant.PREF_KEY_MONTHLY_INCOME, nMonthlyIncome);
        UserPreference.getInstance().putSharedPreference(Constant.PREF_KEY_TOLERANCE_DAYS, nToleranceDays);
        UserPreference.getInstance().putSharedPreference(Constant.PREF_KEY_TOLERANCE_PERCENT, nTolerancePercent);

        Common.getInstance().nMinimalDayAmount = nMinimalAmountPerDay;
        Common.getInstance().nSalaryDate = nSalaryDate;
        Common.getInstance().nMonthlyIncome = nMonthlyIncome;
        Common.getInstance().nToleranceDays = nToleranceDays;
        Common.getInstance().nTolerancePercents = nTolerancePercent;

        long timestampCurrent = Common.getInstance().getTimestamp();
        long timestampCurrentPeriodEnd = Common.getInstance().getTimestampCurrentPeriodEnd();

        UsedAmount usedAmount = Common.getInstance().dbHelper.getUsedAmount(timestampCurrentPeriodEnd);

        usedAmount.setUsedAmount(nUsedSalary);
        usedAmount.setTimestampUpdated(timestampCurrent);

        Common.getInstance().dbHelper.updateUsedAmount(usedAmount);

        if(Common.getInstance().timestampInitConfig == 0) {
            Common.getInstance().timestampInitConfig = timestampCurrent;
            UserPreference.getInstance().putSharedPreference(Constant.PREF_KEY_INIT_CONFIG_TIMESTAMP, timestampCurrent);
            UserPreference.getInstance().putSharedPreference(Constant.PREF_KEY_INIT_USED_MONEY, nUsedSalary);

            mContext.launchFragment(0);
        }

        Toast.makeText(mContext, "Setting Info has been saved successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
        if(seekBar == seekBarDays) {
            tvToleranceDays.setText(seekBar.getProgress() + " DAYS");
        } else {
            tvTolerancePercent.setText(seekBar.getProgress() + " %");
        }
    }
}
