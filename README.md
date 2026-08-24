![Title : "Content Packs"](https://raw.githubusercontent.com/Moineau404/ContentPacksMC/master/.assets/title_large.png)

# Introduction

**Content** is a new pack type (such as resource and data) that allows creation of various objects that could otherwise only be defined with code.
It aims to offer an alternative to basic modding by allowing creators to use an intuitive format, stable across Minecraft versions, rather than committing to the maintenance of a code project.

# Content pack type

⚠ _Disclaimer : due to the way Minecraft is coded, content is only loaded at game start (allowing for an in-game reload would recquire a complete rewrite of Minecraft itself lol)._

Content packs are located in the ```<game_dir>/contentpacks``` folder, and can be a folder or a zip file (like other pack types).
It can load ```content```, but also automatically client resources (```assets```) and server data (```data```) depending on active content packs (similar to a mod).
Active packs and their loading order can be configured via a menu with a new button at the bottom of the options screen.

# Features

- Various JSON objects :
    - Armor Materials
    - Blocks
    - Block Set Types
    - Boat Types
    - Color Resolvers
    - Creative Mode Tabs
    - Items
    - Sound Types
    - Tool Materials
    - Tree Growers
    - Wood Types
- Rudimentary error monitoring.
- Most of vanilla objects serialized.
- Extension support for mod makers.

<details>
<summary>Planned features</summary>

- Documentation.
- JSON block collision boxes.
- JSON mob variants.
- Advanced support for EMF, ETF, Polytone and maybe other mods.
- Web interface for creating objects (like [Misode's Data Pack Generators](https://misode.github.io/)).

</details>

# Examples

### Blocks

<details>
<summary>Grass Block</summary>

```minecraft/block/grass_block.json```

```
{
  "type": "minecraft:grass",
  "properties": {
    "map_color": "grass",
    "sound_type": "minecraft:grass",
    "explosion_resistance": 0.6,
    "destroy_time": 0.6,
    "random_ticks": true
  }
}
```
</details>

<details>
<summary>Oak Leaves</summary>

```minecraft/block/oak_leaves.json```

```
{
  "type": "minecraft:tinted_particle_leaves",
  "leaf_particle_chance": 0.01,
  "properties": {
    "map_color": "plant",
    "sound_type": "minecraft:grass",
    "explosion_resistance": 0.2,
    "destroy_time": 0.2,
    "random_ticks": true,
    "occlusion": false,
    "ignited_by_lava": true,
    "push_reaction": "destroy"
  }
}
```
</details>

<details>
<summary>Spruce Sapling</summary>

```minecraft/block/spruce_sapling.json```

```
{
  "type": "minecraft:sapling",
  "tree": "minecraft:spruce",
  "properties": {
    "map_color": "plant",
    "collision": false,
    "sound_type": "minecraft:grass",
    "random_ticks": true,
    "occlusion": false,
    "push_reaction": "destroy"
  }
}
```
</details>

<details>
<summary>Birch Sign</summary>

```minecraft/block/birch_sign.json```

```
{
  "type": "minecraft:standing_sign",
  "wood_type": "minecraft:birch",
  "properties": {
    "map_color": "sand",
    "collision": false,
    "sound_type": "minecraft:wood",
    "explosion_resistance": 1.0,
    "destroy_time": 1.0,
    "occlusion": false,
    "ignited_by_lava": true,
    "force_solid": true,
    "instrument": "bass"
  }
}
```
</details>

### Items

<details>
<summary>Oak Planks</summary>

```minecraft/item/oak_planks.json```

```
{
  "type": "minecraft:block",
  "block": "minecraft:oak_planks",
  "properties": {
    "components": {}
  }
}
```
</details>

<details>
<summary>Spruce Sign</summary>

```minecraft/item/spruce_sign.json```

```
{
  "type": "minecraft:sign",
  "block": "minecraft:spruce_sign",
  "wall_block": "minecraft:spruce_wall_sign",
  "properties": {
    "components": {
      "minecraft:max_stack_size": 16
    }
  }
}
```
</details>

<details>
<summary>Zombie Spawn Egg</summary>

```minecraft/item/zombie_spawn_egg.json```

```
{
  "type": "minecraft:spawn_egg",
  "properties": {
    "components": {
      "minecraft:entity_data": {
        "id": "minecraft:zombie"
      }
    }
  }
}
```
</details>

### Other

<details>
<summary>Armor Material (Iron)</summary>

```minecraft/armor_material/iron.json```

```
{
  "durability": 15,
  "defense": {
    "helmet": 2,
    "chestplate": 6,
    "leggings": 5,
    "boots": 2,
    "body": 5
  },
  "enchantability": 9,
  "equip_sound": "minecraft:item.armor.equip_iron",
  "toughness": 0.0,
  "knockback_resistance": 0.0,
  "repair_items": "#minecraft:repairs_iron_armor",
  "asset": "minecraft:iron"
}
```
</details>

<details>
<summary>Block Set Type (Stone)</summary>

```minecraft/block_set_type/stone.json```

```
{
  "open_by_hand": true,
  "open_by_wind_charge": true,
  "button_activated_by_arrows": false,
  "pressure_plate_sensivity": "mobs",
  "sound_type": "minecraft:stone",
  "door_close": "minecraft:block.iron_door.close",
  "door_open": "minecraft:block.iron_door.open",
  "trapdoor_close": "minecraft:block.iron_trapdoor.close",
  "trapdoor_open": "minecraft:block.iron_trapdoor.open",
  "pressure_plate_off": "minecraft:block.stone_pressure_plate.click_off",
  "pressure_plate_on": "minecraft:block.stone_pressure_plate.click_on",
  "button_off": "minecraft:block.stone_button.click_off",
  "button_on": "minecraft:block.stone_button.click_on"
}
```
</details>

<details>
<summary>Sound Type (Copper)</summary>

```minecraft/sound_type/copper.json```

```
{
  "break": "minecraft:block.copper.break",
  "step": "minecraft:block.copper.step",
  "place": "minecraft:block.copper.place",
  "hit": "minecraft:block.copper.hit",
  "fall": "minecraft:block.copper.fall"
}
```
</details>

<details>
<summary>Tree Grower (Oak)</summary>

```minecraft/tree_grower/oak.json```

```
{
  "secondary_chance": 0.1,
  "tree": "minecraft:oak",
  "secondary_tree": "minecraft:fancy_oak",
  "flowers": "minecraft:oak_bees_005",
  "secondary_flowers": "minecraft:fancy_oak_bees_005"
}
```
</details>

_You may want to download the [test pack](https://raw.githubusercontent.com/Moineau404/ContentPacksMC/master/.assets/test/willow-wood-9.0.zip) shown in the gallery._

# Known limitations

- **Content is loaded at game start : reload means game restart.** (hard limitation!)
- Complexity of blocks and items are limited with json (especially concerning functions, predicates, etc) as it depends on what was made serializable.

# Known bugs

- **Content Packs is in alpha, it may contain bugs, incomplete features and its format may not be stable accross versions!**