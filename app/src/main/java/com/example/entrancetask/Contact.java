package com.example.entrancetask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class Contact implements Target {

    public interface OnChangeListener {
        void OnChange(Contact con);
    }

    public static final int GENDER_MALE   = 1;
    public static final int GENDER_FEMALE = 2;

    private Drawable         icon;
    private String                 iconURL;
    private int                    gender;
    private String                 firstName = "";
    private String                 lastName = "";
    private String                 fullName = "";
    private List<OnChangeListener> listeners = new ArrayList<>();

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(BitmapDrawable icon) {
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

    @Override
    public void onBitmapLoaded(
            Bitmap bitmap,
            Picasso.LoadedFrom from
    )
    {
        icon = new BitmapDrawable(App.getContext().getResources(), bitmap);
        notifyChanged();
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        icon = errorDrawable;
        notifyChanged();
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        icon = placeHolderDrawable;
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
