/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.feasycom.feasymesh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.feasycom.feasymesh.R;
import com.feasycom.feasymesh.library.Group;
import com.feasycom.feasymesh.library.MeshNetwork;
import com.feasycom.feasymesh.library.transport.MeshModel;
import com.feasycom.feasymesh.library.utils.MeshAddress;
import com.feasycom.feasymesh.widgets.RemovableViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private final ArrayList<Group> mGroups = new ArrayList<>();
    private final Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private MeshNetwork mNetwork;

    public GroupAdapter(@NonNull final Context context) {
        this.mContext = context;
    }

    public void updateAdapter(@NonNull final MeshNetwork meshNetwork, @NonNull final List<Group> groups) {
        mNetwork = meshNetwork;
        mGroups.clear();
        mGroups.addAll(groups);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(@NonNull final OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @NonNull
    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View layoutView = LayoutInflater.from(mContext).inflate(R.layout.group_item, parent, false);
        return new GroupAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupAdapter.ViewHolder holder, final int position) {
        if (mNetwork != null && mGroups.size() > 0) {
            final Group group = mGroups.get(position);
            if (group != null) {
                final List<MeshModel> models = mNetwork.getModels(group);
                holder.groupName.setText(group.getName());
                holder.groupAddress.setText(mContext.
                        getString(R.string.group_address_summary, MeshAddress.formatAddress(group.getAddress(), true)));
                holder.groupDeviceCount.setText(mContext.getResources().
                        getQuantityString(R.plurals.device_count, models.size(), models.size()));
            }
        }
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    /**
     * Returns the number of models associated to the group in a particular position
     *
     * @param position position
     */
    public int getModelCount(final int position) {
        if (position >= 0 && !mGroups.isEmpty() && position < mGroups.size()) {
            final Group group = mGroups.get(position);
            return mNetwork.getModels(group).size();
        }
        return 0;
    }

    @FunctionalInterface
    public interface OnItemClickListener {
        void onItemClick(final int address);
    }

    public final class ViewHolder extends RemovableViewHolder {
        @BindView(R.id.group_name)
        TextView groupName;
        @BindView(R.id.group_address)
        TextView groupAddress;
        @BindView(R.id.group_device_count)
        TextView groupDeviceCount;

        private ViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.findViewById(R.id.container).setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mGroups.get(getAdapterPosition()).getAddress());
                }
            });
        }
    }
}
