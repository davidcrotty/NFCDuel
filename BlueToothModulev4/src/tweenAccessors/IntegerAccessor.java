package tweenAccessors;

import libgdx.Splash;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import aurelienribon.tweenengine.TweenAccessor;

public class IntegerAccessor implements TweenAccessor<Float>{

	public static final int MOVERIGHT = 0;
	public static final int WATER = 1;
	
	
	
	@Override
	public int getValues(Float target, int tweenType, float[] returnValues) {
		// TODO Auto-generated method stub
		switch(tweenType)
		{
		case MOVERIGHT:
			returnValues[0] = target.floatValue();
			return 1;
		case WATER :
			returnValues[0] = target.floatValue();
			return 1;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Float target, int tweenType, float[] newValues) {
		// TODO Auto-generated method stub
		switch(tweenType)
		{
		case MOVERIGHT:
			//target.setColor(target.getColor().r,target.getColor().g,target.getColor().b,newValues[0]);
			Splash.fireballPosX = Float.valueOf(newValues[0]);
			//target =  newValues[0];
			break;
		case WATER:
			Splash.waterballPosX = Float.valueOf(newValues[0]);
			break;
		default:
			assert false;
		}
	}

}
