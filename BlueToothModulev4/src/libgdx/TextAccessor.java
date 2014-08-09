package libgdx;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import aurelienribon.tweenengine.TweenAccessor;

public class TextAccessor implements TweenAccessor<Label>{

	public static final int SIZE = 0;
	public static final int ALPHA = 1;
	
	@Override
	public int getValues(Label target, int tweenType, float[] returnValues) {
		// TODO Auto-generated method stub
		switch(tweenType)
		{
		case SIZE:
			returnValues[0] = target.getY();
			return 1;
		case ALPHA:
			returnValues[0] = target.getColor().a;
			return 1;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Label target, int tweenType, float[] newValues) {
		// TODO Auto-generated method stub
		switch(tweenType)
		{
		case SIZE:
			//target.setScaleY(target.getScaleY() - ((target.getScaleY()/max) * newValues[0])); //new values is damage
			//target.setColor(target.getColor().r,target.getColor().g,target.getColor().b,newValues[0]);
			target.setY(newValues[0]);
			break;
		case ALPHA:
			target.setColor(target.getColor().r,target.getColor().g,target.getColor().b,newValues[0]);
			break;
		default:
			assert false;
		}
	}

}
