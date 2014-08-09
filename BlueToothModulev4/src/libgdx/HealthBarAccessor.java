package libgdx;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import android.graphics.Color;
import aurelienribon.tweenengine.TweenAccessor;

public class HealthBarAccessor implements TweenAccessor<Image>{

	public static final int SIZE = 0;
	public static final int TRANSLATE = 1;
	public static final int HUEGREEN = 2;
	public static final int HUEYELLOW = 3;
	public static final int HUERED = 4;
	 //health bars initial scaleValue
	//full is 1.0 always, get num as percentage, * by damage, that - currScaleVal = new scale
	
	
	@Override
	public int getValues(Image target, int tweenType, float[] returnValues) {
		// TODO Auto-generated method stub
		switch(tweenType)
		{
		case SIZE:
			returnValues[0] = target.getScaleX();
			return 1;
		case TRANSLATE:
			returnValues[0] = target.getX();
			return 1;
		case HUEGREEN:
			returnValues[0] = target.getColor().r;
			returnValues[1] = target.getColor().g;
			returnValues[2] = target.getColor().b;
		case HUEYELLOW:
			returnValues[0] = target.getColor().r;
			returnValues[1] = target.getColor().g;
			returnValues[2] = target.getColor().b;
		case HUERED:
			returnValues[0] = target.getColor().r;
			returnValues[1] = target.getColor().g;
			returnValues[2] = target.getColor().b;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Image target, int tweenType, float[] newValues) {
		// TODO Auto-generated method stub
		switch(tweenType)
		{
		case SIZE:
			//target.setScaleY(target.getScaleY() - ((target.getScaleY()/max) * newValues[0])); //new values is damage
			//target.setColor(target.getColor().r,target.getColor().g,target.getColor().b,newValues[0]);
			target.setScaleX(newValues[0]);
			break;
		case TRANSLATE:
			target.setX(newValues[0]);
			break;
		case HUEGREEN:
//			newValues[0] = 
//			newValues[1] = target.setColor().g;
//			newValues[2] = target.setColor().b;
		case HUEYELLOW:
			com.badlogic.gdx.graphics.Color c = target.getColor();
			c.set(newValues[0], newValues[1], newValues[2],255);
			target.setColor(c);
		case HUERED:
			newValues[0] = 232;
			newValues[1] = 16;
			newValues[2] = 25;
		default:
			assert false;
		}
	}

}
