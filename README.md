# RecyclerViewImageLoad

2022.10.27：新增导航页(通过ViewPager结合TabLayout实现) Fragment使用练习；结构调整，迁移大部分逻辑至Fragment，预计下版本进一步优化，细分到各类进行实现


2022.10.12：新增可拖动的回到顶部图标


此项目已实现

1.RecyclerView 向下滑动到末尾时加载新的元素，当前TextView与ImageView反复加载 
(因为开发Footer相关功能暂时禁用,可通过去除MainActivity.kt文件内第53-第64行注释启用

2.回到顶部按钮

3.按钮添加Header与Footer，按钮删除Header与Footer;当无可删除时Toast提示

4.在顶部时下拉松手刷新

5.Ui调整 （回到顶部按钮可移动化

6.导航栏

TODO：

1.修改下滑到末尾时自动加载为 末尾处上拉后松手加载

2.结构性优化

3.可移动按钮始终置于顶层

4.研究Fragment的生命周期，练习创建/销毁
