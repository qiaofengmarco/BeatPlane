package GameUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import GameObject.Enemy;
import GameObject.EnemyPlane_T1;
import GameObject.EnemyPlane_T2;
import GameObject.EnemyPlane_T3;
import GameObject.Explosion;
import GameObject.Missile;
import GameObject.PlayerPlane;
import GameObject.Position;
import GameObject.Stone;

public class GamePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Stone> stones = new ArrayList<Stone>();
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Explosion> exps = new ArrayList<Explosion>();
	private boolean first_time = true;
	private boolean in = false;
	private int cx,cy;

	public GamePanel() {
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				int x = e.getX();
				int y = e.getY();
				// 判断是否点击在玩家飞机区域内
				if (x >= GameFrame.gf.playerplane.position.x
						&& x <= GameFrame.gf.playerplane.position.x + 85
						&& y >= GameFrame.gf.playerplane.position.y
						&& y <= GameFrame.gf.playerplane.position.y + 100) {
					in = true;
					cx=x;
					cy=y;
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				in = false;
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});

		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				if (in) {
					GameFrame.gf.playerplane.position.x += (e.getX()-cx);
					GameFrame.gf.playerplane.position.y += (e.getY()-cy);
					cx=e.getX();
					cy=e.getY();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});
	}

	public void paintComponent(Graphics g) {
		// System.out.println(stones.size()+" "+enemies.size()+" "+exps.size());

		g.setColor(new Color(193, 202, 199));
		g.fillRect(0, 0, getWidth(), getHeight());

		// 绘制初始时候的陨石
		if (first_time) {
			stones.add(new Stone(new Position(12, 38), 3));
			stones.add(new Stone(new Position(392, 138), 6));
			stones.add(new Stone(new Position(102, 338), 1));
			stones.add(new Stone(new Position(430, 400), 5));
			first_time = false;
		}

		if (GameFrame.gf.playerplane.alive) {
			for (int j = 0; j <= enemies.size() - 1; j++) {
				Enemy e = enemies.get(j);
				PlayerPlane p = GameFrame.gf.playerplane;
				switch (e.enemy_kind) {
				case 1:
					if (collision(1, p.position, e.position)) {
						exps.add(new Explosion(new Position(p.position.x + 18,
								p.position.y + 28)));
						p.alive = false;
						LosePanel lp = new LosePanel();
						lp.setLocation(130, 200);
						add(lp);
						GameFrame.gf.c1.play();
					}
					break;
				case 2:
					if (collision(2, p.position, e.position)) {
						exps.add(new Explosion(new Position(p.position.x + 18,
								p.position.y + 28)));
						p.alive = false;
						LosePanel lp = new LosePanel();
						lp.setLocation(130, 200);
						add(lp);
						GameFrame.gf.c1.play();
					}
					break;
				case 3:
					if (collision(3, p.position, e.position)) {
						exps.add(new Explosion(new Position(p.position.x + 18,
								p.position.y + 28)));
						p.alive = false;
						LosePanel lp = new LosePanel();
						lp.setLocation(130, 200);
						add(lp);
						GameFrame.gf.c1.play();
					}
					break;
				}
			}
		}

		createNewStone();
		stoneDrop(g);

		boom(g);

		// 检查子弹是否命中敌人
		for (int j = 0; j <= GameFrame.gf.playerplane.shooted_missiles.size() - 1; j++) {
			Missile m = GameFrame.gf.playerplane.shooted_missiles.get(j);
			for (int k = 0; k <= enemies.size() - 1; k++) {
				Enemy e = enemies.get(k);
				switch (e.enemy_kind) {
				case 1:
					if (hit(1, m.pos, e.position)) {
						m.alive = false;
						e.life--;
						if (e.life == 0) {
							e.alive = false;
							GameFrame.gf.grade.grade += 1000;
							exps.add(new Explosion(new Position(
									e.position.x - 4, e.position.y - 2)));
							GameFrame.gf.c2.play();
						}
					}
					break;
				case 2:
					if (hit(2, m.pos, e.position)) {
						m.alive = false;
						e.life--;
						if (e.life == 0) {
							e.alive = false;
							GameFrame.gf.grade.grade += 3000;
							exps.add(new Explosion(new Position(
									e.position.x + 6, e.position.y + 18)));
							GameFrame.gf.c2.play();
						}
					}
					break;
				case 3:
					if (hit(3, m.pos, e.position)) {
						m.alive = false;
						e.life--;
						if (e.life == 0) {
							e.alive = false;
							GameFrame.gf.grade.grade += 10000;
							exps.add(new Explosion(new Position(
									e.position.x + 29, e.position.y + 53)));
							GameFrame.gf.c2.play();
						}
					}
					break;
				}
			}
		}

		// 移除死亡的子弹和敌人
		for (int j = 0; j <= GameFrame.gf.playerplane.shooted_missiles.size() - 1; j++) {
			if (GameFrame.gf.playerplane.shooted_missiles.get(j).alive == false) {
				GameFrame.gf.playerplane.shooted_missiles.remove(j);
				j = 0;
			}
		}
		for (int j = 0; j <= enemies.size() - 1; j++) {
			if (enemies.get(j).alive == false) {
				enemies.remove(j);
				j = 0;
			}
		}

		createEnemy();
		enemyDrop(g);

		if (GameFrame.gf.playerplane.alive) {
			Image image = new ImageIcon(
					GamePanel.class.getResource("/image/playerplane.png"))
					.getImage();
			g.drawImage(image, GameFrame.gf.playerplane.position.x,
					GameFrame.gf.playerplane.position.y, 68, 80, this);
			shoot(g);
		}

	}

	public void createNewStone() {
		RepaintThread.stone_counter += 10;

		if (RepaintThread.stone_counter == 1000) {
			int kind = (int) (Math.random() * 6) + 1;
			int x = (int) (Math.random() * (GameFrame.gf.panel_5.getWidth() - 106));
			Stone new_stone = new Stone(new Position(x, -100), kind);
			stones.add(new_stone);
			RepaintThread.stone_counter = 0;
		}
	}

	public void stoneDrop(Graphics g) {
		for (int j = 0; j <= stones.size() - 1; j++) { // 陨石掉落
			Stone i = stones.get(j);
			switch (i.kind) {
			case 1:
				Image image = new ImageIcon(
						GamePanel.class.getResource("/image/stone1.jpg"))
						.getImage();
				g.drawImage(image, i.position.x, i.position.y, this);
				break;
			case 2:
				Image image1 = new ImageIcon(
						GamePanel.class.getResource("/image/stone2.jpg"))
						.getImage();
				g.drawImage(image1, i.position.x, i.position.y, this);
				break;
			case 3:
				Image image2 = new ImageIcon(
						GamePanel.class.getResource("/image/stone3.jpg"))
						.getImage();
				g.drawImage(image2, i.position.x, i.position.y, this);
				break;
			case 4:
				Image image3 = new ImageIcon(
						GamePanel.class.getResource("/image/stone4.jpg"))
						.getImage();
				g.drawImage(image3, i.position.x, i.position.y, this);
				break;
			case 5:
				Image image4 = new ImageIcon(
						GamePanel.class.getResource("/image/stone5.jpg"))
						.getImage();
				g.drawImage(image4, i.position.x, i.position.y, this);
				break;
			case 6:
				Image image5 = new ImageIcon(
						GamePanel.class.getResource("/image/stone6.jpg"))
						.getImage();
				g.drawImage(image5, i.position.x, i.position.y, this);
				break;
			}
			i.position.y += 1;
			if (i.position.y >= GameFrame.gf.panel_5.getHeight())
				i.alive = false;
		}

		// 从ArrayList中清除已经死亡的陨石
		for (int j = 0; j <= stones.size() - 1; j++) {
			if (stones.get(j).alive == false) {
				stones.remove(j);
				j = 0;
			}
		}
	}

	public void createEnemy() {
		RepaintThread.enemy_counter += 10;

		if (RepaintThread.enemy_counter == 1000) {
			// 随机生成一种敌机
			int kind = (int) (Math.random() * 3) + 1;
			// 随机生成敌机的位置
			int x = (int) (Math.random() * (GameFrame.gf.panel_5.getWidth() - 105));
			switch (kind) {
			case 1:
				EnemyPlane_T1 e1 = new EnemyPlane_T1(new Position(x, -150));
				enemies.add(e1);
				break;
			case 2:
				EnemyPlane_T2 e2 = new EnemyPlane_T2(new Position(x, -150));
				enemies.add(e2);
				break;
			case 3:
				EnemyPlane_T3 e3 = new EnemyPlane_T3(new Position(x, -150));
				enemies.add(e3);
				break;
			}

			RepaintThread.enemy_counter = 0;
		}
	}

	public void enemyDrop(Graphics g) {
		for (int j = 0; j <= enemies.size() - 1; j++) {
			Enemy i = enemies.get(j);
			switch (i.enemy_kind) {
			case 1:
				i = (EnemyPlane_T1) i;
				Image image = new ImageIcon(
						GamePanel.class.getResource("/image/enemy1.png"))
						.getImage();
				g.drawImage(image, i.position.x, i.position.y, this);
				break;
			case 2:
				i = (EnemyPlane_T2) i;
				Image image2 = new ImageIcon(
						GamePanel.class.getResource("/image/enemy2.png"))
						.getImage();
				g.drawImage(image2, i.position.x, i.position.y, this);
				break;
			case 3:
				i = (EnemyPlane_T3) i;
				Image image3 = new ImageIcon(
						GamePanel.class.getResource("/image/enemy3.png"))
						.getImage();
				g.drawImage(image3, i.position.x, i.position.y, this);
				break;
			}
			i.position.y += i.speed;
			if (i.position.y >= GameFrame.gf.panel_5.getHeight())
				i.alive = false;
		}
		for (int j = 0; j <= enemies.size() - 1; j++) {
			if (enemies.get(j).alive == false) {
				enemies.remove(j);
				j = 0;
			}
		}
	}

	public void shoot(Graphics g) {
		RepaintThread.counter += 10;

		Image missile = new ImageIcon(
				GamePanel.class.getResource("/image/missile.png")).getImage();

		// 为了产生动态效果，设置在指定时刻产生子弹
		if (RepaintThread.counter == 100) {
			Missile m = new Missile(GameFrame.gf.playerplane.position.x + 26,
					GameFrame.gf.playerplane.position.y - 25);
			GameFrame.gf.playerplane.shooted_missiles.add(m);
			RepaintThread.counter = 0;
			GameFrame.gf.c3.play();
		}

		// 把已经产生的子弹发射出去，超越游戏边界就移除
		// 发射
		for (int j = 0; j <= GameFrame.gf.playerplane.shooted_missiles.size() - 1; j++) {
			Missile i = GameFrame.gf.playerplane.shooted_missiles.get(j);
			g.drawImage(missile, i.pos.x, i.pos.y, this);
			i.pos.y -= 25;
			if (i.pos.y < 0)
				i.alive = false;
		}
		// 删除
		for (int j = 0; j <= GameFrame.gf.playerplane.shooted_missiles.size() - 1; j++) {
			if (GameFrame.gf.playerplane.shooted_missiles.get(j).alive == false) {
				GameFrame.gf.playerplane.shooted_missiles
						.remove(GameFrame.gf.playerplane.shooted_missiles
								.get(j));
				j = 0;
			}
		}

	}

	public boolean hit(int enemy_kind, Position m_p, Position e_p) {
		switch (enemy_kind) {
		case 1:
			if (m_p.y >= e_p.y && m_p.y <= e_p.y + 40 && m_p.x >= e_p.x
					&& m_p.x <= e_p.x + 40)
				return true;
			break;
		case 2:
			if (m_p.y >= e_p.y && m_p.y <= e_p.y + 80 && m_p.x >= e_p.x
					&& m_p.x <= e_p.x + 60)
				return true;
			break;
		case 3:
			if (m_p.y >= e_p.y && m_p.y <= e_p.y + 150 && m_p.x >= e_p.x
					&& m_p.x <= e_p.x + 105)
				return true;
			break;
		}
		return false;
	}

	public boolean collision(int enemy_type, Position p_p, Position e_p) {
		switch (enemy_type) {
		case 1:
			if (p_p.x > e_p.x - 40 && p_p.x < e_p.x + 40 && p_p.y > e_p.y - 40
					&& p_p.y < e_p.y + 40) {
				return true;
			}
			break;
		case 2:
			if (p_p.x > e_p.x - 60 && p_p.x < e_p.x + 60 && p_p.y > e_p.y - 80
					&& p_p.y < e_p.y + 80) {
				return true;
			}
			break;
		case 3:
			if (p_p.x > e_p.x - 105 && p_p.x < e_p.x + 105
					&& p_p.y > e_p.y - 150 && p_p.y < e_p.y + 150) {
				return true;
			}
			break;
		}
		return false;
	}

	public void boom(Graphics g) {
		for (int j = 0; j <= exps.size() - 1; j++) {
			Explosion exp = exps.get(j);
			Image explosion = new ImageIcon(
					GamePanel.class.getResource("/image/explosion.png"))
					.getImage();
			g.drawImage(explosion, exp.position.x, exp.position.y, this);
			exps.get(j).life_time--;
		}
		for (int j = 0; j <= exps.size() - 1; j++) {
			if (exps.get(j).life_time == 0) {
				exps.remove(j);
				j = 0;
			}
		}
	}

}
