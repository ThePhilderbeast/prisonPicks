# Prison Picks

## Status
[![Build Status](https://travis-ci.org/ThePhilderbeast/prisonPicks.svg?branch=master)](https://travis-ci.org/ThePhilderbeast/prisonPicks) [![codecov](https://codecov.io/gh/ThePhilderbeast/prisonPicks/branch/master/graph/badge.svg)](https://codecov.io/gh/ThePhilderbeast/prisonPicks)


## What is it
Prison picks is a Spigot Plugin to provide a number of interesting Picks for use on a prison minecraft server including the an explosive pick, the pick o' Plenty and the Explosive pick o' plenty!

## Features
Custom Diamond Picks:

 * The Explolosive pick is a diamond pickaxe that mines all connected block to the block that is hit (up to a 3x3x3 area)

 * The Pick o' Plenty is is fortune on drugs, it will make each block mined with it become the highest value block that is connected to the block being mined, the following values are used for the ores:
   * Anything not specified below 
   * Coal Ore
   * Iron ore
   * Redstone Ore
   * Lapis Ore
   * Gold Ore
   * Quartz Ore
   * Diamond Ore
   * Diamonnd Blocks
   * Emerald Ore

 * The Explosive Pick o' Plenty is a combination of the previous 2 picks, it will mine all connected blocks AND turn them in to the highest value ore (using the same value list as the pick o' plenty)

## Commands
the picks are not added to any form or world generation, nor are there any recipes for them they can only be spawned with the following command:
/pick (explosive|pickoplenty|xpickoplenty) (player to receive pick)

## Permissions

picks.explosive - allows players to use the commands to spawn picks

## Configuration
 * defaults are in bold

worldguard_flag_enable = (true|__false__) - this sets if the picks special effects are allowed in world guard regions by default or not, requires a server reload to change
debug =  = (true|__false__) enables debug output, only enable this if asked by a developer.
