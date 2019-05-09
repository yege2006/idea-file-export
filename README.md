# idea-file-export
计划实现类似eclipse中的export文件的功能，
主要是给一些有特殊需求比如导出文件目录结构等提供便利

## 1.下载项目，调试插件
将项目Clone到本地后，需要在相应的iml文件中的Module节点下，
添加项目标识,然后再运行，否则会导致运行时插件不成功
 ```xml
   <component name="DevKit.ModuleBuildProperties" url="file://$MODULE_DIR$/resources/META-INF/plugin.xml" />
   <component name="NewModuleRootManager" inherit-compiler-output="true">
 ```
 
 ## 2.使用方式
 下载idea-file-export.jar文件，放到idea安装目录的plugins下面重启即可使用。
 右击需要导出的文件，点击Export即可。
 ---
 ## 3.TODO
 - 添加文件列表功能以供勾选
 - 选中文件夹时默认导出所有子目录文件夹    