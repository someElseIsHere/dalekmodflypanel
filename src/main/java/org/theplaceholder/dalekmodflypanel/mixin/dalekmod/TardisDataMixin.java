package org.theplaceholder.dalekmodflypanel.mixin.dalekmod;

import com.swdteam.common.tardis.TardisData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.theplaceholder.dalekmodflypanel.interfaces.ITardisData;

@Mixin(value = TardisData.class, remap = false)
public class TardisDataMixin implements ITardisData {
    @Unique
    private boolean dalekmodflypanel$inFlightMode;

    @Override
    public void dalekmodflypanel$setInFlightMode(boolean inFlightMode) {
        this.dalekmodflypanel$inFlightMode = inFlightMode;
    }

    @Override
    public boolean dalekmodflypanel$isInFlightMode() {
        return dalekmodflypanel$inFlightMode;
    }
}
