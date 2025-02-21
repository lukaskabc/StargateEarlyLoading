# About implementation

Since
a [commit to FancyModLoader](https://github.com/NeoForged/FancyModLoader/commit/d492af572239803b10d1769027d661b14811161f)
by [Monica S.](https://github.com/FiniteReality), FML can discover
`ImmediateWindowProvider` service, which allows it to take control of the early game window and essentially (among other
things)
customize the loading.

That is why this is not really a _mod_ (it will not be listed in-game among other mods).
It is a jar providing `ImmediateWindowProvider` service.
But for the sake of simplicity let's call it a mod.

I believe it is also possible for Forge to use another service (e.g. `IDependencyLocator`)
and use ugly and bad reflection to hack into something like `ImmediateWindowHandler` to replace the
`ImmediateWindowProvider`.

## DisplayWindow

The implementation of `DisplayWindow` from (neo)forge is not well extensible.  
I think the main problems are inaccessible classes, interfaces (mainly `RenderElement` and its contents, then some small
ones - `SimpleFont`, `PerformanceInfo`, and probably others),
methods, and fields in `DisplayWindow`.

For this reason, I was forced to resort to a very desperate
approach - [reflection](https://github.com/lukaskabc/StargateEarlyLoading/tree/main/src/main/java/cz/lukaskabc/minecraft/mod_loader/loading/stargate_early_loading/reflection).  
Yes, it is terrible, slow and whatnot.  
BUT it works! (with some JVMs and without security managers...)

## Programming stuff

So the main class is
[
`StargateEarlyWindow`](https://github.com/lukaskabc/StargateEarlyLoading/blob/main/src/main/java/cz/lukaskabc/minecraft/mod_loader/loading/stargate_early_loading/StargateEarlyLoadingWindow.java)  
The configuration is loaded early in the class construction.  
It loads config, selects random stargate variant and loads its configuration.    
During initialization, the color scheme is forced to black (`Runnable initialize(String[] arguments)`).  
The `start` method had to be overridden for injecting `afterInitRender` method.  
The `afterInitRender` method drops the context with frame buffer created in default DisplayWindow implementation,
clears render elements and injects new ones.  
Then it was required to solve the inaccessible interfaces that are required for the construction of a new
`RenderElement`.
That was made using dynamic proxies in [
`RefRenderElement`](https://github.com/lukaskabc/StargateEarlyLoading/blob/main/src/main/java/cz/lukaskabc/minecraft/mod_loader/loading/stargate_early_loading/reflection/RefRenderElement.java)
class.

And that is somehow it, the rest is rendering with OpenGL without access to any minecraft api and java libraries since
they are not even loaded yet.

