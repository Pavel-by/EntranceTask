package com.example.entrancetask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RVAContacts extends RecyclerView.Adapter<RVAContacts.ContactViewHolder> {

    private final static int CONTACT = 1;
    private final static int INFO = 2;

    private ArrayList<Contact> contacts = new ArrayList<>();
    private Activity activity;
    private boolean isLoadingEnabled = true;
    private String messageText = null;
    private String buttonText = null;
    private View.OnClickListener buttonListener = null;

    private Contact.OnChangeListener contactListener = new Contact.OnChangeListener() {
        @Override
        public void OnChange(Contact con) {
            for (int i = 0; i < contacts.size(); i++) {
                if (con.equals(contacts.get(i))) {
                    notifyItemChanged(i);
                    Log.e("RVAContacts", "Changed on " + i);
                    break;
                }
            }
        }
    };

    public RVAContacts(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        return position == contacts.size() ? INFO : CONTACT;
    }

    @NonNull
    @Override
    public RVAContacts.ContactViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup,
            int i
    )
    {
        switch (i) {
            case INFO: {
                return new ContactViewHolder(activity.getLayoutInflater().inflate(R.layout.list_item_info, viewGroup, false), i);
            }
            default: {
                return new ContactViewHolder(activity.getLayoutInflater().inflate(R.layout.list_item_contact, viewGroup, false), i);
            }
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull RVAContacts.ContactViewHolder holder,
            int i
    )
    {
        switch (getItemViewType(i)) {
            case CONTACT: {
                Contact con = contacts.get(i);
                holder.image.setGender(con.getGender());
                holder.image.setImageBitmap(con.getIcon());
                if (con.getIcon() == null) con.updateIconBitmap();
                holder.text.setText(con.getFullName());
                break;
            }
            case INFO: {
                if (isLoadingEnabled) {
                    holder.infoMessage.setVisibility(View.GONE);
                    holder.infoButton.setVisibility(View.GONE);
                    holder.infoProgressBar.setVisibility(View.VISIBLE);
                } else {
                    if (messageText != null)
                        holder.infoMessage.setText(messageText);
                    holder.infoMessage.setVisibility(messageText != null ? View.VISIBLE : View.GONE);
                    if (buttonText != null && buttonListener != null) {
                        holder.infoButton.setText(buttonText);
                        holder.infoButton.setOnClickListener(buttonListener);
                        holder.infoButton.setVisibility(View.VISIBLE);
                    } else
                        holder.infoButton.setVisibility(View.GONE);
                    holder.infoProgressBar.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size() + 1;
    }

    public void addAll(Collection<Contact> contacts) {
        for (Contact con : contacts) {
            con.addListener(contactListener);
        }
        this.contacts.addAll(contacts);
        if (contacts.size() == this.contacts.size()) notifyDataSetChanged();
        else notifyItemRangeInserted(this.contacts.size() - contacts.size(), contacts.size());
    }

    public void clear() {
        for (Contact con : contacts) {
            con.removeListener(contactListener);
        }
        this.contacts.clear();
        notifyDataSetChanged();
    }

    public void setLoadingEnabled(boolean isLoadingEnabled) {
        if (this.isLoadingEnabled != isLoadingEnabled){
            this.isLoadingEnabled = isLoadingEnabled;
            notifyItemChanged(contacts.size());
        }
    }

    public boolean isLoadingEnabled() {
        return isLoadingEnabled;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        if (messageText == null && this.messageText != null
                          || messageText != null && !messageText.equals(this.messageText))
        {
            this.messageText = messageText;
            notifyItemChanged(contacts.size());
        }
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        if (buttonText == null && this.buttonText != null
                          || buttonText != null && !buttonText.equals(this.buttonText))
        {
            this.buttonText = buttonText;
            notifyItemChanged(contacts.size());
        }
    }

    public View.OnClickListener getButtonListener() {
        return buttonListener;
    }

    public void setButtonListener(View.OnClickListener buttonListener) {
        if (buttonListener != this.buttonListener) {
            this.buttonListener = buttonListener;
            notifyItemChanged(contacts.size());
        }
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        private AvatarImageView image;
        private TextView text;

        private ProgressBar infoProgressBar;
        private TextView    infoMessage;
        private Button      infoButton;

        public ContactViewHolder(@NonNull View itemView, int itemViewType) {
            super(itemView);
            switch (itemViewType) {
                case CONTACT: {
                    this.image = itemView.findViewById(R.id.image);
                    this.text = itemView.findViewById(R.id.text);
                    break;
                }
                case INFO: {
                    this.infoProgressBar = itemView.findViewById(R.id.progressBar);
                    this.infoMessage = itemView.findViewById(R.id.message);
                    this.infoButton = itemView.findViewById(R.id.button);
                    break;
                }
            }
        }
    }
}
