package tweenAccessors;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import aurelienribon.tweenengine.TweenAccessor;

public class ImageButtonAccessor implements TweenAccessor<ImageButton>{

	public static final int ALPHA = 0;
	
	@Override
	public int getValues(ImageButton target, int tweenType, float[] returnValues) {
		// TODO Auto-generated method stub
		switch(tweenType)
		{
		case ALPHA:
			returnValues[0] = target.getColor().a;
			return 1;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(ImageButton target, int tweenType, float[] newValues) {
		// TODO Auto-generated method stub
		switch(tweenType)
		{
		case ALPHA:
			target.setColor(target.getColor().r,target.getColor().g,target.getColor().b,newValues[0]);
			break;
		default:
			assert false;
		}
	}

}
