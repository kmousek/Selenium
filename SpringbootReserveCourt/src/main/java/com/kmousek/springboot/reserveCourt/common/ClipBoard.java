package com.kmousek.springboot.reserveCourt.common;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ClipBoard {
	public void copyToClip(String copyString) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection contents = new StringSelection(copyString);
	    clipboard.setContents(contents, null);
	}	
}
