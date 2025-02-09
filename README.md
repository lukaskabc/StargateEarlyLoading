# Stargate Early Loading

_Not really a neoforge mod_ that replaces the game loading.
Yes, including the early neoforge loading with a custom one.

<details>
<summary>Screenshots</summary>
<img alt="Neoforge early loading" src="https://github.com/lukaskabc/StargateEarlyLoading/blob/main/docs/earlyloading1.png" />
<br>
<img alt="Minecraft loading" src="https://github.com/lukaskabc/StargateEarlyLoading/blob/main/docs/earlyloading2.png" />
</details>

## Setup

1. Place the jar in the mods directory.
2. Go to `config/fml.toml` and set

```toml
earlyWindowControl = true
earlyWindowProvider = "StargateEarlyLoading"
```

## Credits

A lot of source code (Stargate rendering) and resources (textures and configs/data definitions) were adapted from
a fantastic Minecraft mod [StargateJourney](https://github.com/Povstalec/StargateJourney)
made by mighty [Povstalec (Wold)](https://github.com/Povstalec).

Yes, you can use this _mod_ without StargateJourney, but ancients will hunt you in your dreams.

## Implementation

Since
a [commit to FancyModLoader](https://github.com/neoforged/FancyModLoader/commit/d492af572239803b10d1769027d661b14811161f)
by [Monica S.](https://github.com/FiniteReality), FML is able to discover
`ImmediateWindowProvider` service which allows to take control of the early game window and essentially (among other
things)
customize the loading.

This is also the reason why this is not really a _mod_.
It is a jar providing `ImmediateWindowProvider` service.
But for sake of simplicity lets call it a mod.

I believe that it is also possible with Forge using another service (e.g. `IDependencyLocator`)
and using ugly and bad reflection to hack into something like `ImmediateWindowHandler` replacing the
`ImmediateWindowProvider`.

### DisplayWindow

The implementation of `DisplayWindow` from (neo)forge is not well extensible.  
I think the main problem are inaccessible classes, interfaces (mainly `RenderElement` and its contents, then some small
ones - `SimpleFont`, `PerformanceInfo` and probably others),
methods and fields in `DisplayWindow`.

For this reason, I was forced to resort to a very desperate
approach - [reflection](https://github.com/lukaskabc/StargateEarlyLoading/tree/main/src/main/java/cz/lukaskabc/minecraft/mod_loader/loading/stargate_early_loading/reflection).
Yes it is bad, it is slow and everything what not.
BUT it works! (with some JVMs and without security managers...)

___

NOT AN OFFICIAL MINECRAFT PRODUCT/SERVICE/MOD. NOT APPROVED BY OR ASSOCIATED WITH MOJANG OR MICROSOFT