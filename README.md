<p align="center">
  <img width="200px" src="assets/mint.png">
</p>

# chocomint
[![CodeFactor](https://www.codefactor.io/repository/github/undervolt/chocomint/badge)](https://www.codefactor.io/repository/github/undervolt/chocomint)
[![dev chat](https://discordapp.com/api/guilds/646132850302779405/widget.png?style=shield)](https://discord.gg/PYdEpXp)

In-development Minecraft ModLoader, Client and Optimizer.

 ## Status
This client is under heavy development, and might break eventually. We do **not** recommend using it as your daily driver.

We're accepting bug reports, feature requests and code inspections.
In those, try to include every possible detail, since it will improve productivity for the next release.

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
 - Then, [open a pull request](https://docs.github.com/es/free-pro-team@latest/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request) to notify us that your code is ready for review.

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
