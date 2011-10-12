package de.embots.touchflow.gui.components.optionpane;

import de.embots.touchflow.gui.inspector.InspectorView;
import de.embots.touchflow.TouchFlow;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.embots.touchflow.module.core.Module;

public class OptionPane implements WindowListener {
	Module sender;
	Attribute[] options;
	JFrame mainFrame;
	JCheckBox showInInspector;
	private JButton applyButton;
	private static HashMap<Module, OptionPane> openPanes = new HashMap<Module, OptionPane>();

	public void setLocation(int x, int y) {
		mainFrame.setLocation(x, y);
	}

	public static void showOptionPane(Attribute[] options, Module sender) {

		// already open, bring old one to front
		if (openPanes.containsKey(sender)) {
			openPanes.get(sender).requestFocus();
			return;
		}

		OptionPane pan = new OptionPane(options, sender);
		openPanes.put(sender, pan);
		pan.setLocation(TouchFlow.getLocation().x + sender.getGraphXPos(),
				TouchFlow.getLocation().y + sender.getGraphYPos() + 150);
	}

	public void requestFocus() {
		mainFrame.requestFocus();
	}

	public OptionPane(Attribute[] options, Module sender) {

		this.sender = sender;
		this.options = options;

		mainFrame = new JFrame("Options");
		JPanel panMain = new JPanel();
		JPanel hBox = new JPanel();// Box.createVerticalBox();
		hBox.setLayout(new BoxLayout(hBox, BoxLayout.Y_AXIS));

		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.addWindowListener(this);
		mainFrame.getContentPane().add(panMain);

		// Module type

		DisplayAttribute lblTypShow = new DisplayAttribute("<html><u>"
				+ sender.getModuleName() + "</u></html>");
		hBox.add(lblTypShow);

		// Add id Attribute

		DisplayAttribute idShow = new DisplayAttribute("ID: " + sender.getId());
		hBox.add(idShow);

		// Add "show in inspector"

		showInInspector = new JCheckBox("Show in Inspector");
		showInInspector.setSelected(sender.showInInspector());
		hBox.add(showInInspector);

		JPanel attrPanel = new JPanel();
		// new col after 6 elements!
		int numOfCols = (options.length / 6) + 1;
		attrPanel.setLayout(new GridLayout(0, numOfCols));
		hBox.add(attrPanel);

		for (Attribute a : options) {
			a.setMainFrame(this);
			attrPanel.add(a);
		}

		ActionListener buttonListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				applyClicked();

			}
		};

		JPanel buttonPanel = new JPanel();
		applyButton = new JButton("Apply");
		buttonPanel.add(applyButton);
		applyButton.addActionListener(buttonListener);
		applyButton.setForeground(Color.BLACK);
		applyButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		hBox.add(buttonPanel);
		panMain.add(hBox);

		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	/**
	 * called from attributes when data to apply is there
	 */
	public void newData() {
		applyButton.setForeground(Color.RED);
		applyButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
	}

	private void applyClicked() {
		sender.setShowInInspector(showInInspector.isSelected());

		if (showInInspector.isSelected()) {
			InspectorView.addModule(sender);
		} else {
			InspectorView.removeModule(sender);
		}
		sender.reinit(options);

		// make apply button normal
		applyButton.setForeground(Color.BLACK);
		applyButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		// tell attributes that data is applied
		for (Attribute a : options) {
			a.setNewContent(false);
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		openPanes.remove(sender);

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

}
