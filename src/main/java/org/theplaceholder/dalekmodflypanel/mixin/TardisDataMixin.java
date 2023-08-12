package org.theplaceholder.dalekmodflypanel.mixin;

import com.swdteam.common.tardis.TardisData;
import org.spongepowered.asm.mixin.Mixin;
import org.theplaceholder.dalekmodflypanel.interfaces.ITardisData;

@Mixin(TardisData.class)
public class TardisDataMixin implements ITardisData {
    private boolean inFlightMode;

    @Override
    public void dalekmodflypanel$setInFlightMode(boolean inFlightMode) {
        this.inFlightMode = inFlightMode;
    }

    @Override
    public boolean dalekmodflypanel$isInFlightMode() {
        return inFlightMode;
    }
}
