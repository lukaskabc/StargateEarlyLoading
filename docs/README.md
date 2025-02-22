# Customization

It is possible to use custom textures and variants
from resourcepacks made for StargateJourney.

**But a small edit is needed.**

In the [first release on CurseForge](https://www.curseforge.com/minecraft/mc-mods/stargate-early-loading/files/6218429/additional-files)
, there are StargateJourney: Refreshed and MoreGates addon ported as examples.

## Missing features

Some features are not yet implemented.

Those includes:

- encoded_symbols_glow
- engage_encoded_symbols

## Replacing default textures

Lets say you want to use textures from
[Stargate Journey: Refreshed](https://www.curseforge.com/minecraft/texture-packs/stargate-journey-refreshed)
by SGUDest1ny.

This will replace the default textures, it won't add new variants.

1. Download the resourcepack
2. In your config directory create folders:
    - `stargate-early-loading/assets/stargate/milky_way/default`
    - `stargate-early-loading/assets/stargate/milky_way/promo`
    - `stargate-early-loading/assets/stargate/milky_way/sg-1`
3. Open the SGJ: Refreshed resourcepack and find `assets/sgjourney/textures/entity/stargate/milky_way`
4. Copy folders `promo` and `sg-1` to `stargate-early-loading/assets/stargate/milky_way`
5. Copy textures from the resourcepack's `milky_way` to `stargate-early-loading/assets/stargate/milky_way/default`
6. Now, this mod uses the same texture (from default variant) for all variants. You need to change that in variant
   configs.  
   Find default configs
   [here](https://github.com/lukaskabc/StargateEarlyLoading/tree/main/src/main/resources/assets/stargate/milky_way)
7. Copy files `promo/promo.json`, `sg-1/sg-1.json` to their respective directories in your config directory
8. Update their `texture` and `engaged_texture` fields
   e.g. for the promo variant:

```json
{
  "texture": "assets/stargate/milky_way/promo/promo_stargate.png",
  "engaged_texture": "assets/stargate/milky_way/promo/promo_stargate_engaged.png",
  "symbols": {
    "symbol_color": {
      "red": 0.188,
      "green": 0.192,
      "blue": 0.247,
      "alpha": 1.0
    },
    "permanent_symbols": "milky_way:terra",
    "permanent_point_of_origin": "assets/symbols/milky_way/tauri_origin.png"
  },
  "stargate_model": {
    "movie_chevron_locking": true,
    "movie_primary_chevron": true
  }
}
```

## Adding new variants

Since in the latest StargateJourney version variants configs were moved to resourcepacks, you don't need the datapacks.

As an example I will take the
[Stargate Journey: MoreGates Addon](https://www.curseforge.com/minecraft/mc-mods/more-gates-mod-ver)
by Aspect_Xero.
This one is packaged as mod (.jar), just open the file with 7zip or similar as an archive and look into assets
directory.

Let's add the milky_way crafter variant.

1. Download the mod and open it as an archive
2. In your config directory create folder:
    - `stargate-early-loading/assets/stargate/milky_way/more_gates_crafter`
3. Copy textures from the mod
   `assets/moregates/textures/entity/stargate/milky_way/crafter`
   to the created directory in config directory
   (you don't need textures for event horizon, only `crafter_stargate.png` and `crafter_stargate_engaged.png`).
4. Now you need to copy the variant config from the mod
   `assets/moregates/sgjourney/stargate_variant/milky_way/crafter.json`
   to your config directory
   `stargate-early-loading/assets/stargate/milky_way/more_gates_crafter/more_gates_crafter.json`
5. Open the copied config, we need to make some changes:
6. You can delete:
    - `wormhole_sounds`
    - `wormhole`
    - `chevron_encode_sounds`
    - `chevron_open_sounds`
    - `fail_sounds`
    - `shiny_wormhole`
    - `rotation_sounds`
    - `chevron_engaged_sounds`
    - `chevron_incoming_sounds`
    - `engage_symbols_on_incoming`

You can check possible configuration options in
[
`Config`](https://github.com/lukaskabc/StargateEarlyLoading/blob/main/src/main/java/cz/lukaskabc/minecraft/mod_loader/loading/stargate_early_loading/Config.java)
and [
`stargate/variant`](https://github.com/lukaskabc/StargateEarlyLoading/tree/main/src/main/java/cz/lukaskabc/minecraft/mod_loader/loading/stargate_early_loading/stargate/variant)

7. All variants requires `symbols.permanent_symbols` and `symbols.permanent_point_of_origin` fields.
   The loading will crash without them!
8. After updating fields
    - `symbols.permanent_symbols`
    - `symbols.permanent_point_of_origin`
    - `texture`
    - `engaged_texture`

The resulting `more_gates_crafter.json` should look like this:

```json
{
  "symbols": {
    "permanent_symbols": "milky_way:terra",
    "permanent_point_of_origin": "assets/symbols/milky_way/tauri_origin.png",
    "encoded_symbols_glow": true,
    "engage_encoded_symbols": true,
    "engage_symbols_on_incoming": true,
    "symbol_color": {
      "alpha": 1,
      "red": 0.10588235294118,
      "blue": 0.10588235294118,
      "green": 0.10588235294118
    },
    "encoded_symbol_color": {
      "alpha": 1,
      "red": 0.29803921568627,
      "blue": 0,
      "green": 1
    },
    "engaged_symbol_color": {
      "alpha": 1,
      "red": 0.29803921568627,
      "blue": 0,
      "green": 1
    }
  },
  "stargate_model": {
    "movie_chevron_locking": true,
    "movie_primary_chevron": true
  },
  "texture": "assets/stargate/milky_way/more_gates_crafter/crafter_stargate.png",
  "engaged_texture": "assets/stargate/milky_way/more_gates_crafter/crafter_stargate_engaged.png"
}
```

Since at the time of writing this, MoreGates are not updated for newest StargateJourney.
Fields `movie_chevron_locking` and `movie_primary_chevron` had to be moved into `stargate_model` object.

9. And the very last step is to register the variant in `stargate-early-loading.json`

```json
{
  "variants": {
    "milky_way": [
      "more_gates_crafter"
    ]
  }
}
```

## Adding symbols

To add symbols, register their name, texture and size in `stargate-early-loading.json`.
Then you can use the name in variant config.
