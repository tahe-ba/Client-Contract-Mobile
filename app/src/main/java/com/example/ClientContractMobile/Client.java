package com.example.ClientContractMobile;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ClientContract.R;

public class Client extends AppCompatActivity {

    private TextView _txtViewHeader;
    private LinearLayout _layoutClientSearchClient, _layoutClientDetails, _layoutClientNavButtons, _layoutClientCrudButtons, _layoutClientConfirmationButtons;
    private EditText _edtTxtSearchClientName, _edtTxtSelectedClientName, _edtTxtSelectedClientAddress, _edtTxtSelectedClientTel, _edtTxtSelectedClientFax, _edtTxtSelectedClientContact, _edtTxtSelectedClientTelContact;
    private Button _btnSearchClient, _btnAddNewClient, _btnAddClient, _btnUpdateClient, _btnDeleteClient, _btnConfirm, _btnCancel;
    private ImageButton _btnFirstPage, _btnPrevious, _btnNext, _btnLastPage;
    private dataBaseHelper _db;
    final Cursor[] mFetch = new Cursor[1];
    private int _currentClientIndex = 0;

    private void editTxtStatus(boolean status) {
        _edtTxtSelectedClientName.setEnabled(status);
        _edtTxtSelectedClientAddress.setEnabled(status);
        _edtTxtSelectedClientTel.setEnabled(status);
        _edtTxtSelectedClientFax.setEnabled(status);
        _edtTxtSelectedClientContact.setEnabled(status);
        _edtTxtSelectedClientTelContact.setEnabled(status);
    }

    @SuppressLint("SetTextI18n")
    private void setViewMyClients() {
        _txtViewHeader.setText("My Clients");
        _layoutClientDetails.setVisibility(View.GONE);
        _layoutClientConfirmationButtons.setVisibility(View.GONE);
        _layoutClientCrudButtons.setVisibility(View.GONE);
        _layoutClientSearchClient.setVisibility(View.VISIBLE);
        _btnAddNewClient.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void clearEdtTxt() {
        _edtTxtSelectedClientName.setText("");
        _edtTxtSelectedClientAddress.setText("");
        _edtTxtSelectedClientTel.setText("");
        _edtTxtSelectedClientFax.setText("");
        _edtTxtSelectedClientContact.setText("");
        _edtTxtSelectedClientTelContact.setText("");
    }

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        _db = new dataBaseHelper(this);
        _txtViewHeader = findViewById(R.id.txtViewHeader);
        _layoutClientCrudButtons = findViewById(R.id.layoutClientCrudButtons);
        _layoutClientConfirmationButtons = findViewById(R.id.layoutClientConfirmationButtons);
        _layoutClientSearchClient = findViewById(R.id.layoutClientSearchClient);
        _layoutClientDetails = findViewById(R.id.layoutClientSearchDetails);
        _layoutClientNavButtons = findViewById(R.id.layoutClientNavButtons);
        _edtTxtSearchClientName = findViewById(R.id.edtTxtSearchClientName);
        _edtTxtSelectedClientName = findViewById(R.id.edtTxtSelectedClientName);
        _edtTxtSelectedClientAddress = findViewById(R.id.edtTxtSelectedClientAddress);
        _edtTxtSelectedClientTel = findViewById(R.id.edtTxtSelectedClientTel);
        _edtTxtSelectedClientFax = findViewById(R.id.edtTxtSelectedClientFax);
        _edtTxtSelectedClientContact = findViewById(R.id.edtTxtSelectedClientContact);
        _edtTxtSelectedClientTelContact = findViewById(R.id.edtTxtSelectedClientTelContact);
        _btnSearchClient = findViewById(R.id.btnSearchClient);
        _btnAddNewClient = findViewById(R.id.btnAddNewContract);
        _btnAddClient = findViewById(R.id.btnAddClient);
        _btnUpdateClient = findViewById(R.id.btnUpdateClient);
        _btnDeleteClient = findViewById(R.id.btnDeleteClient);
        _btnConfirm = findViewById(R.id.btnConfirm);
        _btnCancel = findViewById(R.id.btnCancel);
        _btnFirstPage = findViewById(R.id.btnFirstPage);
        _btnPrevious = findViewById(R.id.btnPrevious);
        _btnNext = findViewById(R.id.btnNext);
        _btnLastPage = findViewById(R.id.btnLastPage);
        _layoutClientDetails.setVisibility(View.GONE);
        _layoutClientConfirmationButtons.setVisibility(View.GONE);
        _layoutClientCrudButtons.setVisibility(View.GONE);
        //Use case 1 : Add new client
        _btnAddNewClient.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                _txtViewHeader.setText("Add new client");
                _layoutClientDetails.setVisibility(View.VISIBLE);
                _layoutClientConfirmationButtons.setVisibility(View.VISIBLE);
                _layoutClientCrudButtons.setVisibility(View.GONE);
                _layoutClientSearchClient.setVisibility(View.GONE);
                _layoutClientNavButtons.setVisibility(View.GONE);
                _btnAddNewClient.setVisibility(View.GONE);
                clearEdtTxt();
                editTxtStatus(true);
                // use case 1-1 : confirm add new client
                _btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        if (_edtTxtSelectedClientName.getText().toString().isEmpty()) {
                            makeToast("Please fill at least the name field");
                        } else {
//                            _db.deleteAllClient();
                            if (_db.addClient(_edtTxtSelectedClientName.getText().toString(), _edtTxtSelectedClientAddress.getText().toString(), _edtTxtSelectedClientTel.getText().toString(), _edtTxtSelectedClientFax.getText().toString(), _edtTxtSelectedClientContact.getText().toString(), _edtTxtSelectedClientTelContact.getText().toString())) {
                                makeToast("Client added successfully");
                                setViewMyClients();
                                clearEdtTxt();
                            } else {
                                makeToast("Error adding client");
                            }
                        }
                    }
                });
                // use case 1-2 : cancel Add new client
                _btnCancel.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        setViewMyClients();
                        clearEdtTxt();
                    }
                });
            }
        });


        // use case 2 : search client
        _btnSearchClient.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // use case 2-1 : update client
                if (_edtTxtSearchClientName.getText().toString().isEmpty()) {
                    makeToast("Please enter a client name");
                } else {
                    editTxtStatus(false);
                    _btnPrevious.setVisibility(View.VISIBLE);
                    _btnNext.setVisibility(View.VISIBLE);
                    _btnFirstPage.setVisibility(View.VISIBLE);
                    _btnLastPage.setVisibility(View.VISIBLE);
                    _txtViewHeader.setText("Search client");
                    _layoutClientDetails.setVisibility(View.VISIBLE);
                    _layoutClientCrudButtons.setVisibility(View.VISIBLE);
                    _layoutClientNavButtons.setVisibility(View.VISIBLE);
                    _layoutClientConfirmationButtons.setVisibility(View.GONE);
                    _layoutClientSearchClient.setVisibility(View.GONE);
                    _btnAddNewClient.setVisibility(View.GONE);
                    _btnAddClient.setVisibility(View.GONE);
                    // TODO : search client in database
                    mFetch[0] = _db.searchClient(_edtTxtSearchClientName.getText().toString());
                    int sqlCount = mFetch[0].getCount();
                    if (mFetch[0].getCount() == 0) {
                        makeToast("No client found");
                        setViewMyClients();

                    } else {
                        mFetch[0].moveToFirst();
                        _currentClientIndex = Integer.parseInt(mFetch[0].getString(0));
                        makeToast(sqlCount + " client(s) found");
                        _edtTxtSelectedClientName.setText(mFetch[0].getString(1));
                        _edtTxtSelectedClientAddress.setText(mFetch[0].getString(2));
                        _edtTxtSelectedClientTel.setText(mFetch[0].getString(3));
                        _edtTxtSelectedClientFax.setText(mFetch[0].getString(4));
                        _edtTxtSelectedClientContact.setText(mFetch[0].getString(5));
                        _edtTxtSelectedClientTelContact.setText(mFetch[0].getString(6));
                    }
                    if (mFetch[0].getCount() == 1) {
                        _layoutClientNavButtons.setVisibility(View.GONE);
                    } else if (mFetch[0].getCount() > 1) {
                        _btnFirstPage.setVisibility(View.INVISIBLE);
                        _btnPrevious.setVisibility(View.INVISIBLE);
                    } else {
                        _layoutClientNavButtons.setVisibility(View.INVISIBLE);
                        makeToast("No client found");
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
                                _currentClientIndex = Integer.parseInt(mFetch[0].getString(0));
                                _edtTxtSelectedClientName.setText(mFetch[0].getString(1));
                                _edtTxtSelectedClientAddress.setText(mFetch[0].getString(2));
                                _edtTxtSelectedClientTel.setText(mFetch[0].getString(3));
                                _edtTxtSelectedClientFax.setText(mFetch[0].getString(4));
                                _edtTxtSelectedClientContact.setText(mFetch[0].getString(5));
                                _edtTxtSelectedClientTelContact.setText(mFetch[0].getString(6));
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
                                _currentClientIndex = Integer.parseInt(mFetch[0].getString(0));
                                _edtTxtSelectedClientName.setText(mFetch[0].getString(1));
                                _edtTxtSelectedClientAddress.setText(mFetch[0].getString(2));
                                _edtTxtSelectedClientTel.setText(mFetch[0].getString(3));
                                _edtTxtSelectedClientFax.setText(mFetch[0].getString(4));
                                _edtTxtSelectedClientContact.setText(mFetch[0].getString(5));
                                _edtTxtSelectedClientTelContact.setText(mFetch[0].getString(6));
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
                                _currentClientIndex = Integer.parseInt(mFetch[0].getString(0));
                                _edtTxtSelectedClientName.setText(mFetch[0].getString(1));
                                _edtTxtSelectedClientAddress.setText(mFetch[0].getString(2));
                                _edtTxtSelectedClientTel.setText(mFetch[0].getString(3));
                                _edtTxtSelectedClientFax.setText(mFetch[0].getString(4));
                                _edtTxtSelectedClientContact.setText(mFetch[0].getString(5));
                                _edtTxtSelectedClientTelContact.setText(mFetch[0].getString(6));
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
                                _currentClientIndex = Integer.parseInt(mFetch[0].getString(0));
                                _edtTxtSelectedClientName.setText(mFetch[0].getString(1));
                                _edtTxtSelectedClientAddress.setText(mFetch[0].getString(2));
                                _edtTxtSelectedClientTel.setText(mFetch[0].getString(3));
                                _edtTxtSelectedClientFax.setText(mFetch[0].getString(4));
                                _edtTxtSelectedClientContact.setText(mFetch[0].getString(5));
                                _edtTxtSelectedClientTelContact.setText(mFetch[0].getString(6));
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
                //use case 2-1 : edit client
                _btnUpdateClient.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        _txtViewHeader.setText("Editing client details");
                        editTxtStatus(true);
                        _layoutClientNavButtons.setVisibility(View.INVISIBLE);
                        _layoutClientCrudButtons.setVisibility(View.GONE);
                        _layoutClientConfirmationButtons.setVisibility(View.VISIBLE);
                        _btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onClick(View v) {
                                _db.updateClient(String.valueOf(_currentClientIndex), _edtTxtSelectedClientName.getText().toString(), _edtTxtSelectedClientAddress.getText().toString(), _edtTxtSelectedClientTel.getText().toString(), _edtTxtSelectedClientFax.getText().toString(), _edtTxtSelectedClientContact.getText().toString(), _edtTxtSelectedClientTelContact.getText().toString());
                                makeToast("Client updated successfully");
                                setViewMyClients();
                                clearEdtTxt();

                            }
                        });
                        _btnCancel.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onClick(View v) {
                                setViewMyClients();
                                clearEdtTxt();
                            }
                        });
                    }
                });
                // use case 2-2 : delete client
                _btnDeleteClient.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        _txtViewHeader.setText("Deleting client");
                        _layoutClientCrudButtons.setVisibility(View.GONE);
                        _layoutClientConfirmationButtons.setVisibility(View.VISIBLE);
                        _btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onClick(View v) {
                                _db.deleteClient(String.valueOf(_currentClientIndex));
                                makeToast("Client deleted successfully");
                                setViewMyClients();
                                clearEdtTxt();
                            }
                        });
                        _btnCancel.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onClick(View v) {
                                setViewMyClients();
                                clearEdtTxt();
                            }
                        });
                    }
                });
            }
        });
    }
}