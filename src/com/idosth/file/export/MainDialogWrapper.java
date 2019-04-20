package com.idosth.file.export;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.event.*;

public class MainDialogWrapper extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton browseBtn;
    private JTextField pathEditText;
    private JPanel fileListPanel;
    private JRadioButton onlyFileRadio;
    private JRadioButton structureRadio;
    private JLabel pathLabel;
    //用于接收选中的文件
    private AnActionEvent event;
    private JBList fieldList;

    public MainDialogWrapper(AnActionEvent event) {
        //初始默认选中导出文件带目录结构
        structureRadio.setSelected(true);
        //设置内容Panel
        initListeners();
        this.setTitle("Export");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        initFileList(event);
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void initListeners(){

        browseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //添加文件选择器
                FileChooserDescriptor descriptor = new FileChooserDescriptor(false,true,false,false,false,false);
                descriptor.setShowFileSystemRoots(true);
                descriptor.setHideIgnored(true);
                VirtualFile virtualFile = FileChooser.chooseFile(descriptor,null,null);
                if (virtualFile.exists()) {
                    pathEditText.setText(virtualFile.getCanonicalPath());
                }
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // 按下ESC关闭事件
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initFileList(AnActionEvent event) {
        VirtualFile[] data = event.getData(DataKeys.VIRTUAL_FILE_ARRAY);
        fieldList = new JBList(data);
        fieldList.setEmptyText("No File Selected!");
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);
        fileListPanel = decorator.createPanel();
    }
}
