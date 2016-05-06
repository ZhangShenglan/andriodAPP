package com.app.core.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.app.core.utils.CustomDialog;
import com.app.core.utils.SingleToast;

/**
 * 基础Fragement
 * 封装了对话框和弹出层
 * 封装了findViewById
 */
public class BaseFragment extends Fragment {

    protected LayoutInflater mInflater;
    private ProgressDialog mProgressDialog;
    private  SingleToast mSingleToast;
    protected void showProgressBar(boolean show) {
        showProgressBar(show, "");
    }

    protected void setProgressBarProgress() {
        if (mProgressDialog == null) {
            return;
        }

        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgress(30);
    }

    protected void showProgressBar(boolean show, String message) {
        if (mProgressDialog == null) {
            return;
        }

        if (show) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } else {
            mProgressDialog.hide();
        }
    }

    public AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    protected void showProgressBar(int messageId) {
        String message = getString(messageId);
        showProgressBar(true, message);
    }

    protected boolean progressBarIsShowing() {
        return mProgressDialog.isShowing();
    }



    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        mInflater = LayoutInflater.from(getActivity());

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);

        mSingleToast = new SingleToast(getActivity());

    }

    @Override
    public void onDestroy() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

        super.onDestroy();
    }



    protected void showDialog(String title, String msg, DialogInterface.OnClickListener clickOk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", clickOk)
                .setNegativeButton("取消", null)
                .show();
        CustomDialog.dialogTitleLineColor(getActivity(), dialog);
    }

    protected void showDialog(String[] titles, DialogInterface.OnClickListener clickOk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(titles, clickOk).show();
    }




    public void showButtomToast(String msg) {
        if (!isResumed() || mSingleToast == null) {
            return;
        }

        mSingleToast.showButtomToast(msg);
    }

    public void showMiddleToast(int id) {
        if (!isResumed() || mSingleToast == null) {
            return;
        }
        mSingleToast.showMiddleToast(id);
    }

    public void showMiddleToast(String msg) {
        if (!isResumed() || mSingleToast == null) {
            return;
        }
        mSingleToast.showMiddleToast(msg);
    }

    public void showButtomToast(int messageId) {
        if (!isResumed() || mSingleToast == null) {
            return;
        }

        mSingleToast.showButtomToast(messageId);
    }



    protected void showDialogLoading() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showDialogLoading();
        }
    }

    protected void hideProgressDialog() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideProgressDialog();
        }
    }
}
