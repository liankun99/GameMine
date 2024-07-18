package com_Mine;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MineGameJFrame extends JFrame {

    public MineGameJFrame() {
        //设置窗口标题

        MineGameJpanel mineGameJpanel = new MineGameJpanel();
        this.setTitle("扫雷");
        // 用于设置当前窗口的宽和高20 * 16
        this.setSize((mineGameJpanel.MapLine+1)*30+8, (mineGameJpanel.MapLine+4)*30);
        //设置窗口的图标
        this.setIconImage(getImageByPath("/img/mineIcon.png"));
        //设置点击关闭后的处理方法
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //使窗口居中显示
        this.setLocationRelativeTo(null);
        //允许窗口改变大小
        this.setResizable(true);
        //创建画布
        //把画布添加到窗口中
        this.add(mineGameJpanel);
        //设置窗口的背景颜色
        this.setBackground(new Color(192, 192, 192));
        //显示窗口
        this.setVisible(true);
    }

    public Image getImageByPath(String path) {
        // TODO: 此路径为相对路径，并不是获取该系统文件夹下路径图片。
        ImageIcon image = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(path)));
        // TODO: 根据 icon 获取 image
        return image.getImage();
    }
}
