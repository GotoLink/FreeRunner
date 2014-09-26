package balkondeuralpha.freerunner.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialHay extends Material {
	public MaterialHay(MapColor mapcolor){
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
}
