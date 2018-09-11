package org.openobservatory.ooniprobe.item;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.openobservatory.ooniprobe.R;
import org.openobservatory.ooniprobe.model.Network;
import org.openobservatory.ooniprobe.model.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import localhost.toolkit.widget.HeterogeneousRecyclerItem;

public class MiddleboxesItem extends HeterogeneousRecyclerItem<Result, MiddleboxesItem.ViewHolder> {
	private View.OnClickListener onClickListener;
	private View.OnLongClickListener onLongClickListener;

	public MiddleboxesItem(Result extra, View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
		super(extra);
		this.onClickListener = onClickListener;
		this.onLongClickListener = onLongClickListener;
	}

	@Override public ViewHolder onCreateViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
		return new ViewHolder(layoutInflater.inflate(R.layout.item_middleboxes, viewGroup, false));
	}

	@Override public void onBindViewHolder(ViewHolder viewHolder) {
		viewHolder.itemView.setTag(extra);
		viewHolder.itemView.setOnClickListener(onClickListener);
		viewHolder.itemView.setOnLongClickListener(onLongClickListener);
		viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), extra.is_viewed ? android.R.color.transparent : R.color.color_yellow0));
		viewHolder.asnName.setText(Network.getAsnName(viewHolder.asnName.getContext(), extra.network));
		viewHolder.startTime.setText(DateFormat.getDateFormat(viewHolder.startTime.getContext()).format(extra.start_time));
		if (extra.countMeasurement(true, null) > 0) {
			viewHolder.status.setText(R.string.TestResults_Overview_MiddleBoxes_Found);
			viewHolder.status.setTextColor(ContextCompat.getColor(viewHolder.status.getContext(), R.color.color_yellow8)); // TODO
		} else if (extra.countMeasurement(null, false) == 0) {
			viewHolder.status.setText(R.string.TestResults_Overview_MiddleBoxes_Failed);
			viewHolder.status.setTextColor(ContextCompat.getColor(viewHolder.status.getContext(), R.color.color_yellow8)); // TODO
		} else {
			viewHolder.status.setText(R.string.TestResults_Overview_MiddleBoxes_NotFound);
			viewHolder.status.setTextColor(ContextCompat.getColor(viewHolder.status.getContext(), R.color.color_yellow8)); // TODO
		}
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.asnName) TextView asnName;
		@BindView(R.id.startTime) TextView startTime;
		@BindView(R.id.status) TextView status;

		ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
