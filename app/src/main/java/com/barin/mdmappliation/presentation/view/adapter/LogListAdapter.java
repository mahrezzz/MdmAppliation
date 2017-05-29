package com.barin.mdmappliation.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import com.barin.mdmappliation.R;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.presentation.di.scope.PerActivity;
import com.barin.mdmappliation.presentation.model.AppLog;
import rx.Observable;


@PerActivity public class LogListAdapter
    extends RecyclerView.Adapter<LogListAdapter.LogViewHolder> {

  public interface OnItemClickListener {
    void onLogItemClicked(AppLog logInfo);
  }

  private List<AppLog> logCollection;
  private final LayoutInflater layoutInflater;
  private OnItemClickListener onItemClickListener;

  @Inject public LogListAdapter(Context context) {
    this.layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.logCollection = new ArrayList<>();
  }

  @Override public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    final View view = this.layoutInflater.inflate(R.layout.log_row, parent, false);
    return new LogViewHolder(view);
  }

  @Override public void onBindViewHolder(LogViewHolder holder, int position) {

    final AppLog logInfo = this.logCollection.get(position);


    holder.textViewLogType.setText(logInfo.getmType().type.trim());
    holder.textViewCreatedDate.setText(logInfo.getmDate().trim());
    holder.textViewLogMesssage.setText(logInfo.getMessage().trim());

    holder.itemView.setOnClickListener(click -> {
      if (null != LogListAdapter.this.onItemClickListener) {
        onItemClickListener.onLogItemClicked(logInfo);
      }
    });
  }

  @Override public int getItemCount() {
    return (logCollection != null) ? this.logCollection.size() : 0;
  }

  static class LogViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.txtLogType) TextView textViewLogType;
    @Bind(R.id.txtLogMessage) TextView textViewLogMesssage;
    @Bind(R.id.txtCreatedDate) TextView textViewCreatedDate;

    public LogViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  public void setLogCollection(List<AppLog> logs) {
    AppUtil.checkForNull(logs, "@logList adapter --> log info collection==null");
    this.logCollection.clear();
    Observable.from(logs).forEach(logCollection::add);
    this.notifyDataSetChanged();
  }
}


