package org.twinone.irremote.providers.globalcache;

import java.util.Locale;

import org.twinone.irremote.components.Button;
import org.twinone.irremote.components.ComponentUtils;
import org.twinone.irremote.components.Remote;
import org.twinone.irremote.ir.Signal;
import org.twinone.irremote.ir.SignalFactory;

import android.annotation.SuppressLint;
import android.content.Context;

public class IrCode extends GCBaseListable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3967117959677153127L;

	public String Key;
	/** The key for API requests for this codeset */
	public String KeyName;
	/** The name of the codeset to display to the user */
	public String IRCode;

	/** Returns a signal that can be directly sent over the IR transmitter */
	@Override
	public String getKey() {
		return Key;
	}

	public Signal getSignal() {
		return SignalFactory.parse(Signal.FORMAT_GLOBALCACHE, IRCode);
	}

	@Override
	public int getType() {
		return UriData.TYPE_IR_CODE;
	}

	@Override
	public String getDisplayName() {
		// return KeyName + "(" + Key + ")";
		return KeyName;
	}

	public static Remote toRemote(Context c, String name, IrCode[] irCodes) {
		Remote remote = new Remote();
		remote.name = name;
		for (IrCode code : irCodes) {
			remote.addButton(IrCode.toButton(c, code));
		}
		return remote;
	}

	public static Button toButton(Context c, IrCode irCode) {
		Button button = new Button();
		button.text = irCode.KeyName;
		button.code = SignalFactory
				.toPronto(SignalFactory.parse(irCode.IRCode));
		button.id = getBestMatchId(irCode);
		if (button.id != Button.ID_NONE) {
			button.text = ComponentUtils.getCommonButtonDisplyaName(button.id, c);
		}
		return button;
	}

	/** Attempt to get an ID for this button name */
	@SuppressLint("DefaultLocale")
	private static int getBestMatchId(IrCode irCode) {
		final String button = irCode.Key.toLowerCase(Locale.US);

		// Power
		if (button.equals("on, power onoff"))
			return Button.ID_POWER_ON;
		if (button.equals("off, power onoff"))
			return Button.ID_POWER_OFF;
		if (button.contains("power onoff"))
			return Button.ID_POWER;

		// Volumes, channels
		if (button.contains("volume up"))
			return Button.ID_VOL_UP;
		if (button.contains("volume down"))
			return Button.ID_VOL_DOWN;
		if (button.contains("channel up"))
			return Button.ID_CH_UP;
		if (button.contains("channel down"))
			return Button.ID_CH_DOWN;

		// Navigation
		if (button.contains("menu up"))
			return Button.ID_NAV_UP;
		if (button.contains("menu down"))
			return Button.ID_NAV_DOWN;
		if (button.contains("menu left"))
			return Button.ID_NAV_LEFT;
		if (button.contains("menu right"))
			return Button.ID_NAV_RIGHT;
		if (button.contains("menu select"))
			return Button.ID_NAV_OK;

		if (button.equals("back"))
			return Button.ID_BACK;
		if (button.contains("mute"))
			return Button.ID_MUTE;
		// At this point we can safely return a generic "menu" for any button
		// that matches menu, because the specific menu [direction] are already
		// returned above
		if (button.contains("menu"))
			return Button.ID_MENU;

		// Digits
		if (button.contains("digit 0"))
			return Button.ID_DIGIT_0;
		if (button.contains("digit 1"))
			return Button.ID_DIGIT_1;
		if (button.contains("digit 2"))
			return Button.ID_DIGIT_2;
		if (button.contains("digit 3"))
			return Button.ID_DIGIT_3;
		if (button.contains("digit 4"))
			return Button.ID_DIGIT_4;
		if (button.contains("digit 5"))
			return Button.ID_DIGIT_5;
		if (button.contains("digit 6"))
			return Button.ID_DIGIT_6;
		if (button.contains("digit 7"))
			return Button.ID_DIGIT_7;
		if (button.contains("digit 8"))
			return Button.ID_DIGIT_8;
		if (button.contains("digit 9"))
			return Button.ID_DIGIT_9;

		if (button.contains("exit"))
			return Button.ID_EXIT;
		if (button.contains("last"))
			return Button.ID_LAST;
		if (button.contains("guide"))
			return Button.ID_GUIDE;
		// Cable
		if (button.contains("play"))
			return Button.ID_PLAY;
		if (button.contains("pause"))
			return Button.ID_PAUSE;
		if (button.contains("pause"))
			return Button.ID_PAUSE;
		if (button.contains("pause"))
			return Button.ID_PAUSE;

		return Button.ID_NONE;
	}

}
