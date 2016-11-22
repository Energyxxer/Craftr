package com.energyxxer.cbe.ui.components;

import java.awt.LayoutManager;

import javax.swing.JPanel;

public abstract class XForm extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 874807131836539366L;

	public XForm() {
		super();
	}

	public XForm(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public XForm(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public XForm(LayoutManager layout) {
		super(layout);
	}

	public abstract Object getValue();
}
