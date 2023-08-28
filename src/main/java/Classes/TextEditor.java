package Classes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;

public class TextEditor extends JFrame implements ActionListener {
	private JTextArea textArea;
	private final JScrollPane scrollPanel;
	private JSpinner fontSize;
	private JButton colorSelectButton;
	private JComboBox<String> fonts;
	private JMenuBar menuBar;

	private JMenuItem open;
	private JMenuItem saveAs;
	private JMenuItem leave;

	public TextEditor() {
		// <-- Init main frame --> //
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Word Weave");
		this.setIconImage(new ImageIcon("appIcon.jpg").getImage());
		this.setSize(500, 500);
		this.setLayout(new FlowLayout());
		this.setResizable(false);
		// <-- Init main frame --> //

		textArea = initTextArea();
		scrollPanel = initScrollPanel(textArea);
		fontSize = initFontSizeSpinner();
		fonts = initFontsList();
		menuBar = new JMenuBar();
		menuBar.add(createFileMenu());

		colorSelectButton = new JButton("Select text color");
		colorSelectButton.addActionListener(this);

		this.setJMenuBar(menuBar);
		this.add(fonts);
		this.add(new JLabel("Current text size : "));
		this.add(fontSize);
		this.add(colorSelectButton);
		this.add(scrollPanel);
		this.setVisible(true);
	}

	private JTextArea initTextArea() {
		JTextArea area = new JTextArea();
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setTabSize(3);
		area.setFont(new Font("Monospaced Bold", Font.PLAIN, 16));

		return area;
	}

	private JScrollPane initScrollPanel(JTextArea textArea) {
		JScrollPane panel = new JScrollPane(textArea);
		panel.setPreferredSize(new Dimension(475, 450));
		panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		return panel;

	}

	private JSpinner initFontSizeSpinner() {
		JSpinner spinner = new JSpinner();
		spinner.setPreferredSize(new Dimension(33, 18));
		spinner.setValue(20);
		spinner.addChangeListener(
				e -> textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) fontSize.getValue())));

		return spinner;
	}

	private JComboBox<String> initFontsList() {
		String[] allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		JComboBox<String> fontsList = new JComboBox<>(allFonts);

		fontsList.addActionListener(e -> textArea
				.setFont(new Font(fontsList.getSelectedItem().toString(), Font.PLAIN, (int) fontSize.getValue())));

		return fontsList;
	}

	private JMenu createFileMenu() {
		JMenu fileMenu = new JMenu("File");

		open = new JMenuItem("Open");
		saveAs = new JMenuItem("Save as...");
		leave = new JMenuItem("Leave");

		open.addActionListener(this);
		saveAs.addActionListener(this);
		leave.addActionListener(this);

		fileMenu.add(open);
		fileMenu.add(saveAs);
		fileMenu.addSeparator();
		fileMenu.add(leave);

		return fileMenu;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == colorSelectButton) {
			JColorChooser colorChooser = new JColorChooser();
			Color color = JColorChooser.showDialog(null, "Select a color", Color.black);

			textArea.setForeground(color);
		}

		if (e.getSource() == open) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("C:\\"));

			int answer = fileChooser.showOpenDialog(null);

			if (answer == JFileChooser.APPROVE_OPTION) {
				File gettedFile = new File(fileChooser.getSelectedFile().getAbsolutePath());

				try (BufferedReader reader = new BufferedReader(new FileReader(gettedFile))) {
					while (reader.ready()) {
						textArea.setText(reader.readLine());
					}

				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "Some error has occurred", "SAVE ERROR",
							JOptionPane.ERROR_MESSAGE);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Some error has occurred", "SAVE ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		if (e.getSource() == saveAs) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("C:\\"));

			int answer = fileChooser.showSaveDialog(null);

			if (answer == JFileChooser.APPROVE_OPTION) {
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

				try (BufferedWriter inputStream = new BufferedWriter(new FileWriter(file))) {
					inputStream.write(textArea.getText());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Some error has occurred", "SAVE ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		if (e.getSource() == leave) {
			System.exit(0);
		}
	}
}
