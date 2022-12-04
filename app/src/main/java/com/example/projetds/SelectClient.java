package com.example.projetds;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class SelectClient extends AppCompatActivity {


    private TextView _txtViewHeader;
    private LinearLayout _layoutContractSelectClient, _layoutSelectClientDetails, _layoutSelectNavButtons;
    private EditText _edtTxtSearchName, _edtTxtSelectedClientName, _edtTxtSelectedClientAddress, _edtTxtSelectedClientTel, _edtTxtSelectedClientFax, _edtTxtSelectedClientContact, _edtTxtSelectedClientTelContact;
    private ImageButton _btnFirstPage, _btnPrevious, _btnNext, _btnLastPage;
    private Button _btnSelectThisClient, _btnSearchClient;
    private dataBaseHelper _db;
    final Cursor[] mFetch = new Cursor[1];
    private int _currentClientIndex;

    private void editTextStatus(boolean status) {
        _edtTxtSelectedClientName.setEnabled(status);
        _edtTxtSelectedClientAddress.setEnabled(status);
        _edtTxtSelectedClientTel.setEnabled(status);
        _edtTxtSelectedClientFax.setEnabled(status);
        _edtTxtSelectedClientContact.setEnabled(status);
        _edtTxtSelectedClientTelContact.setEnabled(status);
    }


    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_client);
        _db = new dataBaseHelper(this);

        //link to the xml file
        _txtViewHeader = findViewById(R.id.txtViewHeader);
        _layoutContractSelectClient = findViewById(R.id.layoutContractSelectClient);
        _layoutSelectClientDetails = findViewById(R.id.layoutSelectClientDetails);
        _layoutSelectNavButtons = findViewById(R.id.layoutSelectNavButtons);
        _edtTxtSearchName = findViewById(R.id.edtTxtSearchName);
        _edtTxtSelectedClientName = findViewById(R.id.edtTxtSelectedClientName);
        _edtTxtSelectedClientAddress = findViewById(R.id.edtTxtSelectedClientAddress);
        _edtTxtSelectedClientTel = findViewById(R.id.edtTxtSelectedClientTel);
        _edtTxtSelectedClientFax = findViewById(R.id.edtTxtSelectedClientFax);
        _edtTxtSelectedClientContact = findViewById(R.id.edtTxtSelectedClientContact);
        _edtTxtSelectedClientTelContact = findViewById(R.id.edtTxtSelectedClientTelContact);
        _btnFirstPage = findViewById(R.id.btnFirstPage);
        _btnPrevious = findViewById(R.id.btnPrevious);
        _btnNext = findViewById(R.id.btnNext);
        _btnLastPage = findViewById(R.id.btnLastPage);
        _btnSelectThisClient = findViewById(R.id.btnSelectThisClient);
        _btnSearchClient = findViewById(R.id.btnSearchClient);

        //all edit text are disabled
        editTextStatus(false);

        _layoutSelectClientDetails.setVisibility(LinearLayout.INVISIBLE);
        _btnSelectThisClient.setVisibility(Button.INVISIBLE);

        _btnSearchClient.setOnClickListener(v -> {
            _layoutSelectClientDetails.setVisibility(LinearLayout.VISIBLE);
            _btnSelectThisClient.setVisibility(Button.VISIBLE);
            if (_edtTxtSearchName.getText().toString().isEmpty() || _edtTxtSearchName.getText().toString().equals(" ")) {
                makeToast("Please enter a name");
                _layoutSelectClientDetails.setVisibility(LinearLayout.INVISIBLE);
                _btnSelectThisClient.setVisibility(Button.INVISIBLE);
            } else {
                editTextStatus(false);

                _btnFirstPage.setVisibility(ImageButton.VISIBLE);
                _btnPrevious.setVisibility(ImageButton.VISIBLE);
                _btnNext.setVisibility(ImageButton.VISIBLE);
                _btnLastPage.setVisibility(ImageButton.VISIBLE);
                _layoutSelectNavButtons.setVisibility(LinearLayout.VISIBLE);
                _layoutSelectClientDetails.setVisibility(LinearLayout.VISIBLE);

                mFetch[0] = _db.searchClient(_edtTxtSearchName.getText().toString());
                int sqlCount = mFetch[0].getCount();
                if (mFetch[0].getCount() == 0) {
                    _layoutSelectNavButtons.setVisibility(LinearLayout.INVISIBLE);
                    _layoutSelectClientDetails.setVisibility(LinearLayout.INVISIBLE);
                    _btnSelectThisClient.setVisibility(Button.INVISIBLE);

                    makeToast("No client found");
                } else {
                    mFetch[0].moveToFirst();
                    _btnPrevious.setVisibility(ImageButton.INVISIBLE);
                    _btnFirstPage.setVisibility(ImageButton.INVISIBLE);
                    _currentClientIndex = Integer.parseInt(mFetch[0].getString(0));
                    makeToast(sqlCount + " Client(s) found");
                    _edtTxtSelectedClientName.setText(mFetch[0].getString(1));
                    _edtTxtSelectedClientAddress.setText(mFetch[0].getString(2));
                    _edtTxtSelectedClientTel.setText(mFetch[0].getString(3));
                    _edtTxtSelectedClientFax.setText(mFetch[0].getString(4));
                    _edtTxtSelectedClientContact.setText(mFetch[0].getString(5));
                    _edtTxtSelectedClientTelContact.setText(mFetch[0].getString(6));

                    if (sqlCount == 1) {
                        _layoutSelectNavButtons.setVisibility(LinearLayout.INVISIBLE);
                        _btnSelectThisClient.setVisibility(Button.VISIBLE);

                    } else if (sqlCount > 1) {
                        _layoutSelectNavButtons.setVisibility(LinearLayout.VISIBLE);
                        _btnSelectThisClient.setVisibility(Button.VISIBLE);
                    } else {
                        makeToast("Error");
                        _layoutSelectNavButtons.setVisibility(LinearLayout.INVISIBLE);
                        finish();
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
                    if (sqlCount >= 1) {
                        _txtViewHeader.setText("Select a client");

                        _btnSelectThisClient.setEnabled(true);
                        _btnSelectThisClient.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // pass the selected client to the next activity
                                Intent intent = new Intent(getApplicationContext(), Contract.class);

                                intent.putExtra("clientName", _edtTxtSelectedClientName.getText().toString());
                                intent.putExtra("clientAddress", _edtTxtSelectedClientAddress.getText().toString());
                                intent.putExtra("clientTel", _edtTxtSelectedClientTel.getText().toString());
                                intent.putExtra("clientFax", _edtTxtSelectedClientFax.getText().toString());
                                intent.putExtra("clientContact", _edtTxtSelectedClientContact.getText().toString());
                                intent.putExtra("clientTelContact", _edtTxtSelectedClientTelContact.getText().toString());
                                intent.putExtra("clientIndex", _currentClientIndex);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                    } else {
                        _btnSelectThisClient.setVisibility(View.INVISIBLE);
                    }
                }

            }
        });
    }
}