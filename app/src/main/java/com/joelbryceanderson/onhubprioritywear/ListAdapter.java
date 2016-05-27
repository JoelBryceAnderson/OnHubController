package com.joelbryceanderson.onhubprioritywear;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.joelbryceanderson.onhubprioritywear.ListItem;
import com.joelbryceanderson.onhubprioritywear.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JAnderson on 5/14/16.
 */
public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private Context mContext;
    private List<ListItem> groupList;
    private MainActivity parent;
    private List<RecyclerView.ViewHolder> viewHolderList = new ArrayList<>();

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        protected TextView mTextView;
        protected Switch mSwitch;
        protected LinearLayout mLinearLayout;
        protected ImageView mImageView;
        public ViewHolderItem(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.group_name);
            mSwitch = (Switch) v.findViewById(R.id.group_toggle);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.whole_item_group);
            mImageView = (ImageView) v.findViewById(R.id.image_view_group);
        }
    }

    public static class ViewHolderHeader extends RecyclerView.ViewHolder {
        protected RelativeLayout wholeHeader;
        public ViewHolderHeader(View v) {
            super(v);
            wholeHeader = (RelativeLayout) v.findViewById(R.id.whole_header);
        }
    }

    public static class ViewHolderFooter extends RecyclerView.ViewHolder {
        protected RelativeLayout wholeFooter;
        public ViewHolderFooter(View v) {
            super(v);
            wholeFooter = (RelativeLayout) v.findViewById(R.id.whole_footer);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListAdapter(List<ListItem> myDataset, MainActivity parent) {
        groupList = myDataset;
        this.parent = parent;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            mContext = parent.getContext();
            ViewHolderItem toReturn = new ViewHolderItem(v);
            viewHolderList.add(toReturn);
            return toReturn;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_header, parent, false);
            mContext = parent.getContext();
            ViewHolderHeader toReturn = new ViewHolderHeader(v);
            viewHolderList.add(toReturn);
            return toReturn;
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_footer, parent, false);
            mContext = parent.getContext();
            ViewHolderFooter toReturn = new ViewHolderFooter(v);
            viewHolderList.add(toReturn);
            return toReturn;
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (holder instanceof ViewHolderItem) {
            final ListItem thisItem = groupList.get(position);
            final ViewHolderItem VHitem = (ViewHolderItem) holder;
            VHitem.mTextView.setText(thisItem.getName());
            VHitem.mImageView.setImageDrawable(thisItem.getIcon());
            VHitem.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        VHitem.mSwitch.setClickable(false);
                    }
                }
            });
            VHitem.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!VHitem.mSwitch.isChecked()) {
                        toggleOff();
                        VHitem.mSwitch.toggle();
                        parent.turnPriorityOn(position);
                    }
                }
            });
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return groupList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public void toggleOff() {
        for (RecyclerView.ViewHolder holder : viewHolderList) {
            if (holder.getItemViewType() == TYPE_ITEM) {
                ViewHolderItem VHItem = (ViewHolderItem) holder;
                VHItem.mSwitch.setChecked(false);
            }
        }
    }
}

