# Project: Darwin World

#### [Original instructions source](https://github.com/Soamid/obiektowe-lab/blob/master/proj/Readme.md)

This content has been adapted by Aleksander Smywiński-Pohl based on descriptions and illustrations prepared by Wojciech Kosior. His inspiration came from the book "Land of Lisp" by Conrad Barski, which in turn was inspired by an article in "Scientific American." Finally, modifications were introduced by Radosław Łazarz, partially based on the book "Genetic Algorithms in Search, Optimization, and Machine Learning" by David E. Goldberg. Quite a few people for a single project. :-)

## Project Goal

Let's create a game! However, this won't be a game that we play ourselves. Instead, it will be a world that evolves before our eyes! We will create an environment of steppes and jungles with animals that run, forage, eat, and reproduce. After several million years, we may see them evolve into different species!

The world of our game is quite simple. It consists of a rectangular grid divided into square tiles. Most of the world is covered by steppes, where little vegetation grows, serving as food for animals. However, some regions are overgrown with jungle, where plants grow much faster. Plants will sprout randomly, but their concentration will be higher in the jungle than on the steppes.

## Anatomy of an Animal

We need to track several characteristics of each animal. First, just like plants, animals have `x` and `y` coordinates, which indicate their position on the map.

We also need to track each animal's energy level. This is a Darwinian survival game, so if an animal fails to obtain enough food, it will starve and die. Energy determines how many days an animal can continue functioning. It must find more food before its energy runs out.

Each animal also has a direction it faces, which is important because it moves across the map in that direction every day. There are eight possible directions and corresponding rotations. A rotation of `0` means no change, `1` rotates 45°, `2` rotates 90°, and so on. For example, if an animal is facing north and rotates `1`, it will now face northeast.

Finally, each animal has a set of genes. Every animal has `N` genes, each represented by a number between `0` and `7`. These genes describe the creature's behavior in a simplified way. The animals follow a cyclical pattern of behavior. Each animal stores information about which part of its genome will be used the next day. During each movement, the animal first rotates according to the active gene and then moves one step in that direction. The active gene is then deactivated, and the next one in sequence is activated. If the genes run out, activation loops back to the beginning of the list. For example, the genome:
`0 0 7 0 4`
means the creature will move forward, forward, slightly left, forward, turn around, forward, and so on.

## Eating and Reproduction

Eating is a simple process. An animal eats a plant when it steps onto its tile, gaining a predefined amount of energy.

Reproduction is often the most interesting part of any animal simulation. Only healthy parents can produce healthy offspring, so animals will reproduce only if they have enough energy. When reproducing, parents lose some of their energy, which becomes the starting energy of their offspring.

The newborn animal inherits a genotype that is a combination of both parents' genes. The proportion of genes is based on parental energy levels and determines the split point in the genome. For instance, if one parent has 50 energy points and the other has 150, the offspring will receive 25% of the first parent's genes and 75% of the second's. This distribution determines where the genetic crossover occurs. A random choice decides whether the stronger parent's genes come from the left or right side of its genome. Finally, mutations occur: a random number of genes (selected randomly) in the offspring are changed to completely new values.

## Simulation Process

Each simulated day consists of the following steps:

1. Remove dead animals from the map.
2. Rotate and move each animal.
3. Animals consume plants on the tiles they land on.
4. Sufficiently fed animals on the same tile reproduce.
5. New plants grow on selected tiles of the map.

The simulation is described by several parameters:

* Map height and width
* Map variant (explained below)
* Initial number of plants
* Energy gained per plant consumed
* Number of plants growing daily
* Plant growth variant (explained below)
* Initial number of animals
* Initial animal energy
* Energy required for an animal to be considered well-fed (and ready to reproduce)
* Energy cost of reproduction for parents
* Minimum and maximum number of mutations in offspring (can be `0`)
* Mutation variant (explained below)
* Length of animal genome
* Animal behavior variant (explained below)

## Application Requirements

1. The application should use a graphical user interface implemented with JavaFX.
2. It must allow running simulations with selected configurations:
   * Selecting a predefined configuration
   * Customizing a new configuration
   * Saving configurations for future use
3. Starting a simulation should open a new window for that simulation.
   * Multiple simulations can run simultaneously, each in its own window with its own map.
4. The simulation display should show an animation representing animal positions, energy levels (e.g., via color or a health bar), and plant locations.
5. The program should allow pausing and resuming the simulation at any time (independently for each map).
6. The program must track and display the following statistics:
   * Total number of animals
   * Total number of plants
   * Number of empty tiles
   * Most common genotypes
   * Average energy level of living animals
   * Average lifespan of dead animals (since the start of the simulation)
   * Average number of offspring for living animals
7. After pausing, a user can mark an animal for tracking. The UI should then display:
   * Its genome
   * The currently active gene
   * Its energy level
   * Number of plants eaten
   * Number of direct offspring
   * Number of total descendants
   * Its age (if alive)
   * The day it died (if deceased)
8. After pausing, the user can also:
   * Highlight animals with the dominant genotype
   * Highlight preferred plant-growing areas
9. If enabled at the start of a simulation, daily statistics should be saved to a CSV file, readable by programs like MS Excel.
10. The application should be buildable and runnable using Gradle.

## Grading

The project can earn a total of **32xp** (plus potential bonuses from project challenges). Evaluation will consider:
1. Functionality (16xp) - completeness of features, meeting all requirements, and at least minimally user-friendly UI.
2. Code quality (16xp)
   * Architecture - problem decomposition, application model design, use of design patterns
   * Clean code - readability, adherence to SOLID principles, naming conventions, etc.
   * Performance and implementation - use of appropriate tools and algorithms, correct handling of threads, etc.
   * Error handling and external resource management
   * Tests - verifying key application logic

## FAQ

* A newborn (or generated) animal is set in a random direction and activates a random gene (not necessarily the first one).
* A newborn appears on the same tile as its parents.
* UI does not need to allow arbitrary values for parameters. Constraining allowed ranges is recommended.
* Energy is an integer, and plants are the only energy source. The total energy in a tile should remain constant after reproduction.
* When multiple animals compete for a plant or reproduction on the same tile, priority is given to:
  1. Animals with the highest energy
  2. Oldest animals (if tied)
  3. Animals with the most offspring (if still tied)
  4. A random selection if still tied.

