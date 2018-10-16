package edu.cvtc.MyTODOList.View;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.Color;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;

import edu.cvtc.MyTODOList.model.Event;
import edu.cvtc.MyTODOList.model.Event.EventRecurFreq;


import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.Font;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;

public class Main_UI extends JFrame {

	private JPanel contentPane;
	private JTable table;
	static int numOfDays;
	static int realDay, realMonth, realYear, currentMonth, currentYear;
	static ArrayList<Event> events = new ArrayList<Event>();
	static DefaultListModel eventModel = new DefaultListModel();
	static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
	static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
	static JLabel yearLabel;
	static JButton previousBtn, nextBtn;
	static JTable calendarTable;
	static JComboBox yearList;
	static DefaultTableModel calendarModel;
	static JScrollPane calendarScroll;
	static JPanel calendarPanel;
	static Main_UI mainFrame;
	static GregorianCalendar calendar = new GregorianCalendar();
	static String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		realDay = calendar.get(GregorianCalendar.DAY_OF_MONTH);
		realMonth = calendar.get(GregorianCalendar.MONTH);
		realYear = calendar.get(GregorianCalendar.YEAR);
		currentMonth = realMonth;
		currentYear = realYear;	

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainFrame = new Main_UI();
					mainFrame.setResizable(false);
					mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main_UI() {
		setTitle("MyTODOList - The Stupid Simple Scheduler");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 1320, 850);
		contentPane = new JPanel();
		contentPane.setForeground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel monthLbl = new JLabel(monthNames[currentMonth]);
		monthLbl.setBounds(500, 50, 235, 76);
		monthLbl.setFont(new Font("Tahoma", Font.BOLD, 24));
		
		contentPane.add(monthLbl);
		
		// Initialize and configure main calendar
		yearLabel = new JLabel("Year:");
		yearList = new JComboBox();
		previousBtn = new JButton("<--");
		nextBtn = new JButton("-->");
		calendarModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		calendarTable = new JTable(calendarModel);
		calendarScroll = new JScrollPane(calendarTable);
		calendarPanel = new JPanel(null);

		calendarPanel.add(yearLabel);
		calendarPanel.add(yearList);
		calendarPanel.add(previousBtn);
		calendarPanel.add(nextBtn);
		calendarPanel.add(calendarScroll);
		
		calendarPanel.setBounds(80, 120, 920, 680);
		yearLabel.setBounds(400, 0, 80, 25);
		yearList.setBounds(460 - yearList.getPreferredSize().width / 2, 0, 120, 25);
		previousBtn.setBounds(10, 0, 50, 25);
		nextBtn.setBounds(870, 0, 50, 25);
		calendarScroll.setBounds(10, 25, 920, 650);
		
		for (int i = realYear - 5; i <= realYear + 5; i++) {
			yearList.addItem(String.valueOf(i));
		}
		yearList.setSelectedIndex(5);
		
		String[] headers = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		for (int i = 0; i < 7; i++) {
			calendarModel.addColumn(headers[i]);
		}
		
		calendarTable.getTableHeader().setResizingAllowed(false);
		calendarTable.getTableHeader().setReorderingAllowed(false);
		calendarTable.setColumnSelectionAllowed(true);
		calendarTable.setRowSelectionAllowed(true);
		calendarTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		calendarTable.setRowHeight(120);
		
		calendarModel.setColumnCount(7);
		calendarModel.setRowCount(6);
		
		refreshCalendar(realMonth, realYear);
		
		previousBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentMonth == 0) {
					currentMonth = 11;
					currentYear -= 1;
				} else {
					currentMonth -= 1;
				}
				
				monthLbl.setText(monthNames[currentMonth]);
				refreshCalendar(currentMonth, currentYear);
				
			}
		});
		
		nextBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentMonth == 11) {
					currentMonth = 0;
					currentYear += 1;
				} else {
					currentMonth += 1;
				}
				
				monthLbl.setText(monthNames[currentMonth]);

				refreshCalendar(currentMonth, currentYear);
				
			}
		});
		
		yearList.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (yearList.getSelectedItem() != null) {
					currentYear = Integer.parseInt(yearList.getSelectedItem().toString());
					refreshCalendar(currentMonth, currentYear);
				}
				
			}
		});
		
		contentPane.add(calendarPanel);
//		table = new JTable();
//		table.setBounds(80, 120, 920, 680);
//		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		table.setBorder(new EmptyBorder(5, 5, 5, 5));
//		table.setAlignmentX(Component.LEFT_ALIGNMENT);
//		table.setAlignmentY(Component.TOP_ALIGNMENT);
//		table.setModel(new DefaultTableModel(
//			new Object[][] {
//				{null, null, null, null, null, null, ""},
//				{null, null, null, null, null, null, ""},
//				{null, null, null, null, null, null, null},
//				{null, null, null, null, null, null, null},
//				{null, null, null, null, null, null, null}
//			},
//			new String[] {
//				"New column", "New column", "New column", "New column", "New column", "New column", "Events"
//			}
//		));
//		table.setCellSelectionEnabled(true);
//		table.setBackground(Color.LIGHT_GRAY);
//		table.setRowHeight(136);
//		contentPane.add(table);
		

		JList eventList = new JList(events.toArray());
		JScrollPane eventListScroll = new JScrollPane(eventList);
		eventListScroll.setBounds(1040, 80, 240, 440);
		eventListScroll.setBorder(BorderFactory.createTitledBorder("Events"));
		contentPane.add(eventListScroll);
		
		JTextArea eventDetails = new JTextArea();
		eventDetails.setBounds(1040, 600, 240, 150);
		eventDetails.setEditable(false);
		eventDetails.setWrapStyleWord(true);
		eventDetails.setLineWrap(true);
		eventDetails.setBorder(BorderFactory.createTitledBorder("Event Details"));
		contentPane.add(eventDetails);
		
		JPanel eventDetailsButtonPanel = new JPanel();
		eventDetailsButtonPanel.setBounds(1040, 760, 250, 40);
		eventDetailsButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		eventDetailsButtonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		eventDetailsButtonPanel.setLayout(null);
		contentPane.add(eventDetailsButtonPanel);
		
		JButton updateBtn = new JButton("Update");
		updateBtn.setBounds(0, 0, 110, 40);
		eventDetailsButtonPanel.add(updateBtn);
		
		JButton deleteBtn = new JButton("Delete");
		deleteBtn.setBounds(130, 0, 110, 40);
		deleteBtn.setEnabled(false);
		eventDetailsButtonPanel.add(deleteBtn);
		deleteBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				events.remove(eventList.getSelectedIndex());
				eventDetails.setText("");
				eventList.clearSelection();
				deleteBtn.setEnabled(false);
				updateEventsList(eventList);
			}
		});
		
		JButton createBtn = new JButton("Create");
		createBtn.setBounds(1040, 540, 240, 35);
		contentPane.add(createBtn);
		createBtn.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createEventDialog((JFrame) contentPane.getTopLevelAncestor(), eventList);
				
			}
		});
		
		eventList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				deleteBtn.setEnabled(true);
				if (null != eventList.getSelectedValue()) {
					eventDetails.setText(((Event) eventList.getSelectedValue()).eventDetails());
				}
			}
		});
		
	}
	
	public static void createEventDialog(JFrame parent, JList list) {

		JDialog dialog = new JDialog(parent, true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		inputPanel.setOpaque(true);
		
		JLabel eventNameLabel = new JLabel("Event Name: ");
		
		JTextField eventName = new JTextField(30);
		
		JLabel eventDescLabel = new JLabel("Event Description: ");
		
		JTextArea eventDesc = new JTextArea();
		eventDesc.setWrapStyleWord(true);
		
		JLabel eventDateLabel = new JLabel("Event Date:");
		DatePickerSettings eventDatePickerSettings = new DatePickerSettings();
		eventDatePickerSettings.setAllowKeyboardEditing(false);
		DatePicker eventDatePicker = new DatePicker(eventDatePickerSettings);
		
		JLabel eventTimeLabel = new JLabel("Event Time:");
		TimePickerSettings eventTimeSettings = new TimePickerSettings();
		eventTimeSettings.setAllowKeyboardEditing(false);
		TimePicker eventTime = new TimePicker(eventTimeSettings);
		
		JLabel frequencyLabel = new JLabel("How frequently?");
		JComboBox frequencyList = new JComboBox(Event.EventRecurFreq.values());
		frequencyList.setEnabled(false);
		
		JLabel reoccuringLabel = new JLabel("Reoccuring?");
		JRadioButton noReoccurBtn = new JRadioButton("Nope");
		noReoccurBtn.setSelected(true);
		noReoccurBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frequencyList.setEnabled(false);
				
			}
		});
		
		JRadioButton yesReoccurBtn = new JRadioButton("Yep");
		yesReoccurBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frequencyList.setEnabled(true);
				
			}
		});
		
		ButtonGroup reoccurBtnGroup = new ButtonGroup();
		reoccurBtnGroup.add(noReoccurBtn);
		reoccurBtnGroup.add(yesReoccurBtn);
		
		JLabel priorityLabel = new JLabel("Priority (1: Least important, 10: Most Important)");
		JSlider priority = new JSlider(JSlider.HORIZONTAL, 1, 10, 5);
		priority.setMajorTickSpacing(1);
		priority.setPaintLabels(true);
		priority.setPaintTicks(true);
		priority.setSnapToTicks(true);
		
		JButton createEventBtn = new JButton("Create Event!");
		createEventBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Event newEvent = new Event();
				
				if (eventName.getText().isEmpty()) {
					JOptionPane.showMessageDialog(dialog, "Please provide an event name.", "Missing event name", JOptionPane.ERROR_MESSAGE);
				} else if (eventDatePicker.toString().isEmpty()){
					JOptionPane.showMessageDialog(dialog, "Please provide a starting event date.", "Missing event date", JOptionPane.ERROR_MESSAGE);
				} else {
					newEvent.setEventName(eventName.getText());
					newEvent.setEventDesc(eventDesc.getText());
					newEvent.setEventDate(eventDatePicker.getDate().format(dateFormat).toString());
					newEvent.setEventTime(eventTime.getTimeStringOrEmptyString());
					newEvent.setEventPriority(priority.getValue());
					
					if (yesReoccurBtn.isSelected()) {
						newEvent.setEventRecur(true);
						newEvent.setEventFrequency((EventRecurFreq) frequencyList.getSelectedItem());
					}
					
					events.add(newEvent);
					updateEventsList(list);
					dialog.dispose();
				}

			}
		});
		
		inputPanel.add(eventNameLabel);
		inputPanel.add(eventName);
		inputPanel.add(eventDescLabel);
		inputPanel.add(eventDesc);
		inputPanel.add(eventDateLabel);
		inputPanel.add(eventDatePicker);
		inputPanel.add(eventTimeLabel);
		inputPanel.add(eventTime);
		inputPanel.add(reoccuringLabel);
		inputPanel.add(noReoccurBtn);
		inputPanel.add(yesReoccurBtn);
		inputPanel.add(frequencyLabel);
		inputPanel.add(frequencyList);
		inputPanel.add(priorityLabel);
		inputPanel.add(priority);
		inputPanel.add(createEventBtn);
		
		dialog.setBounds(26, 175, 1100, 400);
		dialog.getContentPane().add(BorderLayout.CENTER, inputPanel);
		dialog.pack();
		dialog.setVisible(true);
		dialog.setResizable(false);
		
	}
	
	public static void updateEventsList(JList list) {
		DefaultListModel tempModel = new DefaultListModel();
		
		for (Event event: events) {
			tempModel.addElement(event);
		}
		
		eventModel = tempModel;
		list.setModel(eventModel);
	}
	
	public static void refreshCalendar(int month, int year) {
		int numOfDays, startOfMonth;
		GregorianCalendar newCalendar = new GregorianCalendar(year, month, 1);
		numOfDays = newCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		startOfMonth = newCalendar.get(GregorianCalendar.DAY_OF_WEEK);
		
		// Clear calendar
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				calendarModel.setValueAt(null, row, col);
			}
		}
		
		// Render calendar
		for (int i = 1; i <= numOfDays; i++) {
			int row = new Integer((i+startOfMonth-2)/7);
			int column = (i+startOfMonth-2) % 7;
			calendarModel.setValueAt(i, row, column);
		}
	}
}
