/*
 * Created by JFormDesigner on Fri Nov 16 22:25:20 CST 2018
 */

package org.jordon.security.view;

import org.jordon.security.core.crypto.symmetry.DESCipherService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author Jordon
 */
public class DESView extends JFrame {

    private DESCipherService desCipherService;

    public static void main(String[] args) {
        new DESView();
    }

    public DESView() {
        initComponents();
        desCipherService = new DESCipherService(true);
    }

    // 加密按钮事件处理
    private void encButtonActionPerformed(ActionEvent e) {
        desCipherService.setWorkingMode((String) workComboBox.getSelectedItem());
        String plaintext = ptArea.getText();
        String key = keyField.getText();
        String cipher = "";
        try {
            cipher = desCipherService.encrypt(plaintext, key);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        cipherArea.setText(cipher);

        printDetails();
    }

    private void printDetails() {
        processArea.setText("");
        List<String> messages = desCipherService.getMessages();
        for (String msg : messages) {
            processArea.append(msg + "\n");
            // 激活自动换行功能
            processArea.setLineWrap(true);
            processArea.setCaretPosition(processArea.getDocument().getLength());
            processArea.paintImmediately(processArea.getBounds());
            processArea.setWrapStyleWord(true);
            // enable the style of String.formatting result
            processArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        }
        desCipherService.clearMessages();
    }

    // 解密按钮事件处理
    private void decButtonActionPerformed(ActionEvent e) {
        String cipher = cipherArea.getText();
        String key = keyField.getText();
        String plaintext = "";
        try {
            plaintext = desCipherService.decrypt(cipher, key);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        ptArea.setText(plaintext);

        printDetails();
    }

    private void chooserButtonActionPerformed(ActionEvent e) {
        // 创建文件选择器
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.showOpenDialog(this);
        // 返回选中的文件集
        File selectedFile = fileChooser.getSelectedFile();

        String cipher;
        try {
            ptArea.setText("encryptinng " + selectedFile.getName());
            desCipherService.setWorkingMode((String) workComboBox.getSelectedItem());
            cipher = desCipherService.encryptFile(selectedFile, keyField.getText());
            cipherArea.setText(cipher);
        } catch (UnsupportedEncodingException | FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Jasion
        textArea1 = new JTextArea();
        scrollPane1 = new JScrollPane();
        ptArea = new JTextArea();
        label1 = new JLabel();
        label2 = new JLabel();
        keyField = new JTextField();
        label3 = new JLabel();
        scrollPane2 = new JScrollPane();
        cipherArea = new JTextArea();
        processPanel = new JScrollPane();
        processArea = new JTextArea();
        processLabel = new JLabel();
        decButton = new JButton();
        encButton = new JButton();
        padComboBox = new JComboBox();
        workComboBox = new JComboBox();
        label5 = new JLabel();
        label6 = new JLabel();
        chooserButton = new JButton();

        //======== this ========
        setTitle("DES-Jordon Yang");
        Container contentPane = getContentPane();

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(ptArea);
        }
        ptArea.setLineWrap(true);  // 激活自动换行功能
        ptArea.setWrapStyleWord(true);   // 激活断行不断字功能
        //---- label1 ----
        label1.setText("\u660e\u6587");

        //---- label2 ----
        label2.setText("\u5bc6\u94a5");

        //---- label3 ----
        label3.setText("\u5bc6\u6587");

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(cipherArea);
        }
        cipherArea.setLineWrap(true);  // 激活自动换行功能
        cipherArea.setWrapStyleWord(true);   // 激活断行不断字功能
        //======== processPanel ========
        {
            processPanel.setViewportView(processArea);
        }

        //---- processLabel ----
        processLabel.setText("\u8be6\u7ec6\u8fc7\u7a0b");

        //---- decButton ----
        decButton.setText("\u89e3\u5bc6");
        decButton.addActionListener(e -> decButtonActionPerformed(e));

        //---- encButton ----
        encButton.setText("\u52a0\u5bc6");
        encButton.addActionListener(e -> encButtonActionPerformed(e));

        //---- label5 ----
        label5.setText("\u586b\u5145\u65b9\u5f0f");

        //---- label6 ----
        label6.setText("\u5de5\u4f5c\u6a21\u5f0f");

        //---- chooserButton ----
        chooserButton.setText("\u9009\u62e9\u6587\u4ef6");
        chooserButton.addActionListener(e -> {
			encButtonActionPerformed(e);
			chooserButtonActionPerformed(e);
		});

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap(20, Short.MAX_VALUE)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                            .addGap(58, 58, 58)
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addComponent(label5)
                                    .addGap(46, 46, 46)
                                    .addComponent(padComboBox, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
                                    .addGap(80, 80, 80)
                                    .addComponent(label6, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
                                    .addGap(58, 58, 58))
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addGap(83, 83, 83)
                                    .addComponent(chooserButton, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(encButton, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
                                    .addGap(87, 87, 87)))
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addComponent(workComboBox, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                .addComponent(decButton, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
                            .addGap(190, 190, 190))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addComponent(processPanel, GroupLayout.PREFERRED_SIZE, 790, GroupLayout.PREFERRED_SIZE)
                                .addComponent(processLabel, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(21, 21, 21)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(label1)
                        .addComponent(textArea1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(label2)
                        .addComponent(label3))
                    .addGap(18, 18, 18)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(keyField, GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
                        .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE))
                    .addGap(35, 35, 35))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(41, 41, 41)
                            .addComponent(label1))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)))
                    .addGap(25, 25, 25)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(label2)
                        .addComponent(keyField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(52, 52, 52)
                            .addComponent(label3)
                            .addGap(40, 40, 40))
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)))
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(label5)
                        .addComponent(label6)
                        .addComponent(workComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(padComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(decButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                        .addComponent(encButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                        .addComponent(chooserButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
                    .addGap(39, 39, 39)
                    .addComponent(processLabel)
                    .addGap(26, 26, 26)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(textArea1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                            .addComponent(processPanel, GroupLayout.PREFERRED_SIZE, 306, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        padComboBox.addItem("标记位数填充");

        workComboBox.addItem("ECB");
        workComboBox.addItem("CBC");
        workComboBox.addItem("CTR");
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Jasion
    private JTextArea textArea1;
    private JScrollPane scrollPane1;
    private JTextArea ptArea;
    private JLabel label1;
    private JLabel label2;
    private JTextField keyField;
    private JLabel label3;
    private JScrollPane scrollPane2;
    private JTextArea cipherArea;
    private JScrollPane processPanel;
    private JTextArea processArea;
    private JLabel processLabel;
    private JButton decButton;
    private JButton encButton;
    private JComboBox padComboBox;
    private JComboBox workComboBox;
    private JLabel label5;
    private JLabel label6;
    private JButton chooserButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
