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

package com.feasycom.feasymesh.node.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.feasycom.feasymesh.R;
import com.feasycom.feasymesh.library.transport.Element;
import com.feasycom.feasymesh.library.transport.ProvisionedMeshNode;
import com.feasycom.feasymesh.library.utils.CompanyIdentifiers;
import com.feasycom.feasymesh.library.utils.MeshAddress;
import com.feasycom.feasymesh.library.utils.MeshParserUtils;
import com.feasycom.feasymesh.light.LightActivity;
import com.feasycom.feasymesh.widgets.RemovableViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.ViewHolder> {
    private static final String TAG = "NodeAdapter";
    private final Context mContext;
    private final List<ProvisionedMeshNode> mNodes = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public NodeAdapter(@NonNull final Context context,
                       @NonNull final LiveData<List<ProvisionedMeshNode>> provisionedNodesLiveData, ProvisionedMeshNode phoneProvisionedNode) {
        this.mContext = context;
        provisionedNodesLiveData.observe((LifecycleOwner) context, nodes -> {
            if (nodes != null) {
                mNodes.clear();
                mNodes.addAll(nodes);
                mNodes.add(phoneProvisionedNode);
                notifyDataSetChanged();
            }
        });
    }

    public void setOnItemClickListener(@NonNull final OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View layoutView = LayoutInflater.from(mContext).inflate(R.layout.network_item, parent, false);
        return new NodeAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ProvisionedMeshNode node = mNodes.get(position);
        if (node != null) {
            holder.name.setText(node.getNodeName());
            holder.unicastAddress.setText(MeshParserUtils.bytesToHex(MeshAddress.addressIntToBytes(node.getUnicastAddress()), false));
            final Map<Integer, Element> elements = node.getElements();
            if (!elements.isEmpty()) {
                holder.nodeInfoContainer.setVisibility(View.VISIBLE);
                if (node.getCompanyIdentifier() != null) {
                    holder.companyIdentifier.setText(CompanyIdentifiers.getCompanyName(node.getCompanyIdentifier().shortValue()));
                } else {
                    holder.companyIdentifier.setText(R.string.unknown);
                }
                holder.elements.setText(String.valueOf(elements.size()));
                holder.models.setText(String.valueOf(getModels(elements)));
            } else {
                holder.companyIdentifier.setText(R.string.unknown);
                holder.elements.setText(String.valueOf(node.getNumberOfElements()));
                holder.models.setText(R.string.unknown);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mNodes.size();
    }

    public ProvisionedMeshNode getItem(final int position) {
        if (mNodes.size() > 0 && position > -1) {
            return mNodes.get(position);
        }
        return null;
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    private int getModels(final Map<Integer, Element> elements) {
        int models = 0;
        for (Element element : elements.values()) {
            models += element.getMeshModels().size();
        }
        return models;
    }

    @FunctionalInterface
    public interface OnItemClickListener {
        void onConfigureClicked(final ProvisionedMeshNode node);
    }

    // final class ViewHolder extends RecyclerView.ViewHolder {
    final class ViewHolder extends RemovableViewHolder {

        @BindView(R.id.container)
        ConstraintLayout container;
        @BindView(R.id.node_name)
        TextView name;
        @BindView(R.id.configured_node_info_container)
        View nodeInfoContainer;
        @BindView(R.id.unicast)
        TextView unicastAddress;
        @BindView(R.id.company_identifier)
        TextView companyIdentifier;
        @BindView(R.id.elements)
        TextView elements;
        @BindView(R.id.models)
        TextView models;
        @BindView(R.id.light)
        ImageButton light;
        @BindView(R.id.list)
        ImageButton list;

        private ViewHolder(final View provisionedView) {
            super(provisionedView);
            ButterKnife.bind(this, provisionedView);
            container.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onConfigureClicked(mNodes.get(getAdapterPosition()));
                }
            });
            light.setOnClickListener(v->{
                Intent intent = new Intent(mContext, LightActivity.class);
                mContext.startActivity(intent);
            });
            list.setOnClickListener(view -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onConfigureClicked(mNodes.get(getAdapterPosition()));
                }
            });
        }
    }
}
