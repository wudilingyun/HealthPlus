package com.vee.healthplus.util.user;


import android.app.Activity;
import android.os.AsyncTask;

import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;

/**
 * Created by wangjiafeng on 13-11-14.
 */
public class SaveProfileTask extends
        AsyncTask<Void, Void, GeneralResponse> {
    private MultiValueMap<String, String> formData;
    private Exception exception;
    private Activity activity;
    private HP_User user;
    private SaveProfileCallBack callBack;

    public SaveProfileTask(Activity activity, HP_User user, SaveProfileCallBack callBack) {
        this.activity = activity;
        this.user = user;
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {
        formData = new LinkedMultiValueMap<String, String>();

        String username = user.userName;
        formData.add("username", username);

        String nickname = user.userNick;
        formData.add("nickname", nickname);

        String email = user.email;
        formData.add("email", email);

        String phone = user.phone;
        formData.add("phone", phone);

        String weight = String.valueOf(user.userWeight);
        formData.add("weight", weight);

        String height = String.valueOf(user.userHeight);
        formData.add("height", height);

        String age = String.valueOf(user.userAge);
        formData.add("age", age);

        String remark = user.remark;
        formData.add("remark", remark);

        String gender = String.valueOf(user.userSex);
        formData.add("gender", gender);
    }

    @Override
    protected GeneralResponse doInBackground(Void... params) {
        // TODO Auto-generated method stub
        try {
            GeneralResponse generalResponse = SpringAndroidService
                    .getInstance(activity.getApplication()).saveProfile(formData);
            return generalResponse;
        } catch (Exception e) {
            this.exception = e;
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(GeneralResponse generalResponse) {
        if (exception != null) {
            String message;

            if (exception instanceof DuplicateConnectionException) {
                message = "The connection already exists.";
            } else if (exception instanceof ResourceAccessException && exception.getCause() instanceof ConnectTimeoutException) {
                message = "connect time out";
            } else {
                message = "A problem occurred with the network connection. Please try again in a few minutes.";
            }
            callBack.onErrorSaveProfile(exception);
        }

        if (generalResponse != null) {
            callBack.onFinishSaveProfile(generalResponse.getReturncode());
        }
    }

    public interface SaveProfileCallBack {
        public void onFinishSaveProfile(int reflag);

        public void onErrorSaveProfile(Exception e);
    }

}
