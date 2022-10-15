# RecyclerViewImageLoad

2022.10.12：新增可拖动的回到顶部图标


此项目已实现

1.RecyclerView 向下滑动到末尾时加载新的元素，当前TextView与ImageView反复加载 
(因为开发Footer相关功能暂时禁用,可通过去除MainActivity.kt文件内第53-第64行注释启用

2.回到顶部按钮

3.按钮添加Header与Footer，按钮删除Header与Footer;当无可删除时Toast提示

4.在顶部时下拉松手刷新

TODO：

1.修改下滑到末尾时自动加载为 末尾处上拉后松手加载

2.Ui调整 （回到顶部按钮可移动化

3.结构性优化，从Main Activity中分离出部分至其他文件/查看是否有冗余代码进行优化；封装等
