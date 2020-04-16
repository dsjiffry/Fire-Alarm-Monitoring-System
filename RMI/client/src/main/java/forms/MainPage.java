/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import Controllers.Client;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import mdlaf.MaterialLookAndFeel;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.components.combobox.MaterialComboBoxUI;
import mdlaf.components.tabbedpane.MaterialTabbedPaneUI;
import mdlaf.components.table.MaterialTableUI;
import mdlaf.components.togglebutton.MaterialToggleButtonUI;
import mdlaf.utils.MaterialColors;
import model.Sensor;

/**
 *
 * @author Shehan
 */
public class MainPage extends javax.swing.JFrame implements Runnable {

    private ArrayList<Sensor> sensors;
    private Vector<String> columns;
    private Vector<Vector<Object>> items;
    private Alert alert;
    private Client rmiclient;

    /**
     * Creates new form MainPage
     */
    public MainPage() {
        columns = new Vector<>();
        columns.add("Floor Number");
        columns.add("Room Number");
        columns.add("Active");

        rmiclient = new Client();

        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        jTable1.setEnabled(false);
        Thread t1 = new Thread(this);
        t1.start();

        deleteSensorButton.setEnabled(false);
        activeToggle.setEnabled(false);
        jTable1.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {

            if (jTable1.getSelectedRow() > -1) {
                int floorNumber = (int) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
                deleteSensorButton.setEnabled(true);
                activeToggle.setEnabled(true);

                if (floorNumber > 0) {
                    for (Sensor sensor : sensors) {
                        if (sensor.getFloorNumber() == floorNumber) {
                            if (sensor.isActive()) {
                                activeToggle.setSelected(true);
                            } else {
                                activeToggle.setSelected(false);
                            }
                        }
                    }
                }
            } else {
                deleteSensorButton.setEnabled(false);
                activeToggle.setEnabled(false);
            }
        });

    }

    public void run() {
        Client tclient = new Client();
        while (true) //Updating sensors every 30s
        {
            tableMessage.setText("updating table...");

            try {
                sensors = tclient.getSensors();
            } catch (Exception e) { // happens if not online. So we try again in 5s
                tableMessage.setText("Please make sure that you are online.");
                threadSleep(5000);
                continue;
            }

            items = new Vector<>();
            List<String> dropDownList = new ArrayList<>();
            dropDownList.add("All");

            for (Sensor sensor : sensors) {
                Vector<Object> row = new Vector<>();//row

                row.add(sensor.getFloorNumber());
                dropDownList.add(String.valueOf(sensor.getFloorNumber()));
                row.add(sensor.getRoomNumber());
                if (sensor.isActive()) {
                    row.add("Active");
                } else {
                    row.add("Inactive");
                }
                items.add(row);
            }

            UpdateTable();
            jTable1.setEnabled(true);

            // Populating the Drop down list
            int selectedRow = jComboBox1.getSelectedIndex();
            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(dropDownList.toArray(new String[0])));
            jComboBox1.setSelectedIndex(selectedRow);

            tableMessage.setText("");
            threadSleep(30000); // sleep for 30s
        }

    }

    public void UpdateTable() {
        int selectedRow = jComboBox1.getSelectedIndex();
        Vector<Vector<Object>> filtereditems = new Vector<>();

        if (selectedRow > 0) {
            int floorNumber = (int) Integer.valueOf(jComboBox1.getItemAt(selectedRow));

            for (Sensor sensor : sensors) {
                if (sensor.getFloorNumber() == floorNumber) {
                    Vector<Object> row = new Vector<>();//row

                    row.add(sensor.getFloorNumber());
                    row.add(sensor.getRoomNumber());
                    if (sensor.isActive()) {
                        row.add("Active");
                    } else {
                        row.add("Inactive");
                    }
                    filtereditems.add(row);
                }
            }

        } else {
            filtereditems = (Vector<Vector<Object>>) items.clone();
        }

        // Populating the Table
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                filtereditems, columns
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        manageSensor = new javax.swing.JPanel();
        label3 = new java.awt.Label();
        jPanel7 = new javax.swing.JPanel();
        activeToggle = new javax.swing.JToggleButton();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        deleteSensorButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        tableMessage = new javax.swing.JLabel();
        addSensor = new javax.swing.JPanel();
        label2 = new java.awt.Label();
        jPanel6 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        addSensorButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        floorNumberInput = new javax.swing.JFormattedTextField();
        roomNumberInput = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(MaterialColors.BLUE_GRAY_900);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        closeButton.setBackground(new java.awt.Color(48, 48, 48));
        closeButton.setForeground(new java.awt.Color(48, 48, 48));
        closeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cross_icon.png"))); // NOI18N
        closeButton.setToolTipText("");
        closeButton.setBorder(null);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setBackground (MaterialColors.BLUE_GRAY_900);

        closeButton.addMouseListener(MaterialUIMovement.getMovement(closeButton, MaterialColors.GRAY_900));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        jPanel1.add(closeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(795, 0, 40, -1));

        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));
        MaterialTabbedPaneUI mtpUI = (MaterialTabbedPaneUI)MaterialTabbedPaneUI.createUI(jTabbedPane1);
        UIManager.put("TabbedPane.selectionForeground",MaterialColors.CYAN_A100);
        UIManager.put("TabbedPane[focus].colorLine",MaterialColors.AMBER_200);
        UIManager.put("TabbedPane.foreground",MaterialColors.WHITE);
        UIManager.put("TabbedPane.border",MaterialColors.GREEN_100);

        mtpUI.installUI(jTabbedPane1);

        jTabbedPane1.setUI(mtpUI);
        jTabbedPane1.setBackground(MaterialColors.BLUE_GRAY_900);

        manageSensor.setBackground(MaterialColors.BLUE_GRAY_900);

        label3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        label3.setForeground(new java.awt.Color(255, 255, 255));
        label3.setText("Manage Sensors");

        jPanel7.setBackground(new java.awt.Color(100, 181, 246));
        jPanel7.setMinimumSize(new java.awt.Dimension(135, 48));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        activeToggle.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        activeToggle.setForeground(new java.awt.Color(255, 255, 255));
        activeToggle.setText("Active");
        activeToggle.setBorder(null);
        UIManager.put("ToggleButton.withoutIcon", true);

        UIManager.put("ToggleButton[withoutIcon].selectedBackground", MaterialColors.GREEN_800);
        UIManager.put("ToggleButton[withoutIcon].selectedForeground", MaterialColors.WHITE);
        UIManager.put("ToggleButton[withoutIcon].background", MaterialColors.RED_800);
        UIManager.put("ToggleButton[withoutIcon].foreground", MaterialColors.WHITE);

        Border border = new EmptyBorder(0,0,0,0);
        UIManager.put("ToggleButton[withoutIcon].selectedBorder", border);
        UIManager.put("ToggleButton[withoutIcon].border", border);

        activeToggle.setUI(new MaterialToggleButtonUI());
        activeToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activeToggleActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Select a floor to view it's sensors");

        MaterialComboBoxUI mtcbUI = (MaterialComboBoxUI)MaterialComboBoxUI.createUI(jComboBox1);
        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        mtcbUI.installUI(jComboBox1);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        deleteSensorButton.setBackground(new java.awt.Color(25, 118, 210));
        deleteSensorButton.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        deleteSensorButton.setForeground(new java.awt.Color(255, 255, 255));
        deleteSensorButton.setText("Delete Sensor");
        deleteSensorButton.setBorder(null);
        deleteSensorButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteSensorButton.setBackground (MaterialColors.BLUE_800);
        deleteSensorButton.setForeground (Color.WHITE);
        deleteSensorButton.addMouseListener(MaterialUIMovement.getMovement(deleteSensorButton, MaterialColors.INDIGO_900));
        deleteSensorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSensorButtonActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Floor Number:");

        MaterialTableUI mtUI = (MaterialTableUI)MaterialTableUI.createUI(jTable1);

        UIManager.put("Table.background", MaterialColors.BLUE_GRAY_700);
        UIManager.put("Table.alternateRowColor",  MaterialColors.BLUE_GRAY_700);
        UIManager.put("Table.foreground", MaterialColors.WHITE);
        UIManager.put("Table.selectionForeground", MaterialColors.WHITE);
        UIManager.put("Table.selectionBackground", MaterialColors.PURPLE_300);

        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.showVerticalLines", true);
        UIManager.put("Table.gridColor", MaterialColors.WHITE);

        UIManager.put("Table.font", (new java.awt.Font("Tahoma", 0, 16)));

        mtUI.installUI(jTable1);
        jTable1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jTable1.setForeground(new java.awt.Color(255, 255, 255));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Floor Number", "Room Number", "Active"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(jTable1);

        tableMessage.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tableMessage.setForeground(new java.awt.Color(255, 255, 255));
        tableMessage.setMaximumSize(new java.awt.Dimension(470, 17));
        tableMessage.setMinimumSize(new java.awt.Dimension(470, 17));

        javax.swing.GroupLayout manageSensorLayout = new javax.swing.GroupLayout(manageSensor);
        manageSensor.setLayout(manageSensorLayout);
        manageSensorLayout.setHorizontalGroup(
            manageSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageSensorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(manageSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(manageSensorLayout.createSequentialGroup()
                        .addGroup(manageSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(manageSensorLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(manageSensorLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(manageSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(activeToggle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(deleteSensorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, manageSensorLayout.createSequentialGroup()
                        .addComponent(tableMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        manageSensorLayout.setVerticalGroup(
            manageSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageSensorLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(manageSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(manageSensorLayout.createSequentialGroup()
                        .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3))
                .addGroup(manageSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(manageSensorLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(manageSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(109, 109, 109)
                        .addComponent(activeToggle, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(deleteSensorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(manageSensorLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(tableMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Manage  Sensors", manageSensor);

        addSensor.setBackground(MaterialColors.BLUE_GRAY_900);

        label2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        label2.setForeground(new java.awt.Color(255, 255, 255));
        label2.setText("Add a New Sensor");

        jPanel6.setBackground(new java.awt.Color(100, 181, 246));
        jPanel6.setMinimumSize(new java.awt.Dimension(135, 48));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        addSensorButton.setBackground(new java.awt.Color(25, 118, 210));
        addSensorButton.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        addSensorButton.setForeground(new java.awt.Color(255, 255, 255));
        addSensorButton.setText("add Sensor");
        addSensorButton.setBorder(null);
        addSensorButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addSensorButton.setBackground (MaterialColors.BLUE_800);
        addSensorButton.setForeground (Color.WHITE);
        addSensorButton.addMouseListener(MaterialUIMovement.getMovement(addSensorButton, MaterialColors.INDIGO_900));
        addSensorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSensorButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Floor Number:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Room Number:");

        floorNumberInput.setBackground(addSensor.getBackground());
        floorNumberInput.setBorder(null);
        floorNumberInput.setForeground(new java.awt.Color(255, 255, 255));
        floorNumberInput.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        floorNumberInput.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        roomNumberInput.setBackground(addSensor.getBackground());
        roomNumberInput.setBorder(null);
        roomNumberInput.setForeground(new java.awt.Color(255, 255, 255));
        roomNumberInput.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        roomNumberInput.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        javax.swing.GroupLayout addSensorLayout = new javax.swing.GroupLayout(addSensor);
        addSensor.setLayout(addSensorLayout);
        addSensorLayout.setHorizontalGroup(
            addSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addSensorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addSensorLayout.createSequentialGroup()
                        .addGroup(addSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(addSensorLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(595, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addSensorLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addSensorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(314, 314, 314))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addSensorLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(addSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(floorNumberInput, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roomNumberInput, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(237, 237, 237))
        );
        addSensorLayout.setVerticalGroup(
            addSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addSensorLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addGroup(addSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(floorNumberInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addGroup(addSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(roomNumberInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(addSensorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Add Sensor", addSensor);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 830, 470));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_closeButtonActionPerformed

    private void addSensorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSensorButtonActionPerformed
        int floorNumber = -1;
        int roomNumber = -1;

        try {
            floorNumber = Integer.valueOf(floorNumberInput.getText());
            roomNumber = Integer.valueOf(roomNumberInput.getText());
        } catch (NumberFormatException e) {
            alert = new Alert("Invalid inputs");
            return;
        }

        if (floorNumber <= 0 || roomNumber <= 0) {
            alert = new Alert("Invalid inputs");
            return;
        }

        if (rmiclient.addSensor(floorNumber, roomNumber)) {
            alert = new Alert("Sensor added sucessfully");
        } else {
            alert = new Alert("Error when adding Sensor");
        }


    }//GEN-LAST:event_addSensorButtonActionPerformed

    private void activeToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activeToggleActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow > -1) {
            activeToggle.setEnabled(false);
            int floorNumber = (int) jTable1.getValueAt(selectedRow, 0);
            int roomNumber = (int) jTable1.getValueAt(selectedRow, 1);
            for (Sensor sensor : sensors) {
                if (sensor.getFloorNumber() == floorNumber) {
                    boolean state = false;
                    if (jTable1.getValueAt(selectedRow, 2).toString().equalsIgnoreCase("Inactive")) {
                        state = true;
                    }
                    boolean result = rmiclient.changeState(floorNumber, roomNumber, state);
                    if (!result) {
                        alert = new Alert("Unable to change Sensor state");
                    }
                }
            }
            UpdateTable();
            activeToggle.setEnabled(true);
        }
    }//GEN-LAST:event_activeToggleActionPerformed

    private void deleteSensorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSensorButtonActionPerformed

        if (jTable1.getSelectedRow() < 0) {
            return;
        }

        int floorNumber = (int) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
        int roomNumber = (int) jTable1.getValueAt(jTable1.getSelectedRow(), 1);

        UIManager.put("Panel.background", MaterialColors.BLUE_GRAY_300);

        int result = JOptionPane.showConfirmDialog(jPanel1,
                "                         Sure you want to delete sensor on floor " + floorNumber + " room number " + roomNumber + "                         ",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        switch (result) {
            case JOptionPane.NO_OPTION:

                break;

            case JOptionPane.YES_OPTION:
                if (rmiclient.removeSensor(floorNumber, roomNumber)) {
                    alert = new Alert("Sensor deleted sucessfully");
                    UpdateTable();
                } else {
                    alert = new Alert("Error Deleting Server");
                }

                break;
        }


    }//GEN-LAST:event_deleteSensorButtonActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        jComboBox1.addActionListener((ActionEvent e) -> {
            UpdateTable();
        });
    }//GEN-LAST:event_jComboBox1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainPage.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainPage.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainPage.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainPage.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainPage().setVisible(true);
            }
        });
    }

    public void threadSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton activeToggle;
    private javax.swing.JPanel addSensor;
    private javax.swing.JButton addSensorButton;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton deleteSensorButton;
    private javax.swing.JFormattedTextField floorNumberInput;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private java.awt.Label label2;
    private java.awt.Label label3;
    private javax.swing.JPanel manageSensor;
    private javax.swing.JFormattedTextField roomNumberInput;
    private javax.swing.JLabel tableMessage;
    // End of variables declaration//GEN-END:variables

}
