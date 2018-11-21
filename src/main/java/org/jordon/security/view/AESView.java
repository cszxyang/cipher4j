/*
 * Created by JFormDesigner on Fri Nov 16 22:27:55 CST 2018
 */

package org.jordon.security.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;

/**
 * @author Jasion
 */
public class AESView extends JFrame {


    public AESView() {
        initComponents();
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
        label4 = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(ptArea);
        }

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

        //======== processPanel ========
        {
            processPanel.setViewportView(processArea);
        }

        //---- label4 ----
        label4.setText("\u8be6\u7ec6\u8fc7\u7a0b");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(21, 21, 21)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(label1)
                        .addComponent(textArea1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(label2)
                        .addComponent(label3))
                    .addGap(18, 18, 18)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(keyField, GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                        .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE))
                    .addGap(35, 35, 35))
                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap(24, Short.MAX_VALUE)
                    .addComponent(label4, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
                    .addGap(602, 602, 602))
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(19, 19, 19)
                    .addComponent(processPanel, GroupLayout.PREFERRED_SIZE, 644, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(20, Short.MAX_VALUE))
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
                            .addComponent(label3))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(25, 25, 25)
                            .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)))
                    .addGap(8, 8, 8)
                    .addComponent(label4)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(processPanel, GroupLayout.PREFERRED_SIZE, 306, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(textArea1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
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
    private JLabel label4;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
