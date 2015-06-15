# tc-bisect
Bisect plugin for TeamCity

Adds special tab on a build page from where you can run an automatic bisect.  Progress and results are also displayed on that tab.  The tab is available for failed builds with multiple changes.

## Description

Bisect operation itself is similar to bisect from Git ([doc](http://git-scm.com/docs/git-bisect)) or Mercurial ([doc](https://selenic.com/hg/help/bisect)).  The plugin leverages VCS-agnostic features of TeamCity so you can use it with any VCS supported by TeamCity.  However, the plugin was tested only with Git at the moment.

This is how looks successful bisect for a failed build with 13 changes.  As you can see it requires only three checks, god bless binary search.

![Screenshot of successful bisect for a failed build with 13 changes](https://raw.githubusercontent.com/tkirill/tc-bisect/f04d094e5520433bb8e3af22a2eef3d94aba66aa/screenshot-13-changes.png)

## Requirements

The plugin targets TeamCity 9 and wasn't tested with other versions at the moment.  Like TeamCity 9 it supports Java 6-8.

## Installation

Steps to install TeamCity plugin according to the [official documentation][tc-plugin-install-doc]:

1. Download [latest release](https://github.com/tkirill/tc-bisect/releases/latest).
2. Shutdown the TeamCity server.
3. Copy the zip archive with the plugin into the <TeamCity Data Directory>/plugins directory.
4. Start the TeamCity server: the plugin files will be unpacked and processed automatically. The plugin will be available in the Plugins List in the Administration area.
5. Add bisect build trigger to configuration where you want to run bisects.  To do this go to the Triggers sections of the Build Configuration Settings page and click "Add build trigger".

Now you can open some failed build in selected configuration and if this build contains multiple changes you will see Bisect tab.

[tc-plugin-install-doc]: https://confluence.jetbrains.com/display/TCD9/Installing+Additional+Plugins "Installing Additional Plugins"
