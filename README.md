# Stargate Early Loading

_Not really a NeoForge mod_ that replaces the game loading.  
Yes, including the NeoForge's **early loading** with a custom **animated** one, see video below.

**Note that this _mod_ is still not considered as stable**

<details>
    <summary>Video</summary>
    <img alt="NeoForge early loading" src="https://github.com/lukaskabc/StargateEarlyLoading/blob/main/docs/earlyloading1.png" />
    <br>
    <h2>ATM10 with the custom loading</h2>
    <p>
        <a href=" https://www.curseforge.com/minecraft/modpacks/all-the-mods-10">All The Mods 10</a> 
        modpack was used for demonstration of longer loading time.
    </p>
</details>

## Setup

How to install the _mod_

1. Place the jar in the `mods` directory.
2. Go to `config/fml.toml` and set:

```toml
earlyWindowControl = true
earlyWindowProvider = "StargateEarlyLoading"
# I also recommend to set
earlyWindowFBScale = 1
earlyWindowMaximized = true
```

3. **Optionally** install `DelayedLoadingOverlay`, which will extend the game loading until the stargate animation is
   finished.

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

This _mod_ should not change any graphics/OpenGL settings applied by neoforge by default.
If you cant launch the game, try it without the mod first to ensure you can launch the neoforge on its own.

## Implementation

For implementation details
see [about implementation](https://github.com/lukaskabc/StargateEarlyLoading/blob/main/docs/README.md)
___

NOT AN OFFICIAL MINECRAFT PRODUCT/SERVICE/MOD. NOT APPROVED BY OR ASSOCIATED WITH MOJANG OR MICROSOFT