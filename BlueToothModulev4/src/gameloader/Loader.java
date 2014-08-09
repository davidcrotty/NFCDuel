package gameloader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Loader {

	//warrior = 5*3 idle
	//warrior = 4*1 attack
	
	//paladin = 2 * 3
	//paladin = 2 * 7 attack
	
	//wizard 1 *4 for both
			
	public static int    FRAME_COLSP1WALK;    // #1 //5*3 for idle
    public static int    FRAME_ROWSP1WALK;
    public static int    FRAME_COLSP2WALK;    // #1 //5*3 for idle
    public static int    FRAME_ROWSP2WALK;
    
    public static int    FRAME_COLSP1ATTACK;    // #1 //5*3 for idle
    public static int    FRAME_ROWSP1ATTACK;
    public static int    FRAME_COLSP2ATTACK;    // #1 //5*3 for idle
    public static int    FRAME_ROWSP2ATTACK;
	
   private  static Animation walkP1Anim, walkP2Anim, attackP1Anim, attackP2Anim;
   public static Texture p1Walk;  
   
	public Loader()
	{
		
	}
	
	
	public static void setTextures(FileHandle p1WalkPath, FileHandle p1AttackPath, FileHandle p2WalkPath, FileHandle p2AttackPath)
	{
		p1Walk = new Texture(p1WalkPath);
		TextureRegion[][] tmp3 = TextureRegion.split(p1Walk, p1Walk.getWidth()/FRAME_COLSP1WALK, p1Walk.getHeight()/FRAME_ROWSP1WALK); 
		TextureRegion[] p1WalkRegion = new TextureRegion[FRAME_COLSP1WALK * FRAME_ROWSP1WALK];
        int index3 = 0;
        for (int i = 0; i < FRAME_ROWSP1WALK; i++) {
            for (int j = 0; j < FRAME_COLSP1WALK; j++) {
            	p1WalkRegion[index3++] = tmp3[i][j];
            }
        }
       
        
        Animation walkP1AnimT = new Animation(0.425f, p1WalkRegion);
        walkP1Anim = walkP1AnimT;
		
		Texture p2attack = new Texture(p1AttackPath);
		TextureRegion[][] tmp4 = TextureRegion.split(p2attack, p2attack.getWidth()/FRAME_COLSP1ATTACK, p2attack.getHeight()/FRAME_ROWSP1ATTACK); 
		TextureRegion[] p1AttackRegion = new TextureRegion[FRAME_COLSP1ATTACK * FRAME_ROWSP1ATTACK];
        int index4 = 0;
        for (int i = 0; i < FRAME_ROWSP1ATTACK; i++) {
            for (int j = 0; j < FRAME_COLSP1ATTACK; j++) {
            	
            	p1AttackRegion[index4++] = tmp4[i][j];
            	
            }
        }
        
        
        Animation attackP1AnimT = new Animation(0.425f, p1AttackRegion );
        attackP1Anim = attackP1AnimT;
        
		
		Texture p2Walk = new Texture(p2WalkPath);
		TextureRegion[][] tmp = TextureRegion.split(p2Walk, p2Walk.getWidth()/FRAME_COLSP2WALK, p2Walk.getHeight()/FRAME_ROWSP2WALK); 
		TextureRegion[] p2WalkRegion = new TextureRegion[FRAME_COLSP2WALK * FRAME_ROWSP2WALK];
        int index = 0;
        for (int i = 0; i < FRAME_ROWSP2WALK; i++) {
            for (int j = 0; j < FRAME_COLSP2WALK; j++) {
            	
            	p2WalkRegion[index++] = tmp[i][j];
            }
        }
        
        
        
        Animation walkP2AnimT = new Animation(0.425f, p2WalkRegion);
        walkP2Anim = walkP2AnimT;
        
        Texture p2Attack = new Texture(p2AttackPath);
		TextureRegion[][] tmp2 = TextureRegion.split(p2Attack, p2Attack.getWidth()/FRAME_COLSP2ATTACK, p2Attack.getHeight()/FRAME_ROWSP2ATTACK); 
		TextureRegion[] p2AttackRegion = new TextureRegion[FRAME_COLSP2ATTACK * FRAME_ROWSP2ATTACK];
        int index2 = 0;
        for (int i = 0; i < FRAME_ROWSP2ATTACK; i++) {
            for (int j = 0; j < FRAME_COLSP2ATTACK; j++) {
            	p2AttackRegion[index2++] = tmp2[i][j];
            }
        }
        
        
        
        Animation walkP2AttackT = new Animation(0.425f, p2AttackRegion);
        attackP2Anim = walkP2AttackT;
        
        
	}
	
	public static Animation getAnimation(String item)
	{
		Animation returnAnimation = null;
		
		if(item.compareTo("P2WALK")==0)
		{
			
			returnAnimation = walkP2Anim;
		} else if (item.compareTo("P2ATTACK")==0)
		{
			returnAnimation = attackP2Anim; 
		} else if(item.compareTo("P1WALK")==0)
		{
			returnAnimation = walkP1Anim;
		} else if(item.compareTo("P1ATTACK")==0)
		{
			returnAnimation = attackP1Anim;
			
		}
		
		
		
		return returnAnimation;
	}


	
	
}
