package io.github.xausky.bh3modmanager.dialog;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import io.github.xausky.bh3modmanager.R;
import io.github.xausky.bh3modmanager.adapter.ApplicationsAdapter;
import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.data.ExFilePickerResult;

/**
 * Created by xausky on 18-3-6.
 */

public class ApplicationChooseDialog extends AlertDialog implements DialogInterface.OnClickListener, AdapterView.OnItemClickListener{
    private static final int APPLICATION_FILE_PICKER_RESULT = 848848;
    private Fragment fragment;
    private OnApplicationChooseDialogResultListener listener;
    public ApplicationChooseDialog(Context context, final Fragment fragment, String packageRegex) {
        super(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.choose_client_dialog, null);
        ListView listView = dialogView.findViewById(R.id.choose_client_dialog_clients);
        listView.setAdapter(new ApplicationsAdapter(context, packageRegex));
        this.setTitle("请选择客户端");
        this.setView(dialogView);
        this.setButton(DialogInterface.BUTTON_POSITIVE, "浏览", this);
        this.fragment = fragment;
        listView.setOnItemClickListener(this);
    }



    public OnApplicationChooseDialogResultListener getListener() {
        return listener;
    }

    public void setListener(OnApplicationChooseDialogResultListener listener) {
        this.listener = listener;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == APPLICATION_FILE_PICKER_RESULT){
            ExFilePickerResult result = ExFilePickerResult.getFromIntent(data);
            if(result!=null){
                if(listener!=null){
                    listener.OnApplicationChooseDialogResult(null, result.getPath() + result.getNames().get(0));
                }
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        ExFilePicker filePicker = new ExFilePicker();
        filePicker.setShowOnlyExtensions("apk");
        filePicker.setCanChooseOnlyOneItem(true);
        filePicker.start(fragment, APPLICATION_FILE_PICKER_RESULT);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ApplicationInfo info = (ApplicationInfo) parent.getAdapter().getItem(position);
        if(listener!=null){
            listener.OnApplicationChooseDialogResult(info.packageName,info.sourceDir);
        }
    }

    public interface OnApplicationChooseDialogResultListener {
        void OnApplicationChooseDialogResult(String packageName, String apkPath);
    }
}