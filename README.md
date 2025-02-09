# Stargate Early Loading

_Not really a NeoForge mod_ that replaces the game loading.  
Yes, including the NeoForge's **early loading** with a custom **animated** one, see screenshots below.

**Note that this is still not ready for real usage.**

<details>
<summary><b>Screenshots</b></summary>
<img alt="NeoForge early loading" src="https://github.com/lukaskabc/StargateEarlyLoading/blob/main/docs/earlyloading1.png" />
<br>
<img alt="Minecraft loading" src="https://github.com/lukaskabc/StargateEarlyLoading/blob/main/docs/earlyloading2.png" />
</details>

## Setup

1. Place the jar in the `mods` directory.
2. Go to `config/fml.toml` and set:

```toml
earlyWindowControl = true
earlyWindowProvider = "StargateEarlyLoading"
earlyWindowFBScale = 1
# I also recommend to set
earlyWindowMaximized = true
```

## Customization

It is possible to apply stargate variants from resourcepacks made
for [StargateJourney](https://github.com/Povstalec/StargateJourney).  
[Please see docs for more information](https://github.com/lukaskabc/StargateEarlyLoading/tree/main/docs).

## Credits

A lot of source code (Stargate rendering) and resources (textures and configs/data definitions) were adapted from
a fantastic Minecraft mod [StargateJourney](https://github.com/Povstalec/StargateJourney)
made by mighty [Povstalec (Wold)](https://github.com/Povstalec).

Yes, you can use this _mod_ without StargateJourney, but the ancients will hunt you in your dreams.

## Game start problems

This _mod_ should not change any graphics settings applied by neoforge by default.
If you cant launch the game, try it without the mod first to ensure you can launch the neoforge.

## Implementation

Since
a [commit to FancyModLoader](https://github.com/NeoForged/FancyModLoader/commit/d492af572239803b10d1769027d661b14811161f)
by [Monica S.](https://github.com/FiniteReality), FML can to discover
`ImmediateWindowProvider` service, which allows it to take control of the early game window and essentially (among other
things)
customize the loading.

That is why this is not really a _mod_ (it will not be listed in-game among other mods).
It is a jar providing `ImmediateWindowProvider` service.
But for the sake of simplicity let's call it a mod.

I believe it is also possible for Forge to use another service (e.g. `IDependencyLocator`)
and use ugly and bad reflection to hack into something like `ImmediateWindowHandler` to replace the
`ImmediateWindowProvider`.

### DisplayWindow

The implementation of `DisplayWindow` from (neo)forge is not well extensible.  
I think the main problems are inaccessible classes, interfaces (mainly `RenderElement` and its contents, then some small
ones - `SimpleFont`, `PerformanceInfo`, and probably others),
methods, and fields in `DisplayWindow`.

For this reason, I was forced to resort to a very desperate
approach - [reflection](https://github.com/lukaskabc/StargateEarlyLoading/tree/main/src/main/java/cz/lukaskabc/minecraft/mod_loader/loading/stargate_early_loading/reflection).  
Yes, it is terrible, slow and whatnot.  
BUT it works! (with some JVMs and without security managers...)

<details>
<summary>Some programming stuff</summary>
So the main class is
<a href="https://github.com/lukaskabc/StargateEarlyLoading/blob/main/src/main/java/cz/lukaskabc/minecraft/mod_loader/loading/stargate_early_loading/StargateEarlyLoadingWindow.java"><code>StargateEarlyWindow</code></a>.<br>
The configuration is loaded early in the class construction.<br>
It loads config, selects random stargate variant and loads its configuration.<br>  
During initialization, the color scheme is forced to black (<code>Runnable initialize(String[] arguments)</code>).<br>
The <code>start</code> method had to be overridden for injecting <code>afterInitRender</code> method.<br>
The <code>afterInitRender</code> method drops the context with frame buffer created in default DisplayWindow implementation,
clears render elements and injects new ones.<br>
Then it was required to solve the inaccessible interfaces that are required for the construction of a new <code>RenderElement</code>.
That was made using dynamic proxies in <a href="https://github.com/lukaskabc/StargateEarlyLoading/blob/main/src/main/java/cz/lukaskabc/minecraft/mod_loader/loading/stargate_early_loading/reflection/RefRenderElement.java"><code>RefRenderElement</code></a> class.
<br><br>
And that is somehow it, the rest is rendering with OpenGL without access to any minecraft api and java libraries since they are not even loaded yet.
</details>

___

NOT AN OFFICIAL MINECRAFT PRODUCT/SERVICE/MOD. NOT APPROVED BY OR ASSOCIATED WITH MOJANG OR MICROSOFT