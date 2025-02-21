# About implementation

Since
a [commit to FancyModLoader](https://github.com/NeoForged/FancyModLoader/commit/d492af572239803b10d1769027d661b14811161f)
by [Monica S.](https://github.com/FiniteReality), FML can discover
`ImmediateWindowProvider` service, which allows to take control of the early game window and essentially (among other
things)
customize the loading.

That is why this is not really a _mod_ (it will not be listed in-game among other mods).
It is a jar providing `ImmediateWindowProvider` service.
But for the sake of simplicity let's call it a mod.

Even if it was packaged as a mod, it seems that it won't be loaded by FML during the loading.
That is also the reason why the `DelayedLoadingOverlay` is implemented as a separate mod.

I believe it is also possible for Forge to use another service (e.g. `IDependencyLocator`)
and use some bad reflection to hack into something like `ImmediateWindowHandler` to replace the
`ImmediateWindowProvider`.
For now, I am not going to do that.

## DisplayWindow

The implementation of `DisplayWindow` from (neo)forge is not well extensible.  
IMHO the main problems are inaccessible classes, interfaces (mainly `RenderElement` and its contents, then some small
ones - `SimpleFont`, `PerformanceInfo`, and probably others),
`DisplayWindow` methods, and fields.

For this reason, I was forced to resort to a very desperate
approach - [reflection](https://github.com/lukaskabc/StargateEarlyLoading/tree/main/src/main/java/cz/lukaskabc/minecraft/mod_loader/loading/stargate_early_loading/reflection).  
Yes, it is terrible, probably slow and whatnot.  
BUT it works! (with some JVMs and without security managers...).

## Programming stuff

So the main class is
[
`StargateEarlyLoadingWindow`](https://github.com/lukaskabc/StargateEarlyLoading/blob/main/src/main/java/cz/lukaskabc/minecraft/mod_loader/loading/stargate_early_loading/StargateEarlyLoadingWindow.java)  
The configuration is loaded early in the class construction.  
It loads config, selects random stargate variant and loads its configuration.  
This delays the window creation until the config is loaded.
It's probably better than showing a white window instead.

During initialization, the color scheme is forced to black (`Runnable initialize(String[] arguments)`).  
The `start` method had to be overridden for injecting `afterInitRender` method.  
The `afterInitRender` method drops the context with frame buffer created in default DisplayWindow implementation,
clears render elements and injects new ones.   
Which also overrides the default loading resolution set by the original `DisplayWindow`.

Then it was required to solve the inaccessible interfaces that are required for the construction of a new
`RenderElement`.
That was made using dynamic proxies in [
`RefRenderElement`](https://github.com/lukaskabc/StargateEarlyLoading/blob/main/src/main/java/cz/lukaskabc/minecraft/mod_loader/loading/stargate_early_loading/reflection/RefRenderElement.java)
class.

That is probably the worst part as Renderer is called several times per frame,
and I did not find a way other than that.

And that is somehow it, the rest is rendering with OpenGL without access to any minecraft api since it is not even
loaded yet.
As mentioned the Stargate rendering and textures were copied and altered from
[StargateJourney](https://github.com/Povstalec/StargateJourney) by [Povstalec (Wold)](https://github.com/Povstalec).
