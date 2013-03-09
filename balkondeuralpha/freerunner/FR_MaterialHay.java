package balkondeuralpha.freerunner;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class FR_MaterialHay extends Material
{
	public FR_MaterialHay(MapColor mapcolor)
	{
		super(mapcolor);
		this.setBurning();
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
	
    @Override
	public boolean blocksMovement()
    {
        return false;
    }
	
	@Override
	public boolean getCanBurn()
	{
		return true;
	}
}
