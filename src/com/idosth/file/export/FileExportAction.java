package com.idosth.file.export;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * 文件右击菜单Demo，Export按钮，位于剪切复制粘贴组最后
 */
public class FileExportAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        MainDialogWrapper dialog = new MainDialogWrapper(e);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        dialog.requestFocus();
    }
}
