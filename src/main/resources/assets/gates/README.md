# Making assets

## Stargate Base

1. Open IntelliJ IDEA with StargateJourney
2. Launch dev instance and create a new world  
   few setup commands

```
/gamerule doDaylightCycle false
/gamerule doWeatherCycle false
/time set noon
/setblock 4 104 0 stone
/setblock 10 106 0 gold_block
/tp @p 10 107 0 90 0
```

Now you are standing centered on a gold block and facing a stone block.
Place the stargate on the stone block.

Clear symbols from the gate.

```
/data merge block 4 105 0 {Symbols: "sgjourney:empty", PointOfOrigin: "sgjourney:empty"}
```

- Disable fly (land on a ground) - otherwise you have different FOV
- Set FOV to normal and brightness to max
- `F11` to enter full screen mode (use FullHD resolution)
- `F1` to disable UI
- Use the `tp` command again to center the view on the stargate
- `F2` to take a screenshot of the gate

You will need following screenshots:

1. the inactive stargate

-------------

2. the inactive stargate without chevrons
   In the stargate class (e.g. `net.povstalec.sgjourney.client.models.PegasusStargateModel`)
   comment out line `this.renderChevrons` in the `renderStargate` method,
   reload the class to apply changes and take a screenshot.

-------------

Additionally for MW gate:

3. take a screenshot with disabled symbol ring dividers
   (`net.povstalec.sgjourney.client.models.GenericStargateModel#renderSymbolRing`)
   and with some unified color for the symbol ring

That will allow to remove the ring from the base texture in photoshop.
After that, the base stargate texture can be rendered above the rotating ring with symbols.

The final trimmed stargate base texture should be `954x947`.