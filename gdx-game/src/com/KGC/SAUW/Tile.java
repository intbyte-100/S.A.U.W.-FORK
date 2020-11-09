package com.KGC.SAUW;
import com.KGC.SAUW.InterfaceAPI.Interface;
import com.KGC.SAUW.InterfaceAPI.Container;
import com.KGC.SAUW.mobs.ItemMob;
import com.KGC.SAUW.mobs.Mobs;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;

public class Tile {
	public int id;
	public int x, y, z;
	public int type;
	public int damage;
	private int instrumenType;
	Block.TileEntity TileEntity = null;
	Interface Interface = null;
	float timer;
	TextureRegion t;
	Rectangle block;
	int WIDTH = Gdx.graphics.getWidth();
	
	public ArrayList<ExtraData> extraData = new ArrayList<ExtraData>();
    public ArrayList<Container> containers = new ArrayList<Container>();
   
	public PointLight PL;
	public Body body;

	public Tile(int X, int Y, int Z, Block bl) {
		this.x = X;
		this.y = Y;
		this.z = Z;
		this.id = bl.id;
		this.type = bl.type;
		this.block = new Rectangle();
		this.block.setPosition(X * WIDTH / 16 + bl.collisionsRect.x, Y * WIDTH / 16 + bl.collisionsRect.y);
		this.block.setSize(bl.collisionsRect.width, bl.collisionsRect.height);
		if (bl.t0 != null) t = TextureRegion.split(bl.t0, bl.t0.getWidth(), bl.t0.getHeight())[0][0];
		if (bl.TileEntity != null) {
			this.TileEntity = bl.TileEntity;
			this.Interface = bl.TileEntity.getGuiScreen();
			if (Interface != null)
				for (int i = 0; i < Interface.slots.size(); i++) {
					if (!Interface.slots.get(i).isInventorySlot) {
						containers.add(new Container(Interface.slots.get(i).ID));
					}
				}
			this.TileEntity.initialize(this);
		}
		damage = bl.getMaxDamage();
		instrumenType = bl.IT;

	}
	public void setBody(Body body) {
		this.body = body;
	}
    public void setLight(RayHandler rh, Block bl) {
		if (bl.lightingRadius != -1) {
			PL = new PointLight(rh, 100, new Color(bl.lightingColor), bl.lightingRadius * WIDTH / 16, x * WIDTH / 16 + WIDTH / 32, y * WIDTH / 16 + WIDTH / 32);
		    PL.attachToBody(body);
		}
	}
	@Override
	public String toString() {
		String output = "{id:" + id + ", x:" + x + ", y:" + y + ", z:" + z;
		if (containers.size() != 0) {
			output += ", Ctnr:[";
			for (int i = 0; i < containers.size(); i++) {
				output += containers.get(i).toString();
				if (i < containers.size() - 1) {
					output += ", ";
				}
			}
			output += "]";
		}
		output += "}";
		return output;
	}
	public Container getContainer(String ID) {
		for (int i = 0; i < containers.size(); i++) {
			if (containers.get(i).ID.equals(ID)) {
				return containers.get(i);
			}
		}
		return null;
	}
	public void setExtraData(String key, Object value) {
		for (ExtraData ED : extraData) {
			if (ED.key.equals(key)) {
				ED.setValue(value);
				return;
			}
		}
		extraData.add(new ExtraData(key));
		for (ExtraData ED : extraData) {
			if (ED.key.equals(key)) {
				ED.setValue(value);
				return;
			}
		}
	}
	public Object getExtraData(String key) {
		for (ExtraData ED : extraData) {
			if (ED.key.equals(key)) {
				return ED.getValue();
			}
		}
		return null;
	}
	public int hit(int IT) {
		if (IT == instrumenType) {
			damage -= 1;
			return 1;
		} else {
			return 0;
		}
	}
	public void update(Camera2D cam, GameInterface GI, Player pl, World w, Maps m, Blocks b, Mobs mobs, Items items) {
		if (damage <= 0 && id != 4) {
			w.setBlock(x, y, z, b.getBlockById(id).RBOD);
			for (int i = 0; i < b.getBlockById(id).drop.length; i++) {
				Random r = new Random();
				int xx = r.nextInt(WIDTH / 16) + WIDTH / 16 * x;
				int yy = r.nextInt(WIDTH / 16) + WIDTH / 16 * y;
				mobs.spawn(new ItemMob(xx, yy, b.getBlockById(id).drop[i][0], b.getBlockById(id).drop[i][1], 0, items));
			}
		}
		if (TileEntity != null) {
			timer += Gdx.graphics.getRawDeltaTime();
			if (timer >= 0.05) {
				timer = 0;
				TileEntity.tick(this);
			}
			if (Gdx.input.isTouched() && !GI.isTouched()) {
				double sc = (double)cam.W / WIDTH;
				int cX = (int)(Gdx.input.getX() * sc + cam.X);
				int cY = (int)(cam.H - Gdx.input.getY() * sc + cam.Y);
				int bX = (cX - (cX % (WIDTH / 16))) / (WIDTH / 16);
				int bY = (cY - (cY % (WIDTH / 16))) / (WIDTH / 16);
				if (bX == x && bY == y) {
					TileEntity.click(this);
					//Interface.isOpen = true;
				}
			}
			if (GI.interactionButton.wasClicked && Maths.distance(x, y, pl.mX, pl.mY) <= 1.5  && ((pl.mY + 1 == y && pl.rot == 0) || (pl.mX + 1 == x && pl.rot == 1) || (pl.mY - 1 == y && pl.rot == 2) || (pl.mX - 1 == x && pl.rot == 3))) {
				TileEntity.onInteractionButtonPressed(this);
				if (Interface != null)
					if (!Interface.isOpen)
				        Interface.open(x, y, z);
			}
		}
	}
}
