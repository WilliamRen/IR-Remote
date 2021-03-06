package org.twinone.irremote.ui;

import org.twinone.irremote.R;
import org.twinone.irremote.components.Button;
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

public class SaveButtonDialog extends DialogFragment implements
		DialogInterface.OnClickListener {

	private static final String ARG_BUTTON = "org.twinone.irremote.arg.button";

	public static void showFor(Activity a, Button button) {
		SaveButtonDialog.newInstance(button).show(
				a.getFragmentManager(), "save_button_dialog");
	}

	private static SaveButtonDialog newInstance(Button button) {
		SaveButtonDialog f = new SaveButtonDialog();
		Bundle b = new Bundle();
		// We are saving this button, don't save as Common Button
		// The user can do this later from the remote itself
		button.id = Button.ID_NONE;
		b.putSerializable(ARG_BUTTON, button);
		f.setArguments(b);
		return f;
	}

	private Button mTargetButton;
	private EditText mButtonName;
	private SelectRemoteLinearLayout mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTargetButton = (Button) getArguments().getSerializable(ARG_BUTTON);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_save_button, null, false);
		mButtonName = (EditText) view.findViewById(R.id.save_button_name);
		mButtonName.setText(mTargetButton.text);
		mListView = (SelectRemoteLinearLayout) view
				.findViewById(R.id.select_remote_listview);

		AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
		ab.setView(view);
		ab.setTitle(R.string.save_button_title);

		ab.setPositiveButton(R.string.save_button_save, this);
		ab.setNegativeButton(android.R.string.cancel, null);
		return ab.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			mTargetButton.text = mButtonName.getText().toString();
			if (mListView.isRemoteSelected()) {
				Remote.addButton(getActivity(),
						mListView.getSelectedRemoteName(), mTargetButton);
			} else {
				Toast.makeText(getActivity(), R.string.select_remote_first,
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

}
