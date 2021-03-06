package org.twinone.irremote.ui;

import org.twinone.irremote.R;
import org.twinone.irremote.components.Remote;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SaveRemoteDialog extends DialogFragment implements
		DialogInterface.OnClickListener {

	private static final String ARG_REMOTE = "org.twinone.irremote.arg.remote";

	public static void showFor(Activity a, Remote remote) {
		SaveRemoteDialog.newInstance(remote).show(a.getFragmentManager(),
				"save_remote_dialog");
	}

	public void show(Activity a) {
		show(a.getFragmentManager(), "save_remote_dialog");
	}

	public static SaveRemoteDialog newInstance(Remote remote) {
		SaveRemoteDialog f = new SaveRemoteDialog();
		Bundle b = new Bundle();
		b.putSerializable(ARG_REMOTE, remote);
		f.setArguments(b);
		return f;
	}

	private Remote mTarget;
	private EditText mRemoteName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTarget = (Remote) getArguments().getSerializable(ARG_REMOTE);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_edittext, null, false);
		mRemoteName = (EditText) view.findViewById(R.id.dialog_edittext_input);
		mRemoteName.setText(mTarget.name);

		AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
		ab.setView(view);

		ab.setTitle(R.string.save_remote_title);
		ab.setMessage(R.string.save_remote_text);
		ab.setPositiveButton(R.string.save_remote_save, this);
		ab.setNegativeButton(android.R.string.cancel, null);

		return ab.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			final String name = mRemoteName.getText().toString();
			if (name == null || name.isEmpty()) {
				Toast.makeText(getActivity(), R.string.save_remote_empty,
						Toast.LENGTH_SHORT).show();
				return;
			}
			mTarget.name = name;
			mTarget.save(getActivity());
			if (mListener != null)
				mListener.onRemoteSaved(mTarget.name);
			break;
		}
	}

	private OnRemoteSavedListener mListener;

	public void setListener(OnRemoteSavedListener listener) {
		mListener = listener;
	}

	public interface OnRemoteSavedListener {
		public void onRemoteSaved(String name);
	}
}
