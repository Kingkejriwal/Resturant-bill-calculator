package com.example.king_sk.calcu;

import java.text.NumberFormat;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    // Widgets in the application
    private EditText txtAmount;
    private EditText txtPeople;
    private EditText txtTipOther;
    private RadioGroup rdoGroupTips;
    private Button btnCalculate;
    private Button btnReset;
    private TextView txtTipAmount;
    private TextView txtTotalToPay;
    private TextView txtTipPerPerson;
    // For the ID of the radio button selected
    private int radioCheckedId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Access the various widgets by their ID in R.java
        txtAmount = (EditText) findViewById(R.id.txtAmount);
        // On app load, the cursor should be in the Amount field
        txtAmount.requestFocus();
        txtPeople = (EditText) findViewById(R.id.txtPeople);
        txtTipOther = (EditText) findViewById(R.id.txtTipOther);
        rdoGroupTips = (RadioGroup) findViewById(R.id.RadioGroupTips);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        // On app load, the Calculate button is disabled
        btnCalculate.setEnabled(false);
        btnReset = (Button) findViewById(R.id.btnReset);
        txtTipAmount = (TextView) findViewById(R.id.txtTipAmount);
        txtTotalToPay = (TextView) findViewById(R.id.txtTotalToPay);
        txtTipPerPerson = (TextView) findViewById(R.id.txtTipPerPerson);
        // On app load, disable the Other Tip Percentage text field
        txtTipOther.setEnabled(false);

        rdoGroupTips.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Enable/disable Other Tip Percentage field
                if (checkedId == R.id.radioFifteen
                        || checkedId == R.id.radioTwenty) {
                    txtTipOther.setEnabled(false);
                    /*          * Enable the Calculate button if Total Amount and No. of
                             *  * People fields have valid values.          */
                    btnCalculate.setEnabled(txtAmount.getText().length() > 0
                            && txtPeople.getText().length() > 0);
                }
                if (checkedId == R.id.radioOther) {
                    // Enable the Other Tip Percentage field
                     txtTipOther.setEnabled(true);
                     // Set the focus to this field
                    txtTipOther.requestFocus();
                    /*         * Enable the Calculate button if Total Amount and No. of
                            *  * People fields have valid values. Also ensure that user
                                    *  * has entered an Other Tip Percentage value before enabling
                                    *  * the Calculate button.         */
                    btnCalculate.setEnabled(txtAmount.getText().length() > 0
                            && txtPeople.getText().length() > 0
                            && txtTipOther.getText().length() > 0);
                }
                // To determine the tip percentage choice made by user
                radioCheckedId = checkedId;    }
        });

        /* * Attach a KeyListener to the Tip Amount, No. of People, and Other Tip * Percentage text fields */
        txtAmount.setOnKeyListener(mKeyListener);
        txtPeople.setOnKeyListener(mKeyListener);
        txtTipOther.setOnKeyListener(mKeyListener);


    }

    private OnKeyListener mKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (v.getId()) {
            case R.id.txtAmount:
                case R.id.txtPeople:
                    btnCalculate.setEnabled(txtAmount.getText().length() > 0
                            && txtPeople.getText().length() > 0);
                    break;
                    case R.id.txtTipOther:
                        btnCalculate.setEnabled(txtAmount.getText().length() > 0
                                && txtPeople.getText().length() > 0
                                && txtTipOther.getText().length() > 0);
                        break;
        }
        return false;
        }
    };
    /* Attach listener to the Calculate and Reset buttons */
    //btnCalculate.setOnClickListener(mClickListener);
    //btnReset.setOnClickListener(mClickListener);

    /** * ClickListener for the Calculate and Reset buttons. * Depending on the button clicked, the corresponding * method is called. */
    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnCalculate) {
                calculate();
            }
            else {
                reset();
            }
        }
    };

    private void reset() {
        txtTipAmount.setText("");
        txtTotalToPay.setText("");
        txtTipPerPerson.setText("");
        txtAmount.setText("");
        txtPeople.setText("");
        txtTipOther.setText("");
        rdoGroupTips.clearCheck();
        rdoGroupTips.check(R.id.radioFifteen);
        // Set focus on the first field
        txtAmount.requestFocus();
    }

    /** * Calculate the tip as per data entered by the user. */
    private void calculate() {
        Double billAmount = Double.parseDouble(
                txtAmount.getText().toString());
        Double totalPeople = Double.parseDouble(
                txtPeople.getText().toString());
        Double percentage = null;
        boolean isError = false;
        if (billAmount < 1.0) {
            showErrorAlert("Enter a valid Total Amount.",
                    txtAmount.getId());
            isError = true;
        }
        if (totalPeople < 1.0) {
            showErrorAlert("Enter a valid value for No. of People.",
                    txtPeople.getId());
            isError = true;
        }
        /*     * If the user never changes the radio selection, then it means
             * the default selection of 15% is in effect. But it's     *
             * safer to verify     */
        if (radioCheckedId == -1) {
            radioCheckedId = rdoGroupTips.getCheckedRadioButtonId();
        }
        if (radioCheckedId == R.id.radioFifteen) {
            percentage = 15.00;
        }
        else if (radioCheckedId == R.id.radioTwenty) {
            percentage = 20.00;
        }
        else if (radioCheckedId == R.id.radioOther) {
            percentage = Double.parseDouble(
                    txtTipOther.getText().toString());
            if (percentage < 1.0) {
                showErrorAlert("Enter a valid Tip percentage",
                        txtTipOther.getId());
                isError = true;
            }
        }
        /*     * If all fields are populated with valid values, then proceed to     * calculate the tip     */
        if (!isError) {
            Double tipAmount = ((billAmount * percentage) / 100);
            Double totalToPay = billAmount + tipAmount;
            Double perPersonPays = totalToPay / totalPeople;
            txtTipAmount.setText(tipAmount.toString());
            txtTotalToPay.setText(totalToPay.toString());
            txtTipPerPerson.setText(perPersonPays.toString());
        }
    }

    private void showErrorAlert(String errorMessage,
                                final int fieldId) {
        new AlertDialog.Builder(this).setTitle("Error")
                .setMessage(errorMessage).setNeutralButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        findViewById(fieldId).requestFocus();
                    }
                }).show();
    }


}
