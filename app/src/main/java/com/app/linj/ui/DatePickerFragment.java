package com.app.linj.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by zhangshenglan on 16/5/6.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    int _year=1970;
    int _month=0;
    int _day=0;
    String date = "";
    TheListener listener;
    public interface TheListener{
        void returnDate(String date);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c=Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        // TODO 日期选择完成事件，取消时不会触发
        _year=year;
        _month=monthOfYear+1;
        _day=dayOfMonth;
        Log.i("log", "year=" + year + ",monthOfYear=" + monthOfYear + ",dayOfMonth=" + dayOfMonth);
    }

    private String getValue(){
        date = ""+_year+_month+_day;
        return ""+_year+_month+_day;
    }


}