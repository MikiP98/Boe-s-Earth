# Boe's Earth

## Description

Boe's Earth is a mod that adds additional blockstated to couple of vanilla blocks.  
Those blockstates may be later used by shaders or resourcepacks to improve visual fidelity.

## Added blockstates

- `is_on_leaves`:
  Added to **Snow**, **Vines** and **Carpets**  
  Example usage (Bliss shader):
  - Snow on top of waving leaves will wave alongside the leaves
  - Vines supported by/based on waving leaves will wave with them

- `snow_on_top`:
  Added to **leaves**  
  Example usage:
  - Leaves with snow on top will have different texture

## FAQ

**Q:** *Snow/Vines are not waving*  
**A:** *First make sure that your shader supports Boe's Earth, e.g. `Bliss`. Secondly, naturally generated blocks always have the custom blockstates disabled, they will however update with time. If you want to speed up the updating process, increase the `randomTickSpeed` gamerule, and decrease it back after to it's default `3`/`5`*

**Q:** *Forge version?*  
**A:** *The mod should work with Syntra Connector, though that is not a guarantee in the future*

## Documentation

The mods adds just another blockstates to existing blocks.  
The usage of those blockstates stays the same as for vanilla blockstates and any other blockstates added by different mods.

Outside of that, the mod also adds an Iris shader define named `BOES_EARTH_BLOCKSTATES`.  
It can be used to add Boe's Earth blockstates support to your shader without breaking compatibility with vanilla blockstates.  
That does **not** mean Boe's Earth blockstates are incompatible with Optifine or other shader loaders, but there will be no way to knowing shader-side if the mod is present or not!

### Example usage - shader

Iris *(vanilla compatible)*:
```properties
#ifdef BOES_EARTH_BLOCKSTATES
block.56=minecraft:birch_leaves minecraft:acacia_leaves snow:is_on_leaves=true vine:is_on_leaves=true blue_carpet:is_on_leaves=true moss_carpet:is_on_leaves=true
#else
block.56=minecraft:birch_leaves minecraft:acacia_leaves
#endif
```

Other shader loaders *(**not** vanilla compatible)*:
```properties
block.56=minecraft:birch_leaves minecraft:acacia_leaves snow:is_on_leaves=true vine:is_on_leaves=true blue_carpet:is_on_leaves=true moss_carpet:is_on_leaves=true
```

### Example usage - resourcepack

```json
{
  "variants": {
    "layers=1,is_on_leaves=false": {
      "model": "minecraft:block/snow_height2"
    },
    "layers=2,is_on_leaves=false": {
      "model": "minecraft:block/snow_height4"
    },
    "layers=1,is_on_leaves=true": {
      "model": "minecraft:block/snow_on_leaves_height2"
    },
    "layers=2,is_on_leaves=true": {
      "model": "minecraft:block/snow_on_leaves_height4"
    }
  }
}
```
Or

```json
{
  "multipart": [
    {
      "when": { "AND": [
        {"layers": "1" },
        {"is_on_leaves": false }
      ]},
      "apply": { "model": "minecraft:block/snow_height2" }
    },
    {
      "when": { "AND": [
        {"layers": "2" },
        {"is_on_leaves": false }
      ]},
      "apply": { "model": "minecraft:block/snow_height4" }
    },
    {
      "when": { "AND": [
        {"layers": "1" },
        {"is_on_leaves": true }
      ]},
      "apply": { "model": "minecraft:block/snow_on_leaves_height2" }
    },
    {
      "when": { "AND": [
        {"layers": "2" },
        {"is_on_leaves": true }
      ]},
      "apply": { "model": "minecraft:block/snow_on_leaves_height4" }
    }
  ]
}
```