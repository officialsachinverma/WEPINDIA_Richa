package com.wepindia.pos.GenericClasses;

/**
 * Created by RichaA on 4/12/2017.
 */

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.NumberPicker;

import com.wepindia.pos.R;


@SuppressLint("InflateParams")
public class MonthYearPicker {

    private static final int MIN_YEAR = 2000;

    private static final int MAX_YEAR = 2050;

    private static final String[] PICKER_DISPLAY_MONTHS_NAMES = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
            "Nov", "Dec" };

    /*private static final String[] MONTHS = new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December" };*/
    private static final String[] MONTHS = new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12" };

    private View view;
    private Activity activity;
    private AlertDialog.Builder builder;
    private AlertDialog pickerDialog;
    private boolean build = false;
    private NumberPicker monthNumberPicker;
    private NumberPicker yearNumberPicker;

    /**
     * Instantiates a new month year picker.
     *
     * @param activity
     *            the activity
     */
    public MonthYearPicker(Activity activity) {
        this.activity = activity;
        this.view = activity.getLayoutInflater().inflate(R.layout.month_year_picker_view, null);
    }

    /**
     * Builds the month year alert dialog.
     *
     * @param positiveButtonListener
     *            the positive listener
     * @param negativeButtonListener
     *            the negative listener
     */
    public void build(DialogInterface.OnClickListener positiveButtonListener, DialogInterface.OnClickListener negativeButtonListener) {
        this.build(-1, -1, positiveButtonListener, negativeButtonListener);
    }

    private int currentYear;

    private int currentMonth;

    /**
     * Builds the month year alert dialog.
     *
     * @param selectedMonth
     *            the selected month 0 to 11 (sets current moth if invalid
     *            value)
     * @param selectedYear
     *            the selected year 1970 to 2099 (sets current year if invalid
     *            value)
     * @param positiveButtonListener
     *            the positive listener
     * @param negativeButtonListener
     *            the negative listener
     */
    public void build(int selectedMonth, int selectedYear, DialogInterface.OnClickListener positiveButtonListener,
                      DialogInterface.OnClickListener negativeButtonListener) {

        final Calendar instance = Calendar.getInstance();
        currentMonth = instance.get(Calendar.MONTH);
        currentYear = instance.get(Calendar.YEAR);

        if (selectedMonth > 11 || selectedMonth < -1) {
            selectedMonth = currentMonth;
        }

        if (selectedYear < MIN_YEAR || selectedYear > MAX_YEAR) {
            selectedYear = currentYear;
        }

        if (selectedMonth == -1) {
            selectedMonth = currentMonth;
        }

        if (selectedYear == -1) {
            selectedYear = currentYear;
        }

        builder = new AlertDialog.Builder(activity);
        builder.setView(view);

        monthNumberPicker = (NumberPicker) view.findViewById(R.id.monthNumberPicker);
        monthNumberPicker.setDisplayedValues(PICKER_DISPLAY_MONTHS_NAMES);

        monthNumberPicker.setMinValue(0);
        monthNumberPicker.setMaxValue(MONTHS.length - 1);

        yearNumberPicker = (NumberPicker) view.findViewById(R.id.yearNumberPicker);
        yearNumberPicker.setMinValue(MIN_YEAR);
        yearNumberPicker.setMaxValue(MAX_YEAR);

        monthNumberPicker.setValue(selectedMonth);
        yearNumberPicker.setValue(selectedYear);

        monthNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        yearNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        builder.setTitle("Please Select month and year of tax");
        builder.setPositiveButton("Ok", positiveButtonListener);
        builder.setNegativeButton("Cancel", negativeButtonListener);
        build = true;
        pickerDialog = builder.create();

    }

    /**
     * Show month year picker dialog.
     */
    public void show() {
        if (build) {
            pickerDialog.show();
        } else {
            throw new IllegalStateException("Build picker before use");
        }
    }

    /**
     * Gets the selected month.
     *
     * @return the selected month
     */
    public int getSelectedMonth() {
        return monthNumberPicker.getValue();
    }

    /**
     * Gets the selected month name.
     *
     * @return the selected month name
     */
    public String getSelectedMonthName() {
        return MONTHS[monthNumberPicker.getValue()];
    }

    /**
     * Gets the selected month name.
     *
     * @return the selected month short name i.e Jan, Feb ...
     */
    public String getSelectedMonthShortName() {
        return PICKER_DISPLAY_MONTHS_NAMES[monthNumberPicker.getValue()];
    }

    /**
     * Gets the selected year.
     *
     * @return the selected year
     */
    public int getSelectedYear() {
        return yearNumberPicker.getValue();
    }

    /**
     * Gets the current year.
     *
     * @return the current year
     */
    public int getCurrentYear() {
        return currentYear;
    }

    /**
     * Gets the current month.
     *
     * @return the current month
     */
    public int getCurrentMonth() {
        return currentMonth;
    }

    /**
     * Sets the month value changed listener.
     *
     * @param valueChangeListener
     *            the new month value changed listener
     */
    public void setMonthValueChangedListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        monthNumberPicker.setOnValueChangedListener(valueChangeListener);
    }

    /**
     * Sets the year value changed listener.
     *
     * @param valueChangeListener
     *            the new year value changed listener
     */
    public void setYearValueChangedListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        yearNumberPicker.setOnValueChangedListener(valueChangeListener);
    }

    /**
     * Sets the month wrap selector wheel.
     *
     * @param wrapSelectorWheel
     *            the new month wrap selector wheel
     */
    public void setMonthWrapSelectorWheel(boolean wrapSelectorWheel) {
        monthNumberPicker.setWrapSelectorWheel(wrapSelectorWheel);
    }

    /**
     * Sets the year wrap selector wheel.
     *
     * @param wrapSelectorWheel
     *            the new year wrap selector wheel
     */
    public void setYearWrapSelectorWheel(boolean wrapSelectorWheel) {
        yearNumberPicker.setWrapSelectorWheel(wrapSelectorWheel);
    }
}
