package com.example.projetds;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Contract extends AppCompatActivity {

    private TextView _txtViewHeader;
    private LinearLayout _layoutContractSearchClient, _layoutContractSearchDetails, _layoutContractNavButtons, _layoutContractCrudButtons, _layoutContractConfirmationButtons;
    Button _btnSearchContract, _btnAddNewContract, _btnSelect_Client, _btnUpdateContract, _btnDeleteContract, _btnConfirm, _btnCancel;
    private ImageButton _btnFirstPage, _btnPrevious, _btnNext, _btnLastPage;
    private EditText _edtTxtCoClSearchName, _edtTxtContractSelectedClientName, _edtTxtContractSelectedClientTel, _edtTxtContractRef, _edtTxtContractStartDate, _edtTxtContractEndDate, _edtTxtContractRoyalty;
    private dataBaseHelper _db;
    final Cursor[] mFetch = new Cursor[1];
    private int _currentConrtractIndex = 0;
    private int _selectedClientId = 0;
    final Calendar myCalendar = Calendar.getInstance();
    private Date _startDate, _endDate = null;

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void updateLabelStart() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        _edtTxtContractStartDate.setText(dateFormat.format(myCalendar.getTime()));

    }

    private void updateLabelEnd() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        _edtTxtContractEndDate.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void editTextStatus(boolean status) {
        _edtTxtContractSelectedClientName.setEnabled(status);
        _edtTxtContractSelectedClientTel.setEnabled(status);
        _edtTxtContractRef.setEnabled(status);
        _edtTxtContractStartDate.setEnabled(status);
        _edtTxtContractEndDate.setEnabled(status);
        _edtTxtContractRoyalty.setEnabled(status);
    }

    @SuppressLint("SetTextI18n")
    public void setViewMyContract() {
        _txtViewHeader.setText("My Contracts");
        _layoutContractSearchDetails.setVisibility(View.GONE);
        _layoutContractNavButtons.setVisibility(View.GONE);
        _layoutContractCrudButtons.setVisibility(View.GONE);
        _layoutContractConfirmationButtons.setVisibility(View.GONE);
        _btnAddNewContract.setVisibility(View.VISIBLE);
        _layoutContractSearchClient.setVisibility(View.VISIBLE);
        _btnSelect_Client.setVisibility(View.GONE);
    }

    public void clearEditText() {
        _edtTxtCoClSearchName.setText("");
        _edtTxtContractSelectedClientName.setText("");
        _edtTxtContractSelectedClientTel.setText("");
        _edtTxtContractRef.setText("");
        _edtTxtContractStartDate.setText("");
        _edtTxtContractEndDate.setText("");
        _edtTxtContractRoyalty.setText("");
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                assert data != null;
                Bundle bundle = data.getExtras();
                _selectedClientId = bundle.getInt("clientIndex");
                String clientName = bundle.getString("clientName");
                String clientTel = bundle.getString("clientTel");
                _edtTxtContractSelectedClientName.setText(clientName);
                _edtTxtContractSelectedClientTel.setText(clientTel);
//                makeToast("Client selected " + _selectedClientId);
                //_btnConfirm.setEnabled(true); // OUTER-JOIN make it only accept creating contract after selecting a client
            }

        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);
        _db = new dataBaseHelper(this);
        _txtViewHeader = findViewById(R.id.txtViewHeader);
        _layoutContractSearchClient = findViewById(R.id.layoutContractSearchClient);
        _layoutContractSearchDetails = findViewById(R.id.layoutSelectClientDetails);
        _layoutContractNavButtons = findViewById(R.id.layoutClientNavButtons);
        _layoutContractCrudButtons = findViewById(R.id.layoutContractCrudButtons);
        _layoutContractConfirmationButtons = findViewById(R.id.layoutContractConfirmationButtons);
        _btnSearchContract = findViewById(R.id.btnSearchContract);
        _btnAddNewContract = findViewById(R.id.btnAddNewContract);
        _btnSelect_Client = findViewById(R.id.btnSelect_Client);
        _btnCancel = findViewById(R.id.btnCancel);
        _btnConfirm = findViewById(R.id.btnConfirm);
        _btnUpdateContract = findViewById(R.id.btnUpdateContract);
        _btnDeleteContract = findViewById(R.id.btnDeleteContract);
        _btnFirstPage = findViewById(R.id.btnFirstPage);
        _btnPrevious = findViewById(R.id.btnPrevious);
        _btnNext = findViewById(R.id.btnNext);
        _btnLastPage = findViewById(R.id.btnLastPage);
        _edtTxtCoClSearchName = findViewById(R.id.edtTxtCoClSearchName);
        _edtTxtContractSelectedClientName = findViewById(R.id.edtTxtContractSelectedClientName);
        _edtTxtContractSelectedClientTel = findViewById(R.id.edtTxtContractSelectedClientTel);
        _edtTxtContractRef = findViewById(R.id.edtTxtContractRef);
        _edtTxtContractStartDate = findViewById(R.id.edtTxtContractStartDate);
        _edtTxtContractEndDate = findViewById(R.id.edtTxtContractEndDate);
        _edtTxtContractRoyalty = findViewById(R.id.edtTxtContractRoyalty);
        _layoutContractSearchDetails.setVisibility(View.GONE);
        _layoutContractNavButtons.setVisibility(View.GONE);
        _layoutContractCrudButtons.setVisibility(View.GONE);
        _layoutContractConfirmationButtons.setVisibility(View.GONE);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                _startDate = myCalendar.getTime();
                if (_endDate != null) {
                    if (_startDate.after(_endDate)) {
                        makeToast("Start date cannot be after end date");
                        _edtTxtContractStartDate.setText("");
                    } else {
                        updateLabelStart();
                    }
                } else {
                    updateLabelStart();
                }
            }
        };

        DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                _endDate = myCalendar.getTime();
                if (_startDate != null) {
                    if (_startDate.after(_endDate)) {
                        makeToast("End date cannot be before start date");
                        _edtTxtContractEndDate.setText("");
                    } else {
                        updateLabelEnd();
                    }
                } else {
                    updateLabelEnd();
                }
            }
        };

        _edtTxtContractStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Contract.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        _edtTxtContractEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Contract.this, date2, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // use case 1: search for contract by reference
        _btnSearchContract.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (_edtTxtCoClSearchName.getText().toString().isEmpty()) {
                    makeToast("Please enter a client name");
                } else {
                    editTextStatus(false);
                    _layoutContractSearchDetails.setVisibility(View.VISIBLE);
                    _layoutContractNavButtons.setVisibility(View.VISIBLE);
                    _layoutContractCrudButtons.setVisibility(View.VISIBLE);
                    _btnPrevious.setVisibility(View.VISIBLE);
                    _btnNext.setVisibility(View.VISIBLE);
                    _btnFirstPage.setVisibility(View.VISIBLE);
                    _btnLastPage.setVisibility(View.VISIBLE);
                    _btnAddNewContract.setVisibility(View.GONE);
                    _btnSelect_Client.setVisibility(View.GONE);
                    _layoutContractSearchClient.setVisibility(View.GONE);
                    _layoutContractConfirmationButtons.setVisibility(View.INVISIBLE);
                    _txtViewHeader.setText("Search Contract");
                    mFetch[0] = _db.getContractByClientName(_edtTxtCoClSearchName.getText().toString());
                    int sqlCount = mFetch[0].getCount();
//                    makeToast("Found " + sqlCount + " contracts");

                    if (mFetch[0].getCount() == 0) {
                        makeToast("No contract found");
                        setViewMyContract();

                    } else {
                        mFetch[0].moveToFirst();
                        _currentConrtractIndex = Integer.parseInt(mFetch[0].getString(0));
                        makeToast(sqlCount + " contracts(s) found");
                        _edtTxtContractSelectedClientName.setText(mFetch[0].getString(7));
                        _edtTxtContractSelectedClientTel.setText(mFetch[0].getString(9));
                        _edtTxtContractRef.setText(mFetch[0].getString(2));
                        _edtTxtContractStartDate.setText(mFetch[0].getString(3));
                        _edtTxtContractEndDate.setText(mFetch[0].getString(4));
                        _edtTxtContractRoyalty.setText(mFetch[0].getString(5));

                    }
                    if (mFetch[0].getCount() == 1) {
                        _layoutContractNavButtons.setVisibility(View.GONE);
                    } else if (mFetch[0].getCount() > 1) {
                        _btnFirstPage.setVisibility(View.INVISIBLE);
                        _btnPrevious.setVisibility(View.INVISIBLE);
                    } else {
                        _layoutContractNavButtons.setVisibility(View.INVISIBLE);
                        makeToast("No contracts found");
                    }
                    _btnNext.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(View v) {
                            if (mFetch[0].isLast()) {
                                _btnLastPage.setVisibility(View.INVISIBLE);
                                _btnNext.setVisibility(View.INVISIBLE);
                            } else {
                                mFetch[0].moveToNext();
                                _btnPrevious.setVisibility(View.VISIBLE);
                                _btnFirstPage.setVisibility(View.VISIBLE);
                                _currentConrtractIndex = Integer.parseInt(mFetch[0].getString(0));
                                _edtTxtContractSelectedClientName.setText(mFetch[0].getString(7));
                                _edtTxtContractSelectedClientTel.setText(mFetch[0].getString(9));
                                _edtTxtContractRef.setText(mFetch[0].getString(2));
                                _edtTxtContractStartDate.setText(mFetch[0].getString(3));
                                _edtTxtContractEndDate.setText(mFetch[0].getString(4));
                                _edtTxtContractRoyalty.setText(mFetch[0].getString(5));
                                if (mFetch[0].isLast()) {
                                    _btnLastPage.setVisibility(View.INVISIBLE);
                                    _btnNext.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    });
                    _btnPrevious.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(View v) {
                            if (mFetch[0].isFirst()) {
                                _btnFirstPage.setVisibility(View.INVISIBLE);
                                _btnPrevious.setVisibility(View.INVISIBLE);
                            } else {
                                mFetch[0].moveToPrevious();
                                _btnNext.setVisibility(View.VISIBLE);
                                _btnLastPage.setVisibility(View.VISIBLE);
                                _currentConrtractIndex = Integer.parseInt(mFetch[0].getString(0));
                                _edtTxtContractSelectedClientName.setText(mFetch[0].getString(7));
                                _edtTxtContractSelectedClientTel.setText(mFetch[0].getString(9));
                                _edtTxtContractRef.setText(mFetch[0].getString(2));
                                _edtTxtContractStartDate.setText(mFetch[0].getString(3));
                                _edtTxtContractEndDate.setText(mFetch[0].getString(4));
                                _edtTxtContractRoyalty.setText(mFetch[0].getString(5));
                                if (mFetch[0].isFirst()) {
                                    _btnFirstPage.setVisibility(View.INVISIBLE);
                                    _btnPrevious.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    });
                    _btnLastPage.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(View v) {
                            if (mFetch[0].isLast()) {
                                _btnLastPage.setVisibility(View.INVISIBLE);
                                _btnNext.setVisibility(View.INVISIBLE);
                            } else {
                                mFetch[0].moveToLast();
                                _btnPrevious.setVisibility(View.VISIBLE);
                                _btnFirstPage.setVisibility(View.VISIBLE);
                                _currentConrtractIndex = Integer.parseInt(mFetch[0].getString(0));
                                _edtTxtContractSelectedClientName.setText(mFetch[0].getString(7));
                                _edtTxtContractSelectedClientTel.setText(mFetch[0].getString(9));
                                _edtTxtContractRef.setText(mFetch[0].getString(2));
                                _edtTxtContractStartDate.setText(mFetch[0].getString(3));
                                _edtTxtContractEndDate.setText(mFetch[0].getString(4));
                                _edtTxtContractRoyalty.setText(mFetch[0].getString(5));
                                if (mFetch[0].isFirst()) {
                                    _btnFirstPage.setVisibility(View.INVISIBLE);
                                    _btnPrevious.setVisibility(View.INVISIBLE);

                                }
                            }
                            _btnLastPage.setVisibility(View.INVISIBLE);
                            _btnNext.setVisibility(View.INVISIBLE);
                        }
                    });

                    _btnFirstPage.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(View v) {
                            if (mFetch[0].isFirst()) {
                                _btnFirstPage.setVisibility(View.INVISIBLE);
                                _btnPrevious.setVisibility(View.INVISIBLE);
                            } else {
                                mFetch[0].moveToFirst();
                                _btnNext.setVisibility(View.VISIBLE);
                                _btnLastPage.setVisibility(View.VISIBLE);
                                _currentConrtractIndex = Integer.parseInt(mFetch[0].getString(0));
                                _edtTxtContractSelectedClientName.setText(mFetch[0].getString(7));
                                _edtTxtContractSelectedClientTel.setText(mFetch[0].getString(9));
                                _edtTxtContractRef.setText(mFetch[0].getString(2));
                                _edtTxtContractStartDate.setText(mFetch[0].getString(3));
                                _edtTxtContractEndDate.setText(mFetch[0].getString(4));
                                _edtTxtContractRoyalty.setText(mFetch[0].getString(5));
                                if (mFetch[0].isLast()) {
                                    _btnLastPage.setVisibility(View.INVISIBLE);
                                    _btnNext.setVisibility(View.INVISIBLE);
                                }
                            }
                            _btnFirstPage.setVisibility(View.INVISIBLE);
                            _btnPrevious.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                //use case 2-1 : edit contract
                _btnUpdateContract.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        _txtViewHeader.setText("Edit Contract");
                        _btnSelect_Client.setVisibility(View.VISIBLE);
                        _layoutContractCrudButtons.setVisibility(View.GONE);
                        _layoutContractConfirmationButtons.setVisibility(View.VISIBLE);
                        _layoutContractSearchClient.setVisibility(View.GONE);
                        _layoutContractNavButtons.setVisibility(View.GONE);
                        editTextStatus(true);
                        _edtTxtContractSelectedClientName.setEnabled(false);
                        _edtTxtContractSelectedClientTel.setEnabled(false);

                        _btnSelect_Client.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent1 = new Intent(Contract.this, SelectClient.class);
                                startActivityIntent.launch(intent1);
                            }
                        });
                        _btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                _db.updateContract(_currentConrtractIndex, _edtTxtContractRef.getText().toString(), _edtTxtContractStartDate.getText().toString(), _edtTxtContractEndDate.getText().toString(), _edtTxtContractRoyalty.getText().toString(), _selectedClientId);
                                makeToast("Contract updated successfully");
                                editTextStatus(false);
                                setViewMyContract();
                                clearEditText();
                            }
                        });
                        _btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editTextStatus(false);
                                setViewMyContract();
                                clearEditText();
                            }
                        });
                    }
                });
                //use case 2-2 : delete contract
                _btnDeleteContract.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        _txtViewHeader.setText("Deleting contract");
                        _layoutContractCrudButtons.setVisibility(View.GONE);
                        _layoutContractConfirmationButtons.setVisibility(View.VISIBLE);
                        _btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onClick(View v) {
                                _db.deleteContract(String.valueOf(_currentConrtractIndex));
                                makeToast("Contract deleted successfully");
                                setViewMyContract();
                                clearEditText();
                            }
                        });
                        _btnCancel.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onClick(View v) {
                                setViewMyContract();
                                clearEditText();
                            }
                        });
                    }
                });
            }
        });

        // use case 4: add new contract
        _btnAddNewContract.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                _txtViewHeader.setText("Add new contract");
                _btnSelect_Client.setVisibility(View.VISIBLE);
                _btnAddNewContract.setVisibility(View.GONE);
                _layoutContractSearchClient.setVisibility(View.GONE);
                _layoutContractSearchDetails.setVisibility(View.VISIBLE);
                _layoutContractConfirmationButtons.setVisibility(View.VISIBLE);
                _edtTxtContractSelectedClientName.setText("");
                _edtTxtContractRef.setText("");
                _edtTxtContractStartDate.setText("");
                _edtTxtContractEndDate.setText("");
                _edtTxtContractRoyalty.setText("");
                _edtTxtContractSelectedClientTel.setText("");
                _edtTxtCoClSearchName.setText("");
                clearEditText();
                editTextStatus(true);

                _edtTxtContractSelectedClientName.setEnabled(false);
                _edtTxtContractSelectedClientTel.setEnabled(false);
                _btnConfirm.setEnabled(true); // OUTER-JOIN make it accept creating contract without selecting a client

                _btnSelect_Client.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(Contract.this, SelectClient.class);
                        startActivityIntent.launch(intent1);
                    }
                });
                // OUTER-JOIN moved out of _btnSelect_Client.setOnClickListener to simulate a contract without a client
                _btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (_edtTxtContractRef.getText().toString().isEmpty() || _edtTxtContractStartDate.getText().toString().isEmpty() || _edtTxtContractEndDate.getText().toString().isEmpty() || _edtTxtContractRoyalty.getText().toString().isEmpty()) {
                            makeToast("Please fill all fields");
                        } else {
                            if (_db.addContract(_edtTxtContractRef.getText().toString(), _edtTxtContractStartDate.getText().toString(), _edtTxtContractEndDate.getText().toString(), _edtTxtContractRoyalty.getText().toString(), _selectedClientId)) {
                                makeToast("Contract added successfully");
                                setViewMyContract();
                            } else {
                                makeToast("Contract not added");
                            }
                        }
                    }
                });
                _btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearEditText();
                        setViewMyContract();
                    }
                });
            }
        });
    }

}