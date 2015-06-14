# tc-bisect
Bisect plugin for TeamCity

Adds special tab on build page from where you can run an automatic bisect.  Progress and results are also displayed on that tab.

## Description

Bisect operation itself is similar to bisect from Git ([doc](http://git-scm.com/docs/git-bisect)) or Mercurial ([doc](https://selenic.com/hg/help/bisect)).  The plugin leverages VCS-agnostic features of TeamCity so you can use it with any VCS supported by TeamCity.  However, the plugin was tested only with Git at the moment.

This is how looks successful bisect for a failed build with 13 changes.  As you can see it requires only three checks, god bless binary search.

//TODO paste link to the screenshot

## Requirements

The plugin targets TeamCity 9 and wasn't tested with other versions at the moment.  Like TeamCity 9 it supports Java 6-8.
