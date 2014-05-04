package com.vee.healthplus.util.user;


import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;

import android.app.Activity;
import android.os.AsyncTask;

import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.s4aservicetest.response.UploadAvatarResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

/**
 * Created by wangjiafeng on 13-11-14.
 */
public class SaveProfileTask extends
        AsyncTask<Void, Void, Object[]> {
    private MultiValueMap<String, String> formData;
    private Exception exception;
    private Activity activity;
    private HP_User user;
    private SaveProfileCallBack callBack;
    private String hdpath;

    public SaveProfileTask(Activity activity, HP_User user, SaveProfileCallBack callBack,String hdpath) {
        this.activity = activity;
        this.user = user;
        this.callBack = callBack;
        this.hdpath=hdpath;
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
    protected Object[] doInBackground(Void... params) {
        // TODO Auto-generated method stub
        try {
            GeneralResponse generalResponse = SpringAndroidService
                    .getInstance(activity.getApplication()).saveProfile(formData);
            UploadAvatarResponse  uploadAvatarResponse=SpringAndroidService
                    .getInstance(activity.getApplication()).uploadAvatar(hdpath);
            return new Object[]{generalResponse,uploadAvatarResponse};
        } catch (Exception e) {
            this.exception = e;
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object[] obs) {
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

        if (obs[0] != null) {
            callBack.onFinishSaveProfile(((GeneralResponse)obs[0]).getReturncode());
        }
    }

    public interface SaveProfileCallBack {
        public void onFinishSaveProfile(int reflag);

        public void onErrorSaveProfile(Exception e);
    }

}
