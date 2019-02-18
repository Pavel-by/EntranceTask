package com.example.entrancetask;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Contact {

    public interface OnChangeListener {
        void OnChange(Contact con);
    }

    public static final int GENDER_MALE   = 1;
    public static final int GENDER_FEMALE = 2;

    private Bitmap                          icon;
    private String                          iconURL;
    private int                             gender;
    private String                          firstName = "";
    private String                          lastName = "";
    private String                          fullName = "";
    private List<OnChangeListener>          listeners = new ArrayList<>();
    private WebHelper.ImageDownloadListener imageDownloadListener = new WebHelper.ImageDownloadListener() {
        @Override
        public void onLoadingComplete(Bitmap bitmap) {
            if (bitmap != icon) {
                icon = bitmap;
                notifyChanged();
            }
            isUpdatingImage = false;
        }

        @Override
        public void onLoadingFailed() {
            isUpdatingImage = false;
        }
    };
    private boolean isUpdatingImage = false;

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
        notifyChanged();
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
        notifyChanged();
    }

    public void setGender(String gender) {
        if (gender.equals("male"))
            this.gender = GENDER_MALE;
        else
            this.gender = GENDER_FEMALE;
        updateFullName();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
        updateFullName();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
        updateFullName();
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        boolean changed = !iconURL.equals(this.iconURL);
        this.iconURL = iconURL;
        if (changed) notifyChanged();
    }

    public String getFullName() {
        return fullName;
    }

    public void addListener(OnChangeListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(OnChangeListener listener) {
        this.listeners.remove(listener);
    }

    public void updateIconBitmap() {
        if (isUpdatingImage) return;
        isUpdatingImage = true;
        WebHelper.getBitmapFromURL(iconURL, imageDownloadListener);
    }

    private void updateFullName() {
        this.fullName = (getGender() == Contact.GENDER_MALE ? "mr " : "ms ") +
                        getFirstName() + " " +
                        getLastName();
        notifyChanged();
    }

    private void notifyChanged() {
        for (OnChangeListener listener : listeners) {
            listener.OnChange(this);
        }
    }
}
