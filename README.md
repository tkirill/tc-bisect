# tc-bisect
Bisect plugin for TeamCity

Adds special tab on build page from where you can run an automatic bisect.  Progress and results are also displayed on that tab.

## Description

Bisect operation itself is similar to bisect from Git ([doc](http://git-scm.com/docs/git-bisect)) or Mercurial ([doc](https://selenic.com/hg/help/bisect)).  The plugin leverages VCS-agnostic features of TeamCity so you can use it with any VCS supported by TeamCity.  However, the plugin was tested only with Git at the moment.

This is how looks successful bisect for a failed build with 13 changes.  As you can see it requires only three checks, god bless binary search.

![Screenshot of successful bisect for a failed build with 13 changes](https://raw.githubusercontent.com/tkirill/tc-bisect/f04d094e5520433bb8e3af22a2eef3d94aba66aa/screenshot-13-changes.png)

## Requirements

The plugin targets TeamCity 9 and wasn't tested with other versions at the moment.  Like TeamCity 9 it supports Java 6-8.

## Installation

Steps to install TeamCity plugin according to the [official documentation][tc-plugin-install-doc]:

1. Shutdown the TeamCity server.
2. Copy the zip archive with the plugin into the <TeamCity Data Directory>/plugins directory.
3. Start the TeamCity server: the plugin files will be unpacked and processed automatically. The plugin will be available in the Plugins List in the Administration area.

[tc-plugin-install-doc]: https://confluence.jetbrains.com/display/TCD9/Installing+Additional+Plugins "Installing Additional Plugins"
