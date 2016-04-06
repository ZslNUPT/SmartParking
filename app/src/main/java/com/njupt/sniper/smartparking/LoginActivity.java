package com.njupt.sniper.smartparking;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * author：Zsl
 * date：2016/4/6
 */
public class LoginActivity extends AccountAuthenticatorActivity {
    private String mUsername;
    private String mPassword;
    private boolean mRequestNewAccount;
    private AccountManager mAccountManager;

    private EditText username;
    private EditText password;
    private TextView btnLogin;

    private String accountType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btnLogin = (TextView) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = username.getText().toString();
                mPassword = password.getText().toString();
                mRequestNewAccount = true;
                finishLogin();
            }
        });
        mAccountManager = AccountManager.get(this);
        accountType=getResources().getString(R.string.account_type);
    }

    private void finishLogin() {

        final Account account = new Account(mUsername,accountType );
      //  final Account[] accounts = mAccountManager.getAccountsByType(accountType);


        if (mRequestNewAccount) {
            mAccountManager.addAccountExplicitly(account, mPassword, null);
            // Set contacts sync for this account.
            //  ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
        } else {
            mAccountManager.setPassword(account, mPassword);
        }
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mUsername);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, SyncStateContract.Constants.ACCOUNT_TYPE);
        setAccountAuthenticatorResult(intent.getExtras());
        //     setResult(RESULT_OK, intent);
        finish();
    }
}
