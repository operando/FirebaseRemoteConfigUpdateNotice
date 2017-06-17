package com.os.operando.updatenotice.sample;

import com.annimon.stream.Optional;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateNotice {

    public int updateApplicationVersion;
    public boolean isShowUpdateNotice;
    public String updateTitle;
    public String updateMessage;

    public static Optional<UpdateNotice> parseJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            UpdateNotice updateNotice = new UpdateNotice();
            updateNotice.updateApplicationVersion = jsonObject.getInt("update_application_version");
            updateNotice.isShowUpdateNotice = jsonObject.getBoolean("is_show_update_notice");
            updateNotice.updateTitle = jsonObject.getString("update_title");
            updateNotice.updateMessage = jsonObject.getString("update_message");

            return Optional.of(updateNotice);
        } catch (JSONException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // {"update_application_version":2,"is_show_update_notice":true,"update_title":"title","update_message":"message"}
}