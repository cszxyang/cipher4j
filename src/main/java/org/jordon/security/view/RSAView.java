/*
 * Created by JFormDesigner on Fri Nov 30 01:17:29 CST 2018
 */

package org.jordon.security.view;

import org.jordon.security.core.crypto.assymmetry.RawRSACipherService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Jasion
 */
public class RSAView extends JFrame {

    private RawRSACipherService service = new RawRSACipherService();

    public static void main(String[] args) {
        RSAView view = new RSAView();
        view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }

    public RSAView() {
        initComponents();
        pField.setText(String.valueOf(service.getP()));
        qField.setText(String.valueOf(service.getQ()));
        nField.setText(String.valueOf(service.getN()));
        phiField.setText(String.valueOf(service.getEulerVal()));
        eField.setText(String.valueOf(service.getE()));
        dField.setText(String.valueOf(service.getD()));
    }

    // 加密按钮事件
    private void encButtonActionPerformed(ActionEvent e) {
        String plaintext = ptArea.getText();
        cipherArea.setText(service.encrypt(plaintext));
    }

    // 解密按钮事件
    private void decButtonActionPerformed(ActionEvent e) {
        String cipher = cipherArea.getText();
        ptArea.setText(service.decrypt(cipher));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Jasion
        encButton = new JButton();
        decButton = new JButton();
        label1 = new JLabel();
        pField = new JTextField();
        label2 = new JLabel();
        qField = new JTextField();
        label3 = new JLabel();
        nField = new JTextField();
        label4 = new JLabel();
        phiField = new JTextField();
        eField = new JTextField();
        label5 = new JLabel();
        label6 = new JLabel();
        dField = new JTextField();
        label7 = new JLabel();
        scrollPane1 = new JScrollPane();
        cipherArea = new JTextArea();
        label8 = new JLabel();
        scrollPane2 = new JScrollPane();
        ptArea = new JTextArea();

        //======== this ========
        setTitle("RSA - jordonyang");
        Container contentPane = getContentPane();

        //---- encButton ----
        encButton.setText("\u52a0\u5bc6");
        encButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
        encButton.addActionListener(e -> encButtonActionPerformed(e));

        //---- decButton ----
        decButton.setText("\u89e3\u5bc6");
        decButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
        decButton.addActionListener(e -> decButtonActionPerformed(e));

        //---- label1 ----
        label1.setText("p");
        label1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));

        //---- pField ----
        pField.setEditable(false);

        //---- label2 ----
        label2.setText("q");
        label2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));

        //---- qField ----
        qField.setEditable(false);

        //---- label3 ----
        label3.setText("n");
        label3.setFont(new Font("Microsoft YaHei UI", Font.ITALIC, 16));

        //---- nField ----
        nField.setEditable(false);

        //---- label4 ----
        label4.setText("\u03c6(n)");
        label4.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));

        //---- phiField ----
        phiField.setEditable(false);

        //---- eField ----
        eField.setEditable(false);

        //---- label5 ----
        label5.setText("e");
        label5.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));

        //---- label6 ----
        label6.setText("d");
        label6.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));

        //---- dField ----
        dField.setEditable(false);

        //---- label7 ----
        label7.setText("\u660e\u6587");
        label7.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(cipherArea);
        }
        cipherArea.setLineWrap(true);  // 激活自动换行功能
        cipherArea.setWrapStyleWord(true);   // 激活断行不断字功能
        //---- label8 ----
        label8.setText("\u5bc6\u6587");
        label8.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(ptArea);
        }

        ptArea.setLineWrap(true);  // 激活自动换行功能
        ptArea.setWrapStyleWord(true);   // 激活断行不断字功能

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 495, GroupLayout.PREFERRED_SIZE)
                        .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 495, GroupLayout.PREFERRED_SIZE)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addGap(213, 213, 213)
                                    .addComponent(encButton)
                                    .addGap(38, 38, 38)
                                    .addComponent(decButton))
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addGap(25, 25, 25)
                                    .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(label7)
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(label1, GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                                                .addComponent(label5, GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(eField, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                                                .addComponent(pField, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
                                            .addGap(34, 34, 34)
                                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(label2, GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                                                .addComponent(label6, GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(qField, GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                                                .addComponent(dField, GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                                            .addGap(42, 42, 42)
                                            .addComponent(label3, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(nField, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
                                            .addGap(45, 45, 45)
                                            .addComponent(label4)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(phiField, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(label8, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))))
                            .addGap(27, 27, 27)))
                    .addContainerGap(44, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(label1)
                        .addComponent(pField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(label2)
                        .addComponent(qField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(nField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(label3)
                        .addComponent(phiField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(label4))
                    .addGap(18, 18, 18)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(eField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(label5)
                        .addComponent(label6)
                        .addComponent(dField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(39, 39, 39)
                    .addComponent(label7)
                    .addGap(10, 10, 10)
                    .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                    .addComponent(label8, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
                    .addGap(36, 36, 36)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(encButton)
                        .addComponent(decButton))
                    .addGap(19, 19, 19))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Jasion
    private JButton encButton;
    private JButton decButton;
    private JLabel label1;
    private JTextField pField;
    private JLabel label2;
    private JTextField qField;
    private JLabel label3;
    private JTextField nField;
    private JLabel label4;
    private JTextField phiField;
    private JTextField eField;
    private JLabel label5;
    private JLabel label6;
    private JTextField dField;
    private JLabel label7;
    private JScrollPane scrollPane1;
    private JTextArea cipherArea;
    private JLabel label8;
    private JScrollPane scrollPane2;
    private JTextArea ptArea;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
