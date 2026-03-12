# 2D Action RPG — Java

A 2D top-down action RPG built from scratch in **pure Java** using Swing and `Graphics2D`, with no external game frameworks. The game features a complete game loop running at 60 FPS, collision detection, animated sprites, an inventory system, combat mechanics, NPC interaction, merchant trading, map transitions, and a full UI.

---

## Table of Contents

- [About the Game](#about-the-game)
- [Features](#features)
- [Controls](#controls)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Running from IntelliJ IDEA](#running-from-intellij-idea)
  - [Running from the Command Line](#running-from-the-command-line)
- [Configuration](#configuration)
- [Gameplay](#gameplay)
  - [Combat](#combat)
  - [Items & Equipment](#items--equipment)
  - [Character Progression](#character-progression)
  - [NPCs & Trading](#npcs--trading)
  - [Maps & Events](#maps--events)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [Technologies](#technologies)

---

## About the Game

This project is a fully functional 2D action RPG developed entirely in Java. The player explores interconnected maps, fights enemies, collects loot, manages an inventory, talks to NPCs, and levels up their character. The game is built without any third-party libraries — all rendering, input handling, collision detection, and audio are implemented from the ground up using the Java standard library.

---

## Features

- **60 FPS game loop** with fixed time-step via `GamePanel`
- **9 distinct game states:** Title, Play, Pause, Dialogue, Character/Inventory, Options, Game Over, Transition, Trade
- **4-directional movement** with smooth sprite animation
- **Melee and ranged combat** — swing weapons and cast fireballs
- **14 item types** including weapons, armour, consumables, currency, and quest items
- **20-slot inventory** with equip/unequip support
- **Experience and levelling system** with stat scaling
- **Enemy AI** with random movement and projectile attacks
- **2 NPCs** — an Old Man with story dialogue and a Merchant for trading
- **Destructible environment** — cut down trees with the axe
- **Event tiles** — healing pools, damage pits, and teleporters between maps
- **2 explorable maps** (exterior world + building interior), infrastructure for up to 10
- **Particle effects** on hit and on destruction
- **HUD** with health/mana bars, floating message log, and level display
- **Full-screen support** toggled from the Options menu
- **Audio system** with 1 background track and 13 sound effects
- **Persistent settings** via `config.txt` (fullscreen, music volume, SE volume)
- **Save/load** infrastructure ready for expansion

---

## Controls

| Key | Action |
|-----|--------|
| `W` | Move up |
| `A` | Move left |
| `S` | Move down |
| `D` | Move right |
| `Enter` | Attack / Interact (talk to NPCs, open chests, activate events) |
| `F` | Cast fireball (costs mana) |
| `P` | Pause / Unpause |
| `C` | Open Character / Inventory screen |
| `Escape` | Back / Close menu |
| `W` / `S` (in menus) | Navigate options up / down |
| `Enter` (in menus) | Confirm selection |

---

## Getting Started

### Prerequisites

- **Java JDK 8 or later** — [Download here](https://adoptium.net/)
- Game assets (sprites, tiles, fonts, audio) placed under `res/`

### Running from IntelliJ IDEA

1. Open the project folder in **IntelliJ IDEA**.
2. Make sure the JDK is configured (*File → Project Structure → SDK*).
3. Navigate to `src/main/Main.java`.
4. Right-click the file → **Run 'Main.main()'**.

The game window opens at **768 × 576** pixels (or full-screen if enabled in `config.txt`).

### Running from the Command Line

```bash
# Compile all sources
javac -d out $(find src -name "*.java")

# Run the game
java -cp out main.Main
```

> **Note:** The working directory must contain `config.txt`. If the file is missing, the game will use default settings (windowed, full volume).

---

## Configuration

Settings are persisted in **`config.txt`** (three lines, in order):

```
Off   # Fullscreen mode: On | Off
1     # Music volume (0.0 – 1.0)
1     # Sound effects volume (0.0 – 1.0)
```

You can change these values from the in-game **Options** menu, and they will be saved automatically.

---

## Gameplay

### Combat

- **Melee attack** — press `Enter` to strike in the direction the player is facing.  
  Damage = `Player Attack - Enemy Defense` (minimum 0).  
  After taking a hit, the player has **1 second of invulnerability**.
- **Ranged attack** — press `F` to launch a fireball. Casting costs mana; if mana is depleted, no fireball is fired.
- **Enemies** move randomly and have a small chance each frame to shoot a rock projectile at the player.
- Defeated enemies drop coins, health hearts, or mana crystals randomly.

### Items & Equipment

| Category | Items |
|----------|-------|
| Weapons | Normal Sword, Axe |
| Armour | Wood Shield, Blue Shield, Boots |
| Consumables | Red Potion, Heart, Mana Crystal |
| Projectiles | Fireball (player), Rock (enemies) |
| Currency | Bronze Coin |
| Quest | Key, Chest, Door |

Weapons and armour can be equipped from the **Inventory / Character** screen (`C`). Boots grant a movement speed bonus.

### Character Progression

- Defeating enemies awards **EXP**.
- Reaching the EXP threshold triggers a **level-up**, increasing core stats: Strength, Dexterity, Defense, and Attack.
- Health is represented as hearts (3 base + bonus); mana is consumed by spell casting.

### NPCs & Trading

- **Old Man** — approach and press `Enter` to trigger multi-line story dialogue.
- **Merchant** — open the trade screen to browse and buy items from the merchant's stock.

### Maps & Events

| Event | Location | Effect |
|-------|----------|--------|
| Healing Pool | Map 0 (specific tile) | Fully restores HP |
| Damage Pit | Map 0 (specific tile) | Deals damage to the player |
| Teleporter | Doorway between maps | Transitions between Map 0 and Map 1 with a fade animation |
| Dry Tree | Map 0 (various) | Destructible with the Axe; drops wood particle effects |

---

## Project Structure

```
2DActionRPGJava/
├── src/
│   ├── main/               # Core engine
│   │   ├── Main.java           # Entry point — creates the JFrame and GamePanel
│   │   ├── GamePanel.java      # Game loop, state machine, render pipeline
│   │   ├── KeyHandler.java     # Keyboard input for all game states
│   │   ├── UI.java             # HUD, menus, dialogue, inventory rendering
│   │   ├── CollisionChecker.java  # Tile / object / entity collision detection
│   │   ├── AssetSetter.java    # Spawns objects, NPCs, and monsters on each map
│   │   ├── EventHandler.java   # Event triggers (pools, pits, teleporters)
│   │   ├── Sound.java          # Background music and sound effect playback
│   │   ├── Config.java         # Load / save config.txt
│   │   └── UtilityTool.java    # Image scaling helpers
│   ├── entity/             # Characters and entities
│   │   ├── Entity.java         # Base class (position, animation, stats, inventory)
│   │   ├── Player.java         # Player logic — movement, combat, item pickup
│   │   ├── Projectile.java     # Projectile base class
│   │   ├── Particle.java       # Hit and destruction particles
│   │   ├── NPC_OldMan.java     # Story NPC with dialogue tree
│   │   └── NPC_Merchant.java   # Merchant NPC with trade inventory
│   ├── monster/            # Enemy types
│   │   └── MON_GreenSlime.java # Slime enemy — AI, stats, drop table
│   ├── object/             # Interactable items and objects (14 types)
│   │   ├── OBJ_Sword_Normal.java
│   │   ├── OBJ_Axe.java
│   │   ├── OBJ_Shield_Wood.java
│   │   ├── OBJ_Shield_Blue.java
│   │   ├── OBJ_Boots.java
│   │   ├── OBJ_Heart.java
│   │   ├── OBJ_ManaCrystal.java
│   │   ├── OBJ_Potion_Red.java
│   │   ├── OBJ_Fireball.java
│   │   ├── OBJ_Rock.java
│   │   ├── OBJ_Coin_Bronze.java
│   │   ├── OBJ_Key.java
│   │   ├── OBJ_Door.java
│   │   └── OBJ_Chest.java
│   ├── tiles/              # Tile system
│   │   ├── Tile.java           # Individual tile data (image, collision flag)
│   │   └── TileManager.java    # Load map files, render visible tiles
│   └── tile_interactive/   # Destructible / interactive map tiles
│       ├── InteractiveTile.java    # Base class with HP and destruction logic
│       ├── IT_DryTree.java         # Destroyable tree (3 HP, axe only)
│       └── IT_Trunk.java           # Stump left after tree is cut down
├── res/                    # Game assets (sprites, tiles, fonts, audio, maps)
├── config.txt              # Persistent settings (fullscreen, volume)
├── 2DActionRPG.iml         # IntelliJ IDEA module descriptor
└── .gitignore
```

---

## Architecture

- **State Machine** — `GamePanel` drives nine game states; `KeyHandler` and `UI` react differently depending on the active state.
- **Entity Inheritance** — all moving objects (player, NPCs, monsters, projectiles) extend `Entity`, which provides shared fields for position, animation, stats, and inventory.
- **Collision Pipeline** — `CollisionChecker` tests tile collision first, then object collision, then entity-to-entity collision in sequence each frame.
- **Layer-sorted Rendering** — entities are sorted by their Y-coordinate each frame so characters appear in front of or behind objects correctly.
- **Double Buffering** — `GamePanel` renders to an off-screen `BufferedImage` (`tempScreen`) before flipping to the display to eliminate flicker.
- **Manager Pattern** — dedicated managers (`TileManager`, `AssetSetter`, `EventHandler`, `Sound`) keep each concern isolated and testable.

---

## Technologies

| Technology | Usage |
|------------|-------|
| Java (JDK 8+) | Core language |
| Java Swing (`JPanel`, `JFrame`) | Window management and rendering surface |
| `java.awt.Graphics2D` | All 2D drawing — sprites, shapes, text |
| `javax.sound.sampled` | Audio playback (WAV files) |
| `javax.imageio.ImageIO` | Sprite and tile image loading |
| IntelliJ IDEA | IDE and project configuration |

