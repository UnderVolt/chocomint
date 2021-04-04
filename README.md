<p align="center">
  <img width="200px" src="assets/mint.png">
</p>

# chocomint
[![Dev chat](https://discordapp.com/api/guilds/646132850302779405/widget.png?style=shield)](https://discord.gg/Bk9f5PJvUg)
[![CodeFactor](https://www.codefactor.io/repository/github/undervolt/chocomint/badge)](https://www.codefactor.io/repository/github/undervolt/chocomint)
[![UnderVolt Website Badge](https://img.shields.io/badge/visit%20our-website-red.svg)](https://undervolt.io)
[![GitHub Forks](https://img.shields.io/github/forks/UnderVolt/Chocomint.svg?style=social&label=Fork&maxAge=2592000)](https://github.com/UnderVolt/Chocomint/network)
[![GitHub Stars](https://img.shields.io/github/stars/UnderVolt/Chocomint.svg?style=social&label=Star&maxAge=2592000)](https://github.com/UnderVolt/Chocomint/stargazers)
[![GitHub Watches](https://img.shields.io/github/watchers/UnderVolt/Chocomint.svg?style=social&label=Watch&maxAge=2592000)](https://github.com/UnderVolt/Chocomint/watchers)
[![HitCount](http://hits.dwyl.com/UnderVolt/chocomint.svg)](http://hits.dwyl.com/UnderVolt/chocomint)

In-development Minecraft ModLoader, Client and Optimizer.

## Status
This client is under heavy development, and might break eventually. We do **not** recommend using development/nightly builds as your daily driver, unless you want to submit bugs/glitch reports.

## Do you have an idea, or something doesn't work?
We're accepting bug reports and feature requests.
To make one, head over to the Issues tab. In there, try to include every possible detail, and send it.
We'll review it, and eventually, if it gets accepted, will be shipped within the next release!

## Running chocomint
Until releases start flowing, you can run chocomint if you have Gradle knowledge.
In order to launch the game, you must run the ``startGame`` script.

## Developing chocomint
Make sure you have the following before beginning:
 - Java Runtime Environment version 1.8.
 - Java Development Kit version 8.
 - A compatible IDE. UnderVolt recommends [IntelliJ IDEA](https://www.jetbrains.com/idea/) or [Visual Studio Code](https://code.visualstudio.com/).
 
To state changes in the codebase, you must follow certain steps:
 - First, [create your own fork](https://docs.github.com/en/free-pro-team@latest/github/getting-started-with-github/fork-a-repo), based on the current codebase.
 - Then, [open a pull request](https://docs.github.com/en/free-pro-team@latest/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request) to notify us that your code is ready for review.

In this way, you may be able to turn into an active contributor of the chocomint project. 

## Building chocomint
In order to build/compile the project into a working Minecraft version (.jar), you'll need to run the ``jar`` Gradle script.

This will create a series of directories. The compiled JAR file will be located in `/build/libs/`, and by default
should be named ``chocomint-1.0-SNAPSHOT.jar``.

## Updating chocomint
To update the repository's source code in your local environment, run the following command, inside the `chocomint` directory:
```shell
git pull
```


## Licence

chocomint's code is protected by the [GNU Public Licence 3](https://opensource.org/licenses/GPL-3.0)

**Important note!** This does not cover Minecraft's original codebase.
Minecraft is a registered trademark of Mojang Synergies AB.

Mojang © 2009-2020.
