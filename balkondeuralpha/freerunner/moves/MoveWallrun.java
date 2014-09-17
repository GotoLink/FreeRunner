package balkondeuralpha.freerunner.moves;

import balkondeuralpha.freerunner.FreeRun;
import balkondeuralpha.freerunner.FreerunPlayer;

public class MoveWallrun extends Move{
    protected float		distance;
    private final float	speed		= 0.03F;
    private final float	limitSpeed	= 0.2F;
	public MoveWallrun(FreerunPlayer freerunhandler){
		super(freerunhandler);
        FreeRun.proxy.setAnimation(this);
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

    @Override
	public void performMove(int lookdirection){
		super.performMove(lookdirection);
		this.distance = 1.8F;
        getPlayer().addExhaustion(0.8F);
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
