package gameloader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EffectsLoader {

	private static int COLP1Fireball = 5;
	private static int ROWP1Fireball = 1;
	
	private static int COLP2Fireball = 5;
	private static int ROWP2Fireball = 1;
	
	private static int COLP2Waterball = 6;
	private static int ROWP2Waterball = 1;
	
	private static int COLP1Waterball = 6;
	private static int ROWP1Waterball = 1;
	
	private static int COLUniversaldefup = 5;
	private static int ROWUniversaldefup = 2;
	
	private static int COLmagicdefup = 3;
	private static int ROWmagicdefup = 1;
	
	private static Animation fireballP1Anim, fireballP2Anim, waterballP1Anim, waterballP2Anim, universalDefUpAnim, universalMagicDefUpAnim;
	
	public EffectsLoader()
	{
		
	}
	
	public static void loadEffects()
	{
		
		
		Texture p1Fireball = new Texture(Gdx.files.internal("spritesheet/effects/fireballP1.png"));
		TextureRegion[][] tmp3 = TextureRegion.split(p1Fireball, p1Fireball.getWidth()/COLP1Fireball, p1Fireball.getHeight()/ROWP1Fireball); 
		TextureRegion[] p1FireballRegion = new TextureRegion[COLP1Fireball * ROWP1Fireball];
        int index3 = 0;
        for (int i = 0; i < ROWP1Fireball; i++) {
            for (int j = 0; j < COLP1Fireball; j++) {
            	p1FireballRegion[index3++] = tmp3[i][j];
            }
        }
       
        Animation fireballP1AnimT = new Animation(0.225f, p1FireballRegion);
        fireballP1Anim = fireballP1AnimT;
        
        Texture p2Fireball = new Texture(Gdx.files.internal("spritesheet/effects/fireballP2.png"));
		TextureRegion[][] tmp2 = TextureRegion.split(p2Fireball, p2Fireball.getWidth()/COLP2Fireball, p2Fireball.getHeight()/ROWP2Fireball); 
		TextureRegion[] p2FireballRegion = new TextureRegion[COLP2Fireball * ROWP2Fireball];
        int index2 = 0;
        for (int i = 0; i < ROWP2Fireball; i++) {
            for (int j = 0; j < COLP2Fireball; j++) {
            	p2FireballRegion[index2++] = tmp2[i][j];
            }
        }
       
        Animation fireballP2AnimT = new Animation(0.225f, p2FireballRegion);
        fireballP2Anim = fireballP2AnimT;
        
        Texture p1Waterball = new Texture(Gdx.files.internal("spritesheet/effects/waterballP1.png"));
		TextureRegion[][] tmp1 = TextureRegion.split(p1Waterball, p1Waterball.getWidth()/COLP1Waterball, p1Waterball.getHeight()/ROWP1Waterball); 
		TextureRegion[] p1WaterballRegion = new TextureRegion[COLP1Waterball * ROWP1Waterball];
        int index1 = 0;
        for (int i = 0; i < ROWP1Waterball; i++) {
            for (int j = 0; j < COLP1Waterball; j++) {
            	p1WaterballRegion[index1++] = tmp1[i][j];
            }
        }
       
        Animation waterballP1AnimT = new Animation(0.225f, p1WaterballRegion);
        waterballP1Anim = waterballP1AnimT;
        
        Texture p2Waterball = new Texture(Gdx.files.internal("spritesheet/effects/waterballP2.png"));
		TextureRegion[][] tmp = TextureRegion.split(p2Waterball, p2Waterball.getWidth()/COLP2Waterball, p2Waterball.getHeight()/ROWP2Waterball); 
		TextureRegion[] p2WaterballRegion = new TextureRegion[COLP2Waterball * ROWP2Waterball];
        int index = 0;
        for (int i = 0; i < ROWP2Waterball; i++) {
            for (int j = 0; j < COLP2Waterball; j++) {
            	p2WaterballRegion[index++] = tmp[i][j];
            }
        }
       
        Animation waterballP2AnimT = new Animation(0.225f, p2WaterballRegion);
        waterballP2Anim = waterballP2AnimT;
        
        Texture universaldefupText = new Texture(Gdx.files.internal("spritesheet/effects/universaldefup.png"));
		TextureRegion[][] tmp4 = TextureRegion.split(universaldefupText, universaldefupText.getWidth()/COLUniversaldefup, universaldefupText.getHeight()/ROWUniversaldefup); 
		TextureRegion[] universaldefupRegion = new TextureRegion[COLUniversaldefup * ROWUniversaldefup];
        int index4 = 0;
        for (int i = 0; i < ROWUniversaldefup; i++) {
            for (int j = 0; j < COLUniversaldefup; j++) {
            	universaldefupRegion[index4++] = tmp4[i][j];
            }
        }
       
        Animation universaldefupAnimT = new Animation(0.225f, universaldefupRegion);
        universalDefUpAnim = universaldefupAnimT;
        
        Texture magicUpText = new Texture(Gdx.files.internal("spritesheet/effects/universalmagicdefup.png"));
		TextureRegion[][] tmp5 = TextureRegion.split(magicUpText, magicUpText.getWidth()/COLmagicdefup, magicUpText.getHeight()/ROWmagicdefup); 
		TextureRegion[] magicUpRegion = new TextureRegion[COLmagicdefup * ROWmagicdefup];
        int index5 = 0;
        for (int i = 0; i < ROWmagicdefup; i++) {
            for (int j = 0; j < COLmagicdefup; j++) {
            	magicUpRegion[index5++] = tmp5[i][j];
            }
        }
       
        Animation magicUpAnimT = new Animation(0.225f, magicUpRegion);
        universalMagicDefUpAnim = magicUpAnimT;
	}
	
	public static Animation getAnimation(String item)
	{
		Animation returnAnimation = null;
		
		if(item.compareTo("P1FIREBALL")==0)
		{
			
			returnAnimation = fireballP1Anim;
		} else if (item.compareTo("P2FIREBALL")==0)
		{
			returnAnimation = fireballP2Anim; 
		} else if(item.compareTo("P1WATERBALL")==0)
		{
			returnAnimation = waterballP1Anim;
		} else if(item.compareTo("P2WATERBALL")==0)
		{
			returnAnimation = waterballP2Anim;
			
		} else if(item.compareTo("MAGICDEFUP")==0)
		{
			returnAnimation = universalMagicDefUpAnim;
			
		} else if(item.compareTo("DEFUP")==0)
		{
			returnAnimation = universalDefUpAnim;
			
		}
		
		
		
		return returnAnimation;
	}
	
}
