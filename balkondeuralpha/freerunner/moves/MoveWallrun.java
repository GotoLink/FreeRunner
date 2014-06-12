package balkondeuralpha.freerunner.moves;

import balkondeuralpha.freerunner.AnimWallrun;
import balkondeuralpha.freerunner.FreerunPlayer;

public class MoveWallrun extends Move{
    protected float		distance;
    private final float	speed		= 0.03F;
    private final float	limitSpeed	= 0.2F;
	public MoveWallrun(FreerunPlayer freerunhandler){
		super(freerunhandler);
		animation = new AnimWallrun();
	}
	
	@Override
	public void updateMove(){
		super.updateMove();
		if (getPlayer().posY - startPosY <= distance){
			nextMotionY += speed;
			doMoves();
		} else{
			moveDone();
		}
		
		if (nextMotionY > limitSpeed){
			nextMotionY = limitSpeed;
		}
	}
	
	public void performMove(int lookdirection, float distance){
		performMove(lookdirection);
		this.distance = distance;
	}
	
	@Override
	protected void doMoves()
	{
        getPlayer().motionY = nextMotionY;
	}
	
	@Override
	public void moveDone(){
		super.moveDone();
		freerunEngine.tryGrabLedge();
	}
	
	@Override
	public float getAnimationProgress()	{
		double d = getPlayer().posY - startPosY;
		return (float) (d / distance);
	}
}
