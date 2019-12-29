package com.trioangle.goferdriver.facebookAccountKit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.gson.Gson;
import com.trioangle.goferdriver.R;
import com.trioangle.goferdriver.configs.SessionManager;
import com.trioangle.goferdriver.datamodel.NumberValidationModel;
import com.trioangle.goferdriver.helper.CustomDialog;
import com.trioangle.goferdriver.interfaces.ApiService;
import com.trioangle.goferdriver.interfaces.ServiceListener;
import com.trioangle.goferdriver.model.JsonResponse;
import com.trioangle.goferdriver.network.AppController;
import com.trioangle.goferdriver.util.CommonKeys;
import com.trioangle.goferdriver.util.CommonMethods;
import com.trioangle.goferdriver.util.RequestCallback;

import javax.inject.Inject;

import static com.trioangle.goferdriver.util.CommonKeys.FACEBOOK_ACCOUNT_KIT_MESSAGE_KEY;
import static com.trioangle.goferdriver.util.CommonKeys.FACEBOOK_ACCOUNT_KIT_PHONE_NUMBER_COUNTRY_CODE_KEY;
import static com.trioangle.goferdriver.util.CommonKeys.FACEBOOK_ACCOUNT_KIT_PHONE_NUMBER_KEY;

public class FacebookAccountKitActivity extends AppCompatActivity implements ServiceListener {

    public @Inject
    SessionManager sessionManager;

    public @Inject
    CommonMethods commonMethods;

    public @Inject
    ApiService apiService;

    public @Inject
    Gson gson;

    public @Inject
    CustomDialog customDialog;


    public AlertDialog dialog;

    public final int FACEBOOK_ACCOUNTKIT_REQUEST_CODE = 157;

    String facebookVerifiedPhoneNumber, facebookVerifiedCountryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppController.getAppComponent().inject(this);

        startCallingFacebookKit();
    }

    public static void openFacebookAccountKitActivity(Activity activity) {
        Intent facebookIntent = new Intent(activity, FacebookAccountKitActivity.class);
        activity.startActivityForResult(facebookIntent, CommonKeys.ACTIVITY_REQUEST_CODE_START_FACEBOOK_ACCOUNT_KIT);
    }


    private void startCallingFacebookKit() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);
        // PhoneNumber phoneNumber = new PhoneNumber(sessionManager.getTemporaryCountryCode(), sessionManager.getTemporaryPhonenumber());
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, FACEBOOK_ACCOUNTKIT_REQUEST_CODE);
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FACEBOOK_ACCOUNTKIT_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null || loginResult.wasCancelled()) {
                showErrorMessageAndCloseActivity();

            } else {
                getPhoneNumber();
            }
        }
    }

    public void getPhoneNumber() {
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                String phoneNumbers, countryCode, phoneNumberWihtoutPlusSign, temporaryPhoneNumber;

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                phoneNumbers = phoneNumber.getPhoneNumber().toString();
                phoneNumberWihtoutPlusSign = phoneNumbers.replace("+", "");
                countryCode = phoneNumber.getCountryCode();
                callPhoneNumberValidationAPI(phoneNumberWihtoutPlusSign, countryCode);
            }

            @Override
            public void onError(final AccountKitError error) {
                showErrorMessageAndCloseActivity();

                // Handle Error
            }
        });
    }

    /*public void phoneNumberChangedErrorMessage() {
        //commonMethods.showMessage(this, dialog, getString(R.string.InvalidMobileNumber));
        Toast.makeText(this, getString(R.string.InvalidMobileNumber), Toast.LENGTH_SHORT).show();
    }
*/

    /*void facebookAccountKitNumberVerificationSuccess() {
        setResult(ApiSessionAppConstants.FACEBOOK_ACCOUNT_KIT_VERIFACATION_SUCCESS);
        finish();
    }

    void facebookAccountKitNumberVerificationFailure() {
        setResult(ApiSessionAppConstants.FACEBOOK_ACCOUNT_KIT_VERIFACATION_FAILURE);
        finish();
    }*/

    // api call
    void callPhoneNumberValidationAPI(String facebookVerifiedPhoneNumber, String facebookVerifiedCountryCode) {
        this.facebookVerifiedCountryCode = facebookVerifiedCountryCode;
        this.facebookVerifiedPhoneNumber = facebookVerifiedPhoneNumber;
        commonMethods.showProgressDialog(this, customDialog);
        apiService.numberValidation(sessionManager.getType(), facebookVerifiedPhoneNumber, facebookVerifiedCountryCode, "", sessionManager.getLanguageCode()).enqueue(new RequestCallback(this));
    }


    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();

        Intent returnIntent = new Intent();
        returnIntent.putExtra(FACEBOOK_ACCOUNT_KIT_PHONE_NUMBER_KEY, facebookVerifiedPhoneNumber);
        returnIntent.putExtra(FACEBOOK_ACCOUNT_KIT_PHONE_NUMBER_COUNTRY_CODE_KEY, facebookVerifiedCountryCode);

        NumberValidationModel numberValidationModel = gson.fromJson(jsonResp.getStrResponse(), NumberValidationModel.class);

        if (numberValidationModel.getStatusCode().equals(CommonKeys.NUMBER_VALIDATION_API_RESULT_OLD_USER)) {

            returnIntent.putExtra(FACEBOOK_ACCOUNT_KIT_MESSAGE_KEY, numberValidationModel.getStatusMessage());
            setResult(CommonKeys.FACEBOOK_ACCOUNT_KIT_RESULT_OLD_USER, returnIntent);
            finish();

        } else if (numberValidationModel.getStatusCode().equals(CommonKeys.NUMBER_VALIDATION_API_RESULT_NEW_USER)) {

            returnIntent.putExtra(FACEBOOK_ACCOUNT_KIT_MESSAGE_KEY, numberValidationModel.getStatusMessage());
            setResult(CommonKeys.FACEBOOK_ACCOUNT_KIT_RESULT_NEW_USER, returnIntent);
            finish();
        } else {
            CommonMethods.DebuggableLogI(numberValidationModel.getStatusCode(), numberValidationModel.getStatusMessage());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        showErrorMessageAndCloseActivity();
    }

    private void showErrorMessageAndCloseActivity() {
        CommonMethods.showServerInternalErrorMessage(this);
        finish();
    }
}
