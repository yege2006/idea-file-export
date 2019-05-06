package com.idosth.file.export;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private String exportModel;
    private JBList fieldList;

    MainDialogWrapper(AnActionEvent event) {
        this.event = event;
        this.setTitle("Export");
        //初始默认选中导出文件带目录结构
        structureRadio.setSelected(true);
        //设置内容Panel
        initListeners();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
    }

    /**
     * ok按钮确认导出点击事件
     */
    private void onOK() {
        String outputPath = pathEditText.getText();
        // 条件校验
        if (null == outputPath || "".equals(outputPath)) {
            Messages.showErrorDialog(this, "Please Select Save Path!", "Error");
            return;
        }
        ListModel<VirtualFile> fileListModel = fieldList.getModel();
        if (fileListModel.getSize() == 0) {
            Messages.showErrorDialog(this, "Please Select Export File!", "Error");
            return;
        }
        try {
            for (int i = 0; i < fileListModel.getSize(); i++) {
                VirtualFile file = fileListModel.getElementAt(i);
                File sourceFile = new File(file.getPath());
                //判断是否需要导出文件目录
                if (structureRadio.isSelected()) {
                    //获取当前项目路径
                    Project project = event.getProject();
                    if (project != null && project.getBasePath() != null) {

                        String[] paths = sourceFile.getCanonicalPath().split("\\\\" + project.getName() + "\\\\");
                        if (paths.length > 1) {
                            File targetFile = new File(outputPath + "/" + project.getName() + "/" + paths[1]);
                            FileUtil.copy(sourceFile, targetFile);
                        }
                    }
                } else if (onlyFileRadio.isSelected()) {
                    File targetFile = new File(outputPath + "/" + sourceFile.getName());
                    FileUtil.copy(sourceFile, targetFile);
                }
            }
        } catch (IOException e) {
            Messages.showErrorDialog(this, "程序出错，请联系作者！", "Error");
            return;
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void initListeners() {

        browseBtn.addActionListener(e -> {
            //添加文件选择器
            FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
            descriptor.setShowFileSystemRoots(true);
            descriptor.setHideIgnored(true);
            VirtualFile virtualFile = FileChooser.chooseFile(descriptor, null, null);
            if (virtualFile != null && virtualFile.exists()) {
                pathEditText.setText(virtualFile.getCanonicalPath());
            }
        });

        //确定点击事件
        buttonOK.addActionListener(e -> onOK());

        //取消点击事件
        buttonCancel.addActionListener(e -> onCancel());

        //关闭点击事件
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // 按下ESC关闭事件
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    /**
     * 自定义Panel
     */
    private void createUIComponents() {
        //获取选中的虚拟文件
        VirtualFile[] data = event.getData(DataKeys.VIRTUAL_FILE_ARRAY);
        List<VirtualFile> fileList = new ArrayList<>();
        for (VirtualFile datum : data) {
            if (!datum.isDirectory()) {
                fileList.add(datum);
            }
        }
        fieldList = new JBList(fileList);
        fieldList.setEmptyText("No File Selected!");
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);
        fileListPanel = decorator.createPanel();
    }
}
