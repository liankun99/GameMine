package com_Mine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;
public class MineGameJpanel extends JPanel {
    /*
    9: 未点开空地
    1~8： 周围雷数
    10：地雷(未点开)
    0 ：被打开的空格子
    插上红旗的格子加100
    问号的格子加200
    数组值为10或者110或者210的格子为雷
     */
    int DateList[][];//存储地图数组
    int MapLine =9;//地图边长
    int i = -1;//[i][j]为y方向,通过鼠标移动监听事件时实获取下标的位置，判断当前所在的数组
    int j = -1;//[i][j]为x方向
    int Pressi = -1;//[i][j]按下右键获取按压的数组坐标
    int Pressj = -1;
    int MineCount = 5;//地雷总数
    int FlagCount = 0;//旗子总数
    int AroundFalgNumber = 0;//格子周围小红旗数量
    int UnkonwNumber = MapLine * MapLine;//未点开格子数
    boolean GameIsEnd = false;//定义游戏没有结束
    boolean PlayisUnSuccessful = true;//定义玩家没有获胜b
    boolean RightClicked = false;//定义右键是否点击当前格子
    public MineGameJpanel(){
        CreatMap(MapLine);
        RandomInsertMine(MineCount, MapLine);
        System.out.println();
        ShowMap(MapLine);
            //鼠标移动事件
        this.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                }

                //此方法在鼠标移动时进行调用
                @Override
                public void mouseMoved(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    //改变放上去的位置显示
                    //如果在雷盘上
                    if (!GameIsEnd && PlayisUnSuccessful) {
                        if (x > 10 && x < (10 + 30 * MapLine) && y > 10 && y < (10 + 30 * MapLine)) {
                            j = (x - 10) / 30;//[i][j]
                            i = (y - 10) / 30;//[i][j]
                        }
                        else {
                            j = -1;
                            i = -1;
                        }
                    }
                    //刷新
                    repaint();
                }
            });
            //鼠标点击事件
        this.addMouseListener(new MouseListener() {

                //鼠标点击
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                //鼠标按下
                @Override
                public void mousePressed(MouseEvent e) {
                    int Mousebutton = e.getButton();
                    int x = e.getX();
                    int y = e.getY();
                    //改变放上去的位置显示
                    //如果在雷盘上
                    if (!GameIsEnd && PlayisUnSuccessful) {
                        if (x > 10 && x < (10 + 30 * MapLine) && y > 10 && y < (10 + 30 * MapLine)) {
                            Pressj = (x - 10) / 30;//[i][j]
                            Pressi = (y - 10) / 30;//[i][j]
                        }
                        else {
                            Pressj = -1;
                            Pressi = -1;
                        }
                    }
                    //刷新
                    repaint();
                    System.out.println(Mousebutton);
                    AroundFalgNumber = 0;
                    //右键按下执行
                    if(1<=DateList[Pressi][Pressj] && DateList[Pressi][Pressj]<=8 && Mousebutton==3){
                        CountFlatNumber(Pressi,Pressj);
                        RightClicked = true;
                    }
                    repaint();
                }

                //鼠标释放
                @Override
                public void mouseReleased(MouseEvent e) {
                    /*
                     * 1:左键
                     * 2：中键
                     * 3：右键
                     * */
                    RightClicked = false;
                    int Mousebutton = e.getButton();
                    System.out.println(i + " " + j);
                    //菜单选择
                    int x = e.getX();
                    int y = e.getY();
                    if ((x>(10 + MapLine * 30) / 2)&& (y> MapLine * 30 + 40)
                        &&x < ((10 + MapLine * 30) / 2+40)&&y < MapLine * 30 + 80){
                        RestartGame();
                    }
                    if (!GameIsEnd && PlayisUnSuccessful) {
                        if (i != -1 && j != -1) {
                            switch (Mousebutton) {
                                case 1:
                                    System.out.println("左键");
                                    if (DateList[i][j] == 10) {
                                        DateList[i][j] = 999;
                                        BreakGame();
                                    }
                                    MouseClick(i, j);
                                    break;
                                case 3:
                                    System.out.println("右键");
                                    MouseRightClick(i, j);
                                    break;
                            }
                        }
                        System.out.print("剩余格子数：   ");
                        System.out.println(UnkonwNumber);
                        if (UnkonwNumber == MineCount) {
                            System.out.println("你获胜了");
                            PlayisUnSuccessful = false;
                        }
                    }
                    repaint();
                }

                //鼠标进入
                @Override
                public void mouseEntered(MouseEvent e) {
                }

                //鼠标退出
                @Override
                public void mouseExited(MouseEvent e) {
                }
            });

    }
    /*
    展示最后地图，标出所有地雷和数字
     */
    public  void UpDateMap(int n) {
        for(int i = 0; i < n;i++){
            for(int j = 0; j < n ; j++){
                if (DateList[i][j] == 10){
                    continue;
                }
                else {
                    ShowGNum(i, j);
                }
            }
            System.out.println();
        }
    }

    /*
    展示地图 变量n为地图边长
     */
    public  void ShowMap(int n) {
        for(int i = 0; i < n;i++){
            for(int j = 0; j < n ; j++){
                System.out.print(" ");
                System.out.printf("%2d",DateList[i][j]);
            }
            System.out.println();
        }
    }

    /*
    洗牌算法随机安排雷的位置，
    MineNum 为地雷数，n 为地图边长
     */
    public  void RandomInsertMine(int MineNumber, int n) {
        int RandomMine;
        int RandomMineX;
        int RandomMineY;
        int NumChange;
        int RandAddNum = 0;
        Random random = new Random();
        int MineNum = MineNumber;
        //数组开头插入雷
        for(int i = 0; i < n;i++){
            for(int j = 0; j < n ; j++){
                DateList[i][j] = 10;
                MineNum--;
                if(MineNum == 0){
                    break;
                }
            }
            if(MineNum == 0){
                break;
            }
        }
        ShowMap(n);
        //洗牌算法插入雷
        int GameNumber = MapLine*MapLine -1;
        for(int i = 0; i < n;i++){
            for(int j = 0; j < n ; j++){
                RandomMine = random.nextInt(n*n -RandAddNum)+RandAddNum;//产生0~n^2-1随机数
                RandAddNum++;
                System.out.print(RandomMine);
                RandomMineX = RandomMine / n ;//数组的行
                RandomMineY = RandomMine % n ;//数组的列
                System.out.println(" " + RandomMineX+" "+RandomMineY);
                NumChange = DateList[RandomMineX][RandomMineY];
                DateList[RandomMineX][RandomMineY] = DateList[i][j];
                DateList[i][j] = NumChange;
                GameNumber--;
                if(GameNumber == 0){
                    break;
                }
            }
            if(GameNumber == 0){
                break;
            }
        }
    }
    /*
     * 创造地图n为地图的边长
     * */

    public  void CreatMap(int n) {
        DateList = new int[n][n];
        for(int i = 0; i < n;i++){
            for(int j = 0; j < n ; j++){
                DateList[i][j] = 9;
            }
        }
        ShowMap(n);

    }

    /*
    读取一个格子周围雷的数量
    x 为列 y 为行 ，例如（1,1）为数组[1][1]
    实现思路：
    通过当前的下标，找到它左上角的数组如（1,1）为[0][0]
    然后通过双重while循环分别匹配 [0][0] [0][1] [0][2]
                                [1][0] [1][1] [1][2]
                                [2][0] [2][1] [2][2]
    中的数是否为1，为1雷数加一
     */
    public    void  ShowGNum (int x,int y) {
        System.out.println("需要寻找数的数组下标"+x+" "+y);
        int RealX = x ;
        int RealY = y ;
        int count = 0;
        while(x >= 0 && y >= 0) {
            x -= 1;
            y -= 1;
            int m = 0;
            while(m < 3) {
                int n = 0;
                while (n < 3) {
                    if (x == RealX && y == RealY) {
                        n++;
                        y += 1;
                        continue;
                    } else if (x < 0 || y < 0) {
                        n++;
                        y += 1;
                        continue;
                    } else if (x >= MapLine || y >= MapLine) {
                        n++;
                        y += 1;
                        continue;
                    } else {
                        System.out.print("   遍历的数组下标");
                        System.out.print("    "+x+ " " +y);
                        if (DateList[x][y] == 10 ||DateList[x][y] == 110
                                || DateList[x][y] == 210 )
                        {
                            count++;
                            y += 1;
                            n++;
                        }
                        else {
                            n++;
                            y++;
                        }
                    }
                }
                y = RealY - 1;
                x += 1;
                m++;
            }
            break;
        }
        DateList[RealX][RealY] = count;
        System.out.println("     ");
        System.out.print("雷数： ");
        System.out.println(count);
    }

    /*
    点击格子判断是否为空，遍历周围为空格子显示
     */
    public  void MapClick(int x,int y ) {
        if(DateList[x][y] == 9) {
            DateList[x][y] = 0;
            int RealX = x ;
            int RealY = y ;
            while(x >= 0 && y >= 0) {
                x -= 1;
                y -= 1;
                int m = 0;
                while(m < 3) {
                    int n = 0;
                    while (n < 3) {
                        if (x == RealX && y == RealY) {
                            n++;
                            y += 1;
                            continue;
                        } else if (x < 0 || y < 0) {
                            n++;
                            y += 1;
                            continue;
                        } else if (x >= MapLine || y >= MapLine) {
                            n++;
                            y += 1;
                            continue;
                        } else {
                            if (DateList[x][y] == 9) {
                                MapClick(x,y);
                                y += 1;
                                n++;
                            }
                            else {
                                y += 1;
                                n++;
                            }
                        }
                    }
                    y = RealY - 1;
                    x += 1;
                    m++;
                }
                break;
            }
        }
    }
    /*
    图片路径
     */

    public Image getImageByPath(String path) {
        // TODO: 此路径为相对路径，并不是获取该系统文件夹下路径图片。
        ImageIcon image = new ImageIcon(this.getClass().getResource(path));
        // TODO: 根据 icon 获取 image
        return image.getImage();
    }
    /*
    绘制地图
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3.0f));
        g.drawLine(7, 7, MapLine * 30 + 13, 7);
        g.drawLine(7, 7, 7, MapLine * 30 + 13);
        g.drawLine(7, MapLine * 30 + 35, MapLine * 30 + 15, MapLine * 30 + 35);
        g.drawLine(7, MapLine * 30 + 35, 7, MapLine * 30 + 85);
        g.setColor(new Color(255, 255, 255));
        g.drawLine(7, MapLine * 30 + 13, MapLine * 30 + 13, MapLine * 30 + 13);
        g.drawLine(MapLine * 30 + 13, MapLine * 30 + 13, MapLine * 30 + 13, 7);
        g.drawLine(7, MapLine * 30 + 85, MapLine * 30 + 15, MapLine * 30 + 85);
        g.drawLine(MapLine * 30 + 15, MapLine * 30 + 85, MapLine * 30 + 15, MapLine * 30 + 35);
        drawLandmine(g);
        //主绘图x 为j,y 为i
        if (!GameIsEnd) {
            for (int i = 0; i < DateList.length; i++) {
                for (int j = 0; j < DateList[i].length; j++) {
                    //绘制全图
                    if (DateList[i][j] == 9 || DateList[i][j] == 10) {
                        g.drawImage(getImageByPath("/img/blank.gif"),
                                10 + j * 30, 10 + i * 30, 30, 30, this);
                    }
                    //绘制数；
                    if (DateList[i][j] < 9 && DateList[i][j] > 0) {
                        g.drawImage(getImageByPath("/img/" + DateList[i][j] + ".gif"),
                                10 + j * 30, 10 + i * 30, 30, 30, this);
                    }
                    //绘制被点开后为空格
                    if (DateList[i][j] == 0) {
                        g.drawImage(getImageByPath("/img/" + DateList[i][j] + ".gif"),
                                10 + j * 30, 10 + i * 30, 30, 30, this);
                    }
                    //绘制小红旗
                    if (DateList[i][j] == 109 || DateList[i][j] == 110) {
                        g.drawImage(getImageByPath("/img/flag.gif"),
                                10 + j * 30, 10 + i * 30, 30, 30, this);
                    }
                    //绘制问号
                    if (DateList[i][j] == 209 || DateList[i][j] == 210) {
                        g.drawImage(getImageByPath("/img/ask.gif"),
                                10 + j * 30, 10 + i * 30, 30, 30, this);
                    }

                }
            }
            //获得当前鼠标在的数组下标，如果是没点开的就显示为空白
            if (i >= 0 && j >= 0 && (DateList[i][j] == 9 || DateList[i][j] == 10) && i < MapLine & j < MapLine) {
                g.drawImage(getImageByPath("/img/0.gif"),
                        10 + j * 30, 10 + i * 30, 30, 30, this);
            }
            if (Pressi >= 0 && Pressj >= 0 && DateList[Pressi][Pressj]!=AroundFalgNumber && RightClicked){
                DrawRClickNumber(g,Pressi,Pressj);
            }
            if (Pressi >= 0 && Pressj >= 0 && DateList[Pressi][Pressj]==AroundFalgNumber && RightClicked){
                ShowClick(Pressi,Pressj);
            }

        }
        else {
            for (int i = 0; i < DateList.length; i++) {
                for (int j = 0; j < DateList[i].length; j++) {
                    //绘制全图空白
                    if (DateList[i][j] == 9 ) {
                        g.drawImage(getImageByPath("/img/blank.gif"),
                                10 + j * 30, 10 + i * 30, 30, 30, this);
                    }
                    //绘制数；
                    if (DateList[i][j] < 9 && DateList[i][j] > 0) {
                        g.drawImage(getImageByPath("/img/" + DateList[i][j] + ".gif"),
                                10 + j * 30, 10 + i * 30, 30, 30, this);
                    }
                    //绘制被点开后为空格
                    if (DateList[i][j] == 0) {
                        g.drawImage(getImageByPath("/img/" + DateList[i][j] + ".gif"),
                                10 + j * 30, 10 + i * 30, 30, 30, this);
                    }
                    //绘制小红旗
                    if (DateList[i][j] == 109 || DateList[i][j] == 110) {
                        g.drawImage(getImageByPath("/img/flag.gif"),
                                10 + j * 30, 10 + i * 30, 30, 30, this);
                    }
                    //绘制问号
                    if (DateList[i][j] == 209 || DateList[i][j] == 210) {
                        g.drawImage(getImageByPath("/img/ask.gif"),
                                10 + j * 30, 10 + i * 30, 30, 30, this);
                    }
                    //绘制没点开的地雷或为?号地雷或插了红旗的雷
                    if (DateList[i][j] == 10 || DateList[i][j] == 210 ||DateList[i][j] == 110 ) {
                        g.drawImage(getImageByPath("/img/mine.gif"),
                                10 + j * 30, 10 + i * 30, 30, 30, this);
                    }
                    //绘制插错了了红旗的地雷
                    if (DateList[i][j] == 109 ) {
                        g.drawImage(getImageByPath("/img/error.gif"),
                                10 + j * 30, 10 + i * 30, 30, 30, this);
                    }
                    //绘制被点开的地雷
                    if (DateList[i][j] == 999 ) {
                        g.drawImage(getImageByPath("/img/blood.gif"),
                                10 + j * 30, 10 + i * 30, 30, 30, this);
                    }



                }
            }

        }
    }


    /*
    鼠标左键单击事件，只能点击之前没有点击过得格子，
    点击后判断这个格子周围的雷数，如果这个格子是空格，把数组数改成0
    然后递归点击它周围的格子。
     */
    public void MouseClick(int x, int y){
        if(DateList[x][y] == 9){
            ShowGNum(x,y);
            UnkonwNumber--;
        }
        if( DateList[x][y] == 0) {
            int RealX = x ;
            int RealY = y ;
            while(x >= 0 && y >= 0) {
                x -= 1;
                y -= 1;
                int m = 0;
                while(m < 3) {
                    int n = 0;
                    while (n < 3) {
                        if (x == RealX && y == RealY) {
                            n++;
                            y += 1;
                            continue;
                        } else if (x < 0 || y < 0) {
                            n++;
                            y += 1;
                            continue;
                        } else if (x >= MapLine || y >= MapLine) {
                            n++;
                            y += 1;
                            continue;
                        } else {
                            if (DateList[x][y] == 9) {
                                MouseClick(x,y);
                                y += 1;
                                n++;
                            }
                            else {
                                y += 1;
                                n++;
                            }
                        }
                    }
                    y = RealY - 1;
                    x += 1;
                    m++;
                }
                break;
            }
        }
    }
    /*
    鼠标右键点击效果
     */
    public void MouseRightClick(int x, int y) {
        System.out.println("MosudeRightclick");
        System.out.println(DateList[x][y]);
        //如果是雷或者未点开格子
        if(DateList[x][y] == 9 || DateList[x][y] == 10){
            DateList[x][y] = DateList[x][y] + 100;
            System.out.println(DateList[x][y]);
            ShowMap(MapLine);
            FlagCount++;
            //计算后为109 或者 110
        }
        //如果是红旗
        else if(DateList[x][y] ==110 || DateList[x][y] ==109){
            DateList[x][y] = DateList[x][y] + 100;
            ShowMap(MapLine);
            FlagCount--;
            //计算后为 209 或者 210
        }
        else if(DateList[x][y] ==210 || DateList[x][y] ==209){
            DateList[x][y] = DateList[x][y] - 200;
            ShowMap(MapLine);
            //计算后为 9 或者 10
        }
        else if(DateList[x][y]<=8&&DateList[x][y]>=1){
            ;
        }
        else {
            ;
        }
        System.out.print("旗子总数：    ");
        System.out.println(FlagCount);

    }
    /*
    结束游戏
     */
    public void BreakGame(){
        GameIsEnd = true;
        System.out.println("游戏结束");

    }
    /*
    重新开始游戏
     */
    public void RestartGame() {
        GameIsEnd = false;
        PlayisUnSuccessful  = true;
        CreatMap(MapLine);
        RandomInsertMine(MineCount, MapLine);
        FlagCount = 0;//旗子总数
        UnkonwNumber = MapLine * MapLine;//未点开格子数
        i = -1;
        j = -1;
        Pressi = -1;//[i][j]按下右键获取按压的数组坐标
        Pressj = -1;
    }
    /*
    划线绘制边框，数字，笑脸
     */
    public void drawLandmine(Graphics g) {
        int b = 0;
        int s = 0;
        int i = 0;
        if ((MineCount - FlagCount) >= 0) {
            if ((MineCount - FlagCount) >= 100) {
                b = (MineCount - FlagCount) / 100;
            }
            s = (MineCount - FlagCount) / 10 % 10;
            i = (MineCount - FlagCount) % 10;
        } else {
            if ((FlagCount - MineCount) < 10) {
                s = 10;
                i = (FlagCount - MineCount) % 10;
            } else if ((FlagCount - MineCount) >= 10 && (FlagCount - MineCount) < 100) {
                b = 10;
                s = (FlagCount - MineCount) / 10 % 10;
                i = (FlagCount - MineCount) % 10;
            }
        }

        g.drawImage(getImageByPath("/img/d" + b + ".gif"), 10, MapLine * 30 + 35, 30, 50, this);
        g.drawImage(getImageByPath("/img/d" + s + ".gif"), 40, MapLine * 30 + 35, 30, 50, this);
        g.drawImage(getImageByPath("/img/d" + i + ".gif"), 70, MapLine * 30 + 35, 30, 50, this);
        g.drawImage(getImageByPath("/img/blank.gif"), (10 + MapLine * 30) / 2 - 5, MapLine * 30 + 38, 45, 45, this);
        if (!GameIsEnd) {
            g.drawImage(getImageByPath("/img/face_normal.gif"), (10 + MapLine * 30) / 2, MapLine * 30 + 40, 40, 40, this);
            if (UnkonwNumber == MineCount) {
                g.drawImage(getImageByPath("/img/face_success.gif"), (10 + MapLine * 30) / 2, MapLine * 30 + 40, 40, 40, this);
            }
        }
        else {
            {
                g.drawImage(getImageByPath("/img/face_fail.gif"), (10 + MapLine * 30) / 2, MapLine * 30 + 40, 40, 40, this);

            }
        }


    }

    /*
    判断周围格子小红旗数量
     */
    public void CountFlatNumber(int x,int y) {
        System.out.println("数组下标"+x+" "+y);
        int RealX = x ;
        int RealY = y ;
        while(x >= 0 && y >= 0) {
            x -= 1;
            y -= 1;
            int m = 0;
            while(m < 3) {
                int n = 0;
                while (n < 3) {
                    if (x == RealX && y == RealY) {
                        n++;
                        y += 1;
                        continue;
                    } else if (x < 0 || y < 0) {
                        n++;
                        y += 1;
                        continue;
                    } else if (x >= MapLine || y >= MapLine) {
                        n++;
                        y += 1;
                        continue;
                    } else {
                        System.out.print("   遍历的数组下标");
                        System.out.print("    "+x+ " " +y);
                        if (DateList[x][y] == 109 ||DateList[x][y] == 110)
                        {
                            AroundFalgNumber++;
                            y += 1;
                            n++;
                        }
                        else {
                            n++;
                            y++;
                        }
                    }
                }
                y = RealY - 1;
                x += 1;
                m++;
            }
            break;
        }
        System.out.println("     ");
        System.out.print("周围旗子数： ");
        System.out.println(AroundFalgNumber);
    }
    /*
    把右键点击的数字周围未点开的地方变为空白
     */
    public void DrawRClickNumber(Graphics g,int x,int y){
        System.out.println("需要寻找数的数组下标"+x+" "+y);
        int RealX = x ;
        int RealY = y ;
        while(x >= 0 && y >= 0) {
            x -= 1;
            y -= 1;
            int m = 0;
            while(m < 3) {
                int n = 0;
                while (n < 3) {
                    if (x == RealX && y == RealY) {
                        n++;
                        y += 1;
                        continue;
                    } else if (x < 0 || y < 0) {
                        n++;
                        y += 1;
                        continue;
                    } else if (x >= MapLine || y >= MapLine) {
                        n++;
                        y += 1;
                        continue;
                    } else {
                        System.out.print("   遍历的数组下标");
                        System.out.print("    "+x+ " " +y);
                        if (DateList[x][y] == 9 ||DateList[x][y] == 10)
                        {
                            g.drawImage(getImageByPath("/img/0.gif"),
                                    10 + y * 30, 10 + x * 30, 30, 30, this);
                            y += 1;
                            n++;
                        }
                        else {
                            n++;
                            y++;
                        }
                    }
                }
                y = RealY - 1;
                x += 1;
                m++;
            }
            break;
        }
    }
    /*
    点开数字周围没标记旗子的位置
     */
    public void ShowClick(int x, int y){
        System.out.println("需要寻找数的数组下标"+x+" "+y);
        int RealX = x ;
        int RealY = y ;
        while(x >= 0 && y >= 0) {
            x -= 1;
            y -= 1;
            int m = 0;
            while(m < 3) {
                int n = 0;
                while (n < 3) {
                    if (x == RealX && y == RealY) {
                        n++;
                        y += 1;
                        continue;
                    } else if (x < 0 || y < 0) {
                        n++;
                        y += 1;
                        continue;
                    } else if (x >= MapLine || y >= MapLine) {
                        n++;
                        y += 1;
                        continue;
                    } else {
                        System.out.print("   遍历的数组下标");
                        System.out.print("    "+x+ " " +y);
                        if (DateList[x][y] == 9 ||DateList[x][y] == 10  )
                        {
                            if (DateList[x][y] == 10) {
                                DateList[x][y] = 999;
                                BreakGame();
                            }
                            MouseClick(x,y);
                            y += 1;
                            n++;
                        }
                        else {
                            n++;
                            y++;
                        }
                    }
                }
                y = RealY - 1;
                x += 1;
                m++;
            }
            break;
        }
    }

}

